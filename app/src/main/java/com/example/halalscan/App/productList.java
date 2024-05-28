package com.example.halalscan.App;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.halalscan.App.MyAdapter;
import com.example.halalscan.App.product;
import com.example.halalscan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class productList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<product> list;
    ArrayList<product> fullList; // Full dataset list
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.productList);
        database = FirebaseDatabase.getInstance().getReference("products");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        fullList = new ArrayList<>(); // Initialization of full dataset list
        myAdapter = new MyAdapter(this, list, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(product item) {
                // Handle item click here, navigate to product details activity
                Intent intent = new Intent(productList.this, products.class);
                // Pass product information to the details activity
                intent.putExtra("productId", item.getId());
                intent.putExtra("productName", item.getName());
                // Add other product details if needed
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear list before adding new data
                fullList.clear(); // Clear full list before adding new data

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    product product = dataSnapshot.getValue(com.example.halalscan.App.product.class);
                    list.add(product);
                    fullList.add(product);  // Add product to full dataset list
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        searchView = findViewById(R.id.editTextSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    myAdapter.updateList(fullList); // Restore original list if search query is empty
                } else {
                    ArrayList<product> filteredList = new ArrayList<>();
                    for (product product : fullList) {
                        if (product.getName().toLowerCase().contains(newText.toLowerCase())) {
                            filteredList.add(product);
                        }
                    }
                    myAdapter.updateList(filteredList); // Update list with filtered results
                }
                return true;
            }
        });
    }
}
