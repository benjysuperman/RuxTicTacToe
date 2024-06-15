package com.cybridz.ruxtictactoe.services.effects;

import android.os.Handler;
import android.os.Looper;

import com.cybridz.ruxtictactoe.enums.Light;
import com.leitianpai.robotsdk.RobotService;
import com.leitianpai.robotsdk.message.AntennaLightMessage;

import java.util.Random;

public class BlinkingLightMessageService {
    private static final int OFF = Light.BLACK;
    private static final Random random = new Random();

    private boolean isFixedInterval;
    private int fromInterval;
    private int toInterval;
    private int fixedInterval;
    private final RobotService robotService;
    private Handler blinkHandler;
    private Runnable blinkRunnable;
    private boolean isOff;
    protected int currentEarsColor;

    public BlinkingLightMessageService(RobotService robotService) {
        this.robotService = robotService;
        this.isOff = false;
        this
                .setIsFixedInterval(500)
                .setRandomEarColor();
    }

    @SuppressWarnings("UnusedReturnValue")
    public BlinkingLightMessageService setCurrentEarsColor(int currentEarsColor) {
        this.currentEarsColor = currentEarsColor;
        return this;
    }

    public BlinkingLightMessageService setRandomEarColor() {
        int color = Light.getRandomLight();
        while (color == Light.BLACK || color == currentEarsColor) {
            color = Light.getRandomLight();
        }
        currentEarsColor = color;
        return this;
    }

    public BlinkingLightMessageService setIsFixedInterval(int fixedInterval) {
        this.fixedInterval = fixedInterval;
        isFixedInterval = true;
        return this;
    }

    public BlinkingLightMessageService setRandomInterval(int from, int to) {
        this.fromInterval = from;
        this.toInterval = to;
        isFixedInterval = false;
        return this;
    }

    protected void lightMessage(int lightValue) {
        AntennaLightMessage antennaLightMessage = new AntennaLightMessage();
        antennaLightMessage.set(lightValue);
        robotService.robotAntennaLight(antennaLightMessage);
    }

    private int getNewInterval() {
        return isFixedInterval ? fixedInterval : random.nextInt(toInterval - fromInterval) + fromInterval;
    }

    public void start() {
        stop();
        blinkHandler = new Handler();
        blinkRunnable = new Runnable() {
            @Override
            public void run() {
                lightMessage(isOff ? currentEarsColor : OFF);
                isOff = !isOff;
                blinkHandler.postDelayed(this, getNewInterval());
            }
        };
        blinkHandler.postDelayed(blinkRunnable, getNewInterval());
    }

    public void stop() {
        if (blinkHandler != null) {
            blinkHandler.removeCallbacks(blinkRunnable);
            blinkHandler = null;
            blinkRunnable = null;
        }
        isOff = true;
        lightMessage(OFF);
    }


}
