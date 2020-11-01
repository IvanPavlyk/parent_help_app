package ca.cmpt276.prj.ui;

/*
    Coin Flip activity, child calls heads or tails
    and the coin flips, displaying result of flip
 */
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import ca.cmpt276.prj.R;

public class CoinFlipActivity extends AppCompatActivity {
    private String child = "EXAMPLE_CHILD";
    private ImageView coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        coin = findViewById(R.id.coinImage);
        setupHeadsButton();
        setupTailsButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.coin_flip_menu, menu);
        return true;
    }

    private void setupHeadsButton(){
        Button heads = findViewById(R.id.headsButton);
        heads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectHeads();
                flipCoinAnimation();
            }
        });
    }

    private void setupTailsButton(){
        Button tails = findViewById(R.id.tailsButton);
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
        selection.setText(getString(R.string.selectionHeads, child));
    }

    private void selectTails(){
        TextView selection = findViewById(R.id.selectionDetails);
        selection.setText(getString(R.string.selectionTails, child));
    }

    private void flipCoinAnimation(){
        ObjectAnimator flipAnimation = ObjectAnimator.ofFloat(coin, "rotationX", 0f, 1800f);
        long animationDuration = 3000;
        flipAnimation.setDuration(animationDuration);
        flipAnimation.addListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setFlipResult(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        flipAnimation.start();
    }

    private void setFlipResult(int result){
        if(result == 0){
            coin.setImageResource(R.drawable.heads_coin);
        } else {
            coin.setImageResource(R.drawable.tails_coin);
        }
    }



}