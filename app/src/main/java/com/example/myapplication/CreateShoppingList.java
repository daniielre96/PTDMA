package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Global.GlobalVars;
import com.example.myapplication.comandVoice.Voice;

public class CreateShoppingList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_shoppinglist);
        ((TextView)findViewById(R.id.textToolbar)).setText("Create a Shopping list");
        ((ImageView)findViewById(R.id.toolbarLeftIcon)).setBackgroundResource(R.drawable.ic_edit);

        if(!((GlobalVars)this.getApplication()).isCreateModifyShoppingList())  Voice.instancia().speak(getString(R.string.CreateModifyShoppingList), TextToSpeech.QUEUE_FLUSH, null, "text");
        ((GlobalVars)this.getApplication()).setCreateModifyShoppingList(true);
    }
}