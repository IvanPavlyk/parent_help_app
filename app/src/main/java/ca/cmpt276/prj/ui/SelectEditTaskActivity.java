package ca.cmpt276.prj.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.manager.Manager;
import ca.cmpt276.prj.model.manager.Task;

public class SelectEditTaskActivity extends AppCompatActivity {
    private Manager temp1 = Manager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        showEditView();
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Task Details");
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    private void showEditView() {
        ArrayList<String> editTasks = new ArrayList<>();
        ListView listview;
        listview = findViewById(R.id.EditTask);
        // MARK
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, editTasks);
        listview.setAdapter(arrayAdapter);

        for (int i = 0; i < temp1.getTaskList().size(); i++) {
            Task temp = temp1.getTask(i);

            editTasks.add("Task: "+temp.getTaskName()+"\nTask Description: "+temp.getDescription());

        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = makeLaunchIntent(SelectEditTaskActivity.this, position);
                startActivity(intent);
            }


        });
    }

    private Intent makeLaunchIntent(Context context, int position) {
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra("index",position);
        return intent;
    }


}