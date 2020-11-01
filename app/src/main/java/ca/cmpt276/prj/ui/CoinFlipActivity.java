package ca.cmpt276.prj.ui;

/*
    Coin Flip activity, child calls heads or tails
    and the coin flips, displaying result of flip
 */
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.CoinSide;
import ca.cmpt276.prj.model.Game;

public class CoinFlipActivity extends AppCompatActivity {
    private ImageView coin;
    private Game game = Game.getInstance();
    private Child child;
    private Button heads = findViewById(R.id.headsButton);
    private Button tails = findViewById(R.id.tailsButton);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        coin = findViewById(R.id.coinImage);
        checkForChild();
        setupButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.coin_flip_menu, menu);
        return true;
    }

    private void checkForChild(){
        if(game.getChildrenList().size() == 0){
            // make a new child, don't add to childrenlist
            child = new Child(null, null);
        } else { child = game.getChild(0); }
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
        if(child.getName() == null){
            selection.setText(getString(R.string.noChildHeadsSelection));
        } else {
            selection.setText(getString(R.string.selectionHeads, child.getName()));
        }
        child.setPick(CoinSide.HEAD);
    }

    private void selectTails(){
        TextView selection = findViewById(R.id.selectionDetails);
        if(child.getName() == null){
            selection.setText(getString(R.string.noChildTailsSelection));
        } else {
            selection.setText(getString(R.string.selectionTails, child.getName()));
        }
        child.setPick(CoinSide.TAIL);
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
                boolean result;
                if(child.getName() == null){
                    result = game.nullChildFlip(child);
                } else {
                    result = game.flip();
                }
                setResultText(result);
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

    private void setResultText(boolean result){
        TextView resultText = findViewById(R.id.resultText);
        CoinSide pick = child.getPick();
        if(result) {
            resultText.setText(getString(R.string.victory));
            if(pick == CoinSide.HEAD){
                heads.setBackgroundColor(Color.GREEN);
            } else {
                tails.setBackgroundColor(Color.GREEN);
            }
        } else {
            resultText.setText(getString(R.string.defeat));
            if(pick == CoinSide.HEAD){
                heads.setBackgroundColor(Color.RED);
            } else {
                tails.setBackgroundColor(Color.RED);
            }
        }
    }

}