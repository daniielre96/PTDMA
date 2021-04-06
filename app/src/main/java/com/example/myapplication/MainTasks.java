package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
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
import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.comandVoice.Listen;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainTasks extends Listen {

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;

    private List<ToDoModel> taskList;

     //private SpeechRecognizer speechRecognizer;

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

        ToDoModel task = new ToDoModel();
        task.setTask("This is a Test Task");
        task.setStatus(0);
        task.setId(1);

        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);        taskList.add(task);
        taskList.add(task);        taskList.add(task);
        taskList.add(task);        taskList.add(task);
        taskList.add(task);

        tasksAdapter.setTasks(taskList);
    }

    @Override
    public void getResult(String result) {

        if(result.equalsIgnoreCase("create a new task")){
            Intent myIntent = new Intent(this.getActivity(), CreateTask.class);
            startActivity(myIntent);
        }
    }
}
