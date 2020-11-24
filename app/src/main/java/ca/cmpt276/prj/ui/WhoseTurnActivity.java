package ca.cmpt276.prj.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.MyTaskDialog;
import ca.cmpt276.prj.model.manager.Manager;
import ca.cmpt276.prj.model.manager.Task;

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
            bar.setTitle("Whose Turn");
        }

        list_view_build();

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WhoseTurnActivity.this, AddTaskActivity.class);
                startActivityForResult(intent,1);
              //  MainActivity.saveInstanceStatic(WhoseTurnActivity.this);
            }
        });
        list_view_build();

        FloatingActionButton edit = findViewById(R.id.edit);
        if(manager.size() != 0) {

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WhoseTurnActivity.this, SelectEditTaskActivity.class);
                    startActivityForResult(intent, 1);


                }

            });
        }else if(manager.size() == 0){
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(WhoseTurnActivity.this, "Please  a new task.\n At the bottom right corner \"+\" sign.",Toast.LENGTH_LONG).show();
                }
            });

        }

        FloatingActionButton delete = findViewById(R.id.delete);
        if(manager.size() != 0) {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WhoseTurnActivity.this, DeleteTaskActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
        }else if(manager.size() == 0){
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(WhoseTurnActivity.this, "Please Add a new task.\n At the bottom right corner \"+\" sign.",Toast.LENGTH_LONG).show();
                }
            });

        }


    }

    public static Intent makeIntent(Context context){
        return new Intent(context, WhoseTurnActivity.class);
    }

    private void list_view_build() {
        ArrayList<String> arr=new ArrayList<>();
        int count=0;
        while(count< manager.size()){
            Task buffer = manager.retrieving(count);
            arr.add("Task: "+buffer.getTaskName()+"\nTask Description: "+buffer.getDescription());
            count+=1;
        }

        ListView listView= findViewById(R.id.list_view);
        // MARK
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arr);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                if(childNameList.size()==0){
                    Toast.makeText(WhoseTurnActivity.this, "Please add a child before add some tasks", Toast.LENGTH_SHORT).show();
                }
                else{
                    Task temp = Manager.getInstance().retrieving(position);
                    MyTaskDialog dialog = new MyTaskDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("child_name",temp.getChildName());
                    bundle.putString("task_name", temp.getTaskName());
                    bundle.putString("description", temp.getDescription());
                    dialog.setArguments(bundle);
                    dialog.show((WhoseTurnActivity.this).getSupportFragmentManager(), "Task Tag");
                }


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


            int child_count = childNameList.size()-1;
            if (child_count < 0) {
                Toast.makeText(this, "No Child Added Yet", Toast.LENGTH_SHORT).show();
                return;
            }
            int random_index = (int)(Math.random() * ((child_count) + 1));
            if (!Objects.requireNonNull(lastTask).equals("")) {
                // MARK
                while (childNameList.get(random_index).getName().equals(lastTask)) {
                    random_index = (int) (Math.random() * ((child_count) + 1));
                }
            }
            String task_assigned_to = childNameList.get(random_index).getName();

            // MARK
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("name", task_assigned_to);
            editor.apply();

            Task temp = new Task(task,description);
            temp.setName(task_assigned_to);
            manager.add(temp);

            Toast.makeText(WhoseTurnActivity.this,"New task added",Toast.LENGTH_LONG).show();
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