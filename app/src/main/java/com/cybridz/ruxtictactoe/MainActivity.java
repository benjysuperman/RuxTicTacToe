package com.cybridz.ruxtictactoe;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cybridz.ruxtictactoe.helpers.AndroidGeneralLayoutHelper;
import com.cybridz.ruxtictactoe.helpers.NetworkHelper;
import com.cybridz.ruxtictactoe.helpers.RobotHelper;
import com.cybridz.ruxtictactoe.services.effects.BlinkingLightMessageService;
import com.cybridz.ruxtictactoe.services.motors.MotorRotationMessageService;
import com.leitianpai.robotsdk.RobotService;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String LOGGER_KEY = "ruxtictactoe";
    private static final int REQUEST_CODE = 456728828;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    /**
     * FLOW STATES
     **/
    private Handler serviceLoadedHandler = new Handler();
    private Runnable serviceLoadedRunner = new Runnable() {
        @Override
        public void run() {
            if(RobotHelper.isActive(robotService)){
                serviceLoadedHandler.removeCallbacks(this);
                play();
            } else {
                serviceLoadedHandler.postDelayed(this, 1000);
            }
        }
    };
    private Map<String, Drawable> drawableMap;
    private boolean launched = false;

    /**
     * RUX ELEMENTS
     **/
    private RobotService robotService;
    private boolean openedServices = false;
    private BlinkingLightMessageService blinkingLightMessageService;
    private MotorRotationMessageService motorRotationMessageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, REQUEST_CODE);
                break;
            }
        }
        NetworkHelper.exitIfNetworkUnavailable(getSystemService(ConnectivityManager.class));
        robotService = RobotService.getInstance(this);
        setContentView(R.layout.activity_main);
        AndroidGeneralLayoutHelper.hideBar(getWindow().getDecorView());
        openServices();
    }

    @Override
    protected void onStart() {
        super.onStart();
        serviceLoadedRunner.run();
    }

    public void play() {
        motorRotationMessageService.sendRandomMotorRotation();
        blinkingLightMessageService.setRandomEarColor();
        blinkingLightMessageService.start();
    }

    protected void openServices() {
        if (!openedServices) {
            openedServices = true;
            motorRotationMessageService = new MotorRotationMessageService(robotService);
            blinkingLightMessageService = new BlinkingLightMessageService(robotService);
        }
    }

    protected void closeServices() {
        if (blinkingLightMessageService != null) {
            blinkingLightMessageService.stop();
        }
        robotService.robotCloseMotor();
        robotService.robotCloseAntennaLight();
        robotService.unbindService();
        serviceLoadedHandler.removeCallbacks(serviceLoadedRunner);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeServices();
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeServices();
    }

}