package ca.cmpt276.prj.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.manager.Manager;
import ca.cmpt276.prj.model.manager.Task;

public class EditOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_one);
        ActionBar actionbar1 = getSupportActionBar();
        actionbar1.setDisplayHomeAsUpEnabled(true);
        actionbar1.setTitle("Edit The task you chose.");

        Intent intent = getIntent();
        Task temp = Manager.getInstance().retrieving(intent.getIntExtra("index",0));
        TextView changetext = findViewById(R.id.EditTaskDetail);
        changetext.setText("Task: "+temp.getName()+"\nTask Description: "+temp.getDescription() );
        CenterTextView();
        ActivateButton(temp.getName());

    }

    private void ActivateButton(String taskName) {
        final Button calculation = (Button)findViewById(R.id.Edit);
        calculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EditedTask = ((EditText)findViewById(R.id.EditTask)).getText().toString();
                String EditedDescription = ((EditText)findViewById(R.id.EditDescription)).getText().toString();


                Intent intent = getIntent();
                Task EditLens = Manager.getInstance().retrieving(intent.getIntExtra("index",0));
                int indexNeeded = Manager.getInstance().returnint(intent.getIntExtra("index",0));
                Manager.getInstance().EditTask(EditedTask,EditedDescription,indexNeeded);

                Intent Home=new Intent(EditOne.this,WhoseTurnActivity.class);
                startActivity(Home);

        }
    });{

    };
}




    private void CenterTextView(){
        TextView AskTask = findViewById(R.id.askTaskl);
        TextView AskDescription = findViewById(R.id.askDescription);

        Display display = getWindowManager().getDefaultDisplay();
        int displayWith = display.getWidth() / 2;
        AskTask.setWidth(displayWith);
        AskDescription.setWidth(displayWith);
    }




}