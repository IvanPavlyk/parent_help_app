package ca.cmpt276.prj.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import ca.cmpt276.prj.R;

public class WhoseTurnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whose_turn);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Timeout Timer");
        }
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
        }
        return super.onOptionsItemSelected(item);
    }
}