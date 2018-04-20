package com.digipodium.derish.locomitra;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by My on 4/16/2018.
 */

public class InviteReceive extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.receive_invite);
        TextView Join=findViewById(R.id.joinClick);
        final EditText etCode=findViewById(R.id.etCode);
        final ProgressBar pbar=findViewById(R.id.pBar);
        pbar.setVisibility(View.INVISIBLE);
        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCode.getText().toString().isEmpty())
                etCode.setError("Insert the code!");
                else
                {
                    pbar.setVisibility(View.VISIBLE);
                }



            }
        });

    }
}
