package com.example.myapplication.comandVoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

public abstract class Listen extends Fragment implements RecognitionListener{

    static SpeechRecognizer instancia = null;

    public void startListening(){

            instancia = SpeechRecognizer.createSpeechRecognizer(this.getContext());
            instancia.setRecognitionListener(this);

            instancia.startListening(getIntent());
    }

    public void stopListening(){
        instancia.stopListening();
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
        getResult(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0));
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    public abstract void getResult(String result);

    static public Intent getIntent(){
        Intent speechReconizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechReconizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechReconizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.US);
        speechReconizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true);

        return speechReconizerIntent;
    }
}
