package com.cybridz.ruxtictactoe.helpers;

public class PromptHelper {

    public static String getMessagePrompt(String system_prompt, String user_prompt) {
        String requestBody = "{";
        requestBody += "\"model\": \"gpt-4o\",";
        requestBody += "\"messages\": [";
        requestBody += "{\"role\":\"system\",\"content\": \"" + system_prompt + "\"},";
        requestBody += "{\"role\":\"user\",\"content\": \"" + user_prompt + "\"}";
        requestBody += "]";
        requestBody += "}";
        return requestBody;
    }

    public static String ruxPlayRequestBody(String system_prompt, String grid, int rux_symbol, int player_symbol) {
        String requestBody = "{";
        requestBody += "\"model\": \"gpt-4o\",";
        requestBody += "\"messages\": [";
        requestBody += "{\"role\": \"system\",\"content\": \"" + system_prompt + "\"},";
        requestBody += "{\"role\": \"user\", \"content\":\"grid:" + grid + ",your_symbol:" + rux_symbol + ",your_symbol:" + player_symbol + "}";
        requestBody += "]";
        requestBody += "}";
        return requestBody;
    }


}
