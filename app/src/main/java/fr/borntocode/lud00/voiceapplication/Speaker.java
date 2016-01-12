package fr.borntocode.lud00.voiceapplication;

import java.util.Locale;

import android.content.Context;
import android.media.AudioManager;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class Speaker implements OnInitListener {
    private static Locale language = Locale.FRANCE;
    private static final String TAG = "TextToSpeechInitializer";

    private static TextToSpeech tts;
    private boolean isReady = false;
    private boolean isAllowed = false;

    public Speaker(Context context){
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            tts.setLanguage(Locale.FRANCE);
            isReady = true;
        } else{
            isReady = false;
        }
    }

    public void speak(String text){
        if(isReady && isAllowed) {
            Bundle key = new Bundle();
            key.putString("key",TextToSpeech.Engine.KEY_PARAM_STREAM);
            tts.speak(text, TextToSpeech.QUEUE_ADD, key, String.valueOf(AudioManager.STREAM_NOTIFICATION));
        }
    }

    public void setSpeedRate(float speechrate) {
        tts.setSpeechRate(speechrate);
    }

    public void setPitchRate(float pitchrate) {
        tts.setPitch(pitchrate);
    }

    public boolean isAllowed(){
        return isAllowed;
    }

    public void allow(boolean allowed){
        isAllowed = allowed;
    }

    public boolean isSpeaking() {
        return tts.isSpeaking();
    }

    public void pause(int duration){
        tts.playSilentUtterance(duration, TextToSpeech.QUEUE_ADD, null);
    }

    public void stop() {
        tts.stop();
    }

    public void destroy() {
        tts.shutdown();
    }
}