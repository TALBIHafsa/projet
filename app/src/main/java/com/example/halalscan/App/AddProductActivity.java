package com.example.halalscan.App;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.halalscan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class AddProductActivity extends AppCompatActivity {




        private static final int REQUEST_IMAGE_CAPTURE = 1;
        private Uri photoURI;
        private String currentPhotoPath;
        private StorageReference storageRef;
    private String currentProductKey;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_product);
            storageRef = FirebaseStorage.getInstance().getReference();

            currentProductKey = FirebaseDatabase.getInstance().getReference("newProducts").push().getKey();

            findViewById(R.id.addPicture).setOnClickListener(v -> takePicture("productImage"));
            findViewById(R.id.addIngredients).setOnClickListener(v -> takePicture("ingredientsImage"));
        }

        private void takePicture(String type) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                currentPhotoPath = type; // Use this to differentiate between product and ingredients images
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                uploadImageToFirebase(imageBitmap, currentPhotoPath);
            }
        }


    private void uploadImageToFirebase(Bitmap bitmap, String type) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final StorageReference imageRef = storageRef.child("newProducts/" + currentProductKey + "/" + type + ".jpg");
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            Toast.makeText(this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
        }).addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                updateDatabaseWithImageUrl(imageUrl, type);
                Toast.makeText(this, "Upload successful", Toast.LENGTH_LONG).show();
            });
        });
    }

    private void updateDatabaseWithImageUrl(String imageUrl, String type) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("newProducts").child(currentProductKey);
        if (type.equals("productImage")) {
            databaseRef.child("productImage").setValue(imageUrl);
        } else if (type.equals("ingredientsImage")) {
            databaseRef.child("ingredientsImage").setValue(imageUrl);
        }
    }}
