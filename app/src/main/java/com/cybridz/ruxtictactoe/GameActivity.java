package com.cybridz.ruxtictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cybridz.AbstractActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AbstractActivity {

    /**
     * Game Activity elements
     */

    private Button back_button;

    private static Map<String, View> cells;
    private static Integer[][] grid;

    private static final int UNSET = 0;

    private static final int O = 1;

    private static final int X = 2;

    private static int round = 1;

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
        /* TODO */
        return false;
    }

    private void setUpGame(){
        cells = new HashMap<>();
        grid = new Integer[3][3];
        emptyBoard();
        for (int i = 0; i < grid.length; i++){
            Log.d(LOGGER_KEY, "Rows " + i + " : " + Arrays.asList(grid[i]));
        }
        boardColumnsWin();
        pickRandomPlayers();
        Log.d(LOGGER_KEY, "Rows : " + Arrays.asList(checkRowForWin()));
        Log.d(LOGGER_KEY, "Columns : " +  Arrays.asList(checkColumnForWin()));
        Log.d(LOGGER_KEY, "Diagonale : " + checkDiagonalForWin());
        Log.d(LOGGER_KEY, "Inverse diagonale : " + checkInverseDiagonalForWin());
    }

    private void boardRowWin(){
        ((TextView) cells.get("cell_10")).setText("X");
        ((TextView) cells.get("cell_11")).setText("X");
        ((TextView) cells.get("cell_12")).setText("X");
        grid[1][0] = X;
        grid[1][1] = X;
        grid[1][2] = X;
    }

    private void boardColumnsWin(){
        ((TextView) cells.get("cell_01")).setText("X");
        ((TextView) cells.get("cell_11")).setText("X");
        ((TextView) cells.get("cell_21")).setText("X");
        ((TextView) cells.get("cell_00")).setText("O");
        ((TextView) cells.get("cell_12")).setText("O");
        ((TextView) cells.get("cell_22")).setText("O");
        grid[0][1] = X;
        grid[1][1] = X;
        grid[2][1] = X;
    }

    private void boardDiagonalWin(){
        ((TextView) cells.get("cell_00")).setText("X");
        ((TextView) cells.get("cell_11")).setText("X");
        ((TextView) cells.get("cell_22")).setText("X");
        grid[0][0] = X;
        grid[1][1] = X;
        grid[2][2] = X;
    }

    private void boardInverseDiagonalWin(){
        ((TextView) cells.get("cell_02")).setText("X");
        ((TextView) cells.get("cell_11")).setText("X");
        ((TextView) cells.get("cell_20")).setText("X");
        grid[0][2] = X;
        grid[1][1] = X;
        grid[2][0] = X;
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
        for (int i=0; i < grid.length; i++){
            for (int j=0; j < grid[i].length; j++){
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

    public static Integer[] checkRowForWin(){
        Integer[] lines = new Integer[3];
        lines[0] = grid[0][0] == grid[0][1] && grid[0][1] == grid[0][2] ? grid[0][0] : UNSET;
        lines[1] = grid[1][0] == grid[1][1] && grid[1][1] == grid[1][2] ? grid[1][0] : UNSET;
        lines[2] = grid[2][0] == grid[2][1] && grid[2][1] == grid[2][2] ? grid[2][0] : UNSET;
        return lines;
    }

    public static Integer[] checkColumnForWin(){
        Integer[] columns = new Integer[3];
        columns[0] = grid[0][0] == grid[1][0] && grid[1][0] == grid[2][0] ? grid[0][0] : UNSET;
        columns[1] = grid[0][1] == grid[1][1] && grid[1][1] == grid[2][1] ? grid[0][1] : UNSET;
        columns[2] = grid[0][2] == grid[1][2] && grid[1][2] == grid[2][2] ? grid[0][2] : UNSET;
        return columns;
    }

    public static Integer checkDiagonalForWin(){
        return grid[0][0] == grid[1][1] &&  grid[1][1] == grid[2][2] ? grid[0][0] : UNSET;
    }

    public static Integer checkInverseDiagonalForWin(){
        return grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] ? grid[0][2] : UNSET;
    }

    @Override
    public void play() {

    }
}