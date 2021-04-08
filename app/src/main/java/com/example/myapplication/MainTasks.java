package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ToDoAdapter;
import com.example.myapplication.MessageParser.Message;
import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.comandVoice.Listen;
import com.example.myapplication.comandVoice.Voice;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTasks extends Listen {

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private Dialog dialog;
    private ImageButton helpButton;

    private List<ToDoModel> taskList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment, container, false);
    }

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((TextView)getActivity().findViewById(R.id.textToolbar)).setText("Tasks");

        final ImageButton microButton = getView().findViewById(R.id.fab);

        helpButton = getActivity().findViewById(R.id.toolbarRightIcon);
        dialog = new Dialog(this.getContext());

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        /** SPEECH RECONIZER **/


        microButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    microButton.setImageResource(R.drawable.ic_grupo_48);
                    ((Listen)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container)).stopListening();
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    microButton.setImageResource(R.drawable.ic_grupo_48_red);
                    ((Listen)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container)).startListening();
                }

                return false;
            }
        });

        taskList = new ArrayList<>();

        tasksRecyclerView = getView().findViewById(R.id.tasksRecycle);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksAdapter = new ToDoAdapter(getContext());
        tasksRecyclerView.setAdapter(tasksAdapter);

        ToDoModel task1 = new ToDoModel();
        task1.setTask("test task");
        task1.setStatus(0);
        task1.setId(0);
        taskList.add(task1);

        for(int i =0; i < 5; i++){
            ToDoModel task = new ToDoModel();
            task.setTask("task " + String.valueOf(i+1));
            task.setStatus(0);
            task.setId(i+1);
            taskList.add(task);
        }

        tasksAdapter.setTasks(taskList);
    }

    @Override
    public void getResult(String result) {

        int action = Message.parseMainToDo(result);

        switch (action) {
            case 0: // UNDEFINED COMMAND (DONE)
                undefinedCommand();
                break;
            case 1: // HELP (DONE)
                openDialog();
                break;
            case 2: // MARK TASK AS DONE (DONE)
                markTaskAsDone(result);
                break;
            case 3: // MARK TASK AS UNDONE (DONE)
                markTaskAsUndone(result);
                break;
            case 4: // MARK ALL TASKS AS DONE (DONE)
                markTasksAsDone();
                break;
            case 5: // MARK ALL TASKS AS UNDONE (DONE)
                markTasksAsUndone();
                break;
            case 6: // DELETE A TASK (DONE)
                deleteTask(result);
                break;
            case 7: // DELETE ALL TASKS (DONE)
                deleteAllTasks();
                break;
            case 8: // CREATE A TASK (DONE)
                createTask();
                break;
            case 9: // MODIFY A TASK (DONE)
                modifyTask(result);
                break;
            case 10: // ENABLE SOUND
                break;
            case 11: // DISABLE SOUND
                break;
        }
    }

    /* COMMANDS ACTIONS METHODS */

    private void undefinedCommand() {
        Voice.instancia().speak(getString(R.string.UndefinedCommand), TextToSpeech.QUEUE_FLUSH, null, "text");
    }

    private void openDialog() {
        dialog.setContentView(R.layout.help_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.2f);
        dialog.getWindow().getAttributes().gravity = Gravity.TOP;
        dialog.show();
    }

    private void createTask() {
        Intent myIntent = new Intent(this.getActivity(), CreateTask.class);
        startActivity(myIntent);
    }

    private void modifyTask(String result) {

        String nameOfTask = Message.getAfterString("task ", result);

        ToDoModel task = taskList.stream().filter(t -> t.getTask().equals(nameOfTask)).findFirst().orElse(null);

        if(task != null){ // task with name found
            Intent intent = new Intent(this.getActivity(), CreateTask.class);
            intent.putExtra("TaskName", nameOfTask);
            startActivity(intent);
        }
        else{ // task not found
            Voice.instancia().speak(getString(R.string.TaskNotFound), TextToSpeech.QUEUE_FLUSH, null, "text");
        }
    }

    private void deleteTask(String result){

        String nameOfTask = Message.getAfterString("task ", result);

        ToDoModel task = taskList.stream().filter(t -> t.getTask().equals(nameOfTask)).findFirst().orElse(null);

        if(task != null){ // task with name found
            taskList.remove(task);
            tasksAdapter.setTasks(taskList);
        }
        else{ // task not found
            Voice.instancia().speak(getString(R.string.TaskNotFound), TextToSpeech.QUEUE_FLUSH, null, "text");
        }
    }

    private void deleteAllTasks(){
        taskList.clear();
        tasksAdapter.setTasks(taskList);
    }

    private void markTasksAsDone(){

        taskList.stream().forEach(t -> t.setStatus(1));
        tasksAdapter.setTasks(taskList);
    }

    private void markTasksAsUndone(){

        taskList.stream().forEach(t -> t.setStatus(0));
        tasksAdapter.setTasks(taskList);
    }

    private void markTaskAsDone(String result){
        String nameOfTask = Message.getBetweenStrings("task ", " as done", result);

        ToDoModel task = taskList.stream().filter(t -> t.getTask().equals(nameOfTask)).findFirst().orElse(null);

        if(task != null){
            task.setStatus(1);
            taskList.set(taskList.indexOf(task), task);
            tasksAdapter.setTasks(taskList);
        }
        else{
            Voice.instancia().speak(getString(R.string.TaskNotFound), TextToSpeech.QUEUE_FLUSH, null, "text");
        }
    }

    private void markTaskAsUndone(String result){
        String nameOfTask = Message.getAfterString("task ", result);

        ToDoModel task = taskList.stream().filter(t -> t.getTask().equals(nameOfTask)).findFirst().orElse(null);

        if(task != null){
            task.setStatus(0);
            taskList.set(taskList.indexOf(task), task);
            tasksAdapter.setTasks(taskList);
        }
        else{
            Voice.instancia().speak(getString(R.string.TaskNotFound), TextToSpeech.QUEUE_FLUSH, null, "text");
        }
    }
}
