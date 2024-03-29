package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Global.GlobalVars;
import com.example.myapplication.MessageParser.Message;
import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.R;
import com.example.myapplication.comandVoice.ListenActivity;
import com.example.myapplication.comandVoice.Voice;
import com.example.myapplication.fragments.MainShoppingList;
import com.example.myapplication.fragments.MainTasks;

public class CreateTask extends ListenActivity {

    private ImageButton helpButton;
    private Dialog dialog;
    private String nameOfTask;

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

        nameOfTask = getIntent().getStringExtra("TaskName");

        if(nameOfTask != null) ((TextView)findViewById(R.id.createTaskName)).setText(nameOfTask);

        if(!((GlobalVars)this.getApplication()).isCreateModiftyTaskWelcome())  Voice.instancia().speak(getString(R.string.CreateModifyTaskWelcome), TextToSpeech.QUEUE_FLUSH, null, "text");
        ((GlobalVars)this.getApplication()).setCreateModiftyTaskWelcome(true);

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

        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        int action = Message.parseMainCreateModifyTask(result);

        switch (action){
            case 0: // UNDEFINED COMMAND (DONE)
                undefinedCommand();
                break;
            case 1: // HELP (DONE)
                openDialog();
                break;
            case 2: // SET THE NAME OF THE TASK (DONE)
                setTaskName(result);
                break;
            case 3: // CREATE THE TASK (DONE)
                createTask();
                break;
        }
    }

    /* COMMANDS ACTIONS METHODS */

    private void undefinedCommand() {
        Voice.instancia().speak(getString(R.string.UndefinedCommand), TextToSpeech.QUEUE_FLUSH, null, "text");
        if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
    }


    private void openDialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.help_dialog, null);
        TextView editText = (TextView) v.findViewById(R.id.helpText);
        editText.setText(R.string.CreateModifyTaskHelp);
        dialog.setContentView(v);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.2f);
        dialog.getWindow().getAttributes().gravity = Gravity.TOP;
        dialog.show();
        Voice.instancia().speak(getString(R.string.HelpMe), TextToSpeech.QUEUE_FLUSH, null, "text");
    }

    private void setTaskName(String result){

        String nameOfTask = Message.getAfterString("name is ", result);

        if(nameOfTask != null && nameOfTask.length() > 0) {

            ((TextView) findViewById(R.id.createTaskName)).setText(nameOfTask);

            Voice.instancia().speak(getString(R.string.NameDefined, "task"), TextToSpeech.QUEUE_FLUSH, null, "text");
        }
        else{
            Voice.instancia().speak("Invalid name", TextToSpeech.QUEUE_FLUSH, null, "text");
            if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
        }
    }

    private void createTask() {
        String nameOfTask = ((TextView)findViewById(R.id.createTaskName)).getText().toString();

        if(nameOfTask != null && nameOfTask.length() != 0){
            if(ToDoModel.getIfExists(nameOfTask) != null){
                Voice.instancia().speak(getString(R.string.Exists, "task"), TextToSpeech.QUEUE_FLUSH, null, "text");
                if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
            }
            else{
                ToDoModel task = new ToDoModel();
                task.setTask(nameOfTask);
                task.setStatus(0);
                task.save();
                if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneSuccess(this);
                finish();
            }
        }
        else{
            Voice.instancia().speak("Please set the name of the task", TextToSpeech.QUEUE_FLUSH, null, "text");
            if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
        }
    }
}