package com.example.myapplication.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ShoppingAdapter;
import com.example.myapplication.Global.GlobalVars;
import com.example.myapplication.MessageParser.Message;
import com.example.myapplication.Model.ShoppingModel;
import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.R;
import com.example.myapplication.activities.CreateShoppingList;
import com.example.myapplication.activities.CreateTask;
import com.example.myapplication.activities.ShoppingListView;
import com.example.myapplication.comandVoice.Listen;
import com.example.myapplication.comandVoice.Voice;

import java.util.ArrayList;
import java.util.List;

public class MainShoppingList extends Listen {

    private RecyclerView shoppingRecyclerView;
    private ShoppingAdapter shoppingAdapter;
    private Dialog dialog;
    private ImageButton helpButton;
    
    private static List<ShoppingModel> shoppingList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        shoppingAdapter.setShoppingList(shoppingList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(!((GlobalVars)this.getActivity().getApplication()).isMainShoppingListWelcome()) Voice.instancia().speak(getString(R.string.MainShoppingListWelcome), TextToSpeech.QUEUE_FLUSH, null, "text");
        ((GlobalVars)this.getActivity().getApplication()).setMainShoppingListWelcome(true);

        ((TextView)getActivity().findViewById(R.id.textToolbar)).setText("Shopping List");

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


        shoppingList = new ArrayList<>();
        
        shoppingRecyclerView = getView().findViewById(R.id.tasksRecycle);
        shoppingRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        shoppingAdapter = new ShoppingAdapter(this);
        shoppingRecyclerView.setAdapter(shoppingAdapter);

        ShoppingModel model = new ShoppingModel();
        ShoppingModel model1 = new ShoppingModel();
        model1.setId(0);;
        model.setId(1);
        model1.setTitle("list test");
        model.setTitle("Lista 1");
        model.setImage(R.drawable.ic_baseline_shopping_cart_24);
        model1.setImage(R.drawable.ic_baseline_shopping_cart_24);

        // Items from shopping List

        ArrayList<ToDoModel> items = new ArrayList<>();
        ToDoModel task = new ToDoModel();
        task.setTask("Item 1");
        task.setStatus(0);
        task.setId(1);

        items.add(task);
        items.add(task);
        items.add(task);
        items.add(task);        items.add(task);
        items.add(task);        items.add(task);
        items.add(task);        items.add(task);
        items.add(task);

        model.setItems(items);
        model1.setItems(new ArrayList<>());

        shoppingList.add(model1);
        shoppingList.add(model);
        shoppingList.add(model);
        shoppingList.add(model);
        shoppingList.add(model);
        shoppingList.add(model);
        shoppingList.add(model);
        shoppingList.add(model);

        shoppingAdapter.setShoppingList(shoppingList);

    }

    @Override
    public void getResult(String result) {

        int action = Message.parseMainShoppingList(result);

        switch (action){
            case 0: // UNDEFINED COMMAND (DONE)
                undefinedCommand();
                break;
            case 1: // HELP (DONE)
                openDialog();
                break;
            case 2: // DELETE A LIST (DONE)
                deleteList(result);
                break;
            case 3: // DELETE ALL LISTS (DONE)
                deleteAllLists();
                break;
            case 4: // CREATE A LIST (DONE)
                createList();
                break;
            case 5: // MODIFY A LIST (DONE)
                modifyList(result);
                break;
            case 6: // SHOW A LIST (DONE)
                showList(result);
                break;
            case 7: // ENABLE SOUND
                break;
            case 8: // DISABLE SOUND
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

        Voice.instancia().speak(getString(R.string.HelpMe), TextToSpeech.QUEUE_FLUSH, null, "text");
    }

    private void deleteAllLists(){
        shoppingList.clear();
        shoppingAdapter.setShoppingList(shoppingList);

        Voice.instancia().speak(getString(R.string.DeleteAll, "lists"), TextToSpeech.QUEUE_FLUSH, null, "text");
    }

    private void deleteList(String result){
        String nameOfList = Message.getAfterString("list ", result);

        ShoppingModel shopping = shoppingList.stream().filter(sl -> sl.getTitle().equals(nameOfList)).findFirst().orElse(null);

        if(shopping != null){ // list with name found
            shoppingList.remove(shopping);
            shoppingAdapter.setShoppingList(shoppingList);

            Voice.instancia().speak(getString(R.string.Delete, "list", nameOfList), TextToSpeech.QUEUE_FLUSH, null, "text");
        }
        else{ // list not found
            Voice.instancia().speak("List not found", TextToSpeech.QUEUE_FLUSH, null, "text");
        }
    }

    private void createList(){
        Intent myIntent = new Intent(this.getActivity(), CreateShoppingList.class);
        startActivity(myIntent);
    }

    private void modifyList(String result){
        String nameOfList = Message.getAfterString("list ", result);

        ShoppingModel shopping = shoppingList.stream().filter(sl -> sl.getTitle().equals(nameOfList)).findFirst().orElse(null);

        if(shopping != null){ // list with name found
            Intent intent = new Intent(this.getActivity(), CreateShoppingList.class);
            intent.putExtra("ListName", nameOfList);
            startActivity(intent);
        }
        else{ // list not found
            Voice.instancia().speak("List not found", TextToSpeech.QUEUE_FLUSH, null, "text");
        }
    }

    private void showList(String result){
        String nameOfList = Message.getAfterString("list ", result);
        ShoppingModel shopping = shoppingList.stream().filter(sl -> sl.getTitle().equals(nameOfList)).findFirst().orElse(null);

        if(shopping != null){ // list with name found
            Intent intent = new Intent(this.getActivity(), ShoppingListView.class);
            intent.putExtra("ListName", shopping.getTitle());
            intent.putExtra("ListItems", shopping.getItems());
            startActivity(intent);
        }
        else{ // list not found
            Voice.instancia().speak("List not found", TextToSpeech.QUEUE_FLUSH, null, "text");
        }
    }

    static public boolean existsList(String nameOfList){
        ShoppingModel shopping = shoppingList.stream().filter(sl -> sl.getTitle().equals(nameOfList)).findFirst().orElse(null);

        return shopping != null;
    }

    static public void addList(String nameOfList){

        ShoppingModel model = new ShoppingModel();
        model.setId(0);
        model.setTitle(nameOfList);
        model.setImage(R.drawable.ic_baseline_shopping_cart_24);
        model.setItems(new ArrayList<>());

        shoppingList.add(model);
    }
}
