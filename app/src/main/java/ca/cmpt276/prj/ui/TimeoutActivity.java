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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

import ca.cmpt276.prj.R;

/**
 * TimeoutActivity responsible for the screen that lets user to start the timer for
 * the preloaded durations and also custom durations, works even when app is closed
 * and phone used for other activities
 */
public class TimeoutActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] duration = {"1", "2", "3","5","10","custom"};
    private int minute;

    private static final int mes = 0;
    private static TextView timeShow;

    private static Timer timer;
    private static TimerTask timerTask;

    private static long curTime = 0;
    private boolean isPause = false;

    private static MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Timeout Timer");
        }

        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        timeShow = findViewById(R.id.timer);

        setupDurationSpinner();

        mp =MediaPlayer.create(TimeoutActivity.this,R.raw.sound);
        if(mp.isPlaying()){
            mp.pause();
        }

    }




    private void getTime() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (curTime == 0) {
                    curTime = minute;
                } else {
                    curTime -= 1000;
                }
                Message message = new Message();
                message.what = mes;
                message.obj = curTime;
                handler.sendMessage(message);
                if(curTime==0){
                    pushNotification();
                }
            }
        };
        timer = new Timer();
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
        //mp.stop();
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

    private void setupDurationSpinner() {
        Spinner spinner = findViewById(R.id.duration);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, duration); //android.R.layout.simple_spinner_item
        spinner.setAdapter(adapter);

    }


    public void destroyTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }


    // start,cancel,pause,resume
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                if(mp.isPlaying()){
                    mp.pause();
                }
                Spinner spinner = findViewById(R.id.duration);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, duration);
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

                destroyTimer();
                getTime();
                isPause = false;
                timer.schedule(timerTask, 0, 1000);

                break;
            case R.id.cancel:
                if(mp.isPlaying()){
                    mp.pause();
                }
                if (curTime == 0) {
                    break;
                }
                timeShow.setText(timeConvert(0));
                curTime = 0;
                isPause = false;
                timer.cancel();
                break;
            case R.id.pause:
                if(mp.isPlaying()){
                    mp.pause();
                }

                if (curTime == 0) {
                    break;
                }

                if (!isPause) {
                    isPause = true;
                    timer.cancel();
                }
                break;


            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == mes) {
                long sRecLen = (long) msg.obj;
                timeShow.setText(timeConvert(sRecLen));
                if (sRecLen <= 0) {
                    mp.start();
                    Toast.makeText(TimeoutActivity.this, "done", Toast.LENGTH_SHORT).show();

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                    }

                    timer.cancel();
                    curTime = 0;
                }
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();


//        destroyTimer();
//        if (handler != null) {
//            handler.removeMessages(mes);
//            handler = null;
//        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }
}