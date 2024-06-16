package com.cybridz.ruxtictactoe.services.motors;

import android.util.Log;

import com.cybridz.ruxtictactoe.enums.RotationType;
import com.leitianpai.robotsdk.RobotService;
import com.leitianpai.robotsdk.message.ActionMessage;
import com.leitianpai.robotsdk.message.AntennaMessage;

import java.util.Random;

public class MotorRotationMessageService {

    private final RobotService robotService;

    public MotorRotationMessageService(RobotService robotService){
        this.robotService = robotService;
    }

    public void sendRandomMotorRotation() {
        int rotationType = RotationType.getRandom();
        Log.d("com.cybridz.ruxspotify.launch", "the rotation type is : " + rotationType);
        ActionMessage actionMessage = new ActionMessage();
        Random random = new Random();
        actionMessage.set(random.nextInt(80), 1, 2);
        robotService.robotActionCommand(actionMessage);
    }
}
