package com.example.halalscan.App;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.widget.ImageView;

import com.example.halalscan.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class favorits extends AppCompatActivity implements OnHeartClickListener {
    private RecyclerView recyclerView;
    private SearchView searchView;
    List<DataClass> dataList;
    MyAdapter adapter;
    DataClass androidData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorits);


        FirebaseApp.initializeApp(this);


        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(favorits.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList=new ArrayList<>();

        showFavorites(dataList);



        adapter=new MyAdapter(favorits.this,dataList);


        adapter.setOnHeartClickListener(favorits.this);
        recyclerView.setAdapter(adapter);



        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });





    }

    private void showFavorites(List<DataClass> dataList) {
        ImageView imageView =findViewById(R.id.imageView);
        TextView textView=findViewById(R.id.textView8);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("favorites");
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products");



        if (currentUser != null) {
            databaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        imageView.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                        for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                            String productid = productSnapshot.getKey();
                            productRef.child(productid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                                    if (productSnapshot.exists()) {
                                        String name = productSnapshot.child("name").getValue(String.class);
                                        String statut = productSnapshot.child("statut").getValue(String.class);
                                        String imageUrl = productSnapshot.child("image").getValue(String.class);
                                        String productId=productSnapshot.child("id").getValue(String.class);

                                        if (name != null && statut != null) {
                                            if (imageUrl != null && !imageUrl.isEmpty() && 1==2) {
                                                FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl).getDownloadUrl().addOnSuccessListener(uri -> {
                                                    String downloadUrl = uri.toString();
                                                    DataClass product = new DataClass(productId,name, statut, downloadUrl, true);
                                                    dataList.add(product);
                                                    adapter.notifyItemInserted(0);
                                                    adapter.notifyItemRangeChanged(0, dataList.size());
                                                }).addOnFailureListener(exception -> {
                                                    // Handle any errors
                                                });
                                            } else {
                                                DataClass product = new DataClass(productId,name, statut, R.drawable.product_icon, true);
                                                dataList.add(product);
                                                adapter.notifyItemInserted(0);
                                                adapter.notifyItemRangeChanged(0, dataList.size());
                                            }
                                        }
                                    } else {

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle onCancelled
                                }
                            });
                        }
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError){                }
            });
        } else { }
    }




    public void onHeartClick(int position) {
        if (position >= 0 && position < dataList.size()) {
            DataClass item = dataList.get(position);
            item.setFavorite(!item.getIsFavorite());
            if (!item.getIsFavorite()) {
                dataList.remove(position);
                Log.e("favorits", "position: " + position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, dataList.size() - position);
                RemoveFromFavorites(item.getDataId());
            }
            if(dataList.isEmpty()){
                ImageView imageView =findViewById(R.id.imageView);
                TextView textView=findViewById(R.id.textView8);
                imageView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
        }


    }
    private void RemoveFromFavorites(String productId) {
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
                        Log.e("RemoveFavorites", "Product ID does not exist under user's Favorites");
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

    private void searchList(String text){
        List<DataClass> dataSearchList=new ArrayList<>();
        for(DataClass data:dataList){
            if(data.getDataTitle().toLowerCase().contains(text.toLowerCase())){
                dataSearchList.add(data);
            }
        }
        if(dataSearchList.isEmpty()){
            Toast.makeText(this, "NOT FOUND", Toast.LENGTH_SHORT).show();
        }
        else adapter.setSearchList(dataSearchList);
    }
}
