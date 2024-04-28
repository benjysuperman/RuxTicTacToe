package com.cybridz.ruxtictactoe.helpers;

import com.leitianpai.robotsdk.RobotService;

public class RobotHelper {

    public static boolean isActive(RobotService robotService){
        try {
            robotService.robotOpenSensor();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
