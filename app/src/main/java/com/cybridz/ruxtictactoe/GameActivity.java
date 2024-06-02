package com.cybridz.ruxtictactoe;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.components.Cell;
import com.cybridz.ruxtictactoe.components.GameOver;
import com.cybridz.ruxtictactoe.helpers.Api;
import com.cybridz.ruxtictactoe.helpers.TestGameScenario;

import org.json.JSONArray;
import org.json.JSONObject;

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

    private static int current_symbol;
    private static String rux_algorithm;

    public static int rux_symbol;

    public static int player_symbol;

    private static final String RUX_FINISHED = "RUX";
    private static final String PLAYER_FINISHED = "PLAYER";
    private static final String DRAW_FINISHED = "DRAW";
    private static final String NOBODY_FINISHED = "NONE";

    // Game modes
    private static final String AI = "AI";
    private static final String ALGO = "ALGO";

    // API
    private Api api;

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

    public void updateCell(Cell cell){
        if(current_symbol == player_symbol){
            if(cell.getView().getText().equals("")){
                cell.getView().setText(player_symbol == Game.X ? "X" : "O");
                String[] ij = cell.getId().split("_");
                int i = Integer.valueOf(ij[0]);
                int j = Integer.valueOf(ij[1]);
                game.getGrid()[i][j] = (player_symbol == Game.X) ? Game.X : Game.O;
                /*
                 * 1. Check if there is a winner or if the grid is full
                 * 2. If false rux plays
                 */
                rux_plays();
            }
        }
    }

    private void setCurrentPlayerTurnVisibility(){
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
        /*
         * 1. Send the grid lastly updated to the assistant
         * 2. Add face Rux thinking
         * 2. Get his move
         * 3. Check if there is a winner
         */
        if( rux_algorithm.equals(AI) ){

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GameOver gameOver;
                        try {
                            String requestBody = "{\"model\": \"gpt-4o\", \"messages\": [{\"role\": \"system\", \"content\": \"" + getProperty(API, "SYSTEM_PROMPT") + "\"},{\"role\": \"user\", \"content\": \"" + game.getGridForRequest() + "\", \"symbol\": " + rux_symbol + "}]}";
                            String response = api.sendRequest(requestBody);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray choices = jsonObject.getJSONArray("choices");
                            JSONObject firstChoice = choices.getJSONObject(0);
                            JSONObject message = firstChoice.getJSONObject("message");
                            JSONObject content = new JSONObject(message.getString("content").replace("```json","").replace("```", ""));
                            String[] coordinates = content.getString("coordinates").split(",");
                            game.getCells().get("cell_" + coordinates[0] + coordinates[1]).getView().setText(rux_symbol == Game.X ? "X" : "O");
                            game.getGrid()[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] = rux_symbol;
                            switch (checkFinished()){
                                case NOBODY_FINISHED:
                                    sharedServices.getRobotService().robotPlayTTs(content.getString("message"));
                                    current_symbol = player_symbol;
                                    setCurrentPlayerTurnVisibility();
                                    break;
                                case RUX_FINISHED:
                                    gameOver = new GameOver(RUX_FINISHED,content.getString("message"), "r_robot_won");
                                    goToEndActivity(gameOver);
                                    break;
                                case PLAYER_FINISHED:
                                    gameOver = new GameOver(PLAYER_FINISHED,content.getString("message"), "r_player_won");
                                    goToEndActivity(gameOver);
                                    break;
                                case DRAW_FINISHED:
                                    gameOver = new GameOver(DRAW_FINISHED,content.getString("message"), "r_noone_won");
                                    goToEndActivity(gameOver);
                                    break;
                            }
                        } catch (Exception e){
                            Log.e(LOGGER_KEY, e.getMessage());
                        }
                    }
                }).start();
        } else {

        }
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
            return DRAW_FINISHED;
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

    public void goToEndActivity(GameOver gameOver) {
        Intent intent = new Intent(GameActivity.this, EndActivity.class);
        intent.putExtra("gameOver", gameOver);
        startActivity(intent);
    }

    public void goToStartActivity() {
        Intent intent = new Intent(GameActivity.this, StartActivity.class);
        startActivity(intent);
    }

    @Override
    public void play() {
        Log.d(LOGGER_KEY, "play");
        rux_algorithm = AI;
        game = new Game(getResources(), getPackageName(), this);
        if( rux_algorithm.equals(AI) ){
            api = new Api(this);
            api.loadClient();
        }
        pickRandomPlayers();
        setCurrentPlayerTurnVisibility();
        // Cheat
        current_symbol = rux_symbol;
        if (current_symbol == rux_symbol){
            player1.setText("Rux");
            player2.setText("Your");
            rux_plays();
        } else {
            player1.setText("Your");
            player2.setText("Rux");
        }

        player1_symbol.setText(current_symbol == Game.X ? "X" : "O");
        player2_symbol.setText(current_symbol == Game.X ? "O" : "X");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( rux_algorithm.equals(AI) && api.isClientLoaded() ){
            api.closeClient();
        }
    }
}