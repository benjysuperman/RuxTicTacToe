package com.cybridz;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cybridz.ruxtictactoe.enums.Emoji;
import com.cybridz.ruxtictactoe.enums.Permissions;
import com.cybridz.ruxtictactoe.enums.PropertyType;
import com.cybridz.ruxtictactoe.helpers.AndroidGeneralLayoutHelper;
import com.cybridz.ruxtictactoe.helpers.NetworkHelper;
import com.cybridz.ruxtictactoe.helpers.RobotHelper;
import com.cybridz.ruxtictactoe.services.SharedServices;

import java.util.Map;
import java.util.Properties;

public abstract class AbstractActivity extends AppCompatActivity {

    private static final boolean TEST_MODE = false;

    protected static SharedPreferences preferences;

    protected static final String[] PERMISSIONS = Permissions.getPermissions();

    public static final String LOGGER_KEY = "ruxtictactoe_logger_key";
    protected static final int REQUEST_CODE = 456728828;

    protected String[] emojiList = Emoji.getList();

    private static final Map<PropertyType, Properties> properties = PropertyType.getProperties();

    protected View current_view;

    protected Handler serviceLoadedHandler = new Handler();
    protected Runnable serviceLoadedRunner = new Runnable() {
        @Override
        public void run() {
            if(sharedServices.getRobotService() != null && RobotHelper.isActive(sharedServices.getRobotService())){
                Log.d(AbstractActivity.LOGGER_KEY, "++++++++++++++++++++++++++Service open++++++++++++++++++++++++++++++");
                serviceLoadedHandler.removeCallbacks(this);
                sharedServices.startServices();
                play();
            } else {
                Log.d(AbstractActivity.LOGGER_KEY, "-------------------------Trying open service------------------------");
                serviceLoadedHandler.postDelayed(this, 1000);
            }
        }
    };

    protected SharedServices sharedServices;

    public String getProperty(PropertyType set, String key) {
        return properties.get(set).getProperty(key);
    }

    public abstract void play();

    protected boolean checkNetwork(){
        return NetworkHelper.isNetworkAvailable(getSystemService(ConnectivityManager.class));
    }

    @SuppressWarnings("all")
    protected void makeAlert(String message, int seconds){
        AlertDialog.Builder builder = new AlertDialog.Builder(AbstractActivity.this);
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
        current_view.postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }, (seconds * 1000L));
    }

    @SuppressWarnings("unused")
    protected void makeAlert(String message){
        makeAlert(message, 3);
    }

    protected void initializeServices(){
        sharedServices = SharedServices.getInstance(this);
        serviceLoadedHandler.postDelayed(serviceLoadedRunner, 1);
        checkNetwork();
        checkPermissions();
        AndroidGeneralLayoutHelper.hideBar(this);
    }

    private void checkPermissions(){
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(AbstractActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(AbstractActivity.this, PERMISSIONS, REQUEST_CODE);
            }
        }
    }

    public static boolean isTestMode() {
        return TEST_MODE;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedServices.closeServices();
    }
}
