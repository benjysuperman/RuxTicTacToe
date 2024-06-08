package com.cybridz.ruxtictactoe;

import android.annotation.SuppressLint;
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
import com.cybridz.ruxtictactoe.enums.GameMode;
import com.cybridz.ruxtictactoe.enums.GameStatus;
import com.cybridz.ruxtictactoe.helpers.Api;
import com.cybridz.ruxtictactoe.helpers.PromptHelper;
import com.cybridz.ruxtictactoe.helpers.TestGameScenario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameActivity extends AbstractActivity {

    /**
     * Game Activity elements
     */
    @SuppressWarnings("FieldCanBeLocal")
    private Button backButton;
    private TextView player1View;
    private TextView player1SymbolView;
    private TextView player2View;
    private TextView player2SymbolView;

    /**
     * Game Control & States
     */
    private static Game game;
    private static int currentSymbol;
    private static String ruxAlgorithm;
    public static int ruxSymbol;
    public static int playerSymbol;
    private String winner;

    /**
     * Needed classes
     */
    private Api api;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        current_view = findViewById(R.id.game_activity);
        initializeServicesIfNeeded();

        backButton = findViewById(R.id.back_btn);
        backButton.setText("Back");
        backButton.setOnClickListener(view -> {
            Log.d(LOGGER_KEY, "clicked back btn");
            goToStartActivity();
        });

        player1View = findViewById(R.id.player1);
        player1SymbolView = findViewById(R.id.player1_symbol);

        player2View = findViewById(R.id.player2);
        player2SymbolView = findViewById(R.id.player2_symbol);


        sharedServices.getMotorRotationMessageService().sendRandomMotorRotation();
        sharedServices.getBlinkingLightMessageService().setRandomEarColor();
        sharedServices.getBlinkingLightMessageService().start();

        play();

        TestGameScenario.checkTestGame(game, ruxSymbol, playerSymbol, ruxSymbol, true);
        TestGameScenario.checkTestGame(game, ruxSymbol, playerSymbol, playerSymbol, false);
    }

    public void updateCell(Cell cell){
        if(currentSymbol == playerSymbol){
            if(cell.getView().getText().equals("")){
                cell.getView().setText(playerSymbol == Game.X ? "X" : "O");
                game.updateGrid(cell, (playerSymbol == Game.X) ? Game.X : Game.O);
                rux_plays();
            }
        }
    }

    private void setCurrentPlayerTurnVisibility(){
            player1View.setTextColor(Color.rgb(29, 161, 242));
            player1View.setTypeface(null, Typeface.BOLD);
            player1SymbolView.setTextColor(Color.rgb(29, 161, 242));
            player1SymbolView.setTypeface(null, Typeface.BOLD);
            player1SymbolView.setTextSize(26);

            player2View.setTextColor(Color.rgb(101, 119, 134));
            player2View.setTypeface(null, Typeface.NORMAL);
            player2SymbolView.setTextColor(Color.rgb(101, 119, 134));
            player2SymbolView.setTypeface(null, Typeface.NORMAL);
            player2SymbolView.setTextSize(18);
    }

    private void rux_plays(){
        /*
         * 1. Send the grid lastly updated to the assistant
         * 2. Add face Rux thinking
         * 2. Get his move
         * 3. Check if there is a winner
         */
        if( ruxAlgorithm.equals(GameMode.AI.getValue()) ){
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Callable<String> callable = () -> {
                GameOver gameOver;
                String system_prompt = getProperty(API, "SYSTEM_PROMPT").replace("\"", "\\\"");
                String grid = game.getGridForRequest();
                String requestBody = PromptHelper.ruxPlayRequestBody(system_prompt, grid, ruxSymbol, playerSymbol);
                String response = api.sendRequest(requestBody);
                String[] coordinates = getCoordinates(response);
                Cell cell = game.getCells().get("cell_" + coordinates[0] + coordinates[1]);
                Objects.requireNonNull(cell).getView().setText(ruxSymbol == Game.X ? "X" : "O");
                game.updateGrid(cell, (ruxSymbol == Game.X) ? Game.X : Game.O);
                winner = game.checkFinished(ruxSymbol, playerSymbol);
                Log.d(LOGGER_KEY, "Grid : " + grid);
                Log.d(LOGGER_KEY, "Winner : " + winner);
                Log.d(LOGGER_KEY, "Message : " + Arrays.asList(coordinates));
                String winnerMessage = makeMessage();
                if(winner.equals(GameStatus.RUX_FINISHED.getValue())){
                    gameOver = new GameOver(GameStatus.RUX_FINISHED.getValue(),winnerMessage, "r_robot_won");
                    goToEndActivity(gameOver);
                } else if (winner.equals(GameStatus.PLAYER_FINISHED.getValue())) {
                    gameOver = new GameOver(GameStatus.RUX_FINISHED.getValue(),winnerMessage, "r_robot_won");
                    goToEndActivity(gameOver);
                } else if (winner.equals(GameStatus.DRAW_FINISHED.getValue())) {
                    gameOver = new GameOver(GameStatus.RUX_FINISHED.getValue(),winnerMessage, "r_noone_won");
                    goToEndActivity(gameOver);
                } else {
                    sharedServices.getRobotService().robotPlayTTs(winnerMessage);
                    currentSymbol = playerSymbol;
                    setCurrentPlayerTurnVisibility();
                }
                return null;
            };
            executor.submit(callable);
            executor.shutdown();
        } else {
            Log.d(LOGGER_KEY, "With min max algorithm selected");
        }
    }

    private static String[] getCoordinates(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray choices = jsonObject.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        JSONObject content = new JSONObject(message.getString("content").replace("```json","").replace("```", ""));
        return content.getString("coordinates").split(";");
    }

    private String makeMessage(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> callable = () -> {
            try {
                String system_prompt = "You make a message for a game of maximum 100 characters";
                String user_prompt = "Please make a message of maximum 100 characters to signify :";
                if(winner.equals(GameStatus.RUX_FINISHED.toString())){
                    user_prompt += "Rux won Tic Tac Toe, means you won the game.";
                } else if (winner.equals(GameStatus.PLAYER_FINISHED.toString())) {
                    user_prompt += "Player won Tic Tac Toe, means your opponent won the game.";
                } else if (winner.equals(GameStatus.DRAW_FINISHED.toString())) {
                    user_prompt += "Draw none won Tic Tac Toe, means neither your opponent neither you won the game.";
                } else {
                    user_prompt += "The game is still in progress and it's your turn to play.";
                }
                String response = api.sendRequest(PromptHelper.getMessagePrompt(system_prompt,user_prompt));
                JSONObject jsonObject = new JSONObject(response);
                JSONArray choices = jsonObject.getJSONArray("choices");
                JSONObject firstChoice = choices.getJSONObject(0);
                return firstChoice.getJSONObject("message").getString("content");
            } catch (Exception e){
                Log.e(LOGGER_KEY, Objects.requireNonNull(e.getMessage()));
                return "";
            }
        };

        Future<String> future = executor.submit(callable);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(LOGGER_KEY, Objects.requireNonNull(e.getMessage()));
        } finally {
            executor.shutdown();
        }

        return "";
    }

    private void pickRandomPlayers() {
        Random random = new Random();
        currentSymbol = random.nextInt(2) + 1;
        ruxSymbol = random.nextInt(2) + 1;
        playerSymbol = ruxSymbol == 1 ? 2 : 1;
        Log.d(LOGGER_KEY, "Current player : "  + (currentSymbol == ruxSymbol ? "Rux" : "Player"));
        Log.d(LOGGER_KEY, "RUX plays with " + (ruxSymbol == 1 ? "O" : "X") + ", number: " + ruxSymbol);
        Log.d(LOGGER_KEY, "Player plays with " + (ruxSymbol == 1 ? "X" : "O") + ", number: " + playerSymbol);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void play() {
        Log.d(LOGGER_KEY, "play");
        ruxAlgorithm = GameMode.AI.getValue();
        game = new Game(getResources(), getPackageName(), this);
        if( ruxAlgorithm.equals(GameMode.AI.getValue()) ){
            api = new Api(this);
            api.loadClient();
        }
        pickRandomPlayers();
        setCurrentPlayerTurnVisibility();
        // Cheat
        currentSymbol = ruxSymbol;
        if (currentSymbol == ruxSymbol){
            player1View.setText("Rux");
            player2View.setText("Your");
            rux_plays();
        } else {
            player1View.setText("Your");
            player2View.setText("Rux");
        }

        player1SymbolView.setText(currentSymbol == Game.X ? "X" : "O");
        player2SymbolView.setText(currentSymbol == Game.X ? "O" : "X");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( ruxAlgorithm.equals(GameMode.AI.getValue()) && api.isClientLoaded() ){
            api.closeClient();
        }
    }
}