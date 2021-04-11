package com.example.myapplication.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.example.myapplication.Adapter.ItemAdapter;
import com.example.myapplication.Adapter.ToDoAdapter;
import com.example.myapplication.Global.GlobalVars;
import com.example.myapplication.MessageParser.Message;
import com.example.myapplication.Model.ItemModel;
import com.example.myapplication.Model.ShoppingModel;
import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.R;
import com.example.myapplication.comandVoice.Listen;
import com.example.myapplication.comandVoice.ListenActivity;
import com.example.myapplication.comandVoice.Voice;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListView extends ListenActivity {

    private String listName;
    private List<ItemModel> items;
    private RecyclerView itemsRecyclerView;
    private ItemAdapter itemAdapter;
    private ImageButton helpButton;
    private Dialog dialog;
    private boolean delete = false;
    private ItemModel elementToDelete;
    private long idList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_shoppinglist);

        idList = getIntent().getLongExtra("ListId", -1);

        if(!((GlobalVars)this.getApplication()).isMainShoppingListWelcome())  Voice.instancia().speak(getString(R.string.ShowShoppingList), TextToSpeech.QUEUE_FLUSH, null, "text");
        ((GlobalVars)this.getApplication()).setMainShoppingListWelcome(true);

        itemsRecyclerView = findViewById(R.id.tasksRecycle);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(this);
        itemsRecyclerView.setAdapter(itemAdapter);

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
    protected void onStart() {
        super.onStart();

        listName = ShoppingModel.findById(ShoppingModel.class, idList).getTitle();
        ((TextView)findViewById(R.id.textToolbar)).setText(listName);
        items = ItemModel.getByList(idList);

        itemAdapter.setItems(items);
    }

    @Override
    public void getResult(String result) {

        if(delete && result.contains("yes")){
            confirmDelete();
            delete = false;
        } else if(delete){
            Voice.instancia().speak(getString(R.string.DeleteCancelled), TextToSpeech.QUEUE_FLUSH, null, "text");
            delete = false;
        } else {

            int action = Message.parseShowShoppingList(result);

            switch (action) {
                case 0: // UNDEFINED COMMAND (DONE)
                    undefinedCommand();
                    break;
                case 1: // HELP (DONE)
                    openDialog();
                    break;
                case 2:  // ADD ELEMENT TO THE LIST (DONE)
                    addElement(result);
                    break;
                case 3: // DELETE ELEMENT FROM LIST
                    deleteElement(result);
                    break;
            }
        }
    }

    /* COMMANDS ACTIONS METHODS */

    private void undefinedCommand() {
        Voice.instancia().speak(getString(R.string.UndefinedCommand), TextToSpeech.QUEUE_FLUSH, null, "text");
        if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
    }


    private void openDialog() {
        dialog.setContentView(R.layout.help_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.2f);
        dialog.getWindow().getAttributes().gravity = Gravity.TOP;
        dialog.show();

        Voice.instancia().speak(getString(R.string.HelpMe), TextToSpeech.QUEUE_FLUSH, null, "text");
    }

    private void addElement(String result){
        String nameOfElement = Message.getAfterString("element ", result);

        ItemModel item = items.stream().filter(it -> it.getName().equals(nameOfElement)).findFirst().orElse(null);

        if(item != null){ // Item exists
            Voice.instancia().speak(getString(R.string.Exists, "item"), TextToSpeech.QUEUE_FLUSH, null, "text");
            if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
        }
        else{ // Item no exists
              ItemModel model = new ItemModel();
              model.setName(nameOfElement);
              model.setStatus(0);
              model.setIdList(idList);

              model.save();
              items.add(model);
              itemAdapter.setItems(items);

            Voice.instancia().speak(getString(R.string.AddElement), TextToSpeech.QUEUE_FLUSH, null, "text");
            if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneSuccess(this);
        }

    }

    private void deleteElement(String result){

        delete = true;
        String nameOfElement = Message.getAfterString("element ", result);

        elementToDelete = items.stream().filter(it -> it.getName().equals(nameOfElement)).findFirst().orElse(null);

        if(elementToDelete != null){ // task with name found

            Voice.instancia().speak(getString(R.string.Delete, "element", nameOfElement), TextToSpeech.QUEUE_FLUSH, null, "text");

            startListening();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopListening();
                }
            }, 10000);
        }
        else{ // item not found
            Voice.instancia().speak(getString(R.string.NotFound, "item"), TextToSpeech.QUEUE_FLUSH, null, "text");
            if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
        }
    }

    private void confirmDelete(){

        elementToDelete.delete();
        items = ItemModel.getByList(idList);
        itemAdapter.setItems(items);
    }
}
