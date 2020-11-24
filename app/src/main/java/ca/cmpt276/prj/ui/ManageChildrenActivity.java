package ca.cmpt276.prj.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.manager.Manager;

/**
 * ManageChildrenActivity responsible for the screen that shoes the list of children
 * lets user to addTask/remove/edit children list, saved between application runs
 */
public class ManageChildrenActivity extends AppCompatActivity {

    private Manager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Manage Children");
        }
        setContentView(R.layout.activity_manage_children);
        manager = Manager.getInstance();
        populateListView();
        Button addChildButton = findViewById(R.id.buttonAddChild);
        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddChildActivity.makeIntent(ManageChildrenActivity.this);
                startActivity(intent);
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
            super(ManageChildrenActivity.this, R.layout.item_view, manager.getChildrenList());//childrenList);
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
                holder.childName = itemView.findViewById(R.id.NameOfChild);
                holder.removeChild = itemView.findViewById(R.id.DeleteChild);
                holder.imagePortraitChild = itemView.findViewById(R.id.picture);
                itemView.setTag(holder);
            }
            else{
                holder = (ChildHolder) itemView.getTag();
            }


            holder.removeChild.setOnClickListener(new View.OnClickListener() {
                private int pos = position;
                @Override
                public void onClick(View view) {
                    manager.removeChild(manager.getChild(pos));
                    populateListView();
                    MainActivity.saveInstanceStatic(ManageChildrenActivity.this);
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
                            manager.getChild(pos).setName(input.getText().toString());
                            populateListView();
                        }
                    });
                    builder.show();
                }
            });
            holder.imagePortraitChild.setOnClickListener(new View.OnClickListener() {
                private int pos = position;
                @Override
                public void onClick(View view) {
                    Intent intent = EditChildPortraitActivity.makeIntent(ManageChildrenActivity.this);
                    Bundle bundle = new Bundle();
                    bundle.putInt("PositionChild", pos);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            Child currentChild = manager.getChild(position);

            TextView textView = itemView.findViewById(R.id.NameOfChild);



            textView.setText(currentChild.getName());
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            itemView.setBackgroundColor(Color.parseColor("#f5a742"));

            ImageView imageChild = itemView.findViewById(R.id.picture);

            imageChild.setImageBitmap(stringToBitmap(currentChild.getPortrait()));
            return itemView;
        }
    }

    //Converting compressed String into bitmap to show the image
    public static Bitmap stringToBitmap(String str){
        Bitmap bitmap = null;
        try{
            byte[] encodeByte = Base64.decode(str, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return bitmap;
    }

    //Static inner class representing every row of the list view
    static class ChildHolder{
        TextView childName;
        Button removeChild;
        ImageView imagePortraitChild;
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }
}