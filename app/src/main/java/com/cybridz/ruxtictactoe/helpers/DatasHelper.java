package com.cybridz.ruxtictactoe.helpers;

import static com.cybridz.AbstractActivity.LOGGER_KEY;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class DatasHelper {

    private final static Map<String, Map<String, StringBuilder>> loadedDatas = new HashMap<>();

    @SuppressWarnings("unused")
    public static String getKeyPairValueFromAssetsFile(String fileName, AssetManager assetManager, String key) {
        if (loadedDatas.containsKey(fileName)) {
            if (Objects.requireNonNull(loadedDatas.get(fileName)).containsKey(key)) {
                return Objects.requireNonNull(Objects.requireNonNull(loadedDatas.get(fileName)).get(key)).toString();
            } else {
                return "";
            }
        }
        HashMap<String, StringBuilder> fileEntries = new HashMap<>();
        loadedDatas.put(fileName, fileEntries);
        StringBuilder fileData = new StringBuilder();
        try {
            InputStream is = assetManager.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(key + ";")) {
                    fileData.append(line.split(";", 2)[1]);
                    Objects.requireNonNull(loadedDatas.get(fileName)).put(key, fileData);
                }
            }
            reader.close();
        } catch (IOException e) {
            Log.d(LOGGER_KEY, "Error while reading file " + fileName, e);
        }
        return Objects.requireNonNull(Objects.requireNonNull(loadedDatas.get(fileName)).get(key)).toString();
    }
}
