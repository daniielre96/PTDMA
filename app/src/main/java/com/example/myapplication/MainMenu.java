package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.myapplication.comandVoice.Voice;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenu extends AppCompatActivity implements TextToSpeech.OnInitListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView bottomNav = findViewById(R.id.navbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Voice.initContext(this, this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainTasks()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.todo_list:
                            selectedFragment = new MainTasks();
                            break;
                        case R.id.events_list:
                            selectedFragment = new MainEvents();
                            break;
                        case R.id.shopping_list:
                            selectedFragment = new MainShoppingList();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }

            };

    @Override
    protected void onDestroy() {
        Voice.destroyVoice();
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if(Voice.onInit(status)) Voice.instancia().speak("Hola Bona Tarda", TextToSpeech.QUEUE_FLUSH, null, "text");

    }
}