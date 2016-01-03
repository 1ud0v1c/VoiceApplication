package fr.borntocode.lud00.voiceapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private Button launchPrompt;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launchPrompt = (Button) findViewById(R.id.launchPrompt);
        launchPrompt.setOnClickListener(promptListener);
    }

    View.OnClickListener promptListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            promptSpeechInput();
        }
    };

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Vous pouvez parler ...");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Désolé, votre appareil ne supporte pas d'entrée vocale...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> buffer = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String result = buffer.get(0);

                    Pattern ff = Pattern.compile("Final|fantasy");
                    if (ff.matcher(result).find()) {
                        try {
                            preparePlayer("ffx-victory.mp3");
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    public void preparePlayer(String path) throws IOException {
        AssetFileDescriptor afd = getAssets().openFd(path);
        player = new MediaPlayer();
        player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        player.prepare();
    }

    @Override
    protected void onStop() {
        if(player.isPlaying()) {
            player.stop();
        }
        super.onStop();
    }

}
