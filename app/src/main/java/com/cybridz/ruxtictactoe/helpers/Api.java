package com.cybridz.ruxtictactoe.helpers;

import static com.cybridz.AbstractActivity.LOGGER_KEY;

import android.util.Log;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.enums.PropertyType;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {

    private static final String HISTORY_KEY = "rux_history";

    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private static OkHttpClient client;
    private final AbstractActivity activity;

    private final Map<String, String> rolePrompt = new LinkedHashMap<>();

    public Api(AbstractActivity activity) {
        this.activity = activity;
    }

    public void loadClient(){
        if(!isClientLoaded()){
            client = new OkHttpClient();
        }
    }

    public void addPromptToHistory(String role, String prompt){
        rolePrompt.put(role, prompt);
    }

    public void clearHistory() {
        rolePrompt.clear();
    }

    public boolean isClientLoaded(){
        return client != null;
    }

    public void closeClient(){
        client = null;
        clearHistory();
    }

    private void logHistory(){
        Log.d(HISTORY_KEY, "-----------------------");
        for (Map.Entry<String, String> entry : rolePrompt.entrySet()) {
          Log.d(HISTORY_KEY, "\nrole : " + entry.getKey()+ ", prompt: " + entry.getValue() + "\n");
        };
    }

    public String makeRequestPrompt(){
        StringBuilder requestPrompt = new StringBuilder();
        requestPrompt.append(PromptHelper.beginPromptRequest());
        for (Map.Entry<String, String> entry : rolePrompt.entrySet()) {
            requestPrompt.append(entry.getValue()).append(",");
        }
        String requestPromptString = requestPrompt.toString();
        requestPromptString = requestPromptString.substring(0, requestPromptString.length() - 1);
        requestPromptString += PromptHelper.closePromptRequest();
        return requestPromptString;
    }

    public String sendRequest(String[] roles, String[] requestBody) throws IOException {
        for (int i=0; i < roles.length; i++){
            addPromptToHistory(roles[i], requestBody[i]);
        }
        String requestPrompt = makeRequestPrompt();
        LoggerHelper.logWithLineNumber(LOGGER_KEY, 80, "Api.java", requestPrompt);
        Response response = null;
        RequestBody body = RequestBody.create(requestPrompt, JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(activity.getProperty(PropertyType.API,"CHAT_ENDPOINT"))
                .addHeader("Authorization", "Bearer " + activity.getProperty(PropertyType.SECRET,"OPENAI_API_KEY"))
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        Log.d(LOGGER_KEY, request.toString());
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            Log.d(LOGGER_KEY, "Response execute error : " + e.getMessage());
        }
        return response != null ? Objects.requireNonNull(response.body()).string() : null;
    }

}
