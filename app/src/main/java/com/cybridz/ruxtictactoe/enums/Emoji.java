package com.cybridz.ruxtictactoe.enums;

import java.util.Random;

public class Emoji {
    private static final String[] emojis = new String[]{
            "\uD83E\uDD16", // Robot face
            "\uD83E\uDD84", // Unicorn face
            "\uD83D\uDC36", // Dog face
            "\uD83D\uDC2E", // Cow face
            "\uD83D\uDC7D", // Alien
            "\uD83C\uDF50", // Peach
            "\uD83D\uDC14", // Rooster
            "\uD83D\uDC27", // Penguin
            "\uD83D\uDC2D", // Mouse face
            "\uD83C\uDF51", // Pineapple
            "\uD83C\uDF44", // Mushroom
            "\uD83C\uDF52", // Cherries
            "\uD83C\uDF53", // Strawberry
            "\uD83C\uDF38", // Cherry blossom
            "\uD83C\uDF3B", // Sunflower
            "\uD83C\uDF3A", // Hibiscus
            "\uD83C\uDF40", // Four leaf clover
            "\uD83D\uDE80", // Rocket
            "\uD83D\uDCA9", // Pile of poo
            "\uD83D\uDE48", // See-no-evil monkey
            "\uD83D\uDE4A", // Speak-no-evil monkey
            "\uD83E\uDD11", // Money-mouth face
            "\uD83D\uDEBD", // Toilet
            "\uD83D\uDC30", // Rabbit face
            "\uD83E\uDDBE", // Plunger
            "\uD83D\uDE4C", // Raising hands
    };

    private static final Random random = new Random();

    public static String[] getList(){
        return emojis;
    }

    public static String getRandomEmoji(){
        return emojis[random.nextInt(emojis.length)];
    }
}
