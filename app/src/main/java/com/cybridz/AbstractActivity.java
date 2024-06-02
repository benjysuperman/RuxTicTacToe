package com.cybridz;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cybridz.ruxtictactoe.helpers.AndroidGeneralLayoutHelper;
import com.cybridz.ruxtictactoe.helpers.NetworkHelper;
import com.cybridz.ruxtictactoe.helpers.RobotHelper;
import com.cybridz.ruxtictactoe.services.SharedServices;
import com.cybridz.ruxtictactoe.services.effects.BlinkingLightMessageService;
import com.cybridz.ruxtictactoe.services.motors.MotorRotationMessageService;
import com.leitianpai.robotsdk.RobotService;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractActivity extends AppCompatActivity {

    protected static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    public static final int API = 0;
    public static final int SECRET = 1;

    private static boolean permissions_check = false;

    public static final String LOGGER_KEY = "ruxtictactoe";
    protected static final int REQUEST_CODE = 456728828;

    private static final Properties api_properties = (new Properties());
    private static final  Properties secret_properties = (new Properties());

    static {
        try {
            api_properties.load(AbstractActivity.class.getClassLoader().getResourceAsStream("properties/api.properties"));
            secret_properties.load(AbstractActivity.class.getClassLoader().getResourceAsStream("properties/secret_key.properties"));
        } catch (IOException e) {
            Log.d(LOGGER_KEY, e.getMessage());
        }
    }

    protected View current_view;

    protected Handler serviceLoadedHandler = new Handler();
    protected Runnable serviceLoadedRunner = new Runnable() {
        @Override
        public void run() {
            if(sharedServices.getRobotService() != null && RobotHelper.isActive(sharedServices.getRobotService())){
                serviceLoadedHandler.removeCallbacks(this);
                play();
            } else {
                serviceLoadedHandler.postDelayed(this, 1000);
            }
        }
    };

    /**
     * FLOW STATES
     **/

    protected Map<String, Drawable> drawableMap;
    protected boolean launched = false;
    protected SharedServices sharedServices;

    public String getProperty(int set, String key) {
        if (set == API)
            return api_properties.getProperty(key);
        if (set == SECRET)
            return secret_properties.getProperty(key);
        return null;
    }

    public abstract void play();

    protected void checkNetwork(){
        NetworkHelper.exitIfNetworkUnavailable(getSystemService(ConnectivityManager.class));
    }

    protected void makeAlert(String message, int seconds){
        AlertDialog.Builder builder = new AlertDialog.Builder(AbstractActivity.this);
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
        current_view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, (seconds * 1000L));
    }

    protected void makeAlert(String message){
        makeAlert(message, 3);
    }

    protected void initializeServicesIfNeeded(){
        checkNetwork();
        checkPermissions();
        openServices();
        AndroidGeneralLayoutHelper.hideBar(getWindow().getDecorView());
    }

    protected void checkPermissions(){
        if(permissions_check){
            return;
        }
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(AbstractActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(AbstractActivity.this, PERMISSIONS, REQUEST_CODE);
                break;
            }
        }
        permissions_check = true;
    }

    protected void openServices() {
        if(sharedServices == null){
            Log.d(AbstractActivity.LOGGER_KEY, "In open service");
            RobotService robotService = RobotService.getInstance(this);
            sharedServices = new SharedServices(robotService, new BlinkingLightMessageService(robotService), new MotorRotationMessageService(robotService));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedServices.closeServices();
        serviceLoadedHandler.removeCallbacks(serviceLoadedRunner);
    }
}
