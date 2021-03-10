package com.example.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.ls.LSOutput;

import java.util.Locale;

public class CountdownActivity extends AppCompatActivity {
    private TextView textViewTimer;
    private Button mButtonStartPause;
    private Button mButtonReset;
    boolean isRunning;
    private long mTimeLeftInMillis;
    private CountDownTimer countDownTimer;
    private  ProgressBar progressBarHours;
    private  ProgressBar progressBarMinutes;
    private  ProgressBar progressBarSeconds;
    private long receivedTimeMilSec;
    private boolean switchChecked;
    private MediaPlayer mediaPlayer;
    private ImageButton buttonSwitchOffSound;
    private ImageButton buttonSwitchOnSound;


    private int hours;
    private int minutes;
    private int seconds;


    private String showedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        textViewTimer = findViewById(R.id.textViewTimer);
        mButtonStartPause = findViewById(R.id.buttonStart_Pause);
        mButtonReset = findViewById(R.id.buttonReset);
        progressBarHours = findViewById(R.id.progressBarHours);
        progressBarMinutes = findViewById(R.id.progressBarMinutes);
        progressBarSeconds = findViewById(R.id.progressBarSeconds);
        buttonSwitchOffSound = findViewById(R.id.ButtonSwitchOffSound);
        buttonSwitchOnSound = findViewById(R.id.ButtonSwitchOnSound);
        mediaPlayer = MediaPlayer.create(this, R.raw.gong);

        Intent intent = getIntent();
        receivedTimeMilSec = intent.getLongExtra("time", 0);
        switchChecked = intent.getBooleanExtra("sound", false);
        System.out.println(switchChecked);
        mTimeLeftInMillis = receivedTimeMilSec;
        if (savedInstanceState != null) {
            mTimeLeftInMillis = savedInstanceState.getLong("mTimeLeftInMillis");
            isRunning = savedInstanceState.getBoolean("isRunning");
            switchChecked = savedInstanceState.getBoolean("sound");
            System.out.println(isRunning);

            if (isRunning) {
                startTimer();
            }
        } else {
            mTimeLeftInMillis = receivedTimeMilSec;
        }


        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDownText();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("millisUntilFinished" + millisUntilFinished);
                System.out.println(mTimeLeftInMillis);
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                isRunning = false;
                mButtonStartPause.setText(R.string.Start);
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
                if (switchChecked){
                mediaPlayer.start();
                }
            }
        }.start();
        isRunning = true;
        mButtonStartPause.setText(R.string.pause);
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setBackgroundColor(0xFF018786);

    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isRunning = false;
        mButtonStartPause.setBackgroundColor(0x008000);
        mButtonStartPause.setText(R.string.Start);
        mButtonReset.setVisibility(View.VISIBLE);

    }

    private void resetTimer() {
        mTimeLeftInMillis = receivedTimeMilSec;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);

    }

    private void updateCountDownText() {
        seconds = (int) (mTimeLeftInMillis / 1000);
        hours = seconds / 3600;
        minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        showedTime = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        textViewTimer.setText(showedTime);
        if (hours == 0){
        progressBarHours.setVisibility(View.INVISIBLE);}
        else {
            progressBarHours.setVisibility(View.VISIBLE);
            progressBarHours.setMax(hours);
            progressBarHours.setProgress(hours);
        }

        progressBarMinutes.setMax(60);
        progressBarSeconds.setMax(60);

        progressBarMinutes.setProgress(minutes);
        progressBarSeconds.setProgress(seconds);

        if(switchChecked){
            buttonSwitchOffSound.setVisibility(View.VISIBLE);
            buttonSwitchOnSound.setVisibility(View.INVISIBLE);
        }else {
            buttonSwitchOffSound.setVisibility(View.INVISIBLE);
            buttonSwitchOnSound.setVisibility(View.VISIBLE);
        }



    }

    public void backToMain(View view) {
        if (isRunning) {
            pauseTimer();
        }

        Intent intent = new Intent(this, InputTimeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("mTimeLeftInMillis", mTimeLeftInMillis);
        outState.putBoolean("isRunning", isRunning);
        outState.putBoolean("sound", switchChecked);

    }

    public void switchOffSound(View view) {
        switchChecked = false;
        updateCountDownText();
    }

    public void switchOnSound(View view) {
        switchChecked = true;
        updateCountDownText();
    }
}



