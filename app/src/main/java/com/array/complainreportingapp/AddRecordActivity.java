package com.array.complainreportingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;


public class AddRecordActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;

    private EditText categoryEditText;
    private EditText severityEditText;
    private EditText descriptionEditText;
    private ImageView  selectedImageView;
    private Button mAddBtn;

    private PersonDBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        //init
        categoryEditText = (EditText)findViewById(R.id.c_category);
        severityEditText = (EditText)findViewById(R.id.c_severity);
        descriptionEditText = (EditText)findViewById(R.id.c_description);
        this.selectedImageView = (ImageView) findViewById(R.id.new_memory_selected_image);
        mAddBtn = (Button)findViewById(R.id.addNewUserButton);



        //listen to add button click
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the save person method
                savePerson();
            }
        });


    }

    public void openGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
    }

    public void openCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    private void savePerson(){
        String category = categoryEditText.getText().toString().trim();
        String severity = severityEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        Bitmap image = ((BitmapDrawable)selectedImageView.getDrawable()).getBitmap();
        dbHelper = new PersonDBHelper(this);

        if(category.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter a Category", Toast.LENGTH_SHORT).show();
        }

        if(severity.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an Severity", Toast.LENGTH_SHORT).show();
        }

        if(description.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an description", Toast.LENGTH_SHORT).show();
        }


        //create new person
        Person person = new Person(category, severity, description, image);
        dbHelper.saveNewPerson(person);

        //finally redirect back home
        // NOTE you can implement an sqlite callback then redirect on success delete
        goBackHome();

    }

    private void goBackHome(){
        startActivity(new Intent(AddRecordActivity.this, MainActivity.class));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                selectedImageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            selectedImageView.setImageBitmap(imageBitmap);
        }
    }
}