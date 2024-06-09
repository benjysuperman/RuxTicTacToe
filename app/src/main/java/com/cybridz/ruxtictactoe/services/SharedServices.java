package com.cybridz.ruxtictactoe.services;

import com.cybridz.ruxtictactoe.services.effects.BlinkingLightMessageService;
import com.cybridz.ruxtictactoe.services.motors.MotorRotationMessageService;
import com.leitianpai.robotsdk.RobotService;

public class SharedServices {

    @SuppressWarnings("unused")
    private static SharedServices instance;
    private RobotService robotService;
    @SuppressWarnings("all")
    private boolean openedServices = false;
    private BlinkingLightMessageService blinkingLightMessageService;
    private MotorRotationMessageService motorRotationMessageService;

    public SharedServices(RobotService robotService, BlinkingLightMessageService blinkingLightMessageService, MotorRotationMessageService rotationMessageService){
        if(instance == null){
            this.robotService = robotService;
            this.blinkingLightMessageService = blinkingLightMessageService;
            this.motorRotationMessageService = rotationMessageService;
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
        this.openedServices = false;
    }

    @SuppressWarnings("unused")
    public void resetStateServices(){
        if(robotService == null){
            return;
        }
        robotService.robotCloseMotor();
        robotService.robotCloseSensor();
        robotService.robotCloseAntennaLight();
        robotService.robotOpenMotor();
        robotService.robotOpenSensor();
        this.openedServices = true;
    }

    public void setOpenedServices(boolean openedServices) {
        this.openedServices = openedServices;
    }

    public boolean isOpenedServices() {
        return openedServices;
    }

    @SuppressWarnings("unused")
    public static SharedServices getInstance(){
        return instance;
    }
}
