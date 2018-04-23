package com.digipodium.derish.locomitra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileactivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_GET = 78;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mdb;
    private FirebaseStorage Storage;
    StorageReference storageReference;
    private String uid;
    private EditText etname;
    private EditText etphone;
    private RadioGroup radioGroup;

    private boolean isImageUpload = false;
    private Uri fullPhotoUri;
    private Uri ImageUrl;
    private CircleImageView ivProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile);
        radioGroup = findViewById(R.id.radioGroup);


        Button submit = findViewById(R.id.submitbtn);
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfileInfo();
                uploadtoStorage();


            }
        });

        mAuth = FirebaseAuth.getInstance();
        mdb = FirebaseDatabase.getInstance();
        Storage = FirebaseStorage.getInstance();
        storageReference = Storage.getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
            finish();
        uid = currentUser.getUid();
        etphone = findViewById(R.id.etphone);
        etname = findViewById(R.id.etname);
        ivProfilePic = findViewById(R.id.ivprofilepic);

        ivProfilePic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        mdb.getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String phone = dataSnapshot.child("phone").getValue(String.class);
                String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                String uname = dataSnapshot.child("uname").getValue(String.class);
                String gender = dataSnapshot.child("gender").getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_GET);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            fullPhotoUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fullPhotoUri);
                ivProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveProfileInfo() {
        int c = 0;
        String imageurl = null;
        String Username = etname.getText().toString();
        String UserPhone = etphone.getText().toString();
        if (!fullPhotoUri.toString().isEmpty()) {

            imageurl = fullPhotoUri.toString();
        }

        if (UserPhone.length() == 10) {
            c = 1;
        }
        int gen = radioGroup.getCheckedRadioButtonId();
        String gender = "";
        if (gen == R.id.radiomale)
            gender = "Male";
        else
            gender = "Female";
        if (!Username.isEmpty() && !UserPhone.isEmpty() && c == 1&&imageurl.isEmpty()) {

            mdb.getReference().child("users").child(uid).child("phone").setValue(UserPhone);
            mdb.getReference().child("users").child(uid).child("uname").setValue(Username);
            mdb.getReference().child("users").child(uid).child("gender").setValue(gender);
            if(!imageurl.isEmpty())
            mdb.getReference().child("users").child(uid).child("imageUrl").setValue(imageurl);

            Toast.makeText(profileactivity.this, "Data saved", Toast.LENGTH_SHORT).show();
        } else {
            if (c == 0)
                Toast.makeText(this, "Number Invlaid", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Data insufficient", Toast.LENGTH_SHORT).show();
        }

    }


    private void uploadtoStorage() {
        if (fullPhotoUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(fullPhotoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(profileactivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(profileactivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        } else {
            Toast.makeText(this, "Select an image", Toast.LENGTH_SHORT).show();
        }



    }
}

