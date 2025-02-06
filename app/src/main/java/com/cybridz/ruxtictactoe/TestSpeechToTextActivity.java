package com.cybridz.ruxtictactoe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.helpers.Api;

import java.io.IOException;

public class TestSpeechToTextActivity extends AbstractActivity {

    /**
     * Start Activity elements
     */
    @SuppressWarnings("FieldCanBeLocal")
    private Button backButton;
    private Button speakButton;
    private TextView text;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String fileName;
    private boolean is_recording;

    /**
     * Needed classes
     */
    private Api api;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_speech_to_text);
        current_view = findViewById(R.id.activity_test_speech_to_text);
        initializePreferences();
        initializeServices();
        backButton = findViewById(R.id.back_btn);
        backButton.setText("Back");
        backButton.setOnClickListener(view -> goToStartActivity());
        speakButton = findViewById(R.id.speak_button);
        speakButton.setOnClickListener(view -> recordSpeech());
        text = findViewById(R.id.rules_lbl);
        is_recording = false;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        fileName = getExternalCacheDir().getAbsolutePath() + "/audiorecordtest.3gp";
        mediaRecorder.setOutputFile(fileName);
        text.setText(Html.fromHtml("<h1 style='font-weight: bold;'>Speak</h1><p>Say something to test microphone</p>"));
    }

    private void recordSpeech() {
        Log.d(LOGGER_KEY, "Clicked on speak btn");
        if(!is_recording){
            is_recording = true;
            startRecording();
        } else {
            is_recording = false;
            stopRecording();
        }

    }

    private void startRecording() {
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.d(LOGGER_KEY, "recording started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        Log.d(LOGGER_KEY, "recording stopped");
        playRecording();
    }

    private void playRecording() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
            // Playback has started
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    releaseMediaPlayer();
                    // Playback has completed
                }
            });
            Log.d(LOGGER_KEY, "playing recording");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void initializePreferences() {
        super.initializePreferences();
    }

    @Override
    public void play() {
        if (sharedServices.isOpenedServices()) {
            sharedServices.getBlinkingLightMessageService().setRandomEarColor();
            sharedServices.getBlinkingLightMessageService().start();
            sharedServices.getRobotService().robotPlayTTs("So wanna try speech to text ?");
        }
        api = new Api(this);
        api.loadClient();
        Log.d(LOGGER_KEY, "playing speech to text activity");
    }

    public void goToStartActivity() {
        sharedServices.getBlinkingLightMessageService().stop();
        startActivity(new Intent(TestSpeechToTextActivity.this, StartActivity.class));
    }
}