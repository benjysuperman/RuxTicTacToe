package com.cybridz.ruxtictactoe.helpers;

import android.view.View;
import android.widget.TextView;

import com.cybridz.ruxtictactoe.Game;
import com.cybridz.ruxtictactoe.GameActivity;
import com.cybridz.ruxtictactoe.components.Cell;

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
        Map<String, Cell> cells = game.getCells();
        int[][] grid = game.getGrid();
        cells.get("cell_10").getView().setText(getSymbolStr(symbol));
        cells.get("cell_11").getView().setText(getSymbolStr(symbol));
        cells.get("cell_12").getView().setText(getSymbolStr(symbol));
        grid[1][0] = getSymbolInt(symbol);
        grid[1][1] = getSymbolInt(symbol);
        grid[1][2] = getSymbolInt(symbol);
    }

    public void boardFullGrid(){
        Map<String, Cell> cells = game.getCells();
        int[][] grid = game.getGrid();
        cells.get("cell_00").getView().setText("X");
        cells.get("cell_01").getView().setText("X");
        cells.get("cell_02").getView().setText("O");
        cells.get("cell_10").getView().setText("O");
        cells.get("cell_11").getView().setText("O");
        cells.get("cell_12").getView().setText("X");
        cells.get("cell_20").getView().setText("X");
        cells.get("cell_21").getView().setText("O");
        cells.get("cell_22").getView().setText("X");
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
        cells.get("cell_01").getView().setText(getSymbolStr(symbol));
        cells.get("cell_11").getView().setText(getSymbolStr(symbol));
        cells.get("cell_21").getView().setText(getSymbolStr(symbol));
        grid[0][1] = getSymbolInt(symbol);
        grid[1][1] = getSymbolInt(symbol);
        grid[2][1] = getSymbolInt(symbol);
    }

    public void boardPending(){
        Map<String, Cell> cells = game.getCells();
        int[][] grid = game.getGrid();
        cells.get("cell_01").getView().setText("X");
        cells.get("cell_11").getView().setText("O");
        cells.get("cell_21").getView().setText("X");
        grid[0][1] = Game.X;
        grid[1][1] = Game.O;
        grid[2][1] = Game.X;
    }

    public void boardDiagonalWin(int symbol){
        Map<String, Cell> cells = game.getCells();
        int[][] grid = game.getGrid();
        cells.get("cell_00").getView().setText(getSymbolStr(symbol));
        cells.get("cell_11").getView().setText(getSymbolStr(symbol));
        cells.get("cell_22").getView().setText(getSymbolStr(symbol));
        grid[0][0] = getSymbolInt(symbol);
        grid[1][1] = getSymbolInt(symbol);
        grid[2][2] = getSymbolInt(symbol);
    }

    public void boardInverseDiagonalWin(int symbol){
        Map<String, Cell> cells = game.getCells();
        int[][] grid = game.getGrid();
        cells.get("cell_02").getView().setText(getSymbolStr(symbol));
        cells.get("cell_11").getView().setText(getSymbolStr(symbol));
        cells.get("cell_20").getView().setText(getSymbolStr(symbol));
        grid[0][2] = getSymbolInt(symbol);
        grid[1][1] = getSymbolInt(symbol);
        grid[2][0] = getSymbolInt(symbol);
    }
}
