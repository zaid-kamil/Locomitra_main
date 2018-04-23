package com.digipodium.derish.locomitra;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.digipodium.derish.locomitra.Models.SaveContactTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 4/1/2018.
 */

public class Save_Contact extends AppCompatActivity {
    FirebaseDatabase fbase = FirebaseDatabase.getInstance();
   public DatabaseReference taskdb;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_contact);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        taskdb = fbase.getReference("User_Contact").child(uid);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_save) {
            saveInfo();

        }
        return true;
    }

    private void saveInfo() {
        final EditText etname = findViewById(R.id.etname);
        final EditText etmail = findViewById(R.id.etmail);
        final EditText etphone = findViewById(R.id.etphone);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            String getName = (String) bd.get("name");
            String getnumber = (String) bd.get("no");

            etname.setText(getName);
            etphone.setText(getnumber);

        }

        String name = etname.getText().toString();
        String mail = etmail.getText().toString();
        String phone = etphone.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || mail.isEmpty()) {

            if (name.isEmpty())
                etname.setError("Please give some text!");
            if (mail.isEmpty())
                etmail.setError("Please give some text!");
            if (phone.isEmpty()) {
                etphone.setError("Please give some text!");


            }


        } else {
            if (phone.length() == 10) {
                SaveContactTask tasksave = new SaveContactTask(name, mail, phone, true);
                taskdb.push().setValue(tasksave).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            etname.setText("");
                            etphone.setText("");
                            etmail.setText("");
                            Intent it=new Intent(Save_Contact.this,Show_details.class);

                            startActivity(it);

                        } else {
                            Toast.makeText(Save_Contact.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                etphone.setError("Enter 10 digit number.");
            }
        }

        final List<String> tasks = new ArrayList<>();
        taskdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                   Toast.makeText(Save_Contact.this, "Data Saved", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


}
