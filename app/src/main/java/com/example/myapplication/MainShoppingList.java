package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ShoppingAdapter;
import com.example.myapplication.Global.GlobalVars;
import com.example.myapplication.Model.ShoppingModel;
import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.comandVoice.Listen;
import com.example.myapplication.comandVoice.Voice;

import java.util.ArrayList;
import java.util.List;

public class MainShoppingList extends Listen {

    private RecyclerView shoppingRecyclerView;
    private ShoppingAdapter shoppingAdapter;
    
    private List<ShoppingModel> shoppingList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(!((GlobalVars)this.getActivity().getApplication()).isMainShoppingListWelcome()) Voice.instancia().speak(getString(R.string.MainShoppingListWelcome), TextToSpeech.QUEUE_FLUSH, null, "text");
        ((GlobalVars)this.getActivity().getApplication()).setMainShoppingListWelcome(true);

        ((TextView)getActivity().findViewById(R.id.textToolbar)).setText("Shopping List");

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


        shoppingList = new ArrayList<>();
        
        shoppingRecyclerView = getView().findViewById(R.id.tasksRecycle);
        shoppingRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        shoppingAdapter = new ShoppingAdapter(this);
        shoppingRecyclerView.setAdapter(shoppingAdapter);

        ShoppingModel model = new ShoppingModel();
        model.setId(1);
        model.setTitle("List 1");
        model.setImage(R.drawable.ic_baseline_shopping_cart_24);

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

        shoppingList.add(model);
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

        if(result.equalsIgnoreCase("create a new shopping list")){
            Intent myIntent = new Intent(this.getActivity(), CreateShoppingList.class);
            startActivity(myIntent);
        }
    }
}
