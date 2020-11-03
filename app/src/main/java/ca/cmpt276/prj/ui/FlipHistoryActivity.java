package ca.cmpt276.prj.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.CoinSide;
import ca.cmpt276.prj.model.Flip;
import ca.cmpt276.prj.model.Game;

public class FlipHistoryActivity extends AppCompatActivity {
    private Game game = Game.getInstance();
    private ArrayList<Flip> flipHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_history);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        flipHistory = game.getFlipsRecord();
        exampleFlips();
        populateListView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exampleFlips(){
        flipHistory.add(new Flip("NULL", "Emily", CoinSide.HEAD, true));
        flipHistory.add(new Flip("NULL", "Emily", CoinSide.HEAD, false));
        flipHistory.add(new Flip("NULL", "Emily", CoinSide.TAIL, true));
        flipHistory.add(new Flip("NULL", "Emily", CoinSide.TAIL, false));
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, FlipHistoryActivity.class);
    }

    private void populateListView(){
        ArrayAdapter<Flip> adapter = new HistoryListAdapter();
        ListView historyList = findViewById(R.id.historyList);
        historyList.setAdapter(adapter);
    }

    private class HistoryListAdapter extends ArrayAdapter<Flip> {
        public HistoryListAdapter(){
            super(FlipHistoryActivity.this, R.layout.history_item, flipHistory);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.history_item, parent, false);
            }

            //find flip to work with
            Flip currentFlip = flipHistory.get(position);

            //fill the view
            ImageView img = itemView.findViewById(R.id.resultImage);
            if(currentFlip.isWin()){
                img.setImageResource(R.drawable.checkmark);
            } else {
                img.setImageResource(R.drawable.incorrect_icon);
            }
            //set text boxes
            TextView childName = itemView.findViewById(R.id.flipChildName);
            childName.setText(currentFlip.getPickerName());

            TextView time = itemView.findViewById(R.id.flipTime);
            time.setText(currentFlip.getTime());

            TextView outcome = itemView.findViewById(R.id.flipSelection);
            if(currentFlip.getOutcome() == CoinSide.HEAD){
                outcome.setText(R.string.headString);
            } else {
                outcome.setText(R.string.tailsString);
            }

            return itemView;
        }
    }

}