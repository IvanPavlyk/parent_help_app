package ca.cmpt276.prj.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.prj.R;

/**
 * TODO: Add class comment here
 * TODO: Exhale state
 * TODO: Wire everything up
 */
public class TakeBreathActivity extends AppCompatActivity {
    private Spinner spinner;
    //Number of breaths to take, number is changed when user chooses
    //different option from the drop down menu on the UI
    private int numberOfBreaths;
    private State currentState = new IdleState();
    private final State inhaleState = new InhaleState();
    private final State exhaleState = new ExhaleState();
    //using ImageView and at first setting image to be transparent and when
    //animation starts setAplha(1.0f) of the imageAnimated
    private ImageView imageAnimated;
    private SoundPool soundPool;
    private int inhalingSound;
    //Number that is responsible for stopping the soundPool
    private int streamID;
    //True only when after switching to Inhale state user did not press button yet
    private boolean pressed = false;
    //if switchFlag true means we are ready to switch state but
    //might need to wait until the animations end
    private boolean switchFlag = false;

    private abstract class State{
        void handleButtonPress(){}
        void handleEnter(){}
        void handleExit(){}
        void handleStop(){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        initializeSpinner();
        initializeHeading();
        initializeResources();
        setUpButtons();
    }

    private void initializeResources() {

        imageAnimated = findViewById(R.id.imageViewAnimated);
        imageAnimated.setAlpha(0.0f);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();
        inhalingSound = soundPool.load(TakeBreathActivity.this, R.raw.breathing_sound, 1);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpButtons() {
        Button btnBreath = findViewById(R.id.btnBreathing);
        btnBreath.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    currentState.handleButtonPress();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(switchFlag){
                        switchFlag = false;
                        currentState.handleExit();
                    }
                    else{
                        currentState.handleStop();
                    }
                }

                return true;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initializeHeading() {
        TextView tvHeading = findViewById(R.id.textViewLetsTake);
        numberOfBreaths = Integer.parseInt(spinner.getSelectedItem().toString());
        if(numberOfBreaths == 1){
            tvHeading.setText("Lets take " + numberOfBreaths + " breath");
        }
        else{
            tvHeading.setText("Lets take " + numberOfBreaths + " breaths");
        }
    }

    private void initializeSpinner() {
        spinner = findViewById(R.id.spinnerList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TakeBreathActivity.this,
                R.layout.spinner_item_layout,
                getResources().getStringArray(R.array.spinner_items));
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                initializeHeading();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, TakeBreathActivity.class);
    }

    private void setState(State newState){
        currentState = newState;
        currentState.handleEnter();
    }

    private class IdleState extends State {
        @Override
        void handleButtonPress() {
            setState(inhaleState);
        }
    }

    private class InhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable3s = new Runnable() {
            @Override
            public void run() {
                Button btn = (Button) findViewById(R.id.btnBreathing);
                btn.setText("Out");
                switchFlag = true;
            }
        };
        Runnable timerRunnable10s = new Runnable() {
            @Override
            public void run() {
                pressed = false;
                stopAnimations();
            }
        };
        @Override
        void handleEnter() {
            Button btn = (Button) findViewById(R.id.btnBreathing);
            btn.setText("In");
            Toast.makeText(TakeBreathActivity.this, "Inhale and hold the In button!", Toast.LENGTH_SHORT).show();
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        void handleButtonPress() {
            if(!pressed){
                timerHandler.postDelayed(timerRunnable3s, 3000);
                timerHandler.postDelayed(timerRunnable10s, 10000);
                startCircleAnimation(imageAnimated);
                pressed = true;
            }
        }

        @Override
        void handleExit() {
            pressed = false;
            stopAnimations();
            setState(exhaleState);
        }

        @Override
        void handleStop() {
            timerHandler.removeCallbacks(timerRunnable3s);
            timerHandler.removeCallbacks(timerRunnable10s);
            pressed = false;
            stopAnimations();
            setState(inhaleState);
        }
    }

    private class ExhaleState extends State {
        @Override
        void handleEnter() {
            Toast.makeText(TakeBreathActivity.this, "Exhale and hold the Out button", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAnimations(){
        imageAnimated.clearAnimation();
        soundPool.stop(streamID);
        imageAnimated.setAlpha(0.0f);
    }
    private void startCircleAnimation(View view){

        ScaleAnimation fade_in =  new ScaleAnimation(0.4f, 1f, 0.4f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(10000);     // animation duration in milliseconds
        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        view.setAlpha(1.0f);
        view.startAnimation(fade_in);
        streamID = soundPool.play(inhalingSound, 1, 1, 1, 1, 1);
    }
}