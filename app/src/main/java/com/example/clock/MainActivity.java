package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView txtTime;
    private TextView btnStart;
    private TextView btnPause;
    private TextView btnResrt;


    private  long pauseOff;
    private Chronometer chronometer;
    private boolean aBoolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chronometer = findViewById(R.id.txt_time);
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnResrt = findViewById(R.id.btn_resrt);




        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!aBoolean){
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOff);
                    chronometer.start();
                    aBoolean=true;
                }

            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aBoolean){
                    chronometer.stop();
                    pauseOff=SystemClock.elapsedRealtime() - chronometer.getBase();
                    aBoolean=false;
                }
            }
        });

        btnResrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOff=0;
            }
        });


    }
}