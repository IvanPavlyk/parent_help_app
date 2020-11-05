package ca.cmpt276.prj.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.Game;

public class MainActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //Hiding the top bar saying the name of the app
        setContentView(R.layout.activity_main);

        Button manageChildrenButton = (Button) findViewById(R.id.buttonManageChildren);
        manageChildrenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ManageChildrenActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        initFlipCoinButton();

        iniTimeoutButton();

        game = Game.getInstance();
        ArrayList<Child> listMain = new ArrayList<>();
        listMain = ManageChildrenActivity.loadListChildrenStatic(this, listMain);
        game.setChildrenList(listMain); //Setting the list of children to be the saved list from the Manage Children Activity
        //Toast for debugging purposes
        //TODO: remove toast before submitting the iteration
        Toast.makeText(this, "Number of children in game instance: " + game.getChildrenList().size(), Toast.LENGTH_SHORT).show();
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

    //For debugging purposes
    //TODO: remove the toast from onResume before submitting the iteration
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Number of children in game instance: " + game.getChildrenList().size(), Toast.LENGTH_SHORT).show();
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


}