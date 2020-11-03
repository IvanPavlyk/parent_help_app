package ca.cmpt276.prj.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        getSupportActionBar().hide();   //Hiding the top bar that says the app name
        setContentView(R.layout.activity_manage_children);
        childrenList = new ArrayList<Child>();
        game = Game.getInstance();
        loadListChildren(); //loading the saved list into the childrenList
        updateListOfChildrenInGame(game);
        final EditText addChildEditText = (EditText) findViewById(R.id.editTextTextPersonName);
        Button addChildButton = (Button) findViewById(R.id.buttonAddChild);
        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addChildEditText.getText().toString().length() > 0) {
                    childrenList.add(new Child(addChildEditText.getText().toString(), CoinSide.HEAD));
                    updateListOfChildrenInGame(game);
                }
            }
        });
        //For debugging purposes
        //TODO: remove the toast before submitting the iteration
        Toast.makeText(this, "Number of children in game instance: " + game.getChildrenList().size(), Toast.LENGTH_SHORT).show();
    }

    //Wiping the children in game instance and updating them every time we add a child or delete a child inside manage activity
    private void updateListOfChildrenInGame(Game game){
        game.wipeChildrens();
        populateListInGame(game);
        populateListView();
        saveListChildren();
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

    //Saving the list to the shared preferences
    private void saveListChildren(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("Shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(childrenList);
        editor.putString("Children list", json);
        editor.apply();
    }

    //Loading the list of children from the shared preferences
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

    //Static method that can be called from other activities to get the saved list
    public static ArrayList<Child> loadListChildrenStatic(Context context, ArrayList<Child> list){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Children list", null);
        Type type = new TypeToken<ArrayList<Child>>() {}.getType();
        list = gson.fromJson(json, type);
        if(list == null){
            list = new ArrayList<Child>();
        }
        return list;
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
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
            View itemView = convertView;
            ChildHolder holder = null;
            if(itemView == null || itemView.getTag() == null){
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
                holder = new ChildHolder();
                holder.childName = (TextView) itemView.findViewById(R.id.textNameOfChild);
                holder.removeChild = (Button) itemView.findViewById(R.id.buttonDeleteChild);
                itemView.setTag(holder);
            }
            else{
                holder = (ChildHolder) itemView.getTag();
            }


            holder.removeChild.setOnClickListener(new View.OnClickListener() {
                private int pos = position;
                @Override
                public void onClick(View view) {
                    childrenList.remove(pos);
                    updateListOfChildrenInGame(game);
                }
            });

            holder.childName.setOnClickListener(new View.OnClickListener() {
                private int pos = position;
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Edit the selected name:");
                    final EditText input = new EditText(getContext());

                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            childrenList.get(pos).setName(input.getText().toString());
                            updateListOfChildrenInGame(game);
                        }
                    });
                    builder.show();
                }
            });
            Child currentChild = childrenList.get(position);
            TextView textView = (TextView) itemView.findViewById(R.id.textNameOfChild);
            textView.setText("" + currentChild.getName());
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setTextSize(25);
            textView.setGravity(Gravity.CENTER);
            itemView.setBackgroundColor(Color.parseColor("#f5a742"));
            return itemView;
        }
    }

    //Static inner class representing every row of the list view
    static class ChildHolder{
        TextView childName;
        Button removeChild;
    }

}