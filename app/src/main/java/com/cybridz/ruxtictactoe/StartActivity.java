package com.cybridz.ruxtictactoe;

import android.annotation.SuppressLint;
import android.content.Intent;
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        current_view = findViewById(R.id.start_activity);
        initializeServices();
        start_button = findViewById(R.id.start_game);
        start_button.setText("Start");
        start_button.setOnClickListener(view -> goToGameActivity());
    }

    public void goToGameActivity(){
        sharedServices.getBlinkingLightMessageService().stop();
        startActivity(new Intent(StartActivity.this, GameActivity.class));
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