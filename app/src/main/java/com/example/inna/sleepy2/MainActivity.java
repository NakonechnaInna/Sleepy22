package com.example.inna.sleepy2;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity {


    private LinearLayout mMenu;
    private FloatingActionButton plusFab;
    private Animation animShow;
    private Animation animHide;
    private RelativeLayout contentLayout;
    private boolean isMenuShown = false;
    private boolean isOnSleep = false;

    private ImageButton startButton;
    private ImageView bgSky;
    private ImageView bgStars;
    private ImageView bgOreol;
    private Chronometer timer;


    private NoiseDetector mNoiseDetectorThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMenu = findViewById(R.id.menu);
        plusFab = findViewById(R.id.plusFab);
        animShow = AnimationUtils.loadAnimation(this, R.anim.anim_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.anim_hide);
        contentLayout = findViewById(R.id.contentLayout);
        bgSky = findViewById(R.id.bgsky);
        bgStars = findViewById(R.id.bgstars);
        bgOreol = findViewById(R.id.bgoreol);
        startButton = findViewById(R.id.startbutton);
        timer = findViewById(R.id.timer);


        //TODO firebase
        // TODO foreground service android, потоки, buffer lendth
        // todo вкладка "анализ сна" с записью кусков
        //todo переделать логику вкладок
        // intent. в интенте поток. в потоке счение звука с ммикрофона
        //проходимся по буферу. если А выше заданного значения - начинается запись / рекция на звук + запись в хранилище отметка времени
        //запись зука. построить спектр? анализ отметок времени - частота/длительность
        //buffsize if buf i > znachenue, record srart

        //Выезжающее меню
        plusFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMenuShown) return;
                plusFab.hide();
                Blurry.with(MainActivity.this)
                        .radius(25)
                        .sampling(2)
                        .async()
                        .animate(500)
                        .onto((ViewGroup) findViewById(R.id.contentLayout));
                mMenu.setVisibility(View.VISIBLE);
                mMenu.startAnimation(animShow);
                startButton.setClickable(false);
                isMenuShown = true;

            }
        });

        //Скрытие меню
        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMenuShown)
                    hideMenu(plusFab, mMenu, animHide, contentLayout, startButton);

            }
        });


    }

    private void hideMenu(FloatingActionButton plusFab, LinearLayout mMenu, Animation animHide, RelativeLayout contentLayout, ImageButton mMoon) {
        plusFab.show();
        mMenu.startAnimation(animHide);
        mMenu.setVisibility(View.GONE);
        Blurry.delete(contentLayout);
        mMoon.setClickable(true);
        isMenuShown = false;
    }

    @Override
    public void onBackPressed() {
        if (isMenuShown) {
            hideMenu(plusFab, mMenu, animHide, contentLayout, startButton);
            return;
        }
        super.onBackPressed();
    }

    public void toHelp(View view) {
        Intent openHelp = new Intent(MainActivity.this, HelpActivity.class);
        MainActivity.this.startActivity(openHelp);
    }

    public void toAlarm(View view) {
        Intent openHelp = new Intent(MainActivity.this, AlarmActivity.class);
        MainActivity.this.startActivity(openHelp);
    }

    public void startButtonClick(View view) {

        if (!isOnSleep) {
            bgSky.setImageResource(R.drawable.timesky);
            bgOreol.setImageResource(R.drawable.timeoreol);
            startButton.setBackgroundResource(R.drawable.sun);
            isOnSleep = true;
            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
            timer.setVisibility(View.VISIBLE);
            mNoiseDetectorThread = new NoiseDetector(this);
            return;
        }
        if (isOnSleep) {
            bgSky.setImageResource(R.drawable.sky);
            bgOreol.setImageResource(R.drawable.oreol);
            startButton.setBackgroundResource(R.drawable.moon);
            try {
                mNoiseDetectorThread.IsNoiseDetectorEnabled = false;
                mNoiseDetectorThread = null;
            } catch (Exception e) {
                System.out.println("Blow Value=" + e.getMessage());
            }
            timer.stop();
            timer.setVisibility(View.GONE);
            isOnSleep = false;
            return;


        }

    }


}

