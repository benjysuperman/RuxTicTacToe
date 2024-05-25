package com.cybridz.ruxtictactoe.services;

import com.cybridz.ruxtictactoe.services.effects.BlinkingLightMessageService;
import com.cybridz.ruxtictactoe.services.motors.MotorRotationMessageService;
import com.leitianpai.robotsdk.RobotService;

public class SharedServices {

    private static SharedServices instance;
    private RobotService robotService;
    private boolean openedServices = false;
    private BlinkingLightMessageService blinkingLightMessageService;
    private MotorRotationMessageService motorRotationMessageService;

    public SharedServices(RobotService robotService, BlinkingLightMessageService blinkingLightMessageService, MotorRotationMessageService rotationMessageService){
        if(instance == null){
            this.robotService = robotService;
            this.blinkingLightMessageService = blinkingLightMessageService;
            this.motorRotationMessageService = rotationMessageService;
            this.openedServices = false;
        }
    }

    public RobotService getRobotService() {
        return robotService;
    }

    public BlinkingLightMessageService getBlinkingLightMessageService() {
        return blinkingLightMessageService;
    }

    public MotorRotationMessageService getMotorRotationMessageService() {
        return motorRotationMessageService;
    }


    public void closeServices(){
        if (blinkingLightMessageService != null) {
            blinkingLightMessageService.stop();
        }
        robotService.robotCloseMotor();
        robotService.robotCloseAntennaLight();
        robotService.robotCloseSensor();
        robotService.unbindService();
    }

    public void resetStateServices(){
        if(robotService == null){
            return;
        }
        robotService.robotCloseMotor();
        robotService.robotCloseSensor();
        robotService.robotCloseAntennaLight();
        robotService.robotOpenMotor();
        robotService.robotOpenSensor();
    }

    public static SharedServices getInstance(){
        return instance;
    }
}
