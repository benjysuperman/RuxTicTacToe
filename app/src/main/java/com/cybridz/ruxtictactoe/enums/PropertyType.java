package com.cybridz.ruxtictactoe.enums;

import static com.cybridz.AbstractActivity.LOGGER_KEY;

import android.util.Log;

import com.cybridz.AbstractActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

public enum PropertyType {

    API,
    SECRET,
    APP;

    public static HashMap<PropertyType, Properties> getProperties() {
        HashMap<PropertyType, Properties> properties = new HashMap<>();
        properties.put(PropertyType.API, new Properties());
        properties.put(PropertyType.SECRET, new Properties());
        properties.put(PropertyType.APP, new Properties());
        try {
            properties.get(PropertyType.API).load(AbstractActivity.class.getClassLoader().getResourceAsStream("properties/api.properties"));
            properties.get(PropertyType.SECRET).load(AbstractActivity.class.getClassLoader().getResourceAsStream("properties/secret.properties"));
            properties.get(PropertyType.APP).load(AbstractActivity.class.getClassLoader().getResourceAsStream("properties/app.properties"));
        } catch (NullPointerException | IOException e) {
            Log.d(LOGGER_KEY, Objects.requireNonNull(e.getMessage()));
        }
        return properties;
    }
}
