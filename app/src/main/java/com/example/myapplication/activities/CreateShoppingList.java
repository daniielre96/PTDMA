package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
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
import com.example.myapplication.Model.ShoppingModel;
import com.example.myapplication.R;
import com.example.myapplication.comandVoice.ListenActivity;
import com.example.myapplication.comandVoice.Voice;
import com.example.myapplication.fragments.MainShoppingList;

import java.util.ArrayList;

public class CreateShoppingList extends ListenActivity {

    private ImageButton helpButton;
    private Dialog dialog;
    private String nameOfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_shoppinglist);
        ((TextView)findViewById(R.id.textToolbar)).setText("Create a Shopping list");
        ((ImageView)findViewById(R.id.toolbarLeftIcon)).setBackgroundResource(R.drawable.ic_edit);

        if(!((GlobalVars)this.getApplication()).isCreateModifyShoppingList())  Voice.instancia().speak(getString(R.string.CreateModifyShoppingList), TextToSpeech.QUEUE_FLUSH, null, "text");

        helpButton = findViewById(R.id.toolbarRightIcon);
        dialog = new Dialog(this);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        nameOfList = getIntent().getStringExtra("ListName");

        if(nameOfList != null) ((TextView)findViewById(R.id.createListName)).setText(nameOfList);

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

        int action = Message.parseCreateModifyShoppingList(result);

        switch (action){
            case 0: // UNDEFINED COMMAND (DONE)
                undefinedCommand();
                break;
            case 1: // HELP (DONE)
                openDialog();
                break;
            case 2: // SET THE NAME OF THE LIST (DONE)
                setNameList(result);
                break;
            case 3: // CREATE THE LIST (DONE)
                createList();
                break;
        }
    }

    private void undefinedCommand() {
        Voice.instancia().speak(getString(R.string.UndefinedCommand), TextToSpeech.QUEUE_FLUSH, null, "text");
        if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
    }


    private void openDialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.help_dialog, null);
        TextView editText = (TextView) v.findViewById(R.id.helpText);
        editText.setText(R.string.CreateModifyShoppingListHelp);
        dialog.setContentView(v);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.2f);
        dialog.getWindow().getAttributes().gravity = Gravity.TOP;
        dialog.show();
        Voice.instancia().speak(getString(R.string.HelpMe), TextToSpeech.QUEUE_FLUSH, null, "text");
    }

    private void setNameList(String result){

        String nameOfList = Message.getAfterString("name is ", result);

        if(nameOfList != null && nameOfList.length() > 0){

            ((TextView)findViewById(R.id.createListName)).setText(nameOfList);

            Voice.instancia().speak(getString(R.string.NameDefined, "list"), TextToSpeech.QUEUE_FLUSH, null, "text");
        }
        else{
            Voice.instancia().speak("Invalid name", TextToSpeech.QUEUE_FLUSH, null, "text");
            if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
        }
    }

    private void createList(){
        String nameOfList = ((TextView)findViewById(R.id.createListName)).getText().toString();

        if(nameOfList.length() != 0){
            if(ShoppingModel.getIfExists(nameOfList) != null){
                Voice.instancia().speak(getString(R.string.Exists, "list"), TextToSpeech.QUEUE_FLUSH, null, "text");
                if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
            }
            else{
                ShoppingModel model = new ShoppingModel();
                model.setTitle(nameOfList);
                model.setImage(R.drawable.ic_baseline_shopping_cart_24);
                model.setItems(new ArrayList<>());

                model.save();
                if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneSuccess(this);
                finish();
            }
        }
        else{
            Voice.instancia().speak("Please set the name of the list", TextToSpeech.QUEUE_FLUSH, null, "text");
            if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
        }
    }
}