package com.digipodium.derish.locomitra;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digipodium.derish.locomitra.Models.Profile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 343;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth mAuth;
    private String TAG = "sign up";
    TextView tvlogin;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mRootDatabaseRef;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etpassword);
        Button btnregister = findViewById(R.id.btnregister);
        mAuth = FirebaseAuth.getInstance();
        tvlogin = findViewById(R.id.tvlogin);
btnregister.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent it=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(it);
    }
});
       tvlogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent it=new Intent(RegisterActivity.this,LoginActivity.class);
               startActivity(it);
           }
       });


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (password.isEmpty() || email.isEmpty()||password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    SignIn();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    final FirebaseUser current_User = FirebaseAuth.getInstance().getCurrentUser();
                                    assert current_User != null;
                                    final String current_user_uid = current_User.getUid();
                                    mRootDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(current_user_uid);
                                    Profile profile=new Profile("","","",current_User.getEmail(),"","",false);
                                    mRootDatabaseRef.setValue(profile);
                                    updateUI(user);
                                    Toast.makeText(RegisterActivity.this, "Successfully registered.", Toast.LENGTH_SHORT).show();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                    Toast.makeText(RegisterActivity.this, "Registration failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });

    }

    private void SignIn() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Signing In");
        mProgressDialog.setMessage("Almost there!");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startActivity(new Intent(RegisterActivity.this, Home.class));
finish();
        }
    }





    private void sendToHome() {
        startActivity(new Intent(RegisterActivity.this, Home.class));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}




