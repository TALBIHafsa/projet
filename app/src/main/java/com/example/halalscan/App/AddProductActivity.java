package com.example.halalscan.App;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.halalscan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;


public class AddProductActivity extends AppCompatActivity {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri photoURI;
    private String currentPhotoPath;
    private ConstraintLayout productcontainer;
    private ConstraintLayout ingredientscontainer;

    private StorageReference storageRef;
    DatabaseReference database;
    private String scannedBarcode; // This will hold the barcode passed from home activity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);  // Set the content view before accessing any UI elements

        productcontainer = findViewById(R.id.productcontainer);  // Now you can safely access your ImageView
        ingredientscontainer = findViewById(R.id.ingredientscontainer);  // Now you can safely access your ImageView
        storageRef = FirebaseStorage.getInstance().getReference();

        scannedBarcode = getIntent().getStringExtra("scannedBarcode");
        if (scannedBarcode == null) {
            Toast.makeText(this, "No barcode received.", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no barcode is received
            return;
        }

        database = FirebaseDatabase.getInstance().getReference("newProducts").child(scannedBarcode);  // Initialize the database reference after scannedBarcode is retrieved

        findViewById(R.id.addPicture).setOnClickListener(v -> takePicture("productImage"));
        findViewById(R.id.addIngredients).setOnClickListener(v -> takePicture("ingredientsImage"));
        showTakenImages();
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

        final StorageReference imageRef = storageRef.child("newProducts/" + scannedBarcode + "/" + type + ".jpg");
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


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return; // Exit if no user is logged in
        }

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("newProducts").child(scannedBarcode);
        databaseRef.child("id").setValue(scannedBarcode);

        databaseRef.child("userEmail").setValue(currentUser.getEmail()); // If you want to store the email


        if (type.equals("productImage")) {
            databaseRef.child("productImage").setValue(imageUrl);
        } else if (type.equals("ingredientsImage")) {
            databaseRef.child("ingredientsImage").setValue(imageUrl);
        }
    }



        private void showTakenImages() {
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
                    );

                    // Check if the dataSnapshot has the specific child "productImage"
                    if (dataSnapshot.hasChild("productImage")) {
                        productcontainer.removeAllViews();
                        String productImageUrl = dataSnapshot.child("productImage").getValue(String.class);
                        ImageView productImageView = new ImageView(AddProductActivity.this);
                        productImageView.setLayoutParams(layoutParams);

                        Picasso.get().load(productImageUrl).into(productImageView);
                        productcontainer.addView(productImageView);
                    }

                    // Check if the dataSnapshot has the specific child "ingredientsImage"
                    if (dataSnapshot.hasChild("ingredientsImage")) {
                        ingredientscontainer.removeAllViews();
                        String ingredientsImageUrl = dataSnapshot.child("ingredientsImage").getValue(String.class);
                        ImageView ingredientsImageView = new ImageView(AddProductActivity.this);
                        ingredientsImageView.setLayoutParams(layoutParams);

                        Picasso.get().load(ingredientsImageUrl).into(ingredientsImageView);
                        ingredientscontainer.addView(ingredientsImageView);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(AddProductActivity.this, "Failed to load images: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    public void goToHome(View v){
        Intent i = new Intent(this, home.class);
        startActivity(i);
    }
}