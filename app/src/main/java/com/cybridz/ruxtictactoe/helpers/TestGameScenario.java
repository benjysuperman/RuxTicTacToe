package com.cybridz.ruxtictactoe.helpers;

import static com.cybridz.AbstractActivity.LOGGER_KEY;

import android.util.Log;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.Game;
import com.cybridz.ruxtictactoe.GameActivity;
import com.cybridz.ruxtictactoe.components.Cell;

import java.util.Map;
import java.util.Objects;

public class TestGameScenario {

    private final Game game;

    public TestGameScenario(Game game){
        this.game = game;
    }

    public static void printResults(int symbol, Game game){
        Log.d(LOGGER_KEY, "Rows : " + game.checkRowForWin(symbol));
        Log.d(LOGGER_KEY, "Columns : " +  game.checkColumnForWin(symbol));
        Log.d(LOGGER_KEY, "Diagonale : " + game.checkDiagonalForWin(symbol));
        Log.d(LOGGER_KEY, "Inverse diagonale : " + game.checkInverseDiagonalForWin(symbol));
    }

    private String getSymbolStr(int symbol){
        return symbol == GameActivity.ruxSymbol ? (GameActivity.ruxSymbol == Game.X ? "X" : "O") : (GameActivity.playerSymbol == Game.X ? "X" : "O");
    }

    private int getSymbolInt(int symbol){
        return symbol == GameActivity.ruxSymbol ? (GameActivity.ruxSymbol == Game.X ? Game.X : Game.O) : (GameActivity.playerSymbol == Game.X ? Game.X : Game.O);
    }

    public static void checkTestGame(Game game, int ruxSymbol, int playerSymbol, int symbol, boolean do_full){
        if(AbstractActivity.isTestMode()){
            TestGameScenario testGameScenario= new TestGameScenario(game);

            Log.d(LOGGER_KEY, "Test row:\n=========\n");
            game.emptyBoard();
            testGameScenario.boardRowWin(symbol);
            Log.d(LOGGER_KEY, "Rux :\n=====\n");
            printResults(ruxSymbol, game);
            Log.d(LOGGER_KEY, "Player :\n========\n");
            printResults(playerSymbol, game);
            Log.d(LOGGER_KEY, "Status : " + game.checkFinished(ruxSymbol, playerSymbol));
            Log.d(LOGGER_KEY, "\n\n");

            Log.d(LOGGER_KEY, "Test column:\n============\n");
            game.emptyBoard();
            testGameScenario.boardColumnsWin(symbol);
            Log.d(LOGGER_KEY, "Rux :\n=====\n");
            printResults(ruxSymbol, game);
            Log.d(LOGGER_KEY, "Player :\n========\n");
            printResults(playerSymbol, game);
            Log.d(LOGGER_KEY, "Status : " + game.checkFinished(ruxSymbol, playerSymbol));
            Log.d(LOGGER_KEY, "\n\n");

            Log.d(LOGGER_KEY, "Test diagonal:\n=============\n");
            game.emptyBoard();
            testGameScenario.boardDiagonalWin(symbol);
            Log.d(LOGGER_KEY, "Rux :\n=====\n");
            printResults(ruxSymbol, game);
            Log.d(LOGGER_KEY, "Player :\n========\n");
            printResults(playerSymbol, game);
            Log.d(LOGGER_KEY, "Status : " + game.checkFinished(ruxSymbol, playerSymbol));
            Log.d(LOGGER_KEY, "\n\n");

            Log.d(LOGGER_KEY, "Test inverse diagonal:\n=====================\n");
            game.emptyBoard();
            testGameScenario.boardInverseDiagonalWin(symbol);
            Log.d(LOGGER_KEY, "Rux :\n=====\n");
            printResults(ruxSymbol, game);
            Log.d(LOGGER_KEY, "Player :\n========\n");
            printResults(playerSymbol, game);
            Log.d(LOGGER_KEY, "Status : " + game.checkFinished(ruxSymbol, playerSymbol));
            Log.d(LOGGER_KEY, "\n\n");

            if(do_full){
                Log.d(LOGGER_KEY, "Grid:\n=====\n");
                game.emptyBoard();
                testGameScenario.boardFullGrid();
                Log.d(LOGGER_KEY, "Rux :\n=====\n");
                printResults(ruxSymbol, game);
                Log.d(LOGGER_KEY, "Player :\n========\n");
                printResults(playerSymbol, game);
                Log.d(LOGGER_KEY, "Status : " + game.checkFinished(ruxSymbol, playerSymbol));
                Log.d(LOGGER_KEY, "\n\n");

                Log.d(LOGGER_KEY, "Grid:\n=====\n");
                game.emptyBoard();
                testGameScenario.boardPending();
                Log.d(LOGGER_KEY, "Rux :\n=====\n");
                printResults(ruxSymbol, game);
                Log.d(LOGGER_KEY, "Player :\n========\n");
                printResults(playerSymbol, game);
                Log.d(LOGGER_KEY, "Status : " + game.checkFinished(ruxSymbol, playerSymbol));
                Log.d(LOGGER_KEY, "\n\n");
            }
        }
    }

    public void boardRowWin(int symbol){
        Map<String, Cell> cells = game.getCells();
        int[][] grid = game.getGrid();
        Objects.requireNonNull(cells.get("cell_10")).getView().setText(getSymbolStr(symbol));
        Objects.requireNonNull(cells.get("cell_11")).getView().setText(getSymbolStr(symbol));
        Objects.requireNonNull(cells.get("cell_12")).getView().setText(getSymbolStr(symbol));
        grid[1][0] = getSymbolInt(symbol);
        grid[1][1] = getSymbolInt(symbol);
        grid[1][2] = getSymbolInt(symbol);
    }

    public void boardFullGrid(){
        Map<String, Cell> cells = game.getCells();
        int[][] grid = game.getGrid();
        Objects.requireNonNull(cells.get("cell_00")).getView().setText("X");
        Objects.requireNonNull(cells.get("cell_01")).getView().setText("X");
        Objects.requireNonNull(cells.get("cell_02")).getView().setText("O");
        Objects.requireNonNull(cells.get("cell_10")).getView().setText("O");
        Objects.requireNonNull(cells.get("cell_11")).getView().setText("O");
        Objects.requireNonNull(cells.get("cell_12")).getView().setText("X");
        Objects.requireNonNull(cells.get("cell_20")).getView().setText("X");
        Objects.requireNonNull(cells.get("cell_21")).getView().setText("O");
        Objects.requireNonNull(cells.get("cell_22")).getView().setText("X");
        grid[0][0] = Game.X;
        grid[0][1] = Game.X;
        grid[0][2] = Game.O;
        grid[1][0] = Game.O;
        grid[1][1] = Game.O;
        grid[1][2] = Game.X;
        grid[2][0] = Game.X;
        grid[2][1] = Game.O;
        grid[2][2] = Game.X;
    }

    public void boardColumnsWin(int symbol){
        Map<String, Cell> cells = game.getCells();
        int[][] grid = game.getGrid();
        Objects.requireNonNull(cells.get("cell_01")).getView().setText(getSymbolStr(symbol));
        Objects.requireNonNull(cells.get("cell_11")).getView().setText(getSymbolStr(symbol));
        Objects.requireNonNull(cells.get("cell_21")).getView().setText(getSymbolStr(symbol));
        grid[0][1] = getSymbolInt(symbol);
        grid[1][1] = getSymbolInt(symbol);
        grid[2][1] = getSymbolInt(symbol);
    }

    public void boardPending(){
        Map<String, Cell> cells = game.getCells();
        int[][] grid = game.getGrid();
        Objects.requireNonNull(cells.get("cell_01")).getView().setText("X");
        Objects.requireNonNull(cells.get("cell_11")).getView().setText("O");
        Objects.requireNonNull(cells.get("cell_21")).getView().setText("X");
        grid[0][1] = Game.X;
        grid[1][1] = Game.O;
        grid[2][1] = Game.X;
    }

    public void boardDiagonalWin(int symbol){
        Map<String, Cell> cells = game.getCells();
        int[][] grid = game.getGrid();
        Objects.requireNonNull(cells.get("cell_00")).getView().setText(getSymbolStr(symbol));
        Objects.requireNonNull(cells.get("cell_11")).getView().setText(getSymbolStr(symbol));
        Objects.requireNonNull(cells.get("cell_22")).getView().setText(getSymbolStr(symbol));
        grid[0][0] = getSymbolInt(symbol);
        grid[1][1] = getSymbolInt(symbol);
        grid[2][2] = getSymbolInt(symbol);
    }

    public void boardInverseDiagonalWin(int symbol){
        Map<String, Cell> cells = game.getCells();
        int[][] grid = game.getGrid();
        Objects.requireNonNull(cells.get("cell_02")).getView().setText(getSymbolStr(symbol));
        Objects.requireNonNull(cells.get("cell_11")).getView().setText(getSymbolStr(symbol));
        Objects.requireNonNull(cells.get("cell_20")).getView().setText(getSymbolStr(symbol));
        grid[0][2] = getSymbolInt(symbol);
        grid[1][1] = getSymbolInt(symbol);
        grid[2][0] = getSymbolInt(symbol);
    }
}
