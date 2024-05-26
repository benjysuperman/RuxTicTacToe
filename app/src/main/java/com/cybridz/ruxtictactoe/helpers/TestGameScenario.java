package com.cybridz.ruxtictactoe.helpers;

import android.view.View;
import android.widget.TextView;

import com.cybridz.ruxtictactoe.Game;

import java.util.Map;

public class TestGameScenario {

    private final Game game;

    public TestGameScenario(Game game){
        this.game = game;
    }

    public void boardRowWin(){
        Map<String, View> cells = game.getCells();
        Integer[][] grid = game.getGrid();
        ((TextView) cells.get("cell_10")).setText("X");
        ((TextView) cells.get("cell_11")).setText("X");
        ((TextView) cells.get("cell_12")).setText("X");
        grid[1][0] = Game.X;
        grid[1][1] = Game.X;
        grid[1][2] = Game.X;
    }

    public void boardColumnsWin(){
        Map<String, View> cells = game.getCells();
        Integer[][] grid = game.getGrid();
        ((TextView) cells.get("cell_01")).setText("X");
        ((TextView) cells.get("cell_11")).setText("X");
        ((TextView) cells.get("cell_21")).setText("X");
        ((TextView) cells.get("cell_00")).setText("O");
        ((TextView) cells.get("cell_12")).setText("O");
        ((TextView) cells.get("cell_22")).setText("O");
        grid[0][1] = Game.X;
        grid[1][1] = Game.X;
        grid[2][1] = Game.X;
    }

    public void boardDiagonalWin(){
        Map<String, View> cells = game.getCells();
        Integer[][] grid = game.getGrid();
        ((TextView) cells.get("cell_00")).setText("X");
        ((TextView) cells.get("cell_11")).setText("X");
        ((TextView) cells.get("cell_22")).setText("X");
        grid[0][0] = Game.X;
        grid[1][1] = Game.X;
        grid[2][2] = Game.X;
    }

    public void boardInverseDiagonalWin(){
        Map<String, View> cells = game.getCells();
        Integer[][] grid = game.getGrid();
        ((TextView) cells.get("cell_02")).setText("X");
        ((TextView) cells.get("cell_11")).setText("X");
        ((TextView) cells.get("cell_20")).setText("X");
        grid[0][2] = Game.X;
        grid[1][1] = Game.X;
        grid[2][0] = Game.X;
    }
}
