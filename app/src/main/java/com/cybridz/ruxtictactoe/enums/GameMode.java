package com.cybridz.ruxtictactoe.enums;

public enum GameMode {
    AI("AI"),
    @SuppressWarnings("unused")
    ALGO("ALGO");

    private final String value;

    GameMode(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
