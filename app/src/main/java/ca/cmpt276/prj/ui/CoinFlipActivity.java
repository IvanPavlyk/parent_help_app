package ca.cmpt276.prj.ui;

/*
    Coin Flip activity, child calls heads or tails
    and the coin flips, displaying result of flip
 */
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

public class CoinFlipActivity extends AppCompatActivity {
    private ImageView coin;
    private Game game = Game.getInstance();
    private String childName;
    private boolean pickedHeads;
    private Button heads;
    private Button tails;
    private static final String EMPTY_STRING = "";
    private MediaPlayer coinSound;

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
                Intent intent = FlipHistoryActivity.makeIntent(CoinFlipActivity.this);
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
        tails = findViewById(R.id.tailsButton);
        coinSound = MediaPlayer.create(this, R.raw.coin_sound);


    }

    private void checkForChild(){
        if(game.getChildrenList().size() == 0){
            childName = EMPTY_STRING;
        } else { childName = game.getChild(0).getName(); }
        TextView childNameText = findViewById(R.id.childName);
        childNameText.setText(childName);
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
    }

    private void selectHeads(){
        TextView selection = findViewById(R.id.selectionDetails);
        if(childName.equals(EMPTY_STRING)){
            selection.setText(getString(R.string.noChildHeadsSelection));
        } else {
            selection.setText(getString(R.string.selectionHeads, childName));
            game.getChild(0).setPick(CoinSide.HEAD);
        }
        pickedHeads = true;
    }

    private void selectTails(){
        TextView selection = findViewById(R.id.selectionDetails);
        if(childName.equals(EMPTY_STRING)){
            selection.setText(getString(R.string.noChildTailsSelection));
        } else {
            selection.setText(getString(R.string.selectionTails, childName));
            game.getChild(0).setPick(CoinSide.TAIL);
        }
        pickedHeads = false;
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
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                boolean result;
                if(childName.equals(EMPTY_STRING)){
                    result = game.plainCoinFlip(pickedHeads);
                } else {
                    result = game.flip();
                }
                showFlipResult(result);
                setResultText(result);
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
            if(pickedHeads){
                coin.setImageResource(R.drawable.heads_coin);
            } else {
                coin.setImageResource(R.drawable.tails_coin);
            }
        } else {
            if(pickedHeads){
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
            if(pickedHeads){
                heads.setBackgroundColor(Color.GREEN);
            } else {
                tails.setBackgroundColor(Color.GREEN);
            }
        } else {
            resultText.setText(getString(R.string.defeat));
            if(pickedHeads){
                heads.setBackgroundColor(Color.RED);
            } else {
                tails.setBackgroundColor(Color.RED);
            }
        }
    }


}