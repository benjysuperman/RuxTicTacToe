package com.cybridz.ruxtictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cybridz.AbstractActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AbstractActivity {

    /**
     * Game Activity elements
     */

    private Button back_button;

    private static Map<String, View> cells;
    private static int[][] grid;

    private static final int UNSET = 0;

    private static final int O = 1;

    private static final int X = 2;

    private static int current_player;

    private static int rux_player_number;

    private static int opponent_player_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOGGER_KEY, "Create game activity");
        setContentView(R.layout.game_activity);
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
        if(grid == null){
            setUpGame();
        }
    }

    private void rux_plays(){

        checkGameOver();
        current_player = opponent_player_number;
    }

    private void opponent_plays(){
        current_player = rux_player_number;
    }

    private boolean checkGameOver() {
        boolean is_game_over = false;
        for (int i=0; i < 3; i++){
            for (int j=0; j < 3; j++){

            }
        }
    }

    private void setUpGame(){
        cells = new HashMap<>();
        grid = new int[3][3];
        emptyBoard();
        pickRandomPlayers();
    }

    private void pickRandomPlayers() {
        Random random = new Random();
        current_player = random.nextInt(2) + 1;
        rux_player_number = random.nextInt(2) + 1;
        opponent_player_number = rux_player_number == 1 ? 2 : 1;
        Log.d(LOGGER_KEY, "RUX plays with " + (rux_player_number == 1 ? "O" : "X") + ", number: " + rux_player_number);
        Log.d(LOGGER_KEY, "Opponent plays with " + (rux_player_number == 1 ? "X" : "O") + ", number: " + opponent_player_number);
    }

    private void emptyBoard(){
        for (int i=0; i < 3; i++){
            for (int j=0; j < 3; j++){
                String cellID = "cell_" + i + j;
                int resID = getResources().getIdentifier(cellID, "id", getPackageName());
                TextView cell = findViewById(resID);
                cell.setText("");
                cells.put(cellID, cell);
                grid[i][j] = UNSET;
            }
        }
    }

    public void goToStartActivity(){
        startActivity(new Intent(GameActivity.this, StartActivity.class));
    }

    @Override
    public void play() {
        Log.d(LOGGER_KEY, "In game activity");
    }
}