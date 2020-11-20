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

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_add_task);
        ActionBar bar = getSupportActionBar();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab=getSupportActionBar();
        assert ab != null;
        ab.setTitle("Lens Details");
        ab.setDisplayHomeAsUpEnabled(true);
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
                EditText make=(EditText)findViewById(R.id.addName);
                String n = make.getText().toString();

                EditText aperture=(EditText)findViewById(R.id.addTask);
                String t = aperture.getText().toString();

                EditText focal=(EditText)findViewById(R.id.addDescription);
                String d=focal.getText().toString();

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("name", n);
                bundle.putString("aperture", t);
                bundle.putString("len_focal", d);
                intent.putExtra("Len_information", bundle);
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
}