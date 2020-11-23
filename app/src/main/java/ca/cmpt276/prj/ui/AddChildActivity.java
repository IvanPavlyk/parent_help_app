package ca.cmpt276.prj.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.manager.CoinSide;
import ca.cmpt276.prj.model.manager.Manager;

/**
 * AddChildActivity class that is booted from the ManageChildrenActivity
 * Lets user to add a child (name and portrait), when no image used - default is used
 * Button with camera icon responsible for taking the portrait from the camera
 * Button with gallery icon responsible for taking the portrait from the gallery
 */
public class AddChildActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 228;
    private static final int CAMERA_REQUEST = 1337;

    private Button buttonAddChild;
    private ImageButton buttonAddPortraitCamera;
    private ImageButton buttonAddPortraitGallery;
    private EditText editTextChildName;
    private ImageView imageViewChildPortrait;
    private Manager game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Add a child");
        }
        setContentView(R.layout.activity_add_child);
        initializeResources();
        game = Manager.getInstance();
        setListeners();
    }

    private void initializeResources() {
        buttonAddChild = findViewById(R.id.buttonAddChildScreen);
        buttonAddPortraitCamera = findViewById(R.id.imageButtonCamera);
        buttonAddPortraitGallery = findViewById(R.id.imageButtonGallery);
        editTextChildName = findViewById(R.id.ediTextChildName);
        imageViewChildPortrait = findViewById(R.id.imagePortrait);
        imageViewChildPortrait.setImageResource(R.drawable.default_portrait);
    }

    private void setListeners() {
        buttonAddPortraitGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromGallery();
            }
        });
        buttonAddPortraitCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromCamera();
            }
        });
        buttonAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextChildName.getText().toString().length() > 0) {
                    Bitmap bitmap = ((BitmapDrawable)imageViewChildPortrait.getDrawable()).getBitmap();
                    game.addChild(new Child(editTextChildName.getText().toString(), CoinSide.HEAD, bitmapToString(bitmap)));
                    Toast.makeText(AddChildActivity.this, "Added child successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddChildActivity.this, "Please input valid name!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //Converting bitmap into String compressed version to transfer the image along
    public static String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] b = outputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
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
                Uri image = Objects.requireNonNull(data).getData();
                InputStream imageStream = getContentResolver().openInputStream(Objects.requireNonNull(image));
                imageViewChildPortrait.setImageBitmap(BitmapFactory.decodeStream(imageStream));
            } catch (IOException exception){
                exception.printStackTrace();
            }
        }
        else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Bundle extras = Objects.requireNonNull(data).getExtras();
            Bitmap image  = (Bitmap) Objects.requireNonNull(extras).get("data");
            imageViewChildPortrait.setImageBitmap(image);
        }
        else{
            Toast.makeText(this, "Couldn't insert image", Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AddChildActivity.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }
}