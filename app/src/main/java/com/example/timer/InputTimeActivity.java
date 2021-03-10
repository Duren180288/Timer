package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.FormatException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class InputTimeActivity extends AppCompatActivity {
    private EditText hours;
    private EditText minutes;
    private Switch sound;
    private long timeToMilisec;
    private long hour;
    private long minute;
    private boolean switchChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hours = findViewById(R.id.editTextTimeHours);
        minutes = findViewById(R.id.editTextTimeMinutes);
        sound = findViewById(R.id.switchSound);

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchChecked = ((Switch) v).isChecked();
            }
        });

    }

    public void start(View view) {

        String hoursSt = hours.getText().toString().trim();
//        if (hoursSt.contains("[^0-9]")){
//            Toast.makeText(this, "Input only digitals 0 - 9", Toast.LENGTH_SHORT).show();
//        }
        String minutesSt = minutes.getText().toString().trim();
        if (!hoursSt.isEmpty() || !minutesSt.isEmpty()) {

            try {
                hour = Long.parseLong(hoursSt);

            } catch (Exception f) {
                hour = 0;
            }
            try {
                minute = Long.parseLong(minutesSt);
            } catch (Exception f) {
                minute = 0;
            }

            timeToMilisec = hour * 3600 * 1000 + minute * 60 * 1000;
        } else {
            Toast.makeText(this, R.string.Warning_input, Toast.LENGTH_SHORT).show();
        }


        if (timeToMilisec >= 1892160000000L) {
            System.out.println("perebor");
            int days = (int) (1892160000000L / 1000 / 3600 / 24);
            int yaers = days / 365;
            Toast.makeText(this, "Are You crazy?\n It's more than " + yaers + "yaers\n Try again!!!", Toast.LENGTH_SHORT).show();

        } else if (hour > 0 || minute > 0) {
            System.out.println(timeToMilisec);
            Intent intent = new Intent(this, CountdownActivity.class);
            intent.putExtra("time", timeToMilisec);
            intent.putExtra("sound", switchChecked);
            startActivity(intent);
        }

    }
}
