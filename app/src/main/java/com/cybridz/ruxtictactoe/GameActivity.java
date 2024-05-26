package com.cybridz.ruxtictactoe;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.helpers.TestGameScenario;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AbstractActivity {

    /**
     * Game Activity elements
     */

    private Button back_button;

    private static Game game;

    private static int round = 1;

    private static int current_player;

    private static int rux_player_number;

    private static int opponent_player_number;

    private TestGameScenario testGameScenario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOGGER_KEY, "Create game activity");
        setContentView(R.layout.game_activity);
        current_view = findViewById(R.id.game_activity);
        initializeServicesIfNeeded();
        back_button = findViewById(R.id.back_btn);
        back_button.setText("Back");
        back_button.setOnClickListener(view -> {
            Log.d(LOGGER_KEY, "clicked back btn");
            goToStartActivity();
        });
        sharedServices.getMotorRotationMessageService().sendRandomMotorRotation();
        sharedServices.getBlinkingLightMessageService().setRandomEarColor();
        sharedServices.getBlinkingLightMessageService().start();
        runGame();
    }

    private void runGame(){
        game = new Game(getResources(), getPackageName(), this);
        testGameScenario = new TestGameScenario(game);
        testGameScenario.boardDiagonalWin();
        pickRandomPlayers();
        game.printWinResults();
    }


    private void rux_plays(){
        checkGameOver();
        current_player = opponent_player_number;
    }

    private void opponent_plays(){
        current_player = rux_player_number;
    }

    private boolean checkGameOver() {
        /* TODO */
        return false;
    }

    private void pickRandomPlayers() {
        Random random = new Random();
        current_player = random.nextInt(2) + 1;
        rux_player_number = random.nextInt(2) + 1;
        opponent_player_number = rux_player_number == 1 ? 2 : 1;
        Log.d(LOGGER_KEY, "RUX plays with " + (rux_player_number == 1 ? "O" : "X") + ", number: " + rux_player_number);
        Log.d(LOGGER_KEY, "Opponent plays with " + (rux_player_number == 1 ? "X" : "O") + ", number: " + opponent_player_number);
    }

    public void goToStartActivity() {
        startActivity(new Intent(GameActivity.this, StartActivity.class));
    }

    @Override
    public void play() {

    }
}