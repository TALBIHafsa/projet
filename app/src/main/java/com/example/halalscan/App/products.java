package com.example.halalscan.App;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.halalscan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class products extends AppCompatActivity {

    DatabaseReference database;
    TextView productNameTextView, ingredientsTextView, haramIngredientsTextView, countryNameTextView;
    ImageView productImageView, heart, statutImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        initializeViews();
        heart = findViewById(R.id.heart);

        String productId = getIntent().getStringExtra("productId");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference productRef = rootRef.child("products").child(productId);
        checkFavoriteStatus(productId,heart);

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Firebase Data", "Data snapshot: " + dataSnapshot.toString()); // Log the complete snapshot
                if (dataSnapshot.exists()) {
                    product product = dataSnapshot.getValue(product.class);
                    if (product != null && product.getCountry() != null) {
                        updateProductDetails(product);
                        fetchCountryName(rootRef, product.getCountry());
                    } else {
                        Log.e("Product Fetch", "Country code missing or product is null");
                        countryNameTextView.setText("Country code missing");
                    }
                } else {
                    Toast.makeText(products.this, "Product not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(products.this, "Failed to load product details", Toast.LENGTH_SHORT).show();
            }
        });

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heart.setSelected(!heart.isSelected());
                if(heart.isSelected()){
                    addtofavorite(productId);
                }
                else{
                    removeFromFavorite(productId);
                }
            }
        });


    }

    private void removeFromFavorite(String productId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("favorites");

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userFavoritesRef = databaseReference.child(userId);

            userFavoritesRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                    if (productSnapshot.exists()) {
                        userFavoritesRef.child(productId).removeValue((databaseError, databaseReference) -> {
                            if (databaseError == null) {
                                userFavoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                        if (!userSnapshot.hasChildren()) {
                                            databaseReference.child(userId).removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e("RemoveFavorites", "Database error: " + databaseError.getMessage());
                                    }
                                });
                            } else {
                                Log.e("RemoveFavorites", "Failed to remove product: " + databaseError.getMessage());
                            }
                        });
                    } else {
                        Log.e("RemoveFavorites", "Product ID does not exist under user's favorites");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("RemoveFavorites", "Database error: " + databaseError.getMessage());
                }
            });
        } else {
            // Handle the case where user is not logged in
            Log.e("RemoveFavorites", "No user is logged in.");
        }
    }

    private void addtofavorite(String productId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("favorites");

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userFavoritesRef = databaseReference.child(userId);

            userFavoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userFavoritesRef.child(productId).setValue(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Database error: " + databaseError.getMessage());
                }
            });
        } else {
            // Handle the case where the user is not logged in
            Log.e(TAG, "No user is logged in.");
        }
    }


    private void initializeViews() {
        productNameTextView = findViewById(R.id.name);
        ingredientsTextView = findViewById(R.id.ingredients);
        haramIngredientsTextView = findViewById(R.id.haramIngredients);
        productImageView = findViewById(R.id.imageView30);
        statutImageView = findViewById(R.id.statut);
        countryNameTextView = findViewById(R.id.country);
        
    }
    public void checkFavoriteStatus(String productId, View heart) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("favorites");

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userFavoritesRef = databaseReference.child(userId).child(productId);

            userFavoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Product ID exists in user's favorites
                        heart.setSelected(true);
                    } else {
                        // Product ID does not exist in user's favorites
                        heart.setSelected(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Database error: " + databaseError.getMessage());
                }
            });
        } else {
            // Handle the case where user is not logged in
            Log.e(TAG, "No user is logged in.");
            heart.setSelected(false);
        }
    }

    private void fetchCountryName(DatabaseReference rootRef, String countryCode) {
        if (countryCode != null && !countryCode.isEmpty()) {
            DatabaseReference countryRef = rootRef.child("countries").child(countryCode);
            countryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot countrySnapshot) {
                    if (countrySnapshot.exists()) {
                        String countryName = countrySnapshot.getValue(String.class);
                        countryNameTextView.setText(countryName);
                    } else {
                        countryNameTextView.setText("Country not found");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(products.this, "Failed to load country details", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            countryNameTextView.setText("No country code available");
        }
    }

    private void updateProductDetails(product product) {
        productNameTextView.setText(product.getName());
        Picasso.get().load(product.getImage()).into(productImageView);

        if (product.getIngredients() != null) {
            ingredientsTextView.setText(String.join(", ", product.getIngredients()));
        } else {
            ingredientsTextView.setText("Ingredients not available");
        }

        if (product.getHaramIngredients() != null) {
            haramIngredientsTextView.setText(String.join(", ", product.getHaramIngredients()));
        } else {
            haramIngredientsTextView.setText("All the ingredients are halal");
        }

        updateStatutImageView(product.getStatut());
    }

    private void updateStatutImageView(String statut) {
        if ("haram".equalsIgnoreCase(statut)) {
            statutImageView.setImageResource(R.drawable.haram);
        } else if ("halal".equalsIgnoreCase(statut)) {
            statutImageView.setImageResource(R.drawable.halal);
        } else {
            statutImageView.setImageResource(R.drawable.mashbouh);
        }
    }

    public void goToHome(View v) {
        finish();
    }
}
