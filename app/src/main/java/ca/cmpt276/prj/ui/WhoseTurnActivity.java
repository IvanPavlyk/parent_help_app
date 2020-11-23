package ca.cmpt276.prj.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.MyTaskDialog;
import ca.cmpt276.prj.model.coinManager.CoinManager;
import ca.cmpt276.prj.model.taskManager.Task;
import ca.cmpt276.prj.model.taskManager.TaskManager;

public class WhoseTurnActivity extends AppCompatActivity {
    private TaskManager taskManager=TaskManager.getInstance();
    private ArrayList<Child> ChildNameList= CoinManager.getInstance().getChildrenList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whose_turn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Whose Turn");

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
        if(taskManager.size() != 0) {

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WhoseTurnActivity.this, EditTaskActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
        }else if(taskManager.size() == 0){
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(WhoseTurnActivity.this, "Please  a new task.\n At the bottom right corner \"+\" sign.",Toast.LENGTH_LONG).show();
                }
            });

        }

        FloatingActionButton delete = findViewById(R.id.delete);
        if(taskManager.size() != 0) {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WhoseTurnActivity.this, deleteTask.class);
                    startActivityForResult(intent, 1);
                }
            });
        }else if(taskManager.size() == 0){
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(WhoseTurnActivity.this, "Please Add a new task.\n At the bottom right corner \"+\" sign.",Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    private void list_view_build() {
        ArrayList<String> arr=new ArrayList<>();
        int count=0;
        while(count< taskManager.size()){
            Task buffer=taskManager.retrieving(count);
            arr.add("Task: "+buffer.getTaskName()+"\nTask Description: "+buffer.getDescription());
            count+=1;
        }

        ListView listView=(ListView)findViewById(R.id.list_view);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arr);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                Task temp = TaskManager.getInstance().retrieving(position);
                MyTaskDialog dialog = new MyTaskDialog();
                Bundle bundle = new Bundle();
                bundle.putString("child_name",temp.getName());
                bundle.putString("task_name", temp.getTaskName());
                bundle.putString("description", temp.getDescription());
                dialog.setArguments(bundle);
                dialog.show((WhoseTurnActivity.this).getSupportFragmentManager(), "Task Tag");


            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK){
            if (data == null) throw new AssertionError();
            Bundle information;
            information = data.getBundleExtra("task_information");
            if (information == null) throw new AssertionError();

            String task;
            task = information.getString("task");
            String description;
            description = information.getString("description");
            if (task == null) throw new AssertionError();
            if (description == null) throw new AssertionError();


            SharedPreferences sp = getSharedPreferences("last_child", Context.MODE_PRIVATE);
            String lastTask = sp.getString("name", "");


            int child_count = ChildNameList.size()-1;
            if (child_count < 0) {
                Toast.makeText(this, "No Child Added Yet", Toast.LENGTH_SHORT).show();
                return;
            }
            int random_index = (int)(Math.random() * ((child_count) + 1));
            if (!lastTask.equals("")) {
                while (ChildNameList.get(random_index).equals(lastTask)) {
                    random_index = (int) (Math.random() * ((child_count) + 1));
                }
            }
            String task_assigned_to = ChildNameList.get(random_index).getName();


            SharedPreferences.Editor editor = sp.edit();
            editor.putString("name", task_assigned_to);


            Task temp = new Task(task,description);
            temp.setName(task_assigned_to);
            taskManager.add(temp);

            Toast.makeText(WhoseTurnActivity.this,"New task added",Toast.LENGTH_LONG).show();
            list_view_build();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.whose_turn_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}