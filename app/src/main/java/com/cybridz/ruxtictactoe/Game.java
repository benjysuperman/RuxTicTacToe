package com.cybridz.ruxtictactoe;

import android.content.res.Resources;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.components.Cell;
import com.cybridz.ruxtictactoe.enums.GameStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {

    private final Map<String, Cell> cells;
    private final int[][] grid;
    public static final int UNSET_VALUE = 0;
    public static final int RUX_VALUE = 1;
    public static final int PLAYER_VALUE = 2;
    public static String playerSymbol;
    public static String ruxSymbol;
    private final Resources r;
    private final String packageName;
    private final AppCompatActivity activity;
    private final int gridSize;
    private int played;
    private ArrayList<Cell> emptySpots;

    public Game(Resources r, String packageName, AppCompatActivity activity, int gridSize) {
        cells = new HashMap<>();
        this.gridSize = gridSize;
        grid = new int[gridSize][gridSize];
        this.r = r;
        this.packageName = packageName;
        this.activity = activity;
        this.played = 0;
        emptyBoard();
    }

    public Map<String, Cell> getCells() {
        return cells;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getPlayed() {
        return played;
    }

    public void increasePlayed() {
        this.played++;
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
                grid[i][j] = UNSET_VALUE;
                int x = i;
                int y = j;
                cell.getView().setOnClickListener(view -> activity.player_played(cell,x,y));
            }
        }
    }

    public void setEmptySlots(int[][] grid){
        emptySpots = new ArrayList<Cell>();
        for (int r=0; r < gridSize; r++){
            for (int c=0; c < gridSize; c++){
                if (grid[r][c] == UNSET_VALUE){
                    emptySpots.add(cells.get("cell_" + r + c));
                }
            }
        }
    }

    public ArrayList<Cell> getEmptySpots() {
        return emptySpots;
    }

    public String getWinner() {
        // ROWS
        for (int r = 0; r < gridSize; r++) {
            for (int c = 1; c < gridSize - 1; c++) {
                ArrayList<Integer> row = new ArrayList<>();
                row.add(grid[r][c-1]);
                row.add(grid[r][c]);
                row.add(grid[r][c+1]);
                boolean win = !row.contains(UNSET_VALUE) && grid[r][c - 1] == grid[r][c] && grid[r][c] == grid[r][c + 1];
                if (win) {
                    Log.d(AbstractActivity.LOGGER_KEY, "---Win by row");
                    return grid[r][c] == RUX_VALUE ? GameStatus.RUX_FINISHED : GameStatus.PLAYER_FINISHED;
                }
            }
        }

        // COLUMNS
        for (int c = 0; c < gridSize; c++) {
            for (int r = 1; r < gridSize - 1; r++) {
                ArrayList<Integer> column = new ArrayList<>();
                column.add(grid[r-1][c]);
                column.add(grid[r][c]);
                column.add(grid[r+1][c]);
                boolean win = !column.contains(UNSET_VALUE) && grid[r-1][c] == grid[r][c] && grid[r][c] == grid[r+1][c];
                if (win) {
                    Log.d(AbstractActivity.LOGGER_KEY, "---Win by column");
                    return grid[r][c] == RUX_VALUE ? GameStatus.RUX_FINISHED : GameStatus.PLAYER_FINISHED;
                }
            }
        }
        // Diagonal
        for (int r = 1; r < gridSize - 1; r++) {
            for (int c = 1; c < gridSize - 1; c++) {
                ArrayList<Integer> diagonal = new ArrayList<>();
                diagonal.add(grid[r-1][c-1]);
                diagonal.add(grid[r][c]);
                diagonal.add(grid[r+1][c+1]);
                boolean win = !diagonal.contains(UNSET_VALUE) && grid[r - 1][c - 1] == grid[r][c] && grid[r][c] == grid[r + 1][c + 1];
                if (win) {
                    Log.d(AbstractActivity.LOGGER_KEY, "---Win by diagonal");
                    return  grid[r][c] == RUX_VALUE ? GameStatus.RUX_FINISHED : GameStatus.PLAYER_FINISHED;
                }
            }
        }

        // Diagonal Inserse
        for (int r = 1; r < gridSize - 1; r++) {
            for (int c = 1; c < gridSize - 1; c++) {
                ArrayList<Integer> diagonal = new ArrayList<>();
                diagonal.add(grid[r-1][c+1]);
                diagonal.add(grid[r][c]);
                diagonal.add(grid[r+1][c-1]);
                boolean win = !diagonal.contains(UNSET_VALUE) && grid[r - 1][c + 1] == grid[r][c] && grid[r][c] == grid[r + 1][c - 1];
                if (win) {
                    Log.d(AbstractActivity.LOGGER_KEY, "---Win by inverse diagonal");
                    return grid[r][c] == RUX_VALUE ? GameStatus.RUX_FINISHED : GameStatus.PLAYER_FINISHED;
                }
            }
        }
        // checkFull
        if (emptySpots.isEmpty()) {
            return GameStatus.DRAW_FINISHED;
        }
        return GameStatus.NOBODY_FINISHED;
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

    public String getGridForRequest(){
        StringBuilder sb = new StringBuilder();
        for (int[] ints : grid) {
            for (int j = 0; j < grid.length; j++) {
                sb.append(ints[j]);
            }
        }
        return sb.toString();
    }

    public void addToGrid(Cell cell, int symbol, boolean setSymbol) {
        String[] ij = cell.getId().split("_");
        grid[Integer.parseInt(ij[0])][Integer.parseInt(ij[1])] = symbol;
        if(setSymbol){
            cell.getView().setText(symbol == RUX_VALUE ? ruxSymbol : playerSymbol);
        }
    }

    public void removeFromGrid(Cell cell, boolean resetSymbol) {
        String[] ij = cell.getId().split("_");
        grid[Integer.parseInt(ij[0])][Integer.parseInt(ij[1])] = UNSET_VALUE;
        if( resetSymbol ){
            cell.getView().setText("");
        }
    }
}
