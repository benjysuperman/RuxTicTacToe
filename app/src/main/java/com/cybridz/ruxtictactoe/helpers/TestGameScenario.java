package com.cybridz.ruxtictactoe.helpers;

import android.view.View;
import android.widget.TextView;

import com.cybridz.ruxtictactoe.Game;
import com.cybridz.ruxtictactoe.GameActivity;

import java.util.Map;

public class TestGameScenario {

    private final Game game;

    public TestGameScenario(Game game){
        this.game = game;
    }

    private String getSymbolStr(int symbol){
        return symbol == GameActivity.rux_symbol ? (GameActivity.rux_symbol == Game.X ? "X" : "O") : (GameActivity.player_symbol == Game.X ? "X" : "O");
    }

    private int getSymbolInt(int symbol){
        return symbol == GameActivity.rux_symbol ? (GameActivity.rux_symbol == Game.X ? Game.X : Game.O) : (GameActivity.player_symbol == Game.X ? Game.X : Game.O);
    }

    public void boardRowWin(int symbol){
        Map<String, View> cells = game.getCells();
        int[][] grid = game.getGrid();
        ((TextView) cells.get("cell_10")).setText(getSymbolStr(symbol));
        ((TextView) cells.get("cell_11")).setText(getSymbolStr(symbol));
        ((TextView) cells.get("cell_12")).setText(getSymbolStr(symbol));
        grid[1][0] = getSymbolInt(symbol);
        grid[1][1] = getSymbolInt(symbol);
        grid[1][2] = getSymbolInt(symbol);
    }

    public void boardFullGrid(){
        Map<String, View> cells = game.getCells();
        int[][] grid = game.getGrid();
        ((TextView) cells.get("cell_00")).setText("X");
        ((TextView) cells.get("cell_01")).setText("X");
        ((TextView) cells.get("cell_02")).setText("O");
        ((TextView) cells.get("cell_10")).setText("O");
        ((TextView) cells.get("cell_11")).setText("O");
        ((TextView) cells.get("cell_12")).setText("X");
        ((TextView) cells.get("cell_20")).setText("X");
        ((TextView) cells.get("cell_21")).setText("O");
        ((TextView) cells.get("cell_22")).setText("X");
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
        Map<String, View> cells = game.getCells();
        int[][] grid = game.getGrid();
        ((TextView) cells.get("cell_01")).setText(getSymbolStr(symbol));
        ((TextView) cells.get("cell_11")).setText(getSymbolStr(symbol));
        ((TextView) cells.get("cell_21")).setText(getSymbolStr(symbol));
        grid[0][1] = getSymbolInt(symbol);
        grid[1][1] = getSymbolInt(symbol);
        grid[2][1] = getSymbolInt(symbol);
    }

    public void boardPending(){
        Map<String, View> cells = game.getCells();
        int[][] grid = game.getGrid();
        ((TextView) cells.get("cell_01")).setText("X");
        ((TextView) cells.get("cell_11")).setText("O");
        ((TextView) cells.get("cell_21")).setText("X");
        grid[0][1] = Game.X;
        grid[1][1] = Game.O;
        grid[2][1] = Game.X;
    }

    public void boardDiagonalWin(int symbol){
        Map<String, View> cells = game.getCells();
        int[][] grid = game.getGrid();
        ((TextView) cells.get("cell_00")).setText(getSymbolStr(symbol));
        ((TextView) cells.get("cell_11")).setText(getSymbolStr(symbol));
        ((TextView) cells.get("cell_22")).setText(getSymbolStr(symbol));
        grid[0][0] = getSymbolInt(symbol);
        grid[1][1] = getSymbolInt(symbol);
        grid[2][2] = getSymbolInt(symbol);
    }

    public void boardInverseDiagonalWin(int symbol){
        Map<String, View> cells = game.getCells();
        int[][] grid = game.getGrid();
        ((TextView) cells.get("cell_02")).setText(getSymbolStr(symbol));
        ((TextView) cells.get("cell_11")).setText(getSymbolStr(symbol));
        ((TextView) cells.get("cell_20")).setText(getSymbolStr(symbol));
        grid[0][2] = getSymbolInt(symbol);
        grid[1][1] = getSymbolInt(symbol);
        grid[2][0] = getSymbolInt(symbol);
    }
}
