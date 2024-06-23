package com.cybridz.ruxtictactoe.enums;

import java.util.Random;

public class Emoji {
    private static final String[] emojis = new String[]{
            "\uD83D\uDE00", // Grinning face
            "\uD83D\uDE01", // Beaming face
            "\uD83D\uDE02", // Face with tears of joy
            "\uD83D\uDE03", // Smiling face with open mouth
            "\uD83D\uDE04", // Smiling face with open mouth and smiling eyes
            "\uD83D\uDE05", // Smiling face with open mouth and cold sweat
            "\uD83C\uDF38",
            "\uD83E\uDD84"
    };

    private static final Random random = new Random();

    public static String[] getList(){
        return emojis;
    }

    public static String getRandomEmoji(){
        return emojis[random.nextInt(emojis.length)];
    }
}
