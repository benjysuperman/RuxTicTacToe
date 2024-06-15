package com.cybridz.ruxtictactoe.services;

import android.content.Context;

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

    private SharedServices(){}

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
    public void startServices(){
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

    public boolean isOpenedServices() {
        return openedServices;
    }


    public static SharedServices getInstance(Context context){
        if(instance == null){
            instance = new SharedServices();
            instance.robotService = RobotService.getInstance(context);
            instance.blinkingLightMessageService = new BlinkingLightMessageService(instance.robotService);
            instance.motorRotationMessageService = new MotorRotationMessageService(instance.robotService);
        }
        return instance;
    }
}
