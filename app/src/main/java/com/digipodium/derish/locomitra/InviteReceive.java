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

import com.digipodium.derish.locomitra.Models.InviteTask;
import com.digipodium.derish.locomitra.Models.Profile;
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
                                    for (DataSnapshot snapshot : request.getChildren()) {
                                        InviteTask invite_task = snapshot.getValue(InviteTask.class);
                                        String num = String.valueOf(invite_task.contact_number);
                                        if (String.valueOf(invite_task.invite_code).equals(etCode.getText().toString())) {
                                            num = num.replace(" ", "");
                                            findUserId(invite_task.requestPersonId, num);


                                            break;
                                        } else {
                                            Toast.makeText(InviteReceive.this, "NO User", Toast.LENGTH_SHORT).show();
                                        }
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

    private void findUserId(final String requestedpid, final String num) {
        DatabaseReference userdb = FirebaseDatabase.getInstance().getReference("users");
        userdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                        String number = snapshot.child("phone").getValue(String.class);
                        String numb = trim(num);

                        Toast.makeText(InviteReceive.this, "number = " + number, Toast.LENGTH_SHORT).show();
                        Toast.makeText(InviteReceive.this, "num = " + num, Toast.LENGTH_SHORT).show();

                        if ((number).equals(numb)) {
                            userID = snapshot.getKey();
                            Toast.makeText(InviteReceive.this, userID, Toast.LENGTH_SHORT).show();
                            addUserToContact(requestedpid, userID);
                            break;
                        } else {

                            Toast.makeText(InviteReceive.this, "No user found", Toast.LENGTH_SHORT).show();

                        }


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String trim(String num) {
        if (num.contains("-")) {
            num.replace("-", "");
        }
        if (num.contains(" ")) {
            num.replace(" ", "");
        }
        if (num.contains("+91")) {
            num.replace("+91", "");
        }
        if (num.contains("_")) {
            num.replace("_", "");
        }
        Toast.makeText(this, num.trim(), Toast.LENGTH_SHORT).show();
        return num.trim();
    }

    private void addUserToContact(String requestPersonId, String userID) {
        DatabaseReference contactDb = FirebaseDatabase.getInstance().getReference("User_Contact");
        updateBothContacts(contactDb, requestPersonId, userID);
    }

    private void updateBothContacts(final DatabaseReference contactDb, final String requestPersonId, final String userID) {
        DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference("users");
        usersDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String id = snapshot.getKey();
                        if (id.equals(requestPersonId)) {
                            Profile requestPersonProfile = snapshot.getValue(Profile.class);
                            contactDb.child(userID).child(requestPersonId).setValue(requestPersonProfile);
                        } else if (id.equals(userID)) {
                            Profile currentUserProfile = snapshot.getValue(Profile.class);
                            contactDb.child(requestPersonId).child(userID).setValue(currentUserProfile);
                        }
                    }
                    gotoHome();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void gotoHome() {
        Intent intent = new Intent(InviteReceive.this, Home.class);
        startActivity(intent);
    }


}
