package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.myapplication.Global.GlobalVars;
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

        if(Voice.onInit(status)){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                checkPermission();
            } else{
                if(!((GlobalVars)this.getApplication()).isMainToDoWelcome()) Voice.instancia().speak(getString(R.string.MainToDoWelcome), TextToSpeech.QUEUE_FLUSH, null, "text");
            }
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.RECORD_AUDIO)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        Voice.instancia().speak(getString(R.string.MainToDoWelcome), TextToSpeech.QUEUE_FLUSH, null, "text");
                    } else {
                        Voice.instancia().speak(getString(R.string.NotPermisionMessage), TextToSpeech.QUEUE_FLUSH, null, "text");
                    }
                }
            }
        }

        ((GlobalVars)this.getApplication()).setMainToDoWelcome(true);
    }
}