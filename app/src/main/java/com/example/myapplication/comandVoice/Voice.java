package com.example.myapplication.comandVoice;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import org.w3c.dom.Text;

import java.util.Locale;

public class Voice{

    static TextToSpeech instancia = null;

    static public TextToSpeech instancia() {
        if(instancia == null) throw new NullPointerException("Voice not instancied");
        return instancia;
    }

    static public void initContext(Context c, TextToSpeech.OnInitListener listener){
        instancia = new TextToSpeech(c,listener);
    }

    static public boolean onInit(int status){
        if (status == TextToSpeech.SUCCESS){

            int result = instancia.setLanguage(Locale.US);

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
                return false;
            }
        } else {
            Log.e("TTS","Initialization Failed!");
            return false;
        }
        return true;
    }

    static public void destroyVoice(){
        if(instancia != null){
            instancia.stop();
            instancia.shutdown();
            instancia = null;
        }
    }

}


