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
import com.cybridz.ruxtictactoe.enums.Preferences;
import com.cybridz.ruxtictactoe.enums.PropertyType;
import com.cybridz.ruxtictactoe.helpers.Api;
import com.cybridz.ruxtictactoe.helpers.PromptHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private static String currentSymbol;
    private static String ruxAlgorithm;
    public static String ruxSymbol;
    public static String playerSymbol;

    public int retry;

    /**
     * Needed classes
     */
    private Api api;

    private final Random random = new Random();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializePreferences();
        setContentView(R.layout.game_activity);
        initializeServices();
        initializeActivityElements();
    }

    public void initializePreferences() {
        super.initializePreferences();
        ruxAlgorithm =  preferences.getBoolean(Preferences.AI_KEY, Preferences.DEFAULT_USE_AI) ? GameMode.AI.getValue() : GameMode.ALGO.getValue();
        ruxSymbol = emojiList[preferences.getInt(Preferences.RUX_KEY, Preferences.DEFAULT_RUX_POSITION)];
        playerSymbol = emojiList[preferences.getInt(Preferences.PLAYER_KEY, Preferences.DEFAULT_PLAYER_POSITION)];
    }

    @SuppressLint("SetTextI18n")
    public void initializeActivityElements() {
        current_view = findViewById(R.id.game_activity);
        backButton = findViewById(R.id.back_btn);
        backButton.setText("Back");
        backButton.setOnClickListener(view -> goToStartActivity());
        player1View = findViewById(R.id.player1);
        player1SymbolView = findViewById(R.id.player1_symbol);
        player2View = findViewById(R.id.player2);
        player2SymbolView = findViewById(R.id.player2_symbol);
        player1View.setText("Rux");
        player2View.setText("Your");
    }

    public void initializeGame() {
        game = new Game(getResources(), getPackageName(), this, 3);
        setRandomPlayerTurn();
        player1SymbolView.setText(ruxSymbol);
        player2SymbolView.setText(playerSymbol);
        Game.playerSymbol = playerSymbol;
        Game.ruxSymbol = ruxSymbol;
        api = new Api(this);
        api.loadClient();
        if (ruxAlgorithm.equals(GameMode.AI.getValue())) {
            api.addPromptToHistory("system", PromptHelper.makeJsonLine("system", getProperty(PropertyType.API, "SYSTEM_PROMPT")));
            api.addPromptToHistory("user", PromptHelper.makeJsonLine("user", getProperty(PropertyType.API, "GAME_RULES_PROMPT")));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void play() {

        // Motor and lights
        sharedServices.startServices();
        sharedServices.getMotorRotationMessageService().sendRandomMotorRotation();
        sharedServices.getBlinkingLightMessageService().setRandomEarColor();
        sharedServices.getBlinkingLightMessageService().start();

        // GAME
        initializeGame();
        setCurrentPlayerTurnVisibility();
        String beginMessage = currentSymbol.equals(ruxSymbol) ? "Rux begin with " + ruxSymbol : "You begin with " + playerSymbol;
        sharedServices.getRobotService().robotPlayTTs(beginMessage);
        if(currentSymbol.equals(ruxSymbol)) {
            rux_plays(true);
        }
    }

    private void setCurrentPlayerTurnVisibility() {
        int activeColor = Color.rgb(29, 161, 242);
        int normalColor = Color.rgb(101, 119, 134);
        player1View.setTextColor(normalColor);
        player2View.setTextColor(normalColor);
        player1View.setTypeface(null, Typeface.NORMAL);
        player2View.setTypeface(null, Typeface.NORMAL);
        player1SymbolView.setTextColor(normalColor);
        player2SymbolView.setTextColor(normalColor);
        player1SymbolView.setTextSize(18);
        player2SymbolView.setTextSize(18);
        player1SymbolView.setTypeface(null, Typeface.NORMAL);
        player2SymbolView.setTypeface(null, Typeface.NORMAL);
        TextView currentPlayerView = currentSymbol.equals(ruxSymbol) ? player1View : player2View;
        TextView currentPlayerSymbolView = currentSymbol.equals(ruxSymbol) ? player1SymbolView : player2SymbolView;
        currentPlayerView.setTextColor(activeColor);
        currentPlayerView.setTypeface(null, Typeface.BOLD);
        currentPlayerSymbolView.setTextColor(activeColor);
        currentPlayerSymbolView.setTypeface(null, Typeface.BOLD);
        currentPlayerSymbolView.setTextSize(26);
    }

    public void player_played(Cell cell, int i, int j) {
        if (currentSymbol.equals(playerSymbol) && game.getGrid()[i][j] == Game.UNSET_VALUE) {
            game.increasePlayed();
            game.addToGrid(cell, Game.PLAYER_VALUE, true);
            String win = checkWinner(true);
            if (win.equals(GameStatus.NOBODY_FINISHED)) {
                rux_plays( false);
            }
        }
    }

    private void rux_plays(boolean muted) {
        if( !muted ){
            sharedServices.getRobotService().robotPlayTTs("My turn");
            setCurrentPlayerTurnVisibility();
            game.setEmptySlots(game.getGrid());
        }
        if (ruxAlgorithm.equals(GameMode.AI.getValue()) && retry < 4) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Callable<String> callable = () -> {
                String grid = game.getGridForRequest();
                String response;
                response = api.sendRequest(new String[] {"user"}, new String[] {PromptHelper.ruxPlayPrompt(grid, Game.RUX_VALUE, Game.PLAYER_VALUE)});
                String[] coordinates = getCoordinates(response);
                Cell cell = game.getCells().get("cell_" + coordinates[0] + coordinates[1]);
                if (game.getGrid()[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] != Game.UNSET_VALUE) {
                    Log.d(LOGGER_KEY, "Cell already taken");
                    retry++;
                    rux_plays(true);
                    return null;
                }
                assert cell != null;
                game.increasePlayed();
                game.addToGrid(cell, Game.RUX_VALUE, true);
                String finished = checkWinner(false);
                if(finished.equals(GameStatus.NOBODY_FINISHED)){
                    currentSymbol = playerSymbol;
                    setCurrentPlayerTurnVisibility();
                }
                retry = 0;
                return null;
            };
            Log.d(LOGGER_KEY, "Playing AI");
            executor.submit(callable);
            executor.shutdown();
        } else {
            Cell cell;
            game.setEmptySlots(game.getGrid());
            ArrayList<Cell> es = game.getEmptySpots();
            cell = checkCanWin(es);
            if (cell == null) {
                cell = checkBlockOpponent(es);
            }
            if (cell == null) {
                cell = getRandomSlot();
            }
            game.increasePlayed();
            game.addToGrid(cell, Game.RUX_VALUE, true);
            String finished = checkWinner(false);
            if(finished.equals(GameStatus.NOBODY_FINISHED)){
                currentSymbol = playerSymbol;
                setCurrentPlayerTurnVisibility();
            }
            Log.d(LOGGER_KEY, "Playing Algo");
            retry = 0;
        }
    }

    public String checkWinner(boolean muted) {
        if (game.getPlayed() < 5) {
            return GameStatus.NOBODY_FINISHED;
        }
        game.setEmptySlots(game.getGrid());
        String winner = game.getWinner();
        Log.d(LOGGER_KEY, winner);
        GameOver gameOver;
        switch (winner){
            case GameStatus.RUX_FINISHED:
                gameOver = new GameOver(GameStatus.RUX_FINISHED, makeMessage(winner), "r_robot_won");
                goToEndActivity(gameOver);
                break;
            case GameStatus.PLAYER_FINISHED:
                gameOver = new GameOver(GameStatus.PLAYER_FINISHED, makeMessage(winner), "r_player_won");
                goToEndActivity(gameOver);
                break;
            case GameStatus.DRAW_FINISHED:
                gameOver = new GameOver(GameStatus.DRAW_FINISHED, makeMessage(winner), "r_noone_won");
                goToEndActivity(gameOver);
                break;
            default:
                if(currentSymbol.equals(playerSymbol) && !muted) {
                    sharedServices.getRobotService().robotPlayTTs("Your turn");
                }
                break;
        }
        return winner;
    }

    public Cell checkCanWin(ArrayList<Cell> emptySlots){
        for (int e = 0; e < emptySlots.size(); e++) {
            Cell cell = emptySlots.get(e);
            game.addToGrid(cell, Game.RUX_VALUE, false);
            String gameWinner = game.getWinner();
            if (gameWinner.equals(GameStatus.RUX_FINISHED)) {
                cell = emptySlots.get(e);
                game.removeFromGrid(cell, false);
                Log.d(LOGGER_KEY, "Can win: " + cell.getId());
                return cell;
            }
            game.removeFromGrid(cell, false);
        }
        return null;
    }

    public Cell checkBlockOpponent(ArrayList<Cell> emptySlots){
        for (int e = 0; e < emptySlots.size(); e++) {
            Cell cell = emptySlots.get(e);
            game.addToGrid(cell, Game.PLAYER_VALUE, false);
            String gameWinner = game.getWinner();
            if (gameWinner.equals(GameStatus.PLAYER_FINISHED)) {
                cell = emptySlots.get(e);
                game.removeFromGrid(cell, false);
                Log.d(LOGGER_KEY, "Block opponent: " + cell.getId());
                return cell;
            }
            game.removeFromGrid(cell, false);
        }
        return null;
    }

    public Cell getRandomSlot() {
        return game.getEmptySpots().get(random.nextInt(game.getEmptySpots().size()));
    }

    private static String[] getCoordinates(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray choices = jsonObject.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        JSONObject content = new JSONObject(message.getString("content").replace("```json", "").replace("```", ""));
        return content.getString("coordinates").split(";");
    }

    private String makeMessage(String winner) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        api.clearHistory();
        Callable<String> callable = () -> {
            try {
                String system_prompt = getProperty(PropertyType.API, "SYSTEM_PROMPT_ENDING_MESSAGE");
                String user_prompt = "";
                switch (winner) {
                    case GameStatus.RUX_FINISHED:
                        user_prompt += "Rux win Tic Tac Toe.";
                        break;
                    case GameStatus.PLAYER_FINISHED:
                        user_prompt += "Player win Tic Tac Toe.";
                        break;
                    case GameStatus.DRAW_FINISHED:
                        user_prompt += "No one win Tic Tac Toe, means neither Rux neither Player won the game.";
                        break;
                    default:
                        user_prompt += "The game is still in progress and it's your turn to play.";
                        break;
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

    private void setRandomPlayerTurn(){
        currentSymbol = random.nextInt(2) + 1 == 1 ? ruxSymbol : playerSymbol;
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