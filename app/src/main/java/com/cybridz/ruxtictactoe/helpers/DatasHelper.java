package com.cybridz.ruxtictactoe.helpers;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DatasHelper {

    private static Map<String, Map<String, StringBuilder>> loadedDatas = new HashMap<>();

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
            e.printStackTrace();
        }
        return Objects.requireNonNull(Objects.requireNonNull(loadedDatas.get(fileName)).get(key)).toString();
    }
}
