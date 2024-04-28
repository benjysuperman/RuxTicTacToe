package com.cybridz.ruxtictactoe.enums;

import java.util.Random;

public class Light extends com.leitianpai.robotsdk.commandlib.Light {
    private static final int[] lights = new int[] {
            com.leitianpai.robotsdk.commandlib.Light.RED,
            com.leitianpai.robotsdk.commandlib.Light.GREEN,
            com.leitianpai.robotsdk.commandlib.Light.BLUE,
            com.leitianpai.robotsdk.commandlib.Light.ORANGE,
            com.leitianpai.robotsdk.commandlib.Light.WHITE,
            com.leitianpai.robotsdk.commandlib.Light.YELLOW,
            com.leitianpai.robotsdk.commandlib.Light.CYAN
    };

    private static final Random random = new Random();

    public static int getRandomLight(){
        return lights[random.nextInt(lights.length)];
    }
}
