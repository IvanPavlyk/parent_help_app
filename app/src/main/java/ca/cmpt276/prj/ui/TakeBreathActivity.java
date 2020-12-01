package ca.cmpt276.prj.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class TakeBreathActivity extends AppCompatActivity {
    private Spinner spinner;
    private int numberOfBreaths;
    private State currentState = new IdleState();
    private final State inhaleState = new InhaleState();
    private final State inhalePressedState = new InhalePressedState();
    private final State exhaleState = new ExhaleState();
    private ImageView imageAnimated;
    private boolean pressed = false;

    private abstract class State{
        void handleButtonPress(){}
        void handleEnter(){}
        void handleExit(){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        initializeSpinner();
        initializeHeading();
        initializeImageView();
        setUpButtons();
    }

    private void initializeImageView() {
        imageAnimated = findViewById(R.id.imageViewAnimated);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpButtons() {
        Button btnBreath = findViewById(R.id.btnBreathing);
        /*btnBreath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentState.handleButtonPress();
            }
        });*/
        btnBreath.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    currentState.handleButtonPress();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageAnimated.clearAnimation();
                    pressed = false;
                    //currentState.handleExit();
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

    private class InhaleState extends State {
        @Override
        void handleButtonPress() {
            setState(inhalePressedState);
        }

        @Override
        void handleEnter() {
            Button btn = (Button) findViewById(R.id.btnBreathing);
            btn.setText("In");
            Toast.makeText(TakeBreathActivity.this, "Inhale and hold the In button!", Toast.LENGTH_SHORT).show();
        }
    }

    private class ExhaleState extends State {

    }

    private void setState(State newState){
        currentState = newState;
        currentState.handleEnter();
    }

    private class IdleState extends State {
        @Override
        void handleButtonPress() {
            setState(inhalePressedState);
        }
    }

    private class InhalePressedState extends State {
        @Override
        void handleEnter() {
            //Toast.makeText(TakeBreathActivity.this, "Entered InhalePressed", Toast.LENGTH_SHORT).show();
            Button btn = (Button) findViewById(R.id.btnBreathing);
            btn.setText("In");
            Toast.makeText(TakeBreathActivity.this, "Inhale and hold the In button!", Toast.LENGTH_SHORT).show();
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        void handleButtonPress() {
            if(!pressed){
                Toast.makeText(TakeBreathActivity.this, "KEKW", Toast.LENGTH_SHORT).show();
                startColorAnimation(imageAnimated);
                pressed = true;
            }
            /*Button btnAnimation = findViewById(R.id.btnBreathing);
            btnAnimation.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        if(!pressed){
                            startColorAnimation(imageAnimated);
                            pressed = true;
                        }
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        imageAnimated.clearAnimation();
                        setState(inhaleState);
                    }

                    return true;
                }
            });*/
            //startColorAnimation(imageAnimated);
        }

        @Override
        void handleExit() {
            pressed = false;
            setState(inhaleState);
        }
    }
    private void startColorAnimation(View view){
        /*int colorStart = view.getSolidColor();
        int colorEnd = 0x00ff00;
        ValueAnimator colorAnim = ObjectAnimator.ofInt(view,
                "backgroundColor",
                colorStart,
                colorEnd);
        colorAnim.setDuration(3000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(1);
        colorAnim.start();*/
        ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(3000);     // animation duration in milliseconds
        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        view.startAnimation(fade_in);
    }
}