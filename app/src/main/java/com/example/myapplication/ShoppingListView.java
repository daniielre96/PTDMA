package com.example.myapplication;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ToDoAdapter;
import com.example.myapplication.MessageParser.Message;
import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.comandVoice.Listen;
import com.example.myapplication.comandVoice.ListenActivity;
import com.example.myapplication.comandVoice.Voice;

import java.util.ArrayList;

public class ShoppingListView extends ListenActivity {

    private String listName;
    private ArrayList<ToDoModel> items;
    private RecyclerView itemsRecyclerView;
    private ToDoAdapter itemAdapter;
    private ImageButton helpButton;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_shoppinglist);

        listName = getIntent().getStringExtra("ListName");
        items = (ArrayList<ToDoModel>) getIntent().getSerializableExtra("ListItems");

        ((TextView)findViewById(R.id.textToolbar)).setText(listName);

        itemsRecyclerView = findViewById(R.id.tasksRecycle);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ToDoAdapter(this);
        itemsRecyclerView.setAdapter(itemAdapter);

        ToDoModel task = new ToDoModel();
        task.setTask("New ITEM");
        task.setStatus(0);
        task.setId(1);

        items.add(task);


        itemAdapter.setTasks(items);

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

        helpButton = findViewById(R.id.toolbarRightIcon);
        dialog = new Dialog(this);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    @Override
    public void getResult(String result) {

        int action = Message.parseCreateModifyEvent(result);

        switch (action){
            case 0: // UNDEFINED COMMAND
                undefinedCommand();
                break;
            case 1: // HELP
                openDialog();
                break;
            case 2:  // ADD ELEMENT TO THE LIST
                break;
            case 3: // DELETE ELEMENT FROM LIST
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
}
