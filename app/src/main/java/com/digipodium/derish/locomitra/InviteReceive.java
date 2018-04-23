package com.digipodium.derish.locomitra;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digipodium.derish.locomitra.Models.Invite_Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 4/16/2018.
 */

public class InviteReceive extends AppCompatActivity {
    DatabaseReference firebaseDatabase;
    private String userID;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.receive_invite);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Invited_Contacts");
        TextView Join = findViewById(R.id.joinClick);
        final EditText etCode = findViewById(R.id.etCode);
        final ProgressBar pbar = findViewById(R.id.pBar);
        final List<String> invite_match_detail = new ArrayList<>();
        pbar.setVisibility(View.INVISIBLE);
        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCode.getText().toString().isEmpty())
                    etCode.setError("Insert the code!");
                else {
                    pbar.setVisibility(View.VISIBLE);
                    firebaseDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                for (DataSnapshot request : dataSnapshot.getChildren()) {
                                    Invite_Task invite_task = request.getValue(Invite_Task.class);
                                    if (invite_task.equals(etCode.getText().toString())) {
                                        userID = dataSnapshot.getKey();
                                        String name = request.child("contact_name").getValue(String.class);
                                        String number = request.child("contact_number").getValue(String.class);
                                        addUserToDatabase(name, number);
                                        break;
                                    } else {
                                        Toast.makeText(InviteReceive.this, "NO User", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }


            }
        });

    }

    private void addUserToDatabase(String name, String number) {

        Intent intent=new Intent(InviteReceive.this,Save_Contact.class);
        intent.putExtra("name",name);
        intent.putExtra("no",number);
        startActivity(intent);

    }
}
