package com.cybridz.ruxtictactoe.helpers;

import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.appcompat.app.AppCompatActivity;

public class AndroidGeneralLayoutHelper {
    public static void hideBar(AppCompatActivity activity) {
        WindowInsetsController insetsController = activity.getWindow().getInsetsController();
        if (insetsController != null) {
            insetsController.hide(WindowInsets.Type.systemBars());
            insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }
    }
}
