package ca.cmpt276.prj.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import ca.cmpt276.prj.R;

public class CoinFlipActivity extends AppCompatActivity {
    private String child = "EXAMPLE_CHILD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
            }
        });
    }

    private void setupTailsButton(){
        Button tails = findViewById(R.id.tailsButton);
        tails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTails();
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



}