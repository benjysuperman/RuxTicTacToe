package com.cybridz.ruxtictactoe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.cybridz.AbstractActivity;

public class StartActivity extends AbstractActivity {

    /**
     * Start Activity elements
     */
    @SuppressWarnings("FieldCanBeLocal")
    private Button start_button;
    private Button game_settings_button;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("ruxtictactoe_app_preferences", MODE_PRIVATE);
        setContentView(R.layout.start_activity);
        current_view = findViewById(R.id.start_activity);
        initializeServices();
        start_button = findViewById(R.id.start_game);
        start_button.setText("Start");
        start_button.setOnClickListener(view -> goToGameActivity());
        game_settings_button = findViewById(R.id.game_settings);
        game_settings_button.setText("Settings");
        game_settings_button.setOnClickListener(view -> getGameSettings());
    }

    public void goToGameActivity(){
        sharedServices.getBlinkingLightMessageService().stop();
        startActivity(new Intent(StartActivity.this, GameActivity.class));
    }

    public void getGameSettings(){
        sharedServices.getBlinkingLightMessageService().stop();
        startActivity(new Intent(StartActivity.this, SettingsActivity.class));
    }

    @Override
    public void play() {
        if (sharedServices.isOpenedServices()){
            sharedServices.getBlinkingLightMessageService().setRandomEarColor();
            sharedServices.getBlinkingLightMessageService().start();
            sharedServices.getRobotService().robotPlayTTs("Let's play Tic Tac Toe");
        }
        Log.d(LOGGER_KEY, "playing start activity");
    }
}