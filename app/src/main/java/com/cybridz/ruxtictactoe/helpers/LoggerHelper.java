package com.cybridz.ruxtictactoe.helpers;

import android.util.Log;

public class LoggerHelper {
    public static void logWithLineNumber(String key, int lineNumber, String theClass, String value) {
        String lineSeparator = "=";
        Log.d(key, theClass + " : l" + lineNumber + ": \n\n"  + value);
    }
}
