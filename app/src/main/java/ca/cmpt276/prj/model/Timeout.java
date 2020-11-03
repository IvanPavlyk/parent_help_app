package ca.cmpt276.prj.model;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import ca.cmpt276.prj.R;

public class Timeout<timeout> extends AppCompatActivity implements View.OnClickListener {

    private String[] duration = {"1", "2", "3","5","10"};
    private int minute;

    private static final int mes = 0;
    private TextView timeShow;

    private Timer timer;
    private TimerTask timerTask;

    private long curTime = 0;
    private boolean isPause = false;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //Vibrator vibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Timeout Timer");
        }



        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        timeShow = (TextView) findViewById(R.id.timer);

        getTime();
        timer.schedule(timerTask, 0, 1000);

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
                mHandler.sendMessage(message);
            }
        };
        timer = new Timer();
    }

    public static String timeConvert(long finishTime) {
        int totalTime = (int) (finishTime / 1000);//seconds
        int hour = 0, minute = 0, second = 0;

        if (totalTime>=3600) {
            hour = totalTime / 3600;
            totalTime = totalTime - 3600 * hour;
        }
        else if (totalTime>=60) {
            minute = totalTime / 60;
            totalTime = totalTime - 60 * minute;
        }
        else if (totalTime>=0) {
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                minute=Integer.parseInt(item)*60*1000;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                destroyTimer();
                getTime();
                isPause = false;
                timer.schedule(timerTask, 0, 1000);

                break;
            case R.id.cancel:
                if (curTime == 0) {
                    break;
                }
                curTime = 0;
                isPause = false;
                timer.cancel();
                break;
            case R.id.pause:
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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case mes:
                    long sRecLen = (long) msg.obj;
                    timeShow.setText(timeConvert(sRecLen));
                    if (sRecLen <= 0) {
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
        if (mHandler != null) {
            mHandler.removeMessages(mes);
            mHandler = null;
        }
    }
}