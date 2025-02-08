package com.cybridz.ruxtictactoe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.enums.GameStatus;
import com.cybridz.ruxtictactoe.enums.PropertyType;
import com.cybridz.ruxtictactoe.helpers.PromptHelper;
import com.cybridz.ruxtictactoe.helpers.api.Api;
import com.cybridz.ruxtictactoe.helpers.api.ApiResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        text.setText(Html.fromHtml("<h1 style='font-weight: bold;'>Speak</h1><p>Say something to test microphone</p>"));
    }

    private void initializeMediaRecorder(){
        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.WEBM);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.OPUS);
            fileName = getExternalCacheDir().getAbsolutePath() + "/audiorecordtest.webm";
            mediaRecorder.setOutputFile(fileName);
        }
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
            initializeMediaRecorder();
            mediaRecorder.prepare();
            mediaRecorder.start();
            speakButton.setText("Recording...");
            speakButton.setBackgroundColor(getResources().getColor(R.color.lightGrey));
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
        speakButton.setText("Speak");
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
        speakButton.setBackgroundColor(typedValue.data);
        try {
            api.getSpeechToText(fileName, new ApiResponseCallback() {
                @Override
                public void onSuccess(String textResponse) {
                    String system_prompt = getProperty(PropertyType.API, "CHAT_PROMPT");
                    String user_prompt = textResponse;
                    api.addPromptToHistory("system",  PromptHelper.makeJsonLine("system", system_prompt));
                    try {
                        String response = api.sendRequest(new String[] {"user"}, new String[] {PromptHelper.makeJsonLine("user", user_prompt)});
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray choices = jsonObject.getJSONArray("choices");
                        JSONObject firstChoice = choices.getJSONObject(0);
                        sharedServices.getRobotService().robotPlayTTs(firstChoice.getJSONObject("message").getString("content"));
                    } catch (IOException | JSONException e) {
                        Log.d(LOGGER_KEY, e.getMessage());
                    }
                    Log.d(LOGGER_KEY, "playing recording");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(LOGGER_KEY, "Speech to text error: " + e.getMessage());
                }
            });
        } catch (IOException e){
            Log.d(LOGGER_KEY, e.getMessage());
        }
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
            //sharedServices.getRobotService().robotPlayTTs("So wanna try speech to text ?");
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