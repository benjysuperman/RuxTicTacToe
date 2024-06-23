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
        preferences = getSharedPreferences("ruxtictactoe_app_preferences", MODE_PRIVATE);
        current_view = findViewById(R.id.settings_activity);
        initializeServices();
        yourGrid = findViewById(R.id.your_symbol_grid);
        ruxGrid = findViewById(R.id.opponent_symbol_grid);
        BaseEmojiAdapter yourEmojiAdapter = new BaseEmojiAdapter(this, emojiList, preferences.getInt("x_emoji_position", 6),"x_emoji_position");
        BaseEmojiAdapter ruxEmojiAdapter = new BaseEmojiAdapter(this, emojiList, preferences.getInt("y_emoji_position", 7), "y_emoji_position");
        yourGrid.setAdapter(yourEmojiAdapter);
        ruxGrid.setAdapter(ruxEmojiAdapter);
        backButton = findViewById(R.id.back_btn);
        backButton.setText("Back");
        backButton.setOnClickListener(view -> goToStartActivity());
        ai_switch = findViewById(R.id.opponent_inpt);
        ai_switch.setChecked(preferences.getBoolean("use_ai", false));
        ai_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("use_ai", isChecked).apply();
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