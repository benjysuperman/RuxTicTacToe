package com.cybridz.ruxtictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.cybridz.AbstractActivity;
public class StartActivity extends AbstractActivity {

    /**
     * Start Activity elements
     */

    private Button start_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOGGER_KEY, getProperty(API, "SYSTEM_PROMPT"));
        setContentView(R.layout.start_activity);
        current_view = findViewById(R.id.start_activity);
        initializeServicesIfNeeded();
        start_button = findViewById(R.id.start_game);
        start_button.setText("Start");
        start_button.setOnClickListener(view -> {
            Log.d(LOGGER_KEY, "clicked start btn");
            goToGameActivity();
        });
        sharedServices.getBlinkingLightMessageService().setRandomEarColor();
        sharedServices.getBlinkingLightMessageService().start();
    }

    public void goToGameActivity(){
        sharedServices.getBlinkingLightMessageService().stop();
        startActivity(new Intent(StartActivity.this, GameActivity.class));
    }

    @Override
    public void play() {
        Log.d(LOGGER_KEY, "playing start activity");
    }
}