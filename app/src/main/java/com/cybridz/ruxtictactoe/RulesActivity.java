package com.cybridz.ruxtictactoe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.enums.GameMode;
import com.cybridz.ruxtictactoe.enums.Preferences;

public class RulesActivity extends AbstractActivity {

    /**
     * Start Activity elements
     */
    @SuppressWarnings("FieldCanBeLocal")
    private Button backButton;
    private TextView text;
    private String ruxSymbol;
    private String playerSymbol;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_activity);
        current_view = findViewById(R.id.rules_activity);
        initializePreferences();
        initializeServices();
        backButton = findViewById(R.id.back_btn);
        backButton.setText("Back");
        backButton.setOnClickListener(view -> goToStartActivity());
        text = findViewById(R.id.rules_lbl);
        text.setText(Html.fromHtml("<h1 style='font-weight: bold;'>Rules</h1>" +
                "<p>The goal is to be the first player to get three of their marks (either '" + playerSymbol + "' or '" + ruxSymbol + "') in a horizontal, vertical, or diagonal row on a 3x3 grid.</p>" +
                "<p>You can compete vs AI in the setting or play against the algorithm!</p>"));
    }

    public void initializePreferences() {
        super.initializePreferences();
        ruxSymbol = emojiList[preferences.getInt(Preferences.RUX_KEY, Preferences.DEFAULT_RUX_POSITION)];
        playerSymbol = emojiList[preferences.getInt(Preferences.PLAYER_KEY, Preferences.DEFAULT_PLAYER_POSITION)];
    }

    @Override
    public void play() {
        if (sharedServices.isOpenedServices()){
            sharedServices.getBlinkingLightMessageService().setRandomEarColor();
            sharedServices.getBlinkingLightMessageService().start();
            sharedServices.getRobotService().robotPlayTTs("Have a look at the rules");
        }
        Log.d(LOGGER_KEY, "playing rules activity");
    }

    public void goToStartActivity(){
        sharedServices.getBlinkingLightMessageService().stop();
        startActivity(new Intent(RulesActivity.this, StartActivity.class));
    }
}