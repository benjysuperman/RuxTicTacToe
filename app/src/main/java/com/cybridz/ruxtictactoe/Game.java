package com.cybridz.ruxtictactoe;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cybridz.AbstractActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Game {

    private Map<String, View> cells;
    private Integer[][] grid;
    public static final int UNSET = 0;
    public static final int O = 1;
    public static final int X = 2;
    private final Resources r;
    private final String packageName;
    private final AppCompatActivity activity;

    public Game(Resources r, String packageName, AppCompatActivity activity) {
        cells = new HashMap<>();
        grid = new Integer[3][3];
        this.r = r;
        this.packageName = packageName;
        this.activity = activity;
        emptyBoard();
    }

    public Map<String, View> getCells() {
        return cells;
    }

    public Integer[][] getGrid() {
        return grid;
    }

    private void emptyBoard(){
        for (int i=0; i < grid.length; i++){
            for (int j=0; j < grid[i].length; j++){
                String cellID = "cell_" + i + j;
                int resID = r.getIdentifier(cellID, "id", packageName);
                TextView cell = activity.findViewById(resID);
                cell.setText("");
                cells.put(cellID, cell);
                grid[i][j] = UNSET;
            }
        }
    }

    public Integer[] checkRowForWin(){
        Integer[] lines = new Integer[3];
        lines[0] = grid[0][0] == grid[0][1] && grid[0][1] == grid[0][2] ? grid[0][0] : UNSET;
        lines[1] = grid[1][0] == grid[1][1] && grid[1][1] == grid[1][2] ? grid[1][0] : UNSET;
        lines[2] = grid[2][0] == grid[2][1] && grid[2][1] == grid[2][2] ? grid[2][0] : UNSET;
        return lines;
    }

    public Integer[] checkColumnForWin(){
        Integer[] columns = new Integer[3];
        columns[0] = grid[0][0] == grid[1][0] && grid[1][0] == grid[2][0] ? grid[0][0] : UNSET;
        columns[1] = grid[0][1] == grid[1][1] && grid[1][1] == grid[2][1] ? grid[0][1] : UNSET;
        columns[2] = grid[0][2] == grid[1][2] && grid[1][2] == grid[2][2] ? grid[0][2] : UNSET;
        return columns;
    }

    public Integer checkDiagonalForWin(){
        return grid[0][0] == grid[1][1] &&  grid[1][1] == grid[2][2] ? grid[0][0] : UNSET;
    }

    public Integer checkInverseDiagonalForWin(){
        return grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] ? grid[0][2] : UNSET;
    }

    public void printWinResults(){
        Log.d(AbstractActivity.LOGGER_KEY, "Rows : " + Arrays.asList(checkRowForWin()));
        Log.d(AbstractActivity.LOGGER_KEY, "Columns : " +  Arrays.asList(checkColumnForWin()));
        Log.d(AbstractActivity.LOGGER_KEY, "Diagonale : " + checkDiagonalForWin());
        Log.d(AbstractActivity.LOGGER_KEY, "Inverse diagonale : " + checkInverseDiagonalForWin());
    }
}
