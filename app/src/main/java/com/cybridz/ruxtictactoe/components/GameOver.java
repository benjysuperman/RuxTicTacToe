package com.cybridz.ruxtictactoe.components;

import java.io.Serializable;

public class GameOver implements Serializable {
    private final String message;

    private final String image;

    private final String winner;

    public GameOver(String winner, String message, String image) {
        this.message = message;
        this.image = image;
        this.winner = winner;
    }

    public String getImage() {
        return image;
    }

    public String getMessage() {
        return message;
    }

    public String getWinner() {
        return winner;
    }
}
