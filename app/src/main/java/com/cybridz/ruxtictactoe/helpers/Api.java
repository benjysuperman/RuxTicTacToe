package com.cybridz.ruxtictactoe.helpers;

import android.util.Log;

import com.cybridz.AbstractActivity;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private static OkHttpClient client;
    private final AbstractActivity activity;

    public Api(AbstractActivity activity) {
        this.activity = activity;
    }

    public void loadClient(){
        if(!isClientLoaded()){
            client = new OkHttpClient();
        }
    }

    public boolean isClientLoaded(){
        return client != null;
    }

    public void closeClient(){
        client = null;
    }

    public String sendRequest(String requestBody) throws IOException {
        Response response = null;
        RequestBody body = RequestBody.create(requestBody, JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(activity.getProperty(AbstractActivity.API,"CHAT_ENDPOINT"))
                .addHeader("Authorization", "Bearer " + activity.getProperty(AbstractActivity.SECRET,"OPENAI_API_KEY"))
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        Log.d(AbstractActivity.LOGGER_KEY, request.toString());

        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(AbstractActivity.LOGGER_KEY, "Response execute error : " + e.getMessage());
        }
        return response != null ? response.body().string() : null;
    }

}
