package ca.cmpt276.prj.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import ca.cmpt276.prj.R;

/**
 * Page that handles the addition of new tasks
 */
public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Task Details");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_button:
                EditText aperture= findViewById(R.id.addTask);
                String t = aperture.getText().toString();

                EditText focal= findViewById(R.id.addDescription);
                String d=focal.getText().toString();

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("task_dialog", t);
                bundle.putString("description", d);
                intent.putExtra("task_information", bundle);
                setResult(RESULT_OK, intent);
                finish();

                return true;

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }
}