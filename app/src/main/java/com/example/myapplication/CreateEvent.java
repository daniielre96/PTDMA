package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Global.GlobalVars;
import com.example.myapplication.comandVoice.Voice;

public class CreateEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event);
        ((TextView)findViewById(R.id.textToolbar)).setText("Create an Event");
        ((ImageView)findViewById(R.id.toolbarLeftIcon)).setBackgroundResource(R.drawable.ic_edit);

        if(!((GlobalVars)this.getApplication()).isCreateModifyEventWelcome())  Voice.instancia().speak(getString(R.string.CrateModifyEventWelcome), TextToSpeech.QUEUE_FLUSH, null, "text");
        ((GlobalVars)this.getApplication()).setCreateModifyShoppingList(true);
    }
}