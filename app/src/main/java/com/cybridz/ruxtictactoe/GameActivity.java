package com.cybridz.ruxtictactoe;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.helpers.TestGameScenario;

import java.util.Random;

public class GameActivity extends AbstractActivity {

    /**
     * Testing purpose
     */
    private boolean test_mode = false;

    /**
     * Game Activity elements
     */

    private Button back_button;

    private TextView player1;
    private TextView player1_symbol;

    private TextView player2;
    private TextView player2_symbol;

    /**
     * Game Control & States
     */

    private static Game game;

    private static int round = 1;

    private static int current_symbol;

    public static int rux_symbol;

    public static int player_symbol;

    private static final String RUX_FINISHED = "RUX";
    private static final String PLAYER_FINISHED = "PLAYER";
    private static final String GAME_OVER_FINISHED = "GAME OVER";
    private static final String NOBODY_FINISHED = "NONE";

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

        player1 = findViewById(R.id.player1);
        player1_symbol = findViewById(R.id.player1_symbol);

        player2 = findViewById(R.id.player2);
        player2_symbol = findViewById(R.id.player2_symbol);


        sharedServices.getMotorRotationMessageService().sendRandomMotorRotation();
        sharedServices.getBlinkingLightMessageService().setRandomEarColor();
        sharedServices.getBlinkingLightMessageService().start();

        play();
        checkTestGame(rux_symbol, false);
        checkTestGame(player_symbol, true);
    }

    public void updateCell(TextView cell, int i, int j){
        if(cell.getText().equals("")){
            if(current_symbol == rux_symbol){
                player_plays();
            } else {
                rux_plays();
            }
            setCurrentPlayerTurnVisibility();
            checkFinished();
        }
    }

    private void setCurrentPlayerTurnVisibility(){
        if(current_symbol == rux_symbol){
            player1.setTextColor(Color.rgb(29, 161, 242));
            player1.setTypeface(null, Typeface.BOLD);
            player1_symbol.setTextColor(Color.rgb(29, 161, 242));
            player1_symbol.setTypeface(null, Typeface.BOLD);
            player1_symbol.setTextSize(26);

            player2.setTextColor(Color.rgb(101, 119, 134));
            player2.setTypeface(null, Typeface.NORMAL);
            player2_symbol.setTextColor(Color.rgb(101, 119, 134));
            player2_symbol.setTypeface(null, Typeface.NORMAL);
            player2_symbol.setTextSize(18);

        } else {

            player2.setTextColor(Color.rgb(29, 161, 242));
            player2.setTypeface(null, Typeface.BOLD);
            player2_symbol.setTextColor(Color.rgb(29, 161, 242));
            player2_symbol.setTypeface(null, Typeface.BOLD);
            player2_symbol.setTextSize(26);

            player1.setTextColor(Color.rgb(101, 119, 134));
            player1.setTypeface(null, Typeface.NORMAL);
            player1_symbol.setTextColor(Color.rgb(101, 119, 134));
            player1_symbol.setTypeface(null, Typeface.NORMAL);
            player1_symbol.setTextSize(18);

        }
    }

    private void checkTestGame(int symbol, boolean do_full){
        if(test_mode){
            TestGameScenario testGameScenario= new TestGameScenario(game);

            Log.d(LOGGER_KEY, "Test row:\n=========\n");
            game.emptyBoard();
            testGameScenario.boardRowWin(symbol);
            Log.d(LOGGER_KEY, "Rux :\n=====\n");
            game.printResults(rux_symbol);
            Log.d(LOGGER_KEY, "Player :\n========\n");
            game.printResults(player_symbol);
            Log.d(LOGGER_KEY, "Status : " + checkFinished());
            Log.d(LOGGER_KEY, "\n\n");

            Log.d(LOGGER_KEY, "Test column:\n============\n");
            game.emptyBoard();
            testGameScenario.boardColumnsWin(symbol);
            Log.d(LOGGER_KEY, "Rux :\n=====\n");
            game.printResults(rux_symbol);
            Log.d(LOGGER_KEY, "Player :\n========\n");
            game.printResults(player_symbol);
            Log.d(LOGGER_KEY, "Status : " + checkFinished());
            Log.d(LOGGER_KEY, "\n\n");

            Log.d(LOGGER_KEY, "Test diagonal:\n=============\n");
            game.emptyBoard();
            testGameScenario.boardDiagonalWin(symbol);
            Log.d(LOGGER_KEY, "Rux :\n=====\n");
            game.printResults(rux_symbol);
            Log.d(LOGGER_KEY, "Player :\n========\n");
            game.printResults(player_symbol);
            Log.d(LOGGER_KEY, "Status : " + checkFinished());
            Log.d(LOGGER_KEY, "\n\n");

            Log.d(LOGGER_KEY, "Test inverse diagonal:\n=====================\n");
            game.emptyBoard();
            testGameScenario.boardInverseDiagonalWin(symbol);
            Log.d(LOGGER_KEY, "Rux :\n=====\n");
            game.printResults(rux_symbol);
            Log.d(LOGGER_KEY, "Player :\n========\n");
            game.printResults(player_symbol);
            Log.d(LOGGER_KEY, "Status : " + checkFinished());
            Log.d(LOGGER_KEY, "\n\n");

            if(do_full){
                Log.d(LOGGER_KEY, "Grid:\n=====\n");
                game.emptyBoard();
                testGameScenario.boardFullGrid();
                Log.d(LOGGER_KEY, "Rux :\n=====\n");
                game.printResults(rux_symbol);
                Log.d(LOGGER_KEY, "Player :\n========\n");
                game.printResults(player_symbol);
                Log.d(LOGGER_KEY, "Status : " + checkFinished());
                Log.d(LOGGER_KEY, "\n\n");

                Log.d(LOGGER_KEY, "Grid:\n=====\n");
                game.emptyBoard();
                testGameScenario.boardPending();
                Log.d(LOGGER_KEY, "Rux :\n=====\n");
                game.printResults(rux_symbol);
                Log.d(LOGGER_KEY, "Player :\n========\n");
                game.printResults(player_symbol);
                Log.d(LOGGER_KEY, "Status : " + checkFinished());
                Log.d(LOGGER_KEY, "\n\n");
            }
        }
    }

    private void rux_plays(){
        current_symbol = rux_symbol;
    }

    private void player_plays(){
        current_symbol = player_symbol;
    }

    private String checkFinished() {
        if(
            game.checkColumnForWin(rux_symbol) == rux_symbol ||
            game.checkRowForWin(rux_symbol) == rux_symbol ||
            game.checkDiagonalForWin(rux_symbol) == rux_symbol ||
            game.checkInverseDiagonalForWin(rux_symbol) == rux_symbol
        ){
            Log.d(LOGGER_KEY, "Rux win");
            return RUX_FINISHED;
        }
        if(
            game.checkColumnForWin(player_symbol) == player_symbol ||
            game.checkRowForWin(player_symbol) == player_symbol ||
            game.checkDiagonalForWin(player_symbol) == player_symbol ||
            game.checkInverseDiagonalForWin(player_symbol) == player_symbol
        ){
            Log.d(LOGGER_KEY, "Player win");
            return PLAYER_FINISHED;
        }

        if (game.gridIsFilled()){
            Log.d(LOGGER_KEY, "No one wins");
            return GAME_OVER_FINISHED;
        }
        Log.d(LOGGER_KEY, "Game not finished");
        return NOBODY_FINISHED;
    }

    private void pickRandomPlayers() {
        Random random = new Random();
        current_symbol = random.nextInt(2) + 1;
        rux_symbol = random.nextInt(2) + 1;
        player_symbol = rux_symbol == 1 ? 2 : 1;
        Log.d(LOGGER_KEY, "Current player : "  + (current_symbol == rux_symbol ? "Rux" : "Player"));
        Log.d(LOGGER_KEY, "RUX plays with " + (rux_symbol == 1 ? "O" : "X") + ", number: " + rux_symbol);
        Log.d(LOGGER_KEY, "Player plays with " + (rux_symbol == 1 ? "X" : "O") + ", number: " + player_symbol);
    }

    public void goToStartActivity() {
        startActivity(new Intent(GameActivity.this, StartActivity.class));
    }

    @Override
    public void play() {
        Log.d(LOGGER_KEY, "play");
        game = new Game(getResources(), getPackageName(), this);
        pickRandomPlayers();
        if(current_symbol == rux_symbol){
            player1.setText("Rux");
            player2.setText("Your");
        } else {
            player1.setText("Your");
            player2.setText("Rux");
        }
        setCurrentPlayerTurnVisibility();
        player1_symbol.setText(current_symbol == Game.X ? "X" : "O");
        player2_symbol.setText(current_symbol == Game.X ? "O" : "X");
    }
}