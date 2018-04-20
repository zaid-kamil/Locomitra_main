package com.digipodium.derish.locomitra;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by ASUS on 3/21/2018.
 */

public class ForgotActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private String TAG="ForgotActivity";
    private EditText etemail;
     String email;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

         final EditText etemail = findViewById(R.id.etmail);
        Button btnsend = findViewById(R.id.btnsend);
        TextView tvback = findViewById(R.id.tvback);
        auth = FirebaseAuth.getInstance();

        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it2 = new Intent(ForgotActivity.this,LoginActivity.class);
                startActivity(it2);
            }
        });

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=etemail.getText().toString().trim();
                Toast.makeText(ForgotActivity.this, email, Toast.LENGTH_SHORT).show();
                forgotpassword();
            }
        });
    }
        private void forgotpassword(){


            if(email.equals("")){
                Toast.makeText(ForgotActivity.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
            }else
            {
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotActivity.this, " reset Password email send", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(ForgotActivity.this,LoginActivity.class));
                        }
                      else
                        {
                            Toast.makeText(ForgotActivity.this, "Error in sending password reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }



    }


