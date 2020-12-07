package ca.cmpt276.prj.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.Manager;

/**
 * Activity that shows the queue of children, allows users to choose the next child to flip
 * or select none and do a coin flip with no children
 */
public class ChooseNextFlipActivity extends AppCompatActivity {

    private Manager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle(R.string.bar_text_next_flip);
        }
        manager = Manager.getInstance();
        setContentView(R.layout.activity_choose_next_flip);
        populateListView();
        registerListClickCallback();
        registerNoneButton();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, ChooseNextFlipActivity.class);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateListView(){
        ArrayAdapter<Child> adapter = new ChildListAdapter();
        ListView list = findViewById(R.id.nextFlipChildQueue);
        list.setAdapter(adapter);
    }

    private void registerListClickCallback(){
        final ListView list = findViewById(R.id.nextFlipChildQueue);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Child child = manager.getChild(position);
                manager.removeChild(child);
                manager.prependChild(child);
                Intent output = new Intent();
                output.putExtra("SELECTION", "CHILD");
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }

    private void registerNoneButton(){
        Button noneBtn = findViewById(R.id.selectNoneButton);
        noneBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent output = new Intent();
                output.putExtra("SELECTION", "NO_CHILD");
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }

    private class ChildListAdapter extends ArrayAdapter<Child> {
        public ChildListAdapter(){
            super(ChooseNextFlipActivity.this, R.layout.flip_child_item, manager.getChildrenList());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.flip_child_item, parent, false);
            }
            Child currentChild = manager.getChild(position);
            ImageView img = itemView.findViewById(R.id.childItemImage);
            img.setImageBitmap(ManageChildrenActivity.stringToBitmap(currentChild.getPortrait()));

            TextView childName = itemView.findViewById(R.id.childNameText);
            childName.setText(currentChild.getName());
            return itemView;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }
}