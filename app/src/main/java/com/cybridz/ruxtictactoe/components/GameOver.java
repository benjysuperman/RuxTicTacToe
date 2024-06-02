package com.cybridz.ruxtictactoe.components;

import java.io.Serializable;

public class GameOver implements Serializable {
    private String message;

    private String image;

    private String winner;

    public GameOver(String winner, String message, String image) {
        this.message = message;
        this.image = image;
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
