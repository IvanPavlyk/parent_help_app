package ca.cmpt276.prj.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.CoinSide;
import ca.cmpt276.prj.model.Flip;
import ca.cmpt276.prj.model.Manager;

/**
 * FlipHistoryActivity responsible for the screen that stores the history about
 * all of the coin flips that were performed, saved between application runs
 */
public class FlipHistoryActivity extends AppCompatActivity {
    private Manager manager = Manager.getInstance();
    private boolean showAllHistory = true;
    private ArrayList<Flip> flipHistory;
    private ArrayList<Flip> childHistory;
    private ArrayList<Flip> listToDisplay;
    private String currentChildName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_history);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        extractFromIntent();
        flipHistory = manager.getFlipsRecord();
        childHistory = manager.getFilteredRecord(currentChildName);
        populateListView();
        setupButtons();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context, String currentChild){
        Intent intent = new Intent(context, FlipHistoryActivity.class);
        intent.putExtra("currentChild", currentChild);
        return intent;
    }

    private void extractFromIntent(){
        Intent intent = getIntent();
        currentChildName = intent.getStringExtra("currentChild");
    }

    private void populateListView(){
        if(showAllHistory){
            listToDisplay = flipHistory;
        } else {
            listToDisplay = childHistory;
        }
        ArrayAdapter<Flip> adapter = new HistoryListAdapter();
        ListView historyList = findViewById(R.id.historyList);
        historyList.setAdapter(adapter);
    }

    private class HistoryListAdapter extends ArrayAdapter<Flip> {
        public HistoryListAdapter(){
            super(FlipHistoryActivity.this, R.layout.history_item, listToDisplay);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.history_item, parent, false);
            }

            //find flip to work with
            Flip currentFlip = listToDisplay.get(position);

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

            // Set flipper portrait
            ImageView flipHistoryPortraitView = itemView.findViewById(R.id.flipHistoryPortrait);
            String flipHistoryPortraitString = currentFlip.getPickerPortrait();
            if (flipHistoryPortraitString != null) {
                byte [] encodeByte = Base64.decode(flipHistoryPortraitString, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                flipHistoryPortraitView.setImageBitmap(bitmap);
            }
            else flipHistoryPortraitView.setImageResource(R.drawable.ic_baseline_account_circle_24);

            return itemView;
        }
    }

    private void setupButtons(){
        Button showAll = findViewById(R.id.showAllHistory);
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllHistory = true;
                populateListView();
            }
        });
        Button showCurrChild = findViewById(R.id.showCurrentChildHistory);
        showCurrChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!currentChildName.equals("")) {
                    showAllHistory = false;
                    populateListView();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }

}