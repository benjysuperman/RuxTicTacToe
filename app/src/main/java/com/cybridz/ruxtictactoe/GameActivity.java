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
import com.cybridz.ruxtictactoe.enums.PropertyType;
import com.cybridz.ruxtictactoe.helpers.Api;
import com.cybridz.ruxtictactoe.helpers.PromptHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        initializeServices();
        backButton = findViewById(R.id.back_btn);
        backButton.setText("Back");
        backButton.setOnClickListener(view -> goToStartActivity());
        player1View = findViewById(R.id.player1);
        player1SymbolView = findViewById(R.id.player1_symbol);
        player2View = findViewById(R.id.player2);
        player2SymbolView = findViewById(R.id.player2_symbol);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void play() {
        sharedServices.startServices();
        sharedServices.getMotorRotationMessageService().sendRandomMotorRotation();
        sharedServices.getBlinkingLightMessageService().setRandomEarColor();
        sharedServices.getBlinkingLightMessageService().start();
        ruxAlgorithm = GameMode.AI.getValue();
        game = new Game(getResources(), getPackageName(), this);
        if (ruxAlgorithm.equals(GameMode.AI.getValue())) {
            api = new Api(this);
            api.loadClient();
            api.addPromptToHistory("system", PromptHelper.makeJsonLine("system", getProperty(PropertyType.API, "SYSTEM_PROMPT")));
            api.addPromptToHistory("user", PromptHelper.makeJsonLine("user", getProperty(PropertyType.API, "GAME_RULES_PROMPT")));
        }
        pickRandomPlayers();
        player1View.setText("Rux");
        player2View.setText("Your");
        player1SymbolView.setText(ruxSymbol == Game.X ? Game.XStr : Game.OStr);
        player2SymbolView.setText(playerSymbol == Game.X ? Game.XStr : Game.OStr);
        setCurrentPlayerTurnVisibility();
        String beginMessage = "Let's begin by " +
                (currentSymbol == ruxSymbol ? "Me with the letter : " + (ruxSymbol == Game.X ? Game.XStr : Game.OStr) :
                "You with the letter : " + (playerSymbol == Game.X ? Game.XStr : Game.OStr));
        sharedServices.getRobotService().robotPlayTTs(beginMessage);

        if(currentSymbol == ruxSymbol) {
            rux_plays(false, true);
        }
    }

    private void setCurrentPlayerTurnVisibility() {
        if (currentSymbol == ruxSymbol) {
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
        } else {
            player2View.setTextColor(Color.rgb(29, 161, 242));
            player2View.setTypeface(null, Typeface.BOLD);
            player2SymbolView.setTextColor(Color.rgb(29, 161, 242));
            player2SymbolView.setTypeface(null, Typeface.BOLD);
            player2SymbolView.setTextSize(26);

            player1View.setTextColor(Color.rgb(101, 119, 134));
            player1View.setTypeface(null, Typeface.NORMAL);
            player1SymbolView.setTextColor(Color.rgb(101, 119, 134));
            player1SymbolView.setTypeface(null, Typeface.NORMAL);
            player1SymbolView.setTextSize(18);
        }
    }

    public void player_played(Cell cell, int i, int j) {
        if (currentSymbol == playerSymbol && game.getGrid()[i][j] == Game.UNSET) {
            game.updateGrid(cell, playerSymbol);
            Log.d(GameActivity.LOGGER_KEY, "Grid before checking win: " + game.getFormattedGrid());
            boolean win = checkWinner(true);
            Log.d(GameActivity.LOGGER_KEY, "Is a win ? : " + win);
            if (!win) {
                rux_plays(false, false);
            }
        }
    }

    private void rux_plays(boolean isRetry, boolean muted) {
        if( !muted ){
            sharedServices.getRobotService().robotPlayTTs("My turn");
        }
        if (ruxAlgorithm.equals(GameMode.AI.getValue())) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Callable<String> callable = () -> {
                String grid = game.getGridForRequest();
                String response;
                if (isRetry) {;
                    response = api.sendRequest(new String[] {"user"}, new String[] {PromptHelper.ruxLearnPrompt(grid, ruxSymbol, playerSymbol)});
                    Log.d(LOGGER_KEY, response);
                    rux_plays(false, true);
                } else {
                    response = api.sendRequest(new String[] {"user"}, new String[] {PromptHelper.ruxPlayPrompt(grid, ruxSymbol, playerSymbol)});
                    String[] coordinates = getCoordinates(response);
                    Cell cell = game.getCells().get("cell_" + coordinates[0] + coordinates[1]);
                    if (game.getGrid()[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] != Game.UNSET) {
                        Log.d(LOGGER_KEY, "Cell already taken");
                        rux_plays(false, true);
                        return null;
                    }
                    assert cell != null;
                    game.updateGrid(cell, ruxSymbol);
                    boolean finished = checkWinner(false);
                    if(!finished){
                        currentSymbol = playerSymbol;
                    }
                }
                return null;
            };
            executor.submit(callable);
            executor.shutdown();
        } else {
            Log.d(LOGGER_KEY, "With min max algorithm selected");
        }
    }

    public boolean checkWinner(boolean muted) {
        winner = game.checkFinished(ruxSymbol, playerSymbol);
        GameOver gameOver;
        boolean finished = false;
        switch (winner){
            case GameStatus.RUX_FINISHED:
                gameOver = new GameOver(GameStatus.RUX_FINISHED, makeMessage(), "r_robot_won");
                goToEndActivity(gameOver);
                finished = true;
                break;
            case GameStatus.PLAYER_FINISHED:
                gameOver = new GameOver(GameStatus.PLAYER_FINISHED, makeMessage(), "r_player_won");
                goToEndActivity(gameOver);
                finished = true;
                break;
            case GameStatus.DRAW_FINISHED:
                gameOver = new GameOver(GameStatus.DRAW_FINISHED, makeMessage(), "r_noone_won");
                goToEndActivity(gameOver);
                finished = true;
                break;
            default:
                if(currentSymbol == playerSymbol && !muted) {
                    sharedServices.getRobotService().robotPlayTTs("Your turn");
                }
                break;
        }
        return finished;
    }

    private static String[] getCoordinates(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray choices = jsonObject.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        JSONObject content = new JSONObject(message.getString("content").replace("```json", "").replace("```", ""));
        return content.getString("coordinates").split(";");
    }

    private String makeMessage() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        api.clearHistory();
        Callable<String> callable = () -> {
            try {
                String system_prompt = getProperty(PropertyType.API, "SYSTEM_PROMPT_ENDING_MESSAGE");
                String user_prompt = "";
                if (winner.equals(GameStatus.RUX_FINISHED)) {
                    user_prompt += "Rux win Tic Tac Toe.";
                } else if (winner.equals(GameStatus.PLAYER_FINISHED)) {
                    user_prompt += "Player win Tic Tac Toe.";
                } else if (winner.equals(GameStatus.DRAW_FINISHED)) {
                    user_prompt += "No one win Tic Tac Toe, means neither Rux neither Player won the game.";
                } else {
                    user_prompt += "The game is still in progress and it's your turn to play.";
                }
                api.addPromptToHistory("system",  PromptHelper.makeJsonLine("system", system_prompt));
                String response = api.sendRequest(new String[] {"user"}, new String[] {PromptHelper.makeJsonLine("user", user_prompt)});
                JSONObject jsonObject = new JSONObject(response);
                JSONArray choices = jsonObject.getJSONArray("choices");
                JSONObject firstChoice = choices.getJSONObject(0);
                return firstChoice.getJSONObject("message").getString("content");
            } catch (Exception e) {
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
    protected void onDestroy() {
        super.onDestroy();
        if (ruxAlgorithm.equals(GameMode.AI.getValue()) && api.isClientLoaded()) {
            api.closeClient();
        }
    }
}