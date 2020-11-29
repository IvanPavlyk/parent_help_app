package ca.cmpt276.prj.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Manager;
import ca.cmpt276.prj.model.Task;

/**
 * Delete a task_dialog
 */
public class DeleteTaskActivity extends AppCompatActivity {
    private Manager temp1 = Manager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_task);

        showEditView();
        ActionBar actionbar = getSupportActionBar();
        Objects.requireNonNull(actionbar).setTitle("Select one task_dialog you want to delete.");
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    private void showEditView() {
        ArrayList<String> editTask = new ArrayList<>();
        ListView listview;
        listview = findViewById(R.id.deleteLen);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, editTask);
        listview.setAdapter(arrayAdapter);
        for (int i = 0; i < temp1.getTaskList().size(); i++) {
            Task temp = temp1.getTask(i);
            editTask.add("Task: "+temp.getTaskName()+"\nTask Description: "+temp.getDescription());
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Manager.getInstance().removeTask(position);
                Intent Home=new Intent(DeleteTaskActivity.this,WhoseTurnActivity.class);
                startActivity(Home);
            }


        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }
}