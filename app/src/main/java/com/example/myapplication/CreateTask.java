package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Global.GlobalVars;
import com.example.myapplication.comandVoice.Voice;

public class CreateTask extends AppCompatActivity {

    private ImageButton helpButton;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);
        ((TextView)findViewById(R.id.textToolbar)).setText("Create a Task");
        ((ImageView)findViewById(R.id.toolbarLeftIcon)).setBackgroundResource(R.drawable.ic_edit);

        helpButton = findViewById(R.id.toolbarRightIcon);
        dialog = new Dialog(this);
        
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        if(!((GlobalVars)this.getApplication()).isCreateModiftyTaskWelcome())  Voice.instancia().speak(getString(R.string.CreateModifyTaskWelcome), TextToSpeech.QUEUE_FLUSH, null, "text");
        ((GlobalVars)this.getApplication()).setCreateModiftyTaskWelcome(true);
    }

    private void openDialog() {
        dialog.setContentView(R.layout.help_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.2f);
        dialog.getWindow().getAttributes().gravity = Gravity.TOP;
        dialog.show();
    }
}