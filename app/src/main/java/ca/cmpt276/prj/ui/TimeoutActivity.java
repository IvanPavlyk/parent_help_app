package ca.cmpt276.prj.ui;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.utils.CountdownUtils;

/**
 * TimeoutActivity responsible for the screen that lets user to start the timer for
 * the preloaded durations and also custom durations, works even when app is closed
 * and phone used for other activities
 */
public class TimeoutActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] duration = {"1", "2", "3","5","10","custom"};
    private String[] timeRate = {"100","25", "50", "75",  "200", "300", "400"};
    private int minute;
    private double timeSpeed;

    private static final int mes = 0;
    @SuppressLint("StaticFieldLeak")
    private static TextView timeShow;
    private static TextView speedShow;

    private static Timer timer;
    private static TimerTask timerTask;

    private static long curTime = 0;
    private boolean isPause = false;

    private static MediaPlayer mp;

    private CountDownProgress countDownProgress;
    private int progress;

    Button pause;
    private CountdownUtils mCountdownUtils = CountdownUtils.instance;
    private Spinner spinner;
    private Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Timeout Timer");
        }


        pause = findViewById(R.id.pause);
        findViewById(R.id.start).setOnClickListener(this);
        pause.setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);

        timeShow = findViewById(R.id.show);
        speedShow=findViewById(R.id.speedShow);

        countDownProgress = (CountDownProgress) findViewById(R.id.countdownProgress);
        spinner = findViewById(R.id.timeDuration);
        spinner1 = findViewById(R.id.rate);
        setupDurationSpinner();

        mp =MediaPlayer.create(TimeoutActivity.this,R.raw.sound);
        if(mp.isPlaying()){
            mp.pause();
        }


        mCountdownUtils.setOnCountdownListener(new CountdownUtils.OnCountdownListener() {
            @Override
            public void updateTime(long duration, long curTime) {
                timeShow.setText(timeConvert(curTime));
                float progress = ((float) (duration - curTime)) / duration;
                countDownProgress.updateProgress(progress);
            }

            @Override
            public void onFinish() {
                pushNotification();
                mp.start();
                Toast.makeText(TimeoutActivity.this, "done", Toast.LENGTH_SHORT).show();

                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCountdownUtils.isPause()) {
            pause.setText("Resume");
        } else {
            pause.setText("Pause");
        }
    }



    private void pushNotification() {
        createNotificationChannel();
        String CHANNEL_ID="CHANNEL_ID";

        Intent intent = new Intent(this,TimeoutActivity.class);
        PendingIntent activity = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentIntent(activity)
                .setTicker("Time up")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Practical Parent")
                .setContentInfo("Time up")
                .setContentText("Time Up! Please click stop/cancel to top alarm")
                .setFullScreenIntent(activity,true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager!=null;
        notificationManager.notify(0,builder.build());
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="Channel Name";
            String description="Channel Description";
            int importance=NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel("CHANNEL_ID",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }



    public static String timeConvert(long finishTime) {
        int totalTime = (int) (finishTime / 1000);//seconds
        int hour = 0, minute = 0, second = 0;

        if (totalTime>=3600) {
            hour = totalTime / 3600;
            totalTime = totalTime - 3600 * hour;
        }
        if (totalTime>=60) {
            minute = totalTime / 60;
            totalTime = totalTime - 60 * minute;
        }
        if (totalTime>=0) {
            second = totalTime;
        }
        StringBuilder sb = new StringBuilder();

        if (hour < 10) {
            sb.append("0").append(hour).append(":");
        } else {
            sb.append(hour).append(":");
        }
        if (minute < 10) {
            sb.append("0").append(minute).append(":");
        } else {
            sb.append(minute).append(":");
        }
        if (second < 10) {
            sb.append("0").append(second);
        } else {
            sb.append(second);
        }
        return sb.toString();

    }

    private void dealInterval() {
        String r = spinner1.getSelectedItem().toString();
        String info = null;
        switch (r) {
            case "25":
                timeSpeed =2;
                info="25";
                break;
            case "50":
                timeSpeed = 1.50;
                info="50";
                break;
            case "75":
                timeSpeed = 1.75;
                info="75";
                break;
            case "100":
                timeSpeed = 1.000;
                info="100 ";
                break;
            case "200":
                timeSpeed = 0.5;
                info="200";
                break;
            case "300":
                timeSpeed = 0.33;
                info="300";
                break;
            case "400":
                timeSpeed = 0.25;
                info="400";
                break;
            default:
                timeSpeed = 1.00;
                break;
        }

        speedShow.setText("Time@"+info+"%");
        mCountdownUtils.setInterval((int)(timeSpeed*1000));
    }


    private void setupDurationSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, duration); //android.R.layout.simple_spinner_item
        spinner.setAdapter(adapter);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item, timeRate);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                dealInterval();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    // start,cancel,pause,resume
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.start:
                if(mp.isPlaying()){
                    mp.pause();
                }

                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, duration);
                String s = spinner.getSelectedItem().toString();
                switch (s) {
                    case "1":
                        minute = 60 * 1000;
                        break;
                    case "2":
                        minute = 2 * 60 * 1000;
                        break;
                    case "3":
                        minute = 3 * 60 * 1000;
                        break;
                    case "5":
                        minute = 5 * 60 * 1000;
                        break;
                    case "10":
                        minute = 10 * 60 * 1000;
                        break;
                    default:
                        EditText duration = findViewById(R.id.inputTime);
                        String dur = duration.getText().toString();
                        if (dur.equals("")) {
                            dur = "0";
                        }
                        minute = Integer.parseInt(dur) * 60 * 1000;
                        break;
                }


                mCountdownUtils.setDuration(minute,timeSpeed);
                mCountdownUtils.startCountdown();

                countDownProgress.setCountdownTime(minute);
                countDownProgress.startCountDownTime();

                break;
            case R.id.cancel:
                if(mp.isPlaying()){
                    mp.pause();
                }

                mCountdownUtils.stopCountdown();
                countDownProgress.stopCountDownTime();
                timeShow.setText("00:00:00");


                break;
            case R.id.pause:
                if(mp.isPlaying()){
                    mp.pause();
                }

                if (mCountdownUtils.isStart()) {
                    if (mCountdownUtils.isPause()) {
                        mCountdownUtils.resumeCountdown();
                        pause.setText("Pause");
                    } else {
                        mCountdownUtils.pauseCountdown();
                        pause.setText("Resume");
                    }
                } else {
                    Toast.makeText(this, "Please start countdown first", Toast.LENGTH_SHORT).show();
                }

                break;


            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }
}