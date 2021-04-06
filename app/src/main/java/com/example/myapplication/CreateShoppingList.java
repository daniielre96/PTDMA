package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Global.GlobalVars;
import com.example.myapplication.comandVoice.ListenActivity;
import com.example.myapplication.comandVoice.Voice;

public class CreateShoppingList extends ListenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_shoppinglist);
        ((TextView)findViewById(R.id.textToolbar)).setText("Create a Shopping list");
        ((ImageView)findViewById(R.id.toolbarLeftIcon)).setBackgroundResource(R.drawable.ic_edit);

        if(!((GlobalVars)this.getApplication()).isCreateModifyShoppingList())  Voice.instancia().speak(getString(R.string.CreateModifyShoppingList), TextToSpeech.QUEUE_FLUSH, null, "text");
        ((GlobalVars)this.getApplication()).setCreateModifyShoppingList(true);

        final ImageButton microButton = findViewById(R.id.fab);

        microButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    microButton.setBackgroundResource(R.drawable.ic_grupo_48);
                    stopListening();
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    microButton.setBackgroundResource(R.drawable.ic_grupo_48_red);
                    startListening();
                }

                return false;
            }
        });
    }

    @Override
    public void getResult(String result) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}