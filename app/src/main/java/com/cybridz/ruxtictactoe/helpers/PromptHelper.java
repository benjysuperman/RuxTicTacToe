package com.cybridz.ruxtictactoe.helpers;

public class PromptHelper {

    public static String beginPromptRequest(){
        String requestBody = "{";
        requestBody += "\"model\": \"gpt-4o\",";
        requestBody += "\"messages\": [";
        return requestBody;
    }

    public static String closePromptRequest(){
        return "]}";
    }

    public static String getSystemPrompt(String system_prompt) {
        return  makeJsonLine("system", system_prompt);
    }

    public static String ruxPlayPrompt(String grid, int rux_symbol, int player_symbol) {
        return makeJsonLine("user",
                "grid:" + grid +
                ",your_symbol:" + rux_symbol +
                ",player_symbol:" + player_symbol);
    }

    public static String makeJsonLine(String role, String content){
        content = content.replace("\"", "\\\"");
        return "{\"role\":\"" + role + "\", \"content\":\"" + content +"\"}";
    }

    public static String ruxLearnPrompt(String grid, int rux_symbol, int player_symbol) {
        return makeJsonLine("user",
                "You can not place your symbol there the cell is already occupied " +
                        "(only 0 is a permitted spot). Actual grid:" + grid +
                        ",your_symbol:" + rux_symbol +
                        ",player_symbol:" + player_symbol);
    }


}
