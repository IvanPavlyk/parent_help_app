package ca.cmpt276.prj.ui;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.CoinSide;
import ca.cmpt276.prj.model.Game;

/**
 * ManageChildrenActivity responsible for the screen that shoes the list of children
 * lets user to add/remove/edit children list, saved between application runs
 */
public class ManageChildrenActivity extends AppCompatActivity {

    private ArrayList<Child> childrenList;
    private Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();   //Hiding the top bar that says the app name
        setContentView(R.layout.activity_manage_children);
        childrenList = new ArrayList<>();
        game = Game.getInstance();
        populateListView();
        final EditText addChildEditText = findViewById(R.id.editTextTextPersonName);
        Button addChildButton = findViewById(R.id.buttonAddChild);
        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addChildEditText.getText().toString().length() > 0) {
                    game.addChild(new Child(addChildEditText.getText().toString(), CoinSide.HEAD));
                    populateListView();
                }
            }
        });
    }


    private void populateListView() {
        ArrayAdapter<Child> adapter = new MyListAdapter();
        ListView list = findViewById(R.id.listViewChildren);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, ManageChildrenActivity.class);
    }

    private class MyListAdapter extends ArrayAdapter<Child>{
        public MyListAdapter(){
            super(ManageChildrenActivity.this, R.layout.item_view, game.getChildrenList());//childrenList);
        }

        @SuppressLint("CutPasteId")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
            View itemView = convertView;
            ChildHolder holder;
            if(itemView == null || itemView.getTag() == null){
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
                holder = new ChildHolder();
                holder.childName = itemView.findViewById(R.id.textNameOfChild);
                holder.removeChild = itemView.findViewById(R.id.buttonDeleteChild);
                itemView.setTag(holder);
            }
            else{
                holder = (ChildHolder) itemView.getTag();
            }


            holder.removeChild.setOnClickListener(new View.OnClickListener() {
                private int pos = position;
                @Override
                public void onClick(View view) {
                    game.removeChild(game.getChild(pos));
                    populateListView();
                    //childrenList.remove(pos);
                    //updateListOfChildrenInGame(game);
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
                            game.getChild(pos).setName(input.getText().toString());
                            populateListView();
                        }
                    });
                    builder.show();
                }
            });
            Child currentChild = game.getChild(position);
            TextView textView = itemView.findViewById(R.id.textNameOfChild);
            textView.setText(currentChild.getName());
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

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }
}