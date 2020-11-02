package ca.cmpt276.prj.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.CoinSide;
import ca.cmpt276.prj.model.Game;

public class ManageChildrenActivity extends AppCompatActivity {

    private ArrayList<Child> childrenList;
    private Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_manage_children);
        game = Game.getInstance();
        //childrenList = game.getChildrenList();
        loadListChildren();
        populateListInGame(game);
        populateListView();
        final EditText addChildEditText = (EditText) findViewById(R.id.editTextTextPersonName);
        Button addChildButton = (Button) findViewById(R.id.buttonAddChild);
        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childrenList.add(new Child(addChildEditText.getText().toString(), CoinSide.HEAD));
                populateListInGame(game);
                populateListView();
            }
        });
    }

    private void populateListView() {
        ArrayAdapter<Child> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listViewChildren);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void populateListInGame(Game game) {
        for(Child child : childrenList){
            game.addChild(child);
        }
    }

    private void saveListChildren(){
        SharedPreferences sharedPreferences = getSharedPreferences("Shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(childrenList);
        editor.putString("Children list", json);
        editor.apply();
    }

    private void loadListChildren(){
        SharedPreferences sharedPreferences = getSharedPreferences("Shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Children list", null);
        Type type = new TypeToken<ArrayList<Child>>() {}.getType();
        childrenList = gson.fromJson(json, type);
        if(childrenList == null){
            childrenList = new ArrayList<Child>();
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, ManageChildrenActivity.class);
    }

    private class MyListAdapter extends ArrayAdapter<Child>{
        public MyListAdapter(){
            super(ManageChildrenActivity.this, R.layout.item_view, childrenList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }
            Child currentChild = childrenList.get(position);
            TextView textView = (TextView) itemView.findViewById(R.id.textNameOfChild);
            textView.setText("" + currentChild.getName());
            return itemView;
        }
    }

}