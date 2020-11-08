package ca.cmpt276.prj.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.Flip;
import ca.cmpt276.prj.model.Game;

/**
 * MainActivity responsible for the first screen that loads up when the application starts running
 * which is the main menu which navigates to all of the other activities
 */
public class MainActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Hiding the top bar saying the name of the app
        setContentView(R.layout.activity_main);

        Button manageChildrenButton = findViewById(R.id.buttonManageChildren);
        manageChildrenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ManageChildrenActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        initFlipCoinButton();

        iniTimeoutButton();

        // Load instance on initialization
        SharedPreferences sharedPreferences = this.getSharedPreferences("saves", MODE_PRIVATE);
        Gson gson = new Gson();
        String instanceSave = sharedPreferences.getString("savedInstance", null);
        Game loadedInstance = gson.fromJson(instanceSave, Game.class);
        Game.loadInstance(loadedInstance);
        game = Game.getInstance();
    }

    private void iniTimeoutButton() {
        Button btn=findViewById(R.id.buttonTimeout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, TimeoutActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initFlipCoinButton(){
        Button flipCoinBtn = findViewById(R.id.buttonFlipCoin);
        flipCoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CoinFlipActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    public void saveInstance() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("saves", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String instanceSave = gson.toJson(Game.getInstance());
        editor.putString("savedInstance", instanceSave);
        editor.apply();
    }
    public static void saveInstanceStatic(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("saves", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String instanceSave = gson.toJson(Game.getInstance());
        editor.putString("savedInstance", instanceSave);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        saveInstance();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveInstance();
    }
}