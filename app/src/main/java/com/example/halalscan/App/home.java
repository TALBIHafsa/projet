package com.example.halalscan.App;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.halalscan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.squareup.picasso.Picasso;

public class home extends AppCompatActivity {
    ImageView imageview;
    TextView name;
    Button btn_scan;
    private LinearLayout productsContainer;

    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productsContainer = findViewById(R.id.productsContainer); // Make sure this ID matches your LinearLayout inside the HorizontalScrollView
        database = FirebaseDatabase.getInstance().getReference("products");

        loadProductImages();

        btn_scan = findViewById(R.id.button4);
        database = FirebaseDatabase.getInstance().getReference("products");

        btn_scan.setOnClickListener(v -> scanCode());
        imageview=findViewById(R.id.imageView12);
        name=findViewById(R.id.textView12);


        showUserData();
    }
    private void showUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        if (currentUser != null) {
            databaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        String userName = dataSnapshot.child("name").getValue(String.class);
                        name.setText(userName);

                        if (dataSnapshot.hasChild("image")) {
                            String image = dataSnapshot.child("image").getValue().toString();
                            Picasso.get().load(image).transform(new CircleTransform()).into(imageview);
                        }
                    }
                    else {
                        // Handle the case where user data is not found
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled
                }
            });
        } else {
            // Handle the case where user is not logged in
        }


    }
    private void loadProductImages() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    product product = snapshot.getValue(product.class);
                    if (product != null) {
                        ImageView imageView = new ImageView(home.this);
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT));
                        Picasso.get().load(product.getImage()).into(imageView);
                        imageView.setOnClickListener(v -> goToProductDetails(product.getId()));
                        productsContainer.addView(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(home.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void goToProductDetails(String productId) {
        Intent intent = new Intent(home.this, products.class);
        intent.putExtra("productId", productId);
        startActivity(intent);
    }
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            String scannedCode = result.getContents();
            Log.d("home", "Scanned Barcode: " + scannedCode); // Confirm barcode is captured
            database.orderByChild("id").equalTo(scannedCode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            product product = dataSnapshot.getValue(product.class);
                            Intent intent = new Intent(home.this, products.class);
                            intent.putExtra("productId", product.getId());
                            intent.putExtra("productName", product.getName());
                            intent.putExtra("productImage", product.getImage());
                            startActivity(intent);
                        }
                    } else {
                        showProductNotFoundDialog(scannedCode);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(home.this, "Failed to read from database", Toast.LENGTH_SHORT).show();
                }
            });
        }
    });

    private void showProductNotFoundDialog(String scannedBarcode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
        builder.setTitle("Product Not Found");
        builder.setMessage("The product with barcode " + scannedBarcode + " is not in our database. Would you like to add it?");
        builder.setPositiveButton("Add Product", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Redirect to Add Product Activity
                Intent intent = new Intent(home.this, AddProductActivity.class);
                intent.putExtra("scannedBarcode", scannedBarcode); // Ensure this is the correct variable holding the barcode

                startActivity(intent);
            }
        });
        builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void goToFavourits(View v){
        Intent i = new Intent(this, favorits.class);
        startActivity(i);
    }
    public void goToNotifications(View v){
        Intent i = new Intent(this, NotificationActivity.class);
        startActivity(i);
    }

    public void goToSearch(View v){
        Intent i = new Intent(this, productList.class);
        startActivity(i);
    }

    public void goToProfil(View v){
        Intent i = new Intent(this, ProfilActivity.class);
        startActivity(i);
    }

}