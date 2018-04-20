package com.digipodium.derish.locomitra;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by My on 4/2/2018.
 */

public class Show_details extends AppCompatActivity {
    FirebaseDatabase fbase = FirebaseDatabase.getInstance();
    public DatabaseReference taskdb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_new_contact_detail);

        final TextView fname=findViewById(R.id.firename);
        final TextView fphone=findViewById(R.id.firephone);
        final TextView femail=findViewById(R.id.fireemail);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final List<String> tasks = new ArrayList<>();
        taskdb =(DatabaseReference) fbase.getReference("User_Contact").child(uid);
        taskdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    String name=data.child("name").getValue(String.class);
                    String emial=data.child("mail").getValue(String.class);
                    String phone=data.child("phone").getValue(String.class);
                    tasks.add(name);
                    tasks.add(emial);
                    tasks.add(phone);


                }
                fname.setText(tasks.get(tasks.size()-3).toString());
                fphone.setText(tasks.get(tasks.size()-1).toString());
                femail.setText(tasks.get(tasks.size()-2).toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
