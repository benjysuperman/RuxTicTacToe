package com.cybridz.ruxtictactoe.enums;

import java.util.Random;

public class RotationType {
    private static final int[] rotation_type = new int[]{1, 2, 3};

    private static final Random random = new Random();

    public static int getRandom(){
        return rotation_type[random.nextInt(rotation_type.length)];
    }
}
