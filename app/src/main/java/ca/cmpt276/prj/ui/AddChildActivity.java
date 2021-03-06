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
import ca.cmpt276.prj.model.CoinSide;
import ca.cmpt276.prj.model.Manager;

import static ca.cmpt276.prj.R.string.added_success_text_addactivity;
import static ca.cmpt276.prj.R.string.camera_not_available_text_addactivity;
import static ca.cmpt276.prj.R.string.couldnot_insert_text_addactivity;
import static ca.cmpt276.prj.R.string.input_valid_name_text_addactivity;

/**
 * AddChildActivity class that is booted from the ManageChildrenActivity
 * Lets user to addTask a child (name and portrait), when no image used - default is used
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

    public static Intent makeIntent(Context context){
        return new Intent(context, AddChildActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle(R.string.add_child_text_addactivity);
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
                    game.appendChild(new Child(editTextChildName.getText().toString(), CoinSide.HEAD, bitmapToString(bitmap)));
                    Toast.makeText(AddChildActivity.this, added_success_text_addactivity, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddChildActivity.this, input_valid_name_text_addactivity, Toast.LENGTH_SHORT).show();
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
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_portrait_text_addactivity)), GALLERY_REQUEST);
    }

    public void takePictureFromCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, CAMERA_REQUEST);
        }
        else{
            Toast.makeText(this, camera_not_available_text_addactivity, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, couldnot_insert_text_addactivity, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.saveInstanceStatic(this);
    }
}