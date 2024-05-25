package com.cybridz.ruxtictactoe.services.motors;

import android.util.Log;

import com.cybridz.ruxtictactoe.enums.RotationType;
import com.leitianpai.robotsdk.RobotService;
import com.leitianpai.robotsdk.message.AntennaMessage;

public class MotorRotationMessageService {

    private RobotService robotService;

    public MotorRotationMessageService(RobotService robotService){
        this.robotService = robotService;
    }

    public void sendRandomMotorRotation() {
        int rotationType = RotationType.getRandom();
        // Log.d("com.cybridz.ruxspotify.launch", "the rotation type is : " + rotationType);
        AntennaMessage antennaMessage = new AntennaMessage();
        antennaMessage.set(rotationType, 1, 9, 90);
        robotService.robotAntennaMotion(antennaMessage);
    }
}
