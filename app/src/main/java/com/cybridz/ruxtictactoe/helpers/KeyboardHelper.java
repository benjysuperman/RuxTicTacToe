package com.cybridz.ruxtictactoe.helpers;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import java.util.List;
public class KeyboardHelper {

    public static void listInstalledKeyboards(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> inputMethods = imm.getEnabledInputMethodList();

        for (InputMethodInfo inputMethod : inputMethods) {
            String id = inputMethod.getId();
            String name = inputMethod.loadLabel(context.getPackageManager()).toString();
            Log.d("keyboardrux", "Keyboard ID: " + id + ", Name: " + name);
        }
    }

}
