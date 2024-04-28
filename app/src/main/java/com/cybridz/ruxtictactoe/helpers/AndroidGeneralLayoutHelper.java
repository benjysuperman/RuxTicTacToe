package com.cybridz.ruxtictactoe.helpers;

import android.view.View;

public class AndroidGeneralLayoutHelper {
    public static void hideBar(View view){
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(uiOptions);
    }
}
