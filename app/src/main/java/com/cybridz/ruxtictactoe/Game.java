package com.cybridz.ruxtictactoe;

import static com.cybridz.AbstractActivity.LOGGER_KEY;

import android.content.res.Resources;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.cybridz.ruxtictactoe.components.Cell;
import com.cybridz.ruxtictactoe.enums.GameStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Game {

    private final Map<String, Cell> cells;
    private final int[][] grid;
    public static final int UNSET = 0;
    public static final int O = 1;
    public static final int X = 2;
    public static String XStr;
    public static String OStr;
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

    public Map<String, Cell> getCells() {
        return cells;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void emptyBoard(){
        GameActivity activity = (GameActivity) this.activity;
        for (int i=0; i < grid.length; i++){
            for (int j=0; j < grid[i].length; j++){
                String cellID = "cell_" + i + j;
                int resID = r.getIdentifier(cellID, "id", packageName);
                Cell cell = new Cell(i + "_" + j, activity.findViewById(resID));
                cell.getView().setText("");
                cells.put(cellID, cell);
                grid[i][j] = UNSET;
                int x = i;
                int y = j;
                cell.getView().setOnClickListener(view -> activity.player_played(cell,x,y));
            }
        }
    }

    public String getFormattedGrid() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < grid.length; i++){
            sb.append("| ");
            for (int j=0; j < grid.length; j++){
                if (j==1){
                    sb.append(" | ");
                }
                sb.append(grid[i][j]);
                if (j==1){
                    sb.append(" | ");
                }
            }
            sb.append(" |\n");
        }
        return "\n============\n" + sb + "\n";
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
        for (int[] ints : grid) {
            for (int j = 0; j < grid.length; j++) {
                if (ints[j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public String checkFinished(int rux, int player) {
        if(checkColumnForWin(rux) == rux || checkRowForWin(rux) == rux || checkDiagonalForWin(rux) == rux || checkInverseDiagonalForWin(rux) == rux){
            Log.d(LOGGER_KEY, "Rux win");
            return GameStatus.RUX_FINISHED;
        }
        if(checkColumnForWin(player) == player || checkRowForWin(player) == player || checkDiagonalForWin(player) == player || checkInverseDiagonalForWin(player) == player){
            Log.d(LOGGER_KEY, "Player win");
            return GameStatus.PLAYER_FINISHED;
        }
        if (gridIsFilled()){
            Log.d(LOGGER_KEY, "No one wins");
            return GameStatus.DRAW_FINISHED;
        }
        Log.d(LOGGER_KEY, "Game not finished");
        return GameStatus.NOBODY_FINISHED;
    }

    public String getGridForRequest(){
        StringBuilder sb = new StringBuilder();
        for (int[] ints : grid) {
            for (int j = 0; j < grid.length; j++) {
                sb.append(ints[j]);
            }
        }
        return sb.toString();
    }

    public void updateGrid(Cell cell, int symbol) {
        String[] ij = cell.getId().split("_");
        grid[Integer.parseInt(ij[0])][Integer.parseInt(ij[1])] = symbol;
        cell.getView().setText(symbol == O ? OStr : XStr);
    }
}
