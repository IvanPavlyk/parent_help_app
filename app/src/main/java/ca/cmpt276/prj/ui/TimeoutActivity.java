package ca.cmpt276.prj.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

import ca.cmpt276.prj.R;

public class TimeoutActivity<timeout> extends AppCompatActivity implements View.OnClickListener {

    private String[] duration = {"1", "2", "3","5","10","custom"};
    private int minute;

    private static final int mes = 0;
    private TextView timeShow;

    private Timer timer;
    private TimerTask timerTask;

    private long curTime = 0;
    private boolean isPause = false;

    private MediaPlayer mp;

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
        timeShow = (TextView) findViewById(R.id.timer);

        setupDurationSpinner();
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
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Practical Parent")
                .setContentText("Time Up!")
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        EditText duration=(EditText)findViewById(R.id.inputTime);
        String dur=duration.getText().toString();
        minute=Integer.parseInt(dur);
        return super.onOptionsItemSelected(item);
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
        Spinner spinner = (Spinner) findViewById(R.id.duration);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, duration);
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

    //    start,cancel,pause,resume
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                Spinner spinner = (Spinner) findViewById(R.id.duration);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, duration);
                String s = spinner.getSelectedItem().toString();

                if(s=="1"){
                    minute=1*60*1000;
                }
                else if(s=="2"){
                    minute=2*60*1000;
                }
                else if(s=="3"){
                    minute=3*60*1000;
                }
                else if (s=="5"){
                    minute=5*60*1000;
                }
                else if(s=="10"){
                    minute=10*60*1000;
                }
                else{
                    EditText duration=(EditText)findViewById(R.id.inputTime);
                    String dur=duration.getText().toString();
                    if(dur.equals("")){
                        dur="0";
                    }
                    minute=Integer.parseInt(dur)*60*1000;
                }

                destroyTimer();
                getTime();
                isPause = false;
                timer.schedule(timerTask, 0, 1000);

                break;
            case R.id.cancel:
                mp.stop();
                if (curTime == 0) {
                    break;
                }

                curTime = 0;
                isPause = false;
                timer.cancel();
                break;
            case R.id.pause:
                mp.stop();
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case mes:
                    long sRecLen = (long) msg.obj;
                    timeShow.setText(timeConvert(sRecLen));
                    if (sRecLen <= 0) {
                        Toast.makeText(TimeoutActivity.this,"done",Toast.LENGTH_SHORT).show();
                        mp=MediaPlayer.create(TimeoutActivity.this,R.raw.sound);
                        mp.start();
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                        }

                        timer.cancel();
                        curTime = 0;
                    }
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyTimer();
        if (handler != null) {
            handler.removeMessages(mes);
            handler = null;
        }
    }
}