package com.cybridz.ruxtictactoe.enums;

public enum GameStatus {
    RUX_FINISHED("RUX"),
    PLAYER_FINISHED("PLAYER"),
    DRAW_FINISHED("DRAW"),
    NOBODY_FINISHED("NONE");

    private final String value;

    GameStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
