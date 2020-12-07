package ca.cmpt276.prj.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.Manager;
import ca.cmpt276.prj.model.Task;

/**
 * A component that manages tasks for children to take turns on
 */
public class WhoseTurnActivity extends AppCompatActivity {
    private static Manager manager = Manager.getInstance();
    private ArrayList<Child> childNameList = Manager.getInstance().getChildrenList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whose_turn);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle(R.string.Whose_Turn_Title);
        }

        list_view_build();

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WhoseTurnActivity.this, AddTaskActivity.class);
                startActivityForResult(intent,1);
            }
        });
        list_view_build();

        FloatingActionButton edit = findViewById(R.id.edit);
        if(manager.taskListSize() > -1) {
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WhoseTurnActivity.this, SelectEditTaskActivity.class);
                    startActivityForResult(intent, 1);
                }

            });
        } else if (manager.taskListSize() == 0){
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(WhoseTurnActivity.this, R.string.No_Task_Alert,Toast.LENGTH_LONG).show();
                }
            });
        }

        FloatingActionButton delete = findViewById(R.id.delete);
        if(manager.taskListSize() > -1) {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WhoseTurnActivity.this, DeleteTaskActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
        }else if(manager.taskListSize() == 0){
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(WhoseTurnActivity.this, R.string.No_Task_Delete_Alert,Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, WhoseTurnActivity.class);
    }

    private class CustomListAdapter extends ArrayAdapter<Task> {

        public CustomListAdapter() {
            super(WhoseTurnActivity.this, R.layout.task_item, manager.getTaskList());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // Initialize view
            View itemView = convertView;
            if (itemView == null) itemView = getLayoutInflater().inflate(R.layout.task_item, parent,false);

            // Find current item
            final Task task = manager.getTask(position);

            // Set name text
            TextView nameText = itemView.findViewById(R.id.Item_Task_Name);
            nameText.setText(task.getTaskName());

            // Set description text
            TextView descriptionText = itemView.findViewById(R.id.Item_Task_Description);
            descriptionText.setText(task.getDescription());

            // Set quick portrait
            ImageView quickPortraitView = itemView.findViewById(R.id.Item_Task_Quick_Portrait);
            String quickPortraitString;
            Child turnHolder = task.getTaskHolder();
            if (turnHolder == null) quickPortraitString = "Default Portrait";
            else quickPortraitString = turnHolder.getPortrait();
            if (!quickPortraitString.equals("Default Portrait")) {
                byte [] encodeByte = Base64.decode(quickPortraitString, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                quickPortraitView.setImageBitmap(bitmap);
            }
            else quickPortraitView.setImageResource(R.drawable.ic_baseline_account_circle_24);

            // Setup view button
            Button viewButton = itemView.findViewById(R.id.Item_View_Button);
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(childNameList.size() == 0){
                        Toast.makeText(WhoseTurnActivity.this, R.string.No_Child_Alert, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        TaskDialog dialog = new TaskDialog();
                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        String task_holder = gson.toJson(task.getTaskHolder());

                        bundle.putString("task_holder",task_holder);
                        bundle.putString("task_name", task.getTaskName());
                        bundle.putString("description", task.getDescription());
                        dialog.setArguments(bundle);
                        dialog.show((WhoseTurnActivity.this).getSupportFragmentManager(), "task_dialog");
                    }
                }
            });

            // Setup advance turn button
            Button advanceTurnButton = itemView.findViewById(R.id.Item_Advance_Turn);
            advanceTurnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    task.advanceTurn();
                    list_view_build();
                }
            });

            return itemView;
        }
    }

    private void list_view_build() {
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new CustomListAdapter());
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            if (data == null) throw new AssertionError();
            Bundle information;
            information = data.getBundleExtra("task_information");
            if (information == null) throw new AssertionError();

            String task;
            task = information.getString("task_dialog");
            String description;
            description = information.getString("description");
            if (task == null) throw new AssertionError();
            if (description == null) throw new AssertionError();

            int child_count = childNameList.size()-1;
            if (child_count < 0) {
                Toast.makeText(this, R.string.No_Child_Added_Yet_Alert, Toast.LENGTH_SHORT).show();
                return;
            }

            Task tempTask = new Task(task, description);
            manager.addTask(tempTask);

            Toast.makeText(WhoseTurnActivity.this, R.string.New_Task_Dialog_Alert,Toast.LENGTH_LONG).show();
            list_view_build();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        list_view_build();
    }
}