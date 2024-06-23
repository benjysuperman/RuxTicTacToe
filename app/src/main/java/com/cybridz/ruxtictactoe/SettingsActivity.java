package com.cybridz.ruxtictactoe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Switch;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.adapters.BaseEmojiAdapter;
import com.cybridz.ruxtictactoe.enums.Preferences;

public class SettingsActivity extends AbstractActivity {

    /**
     * Settings Activity elements
     */
    @SuppressWarnings("FieldCanBeLocal")

    private Button backButton;
    private GridView yourGrid;
    private GridView ruxGrid;
    private Switch ai_switch;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        preferences = getSharedPreferences(Preferences.PREFERENCES_FILE, MODE_PRIVATE);
        current_view = findViewById(R.id.settings_activity);
        initializeServices();
        yourGrid = findViewById(R.id.your_symbol_grid);
        ruxGrid = findViewById(R.id.opponent_symbol_grid);
        BaseEmojiAdapter playerEmojiAdapter = new BaseEmojiAdapter(this, emojiList, preferences.getInt(Preferences.PLAYER_KEY, Preferences.DEFAULT_PLAYER_POSITION),Preferences.PLAYER_KEY);
        BaseEmojiAdapter ruxEmojiAdapter = new BaseEmojiAdapter(this, emojiList, preferences.getInt(Preferences.RUX_KEY, Preferences.DEFAULT_RUX_POSITION),Preferences.RUX_KEY);
        yourGrid.setAdapter(playerEmojiAdapter);
        ruxGrid.setAdapter(ruxEmojiAdapter);
        backButton = findViewById(R.id.back_btn);
        backButton.setText("Back");
        backButton.setOnClickListener(view -> goToStartActivity());
        ai_switch = findViewById(R.id.opponent_inpt);
        ai_switch.setChecked(preferences.getBoolean(Preferences.AI_KEY, Preferences.DEFAULT_USE_AI));
        ai_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean(Preferences.AI_KEY, isChecked).apply();
        });
    }

    public void goToStartActivity(){
        sharedServices.getBlinkingLightMessageService().stop();
        startActivity(new Intent(SettingsActivity.this, StartActivity.class));
    }

    @Override
    public void play() {
        if (sharedServices.isOpenedServices()){
            sharedServices.getBlinkingLightMessageService().setRandomEarColor();
            sharedServices.getBlinkingLightMessageService().start();
            sharedServices.getRobotService().robotPlayTTs("Let's play Tic Tac Toe");
        }
        Log.d(LOGGER_KEY, "playing settings activity");
    }
}