package ca.cmpt276.prj.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.coinManager.CoinManager;

/**
 * EditChildrenActivity is booted from the ManageChildrenActivity when user presses the portrait on the list view item
 * Lets user edit the portrait of the given child
 * Similar functionality to AddChildrenActivity
 */
public class EditChildPortraitActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 228;
    private static final int CAMERA_REQUEST = 1337;

    private Button buttonSaveChild;
    private ImageButton buttonAddPortraitCamera;
    private ImageButton buttonAddPortraitGallery;
    private ImageView imageViewChildPortrait;
    private CoinManager game;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child_portrait);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Edit a portrait");
        }
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("PositionChild");
        game = CoinManager.getInstance();
        initializeResources();
        imageViewChildPortrait.setImageBitmap(ManageChildrenActivity.stringToBitmap(game.getChild(position).getImageString()));
        setListeners();
    }

    private void setListeners() {
        buttonAddPortraitCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromCamera();
            }
        });
        buttonAddPortraitGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromGallery();
            }
        });
        buttonSaveChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ((BitmapDrawable)imageViewChildPortrait.getDrawable()).getBitmap();
                game.getChild(position).setImageString(AddChildActivity.bitmapToString(bitmap));
                Toast.makeText(EditChildPortraitActivity.this, "Edited the portrait successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void takePictureFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select portrait"), GALLERY_REQUEST);
    }

    public void takePictureFromCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, CAMERA_REQUEST);
        }
        else{
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            try{
                Uri image = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(image);
                imageViewChildPortrait.setImageBitmap(BitmapFactory.decodeStream(imageStream));
            } catch (IOException exception){
                exception.printStackTrace();
            }
        }
        else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap image  = (Bitmap)extras.get("data");
            imageViewChildPortrait.setImageBitmap(image);
        }
        else{
            Toast.makeText(this, "Couldn't insert image", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeResources() {
        buttonSaveChild = (Button) findViewById(R.id.buttonSaveChildEditScreen);
        buttonAddPortraitCamera = (ImageButton) findViewById(R.id.imageButtonPortraitEditCamera);
        buttonAddPortraitGallery = (ImageButton) findViewById(R.id.imageButtonPortraiteditGallery);
        imageViewChildPortrait = (ImageView) findViewById(R.id.imageViewPortraitEdit);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, EditChildPortraitActivity.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }
}