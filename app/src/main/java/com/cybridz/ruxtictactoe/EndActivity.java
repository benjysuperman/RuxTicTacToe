package com.cybridz.ruxtictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.components.GameOver;

public class EndActivity extends AbstractActivity {

    /**
     * Start Activity elements
     */

    private Button restart_button;

    private TextView message;

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameOver gameOver = (GameOver) getIntent().getSerializableExtra("gameOver");
        setContentView(R.layout.restart_activity);
        current_view = findViewById(R.id.restart_activity);
        initializeServicesIfNeeded();

        image = findViewById(R.id.gameover);
        updateImageView(image, gameOver.getImage());

        message = findViewById(R.id.message);
        sharedServices.getRobotService().robotPlayTTs(gameOver.getMessage());
        message.setText(gameOver.getWinner() + "\n" + gameOver.getMessage());

        restart_button = findViewById(R.id.restart_game);
        restart_button.setText("Restart");
        restart_button.setOnClickListener(view -> {
            Log.d(LOGGER_KEY, "clicked restart btn");
            goToStartActivity();
        });
        sharedServices.getBlinkingLightMessageService().setRandomEarColor();
        sharedServices.getMotorRotationMessageService().sendRandomMotorRotation();
    }

    private void updateImageView(ImageView imageView, String imageName) {
        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (resId != 0) {
            imageView.setImageDrawable(AppCompatResources.getDrawable(this, resId));
        } else {
            imageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.r_noone_won));
        }
    }

    public void goToStartActivity(){
        sharedServices.getBlinkingLightMessageService().stop();
        startActivity(new Intent(EndActivity.this, StartActivity.class));
    }

    @Override
    public void play() {
        Log.d(LOGGER_KEY, "playing restart activity");
    }
}
