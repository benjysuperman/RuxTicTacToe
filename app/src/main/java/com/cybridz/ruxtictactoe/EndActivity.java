package com.cybridz.ruxtictactoe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.enums.GameStatus;
import com.cybridz.ruxtictactoe.components.GameOver;
import com.cybridz.ruxtictactoe.enums.Light;
import java.util.Objects;

public class EndActivity extends AbstractActivity {

    /**
     * Start Activity elements
     */
    @SuppressWarnings("FieldCanBeLocal")
    private Button restart_button;
    @SuppressWarnings("FieldCanBeLocal")
    private TextView message;
    @SuppressWarnings("FieldCanBeLocal")
    private ImageView image;
    private GameOver gameOver;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameOver = (GameOver) getIntent().getSerializableExtra("gameOver");
        setContentView(R.layout.restart_activity);
        current_view = findViewById(R.id.restart_activity);
        initializeServices();
        image = findViewById(R.id.gameover);
        updateImageView(image, Objects.requireNonNull(gameOver).getImage());
        message = findViewById(R.id.message);
        String messageText = gameOver.getWinner() + "\n" + gameOver.getMessage();
        message.setText(messageText);
        restart_button = findViewById(R.id.restart_game);
        restart_button.setText("Restart");
        restart_button.setOnClickListener(view -> {
            Log.d(LOGGER_KEY, "clicked restart btn");
            goToStartActivity();
        });
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
        sharedServices.getRobotService().robotOpenMotor();
        sharedServices.getBlinkingLightMessageService().setCurrentEarsColor(gameOver.getWinner().equalsIgnoreCase("RUX") ? Light.RED : (gameOver.getWinner().equalsIgnoreCase("PLAYER") ? Light.GREEN : Light.ORANGE));
        sharedServices.getMotorRotationMessageService().sendRandomMotorRotation();
        sharedServices.getRobotService().robotPlayTTs(gameOver.getMessage());
        switch (gameOver.getWinner()){
            case GameStatus.PLAYER_FINISHED:
                sharedServices.getRobotService().robotStartExpression("h0211");
                sharedServices.getRobotService().robotControlSound("a0115");
                break;
            case GameStatus.RUX_FINISHED:
                sharedServices.getRobotService().robotStartExpression("h0027");
                sharedServices.getRobotService().robotControlSound("a0039");
                break;
            case GameStatus.DRAW_FINISHED:
                sharedServices.getRobotService().robotStartExpression("h0046");
                sharedServices.getRobotService().robotControlSound("a0067");
                break;
            default:
                sharedServices.getRobotService().robotStartExpression("h0210");
                sharedServices.getRobotService().robotControlSound("a0112");
                break;
        }
        sharedServices.getMotorRotationMessageService().sendRandomMotorRotation();
        Log.d(LOGGER_KEY, "playing restart activity");
    }
}
