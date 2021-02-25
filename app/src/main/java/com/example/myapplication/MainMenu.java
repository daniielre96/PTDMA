package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        final Button buttonQuit = findViewById(R.id.button3);

        /* Button to quit the app */
        buttonQuit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }
}