package com.cybridz.ruxtictactoe;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cybridz.AbstractActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Game {

    private Map<String, View> cells;
    private int[][] grid;
    public static final int UNSET = 0;
    public static final int O = 1;
    public static final int X = 2;
    private final Resources r;
    private final String packageName;
    private final AppCompatActivity activity;

    public Game(Resources r, String packageName, AppCompatActivity activity) {
        cells = new HashMap<>();
        grid = new int[3][3];
        this.r = r;
        this.packageName = packageName;
        this.activity = activity;
        emptyBoard();
    }

    public Map<String, View> getCells() {
        return cells;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void emptyBoard(){
        for (int i=0; i < grid.length; i++){
            for (int j=0; j < grid[i].length; j++){
                String cellID = "cell_" + i + j;
                int resID = r.getIdentifier(cellID, "id", packageName);
                TextView cell = activity.findViewById(resID);
                cell.setText("");
                int iToClick = j;
                int jToClick = i;
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((GameActivity) activity).updateCell(cell, iToClick, jToClick);
                    }
                });
                cells.put(cellID, cell);
                grid[i][j] = UNSET;
            }
        }
    }

    public int checkRowForWin(int symbol){
        int[] lines = new int[3];
        lines[0] = grid[0][0] == symbol && grid[0][1] == symbol && grid[0][2] == symbol ? symbol : UNSET;
        lines[1] = grid[1][0] == symbol && grid[1][1] == symbol && grid[1][2] == symbol ? symbol : UNSET;
        lines[2] = grid[2][0] == symbol && grid[2][1] == symbol && grid[2][2] == symbol ? symbol : UNSET;
        return IntStream.of(lines).sum() == symbol ? symbol : 0;
    }

    public int checkColumnForWin(int symbol){
        int[] columns = new int[3];
        columns[0] = grid[0][0] == symbol && grid[1][0] == symbol && grid[2][0] == symbol ? symbol : UNSET;
        columns[1] = grid[0][1] == symbol && grid[1][1] == symbol && grid[2][1] == symbol ? symbol : UNSET;
        columns[2] = grid[0][2] == symbol && grid[1][2] == symbol && grid[2][2] == symbol ? symbol : UNSET;
        return IntStream.of(columns).sum() == symbol ? symbol : 0;
    }

    public int checkDiagonalForWin(int symbol){
        return grid[0][0] == symbol && grid[1][1] == symbol && grid[2][2] == symbol ? symbol : UNSET;
    }

    public int checkInverseDiagonalForWin(int symbol){
        return grid[0][2] == symbol && grid[1][1] == symbol && grid[2][0] == symbol ? symbol : UNSET;
    }

    public boolean gridIsFilled(){
        for (int i=0; i < grid.length; i++){
            for (int j=0; j < grid.length; j++){
                if(grid[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }

    public void printResults(int symbol){
        Log.d(AbstractActivity.LOGGER_KEY, "Rows : " + checkRowForWin(symbol));
        Log.d(AbstractActivity.LOGGER_KEY, "Columns : " +  checkColumnForWin(symbol));
        Log.d(AbstractActivity.LOGGER_KEY, "Diagonale : " + checkDiagonalForWin(symbol));
        Log.d(AbstractActivity.LOGGER_KEY, "Inverse diagonale : " + checkInverseDiagonalForWin(symbol));
    }
}
