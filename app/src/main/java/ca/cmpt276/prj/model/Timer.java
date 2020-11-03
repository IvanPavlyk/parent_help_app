package ca.cmpt276.prj.model;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmpt276.prj.R;

public class Timer extends AppCompatActivity {
    private String[] duration = {"1", "2", "3","5","10"};
    private int timeout;

    private final static String TAG = "BroadcastService";
    public static final String COUNTDOWN = "your_package_name.countdown_br";
    Intent bi = new Intent(COUNTDOWN);
    CountDownTimer time = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Options");
        }

        setupDurationSpinner();

        Log.i(TAG, "Starting timer...");

        //final Text timeShow=getText(findViewById(R.id.timer));

        time=new CountDownTimer(timeout,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
               // timeShow.setTextContent("seconds remaining: "+millisUntilFinished/1000);
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);

            }

            @Override
            public void onFinish() {
                //timeShow.setTextContent("done!");
                Log.i(TAG, "Timer finished");
            }
        };
        time.start();
    }

    @Override
    public void onDestroy() {

        time.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public IBinder onBind(Intent arg0) {
//        return null;
//    }

//    private Text getText(View viewById) {
//        return null;
//    }

    private void setupDurationSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.duration);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, duration);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                if(i==0){
                    timeout=1*60*1000;
                }
                else if(i==1){
                    timeout=2*60*1000;
                }
                else if(i==2){
                    timeout=3*60*1000;
                }
                else if(i==3){
                    timeout=5*60*1000;
                }
                else{
                    timeout=10*60*1000;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }





}