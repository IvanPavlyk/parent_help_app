package ca.cmpt276.prj.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.manager.Manager;
import ca.cmpt276.prj.model.manager.Task;

/**
 * Edit a task
 */
public class EditTaskActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_one);
        ActionBar actionbar1 = getSupportActionBar();
        Objects.requireNonNull(actionbar1).setDisplayHomeAsUpEnabled(true);
        actionbar1.setTitle("Edit The task you chose.");

        Intent intent = getIntent();
        Task temp = Manager.getInstance().getTask(intent.getIntExtra("index", 0));
        TextView changetext = findViewById(R.id.EditTaskDetail);
        changetext.setText("Task: " + temp.getTaskName() + "\nTask Description: " + temp.getDescription());
        CenterTextView();
        ActivateButton();
    }

    private void ActivateButton() {
        final Button calculation = findViewById(R.id.Edit);
        calculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EditedTask = ((EditText) findViewById(R.id.EditTask)).getText().toString();
                String EditedDescription = ((EditText) findViewById(R.id.EditDescription)).getText().toString();


                Intent intent = getIntent();
                int indexNeeded = intent.getIntExtra("index", 0);
                Manager.getInstance().editTask(EditedTask, EditedDescription, indexNeeded);

                Intent Home = new Intent(EditTaskActivity.this, WhoseTurnActivity.class);
                startActivity(Home);

            }
        });
        // MARK
    }

    @SuppressWarnings("deprecation")
    private void CenterTextView() {
        TextView AskTask = findViewById(R.id.askTaskl);
        TextView AskDescription = findViewById(R.id.askDescription);

        Display display = getWindowManager().getDefaultDisplay();
        // DEPRECATED
        int displayWith = display.getWidth() / 2;
        AskTask.setWidth(displayWith);
        AskDescription.setWidth(displayWith);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }
}