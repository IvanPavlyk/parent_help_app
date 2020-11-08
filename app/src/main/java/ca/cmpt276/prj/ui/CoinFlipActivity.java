package ca.cmpt276.prj.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.CoinSide;
import ca.cmpt276.prj.model.Game;

/**
 * Coin Flip activity, child calls heads or tails
 * and the coin flips, displaying result of flip
 */
public class CoinFlipActivity extends AppCompatActivity {
    private ImageView coin;
    private Game game = Game.getInstance();
    private String childName;
    private Button heads, tails, reset, flip;
    private static final String EMPTY_STRING = "";
    private MediaPlayer coinSound;
    private int defaultColor = Color.GRAY;
    private CoinSide coinSelection;

    public static Intent makeIntent(Context context){
        return new Intent(context, CoinFlipActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initializeResources();
        checkForChild();
        setupButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.coin_flip_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_history:
                Intent intent = FlipHistoryActivity.makeIntent(CoinFlipActivity.this, childName);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeResources(){
        coin = findViewById(R.id.coinImage);
        heads = findViewById(R.id.headsButton);
        heads.setBackgroundColor(defaultColor);
        tails = findViewById(R.id.tailsButton);
        tails.setBackgroundColor(defaultColor);
        reset = findViewById(R.id.resetButton);
        reset.setBackgroundColor(defaultColor);
        flip = findViewById(R.id.flipButton);
        coinSound = MediaPlayer.create(this, R.raw.coin_sound);
    }

    private void checkForChild(){
        if(game.getChildrenList().size() == 0){
            childName = EMPTY_STRING;
            hideButtons();
        } else {
            childName = game.getChild(0).getName();
            flip.setVisibility(View.GONE);
            TextView child = findViewById(R.id.childName);
            child.setText(childName);
        }

    }

    private void setupButtons(){
        heads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectHeads();
                flipCoinAnimation();
            }
        });
        tails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTails();
                flipCoinAnimation();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView selection = findViewById(R.id.selectionDetails);
                selection.setText(EMPTY_STRING);
                TextView result = findViewById(R.id.resultText);
                result.setText(EMPTY_STRING);
                TextView instructions = findViewById(R.id.selectInstructionsText);
                instructions.setVisibility(View.VISIBLE);
                checkForChild();
                coin.setImageResource(R.drawable.blank_coin);
                heads.setBackgroundColor(defaultColor);
                tails.setBackgroundColor(defaultColor);
                setupButtons();
            }
        });
        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCoinAnimation();
            }
        });
    }

    private void selectHeads(){
        TextView selection = findViewById(R.id.selectionDetails);
        selection.setText(getString(R.string.selectionHeads, childName));
        game.getChild(0).setPick(CoinSide.HEAD);
        coinSelection = CoinSide.HEAD;
    }

    private void selectTails(){
        TextView selection = findViewById(R.id.selectionDetails);
        selection.setText(getString(R.string.selectionTails, childName));
        game.getChild(0).setPick(CoinSide.TAIL);
        coinSelection = CoinSide.TAIL;
    }

    private void flipCoinAnimation(){
        ObjectAnimator flipAnimation = ObjectAnimator.ofFloat(coin, "rotationX", 0f, (5*360f));
        long animationDuration = 1500;
        flipAnimation.setDuration(animationDuration);
        flipAnimation.addListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationStart(Animator animation) {
                heads.setClickable(false);
                tails.setClickable(false);
                flip.setClickable(false);
                TextView instructions = findViewById(R.id.selectInstructionsText);
                instructions.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                boolean result;
                if(childName.equals(EMPTY_STRING)){
                    result = game.plainCoinFlip();
                    displayResultNoChildren(result);
                } else {
                    result = game.flip();
                    showFlipResult(result);
                    setResultText(result);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        coinSound.start();
        flipAnimation.start();
    }

    private void showFlipResult(boolean result){
        if(result){
            if(coinSelection == CoinSide.HEAD){
                coin.setImageResource(R.drawable.heads_coin);
            } else {
                coin.setImageResource(R.drawable.tails_coin);
            }
        } else {
            if(coinSelection == CoinSide.HEAD){
                coin.setImageResource(R.drawable.tails_coin);
            } else {
                coin.setImageResource(R.drawable.heads_coin);
            }
        }
    }

    private void setResultText(boolean result){
        TextView resultText = findViewById(R.id.resultText);
        if(result) {
            resultText.setText(getString(R.string.victory));
            if(coinSelection == CoinSide.HEAD){
                heads.setBackgroundColor(Color.GREEN);
            } else {
                tails.setBackgroundColor(Color.GREEN);
            }
        } else {
            resultText.setText(getString(R.string.defeat));
            if(coinSelection == CoinSide.HEAD){
                heads.setBackgroundColor(Color.RED);
            } else {
                tails.setBackgroundColor(Color.RED);
            }
        }
    }

    private void hideButtons(){
        tails.setVisibility(View.GONE);
        heads.setVisibility(View.GONE);
        TextView selection = findViewById(R.id.selectInstructionsText);
        selection.setVisibility(View.GONE);
    }

    private void displayResultNoChildren(Boolean result){
        TextView resultText = findViewById(R.id.resultText);
        if(result){
            coin.setImageResource(R.drawable.heads_coin);
            resultText.setText(R.string.heads_result);
        } else {
            coin.setImageResource(R.drawable.tails_coin);
            resultText.setText(R.string.tails_result);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }
}