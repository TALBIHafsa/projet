package com.example.halalscan.App;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.halalscan.R;
import com.squareup.picasso.Picasso;

public class products extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_product);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        // Retrieve product details from intent
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        String productName = intent.getStringExtra("productName");

        String productImage = intent.getStringExtra("productImage");

        // Set product name
        TextView productNameTextView = findViewById(R.id.name);
        productNameTextView.setText(productName);

        // Set product image
        ImageView image = findViewById(R.id.imageView30);
        Picasso.get().load(productImage).into(image);










        // Retrieve the TextView by its ID
        TextView ingredientsTextView = findViewById(R.id.ingredients);

        // Sample list of ingredients
        String[] ingredients = {"GLUCOSE SYRUP (FROM WHEAT OR CORN)", "CORN SUGAR", "GELATIN", "DEXTROSE (FROM WHEAT OR CORN)", "CONTAINS LESS THAN 2% OF: CITRIC ACID", "ARTIFIC LAND NATURAL FLAVORS", "SUNFLOWER OIL", "WHITE BEESWAX", "YELLOW BEESWAX", "COLORED WITH FRUIT AND VEGETABLE", "JUICE COLORED WITH SPIRULINA EXTRACT", "MAY CONTAIN: WHEAT, TRACES OF MILK"};

        // Create a StringBuilder to build the text containing all ingredients
        StringBuilder ingredientsText = new StringBuilder();

        // Append each ingredient to the StringBuilder
        for (int i = 0; i < ingredients.length; i++) {
            if (i == ingredients.length - 1) {
                ingredientsText.append(ingredients[i]).append(".");
            } else {
                ingredientsText.append(ingredients[i]).append(",").append("\n"); // Append each ingredient and a newline character
            }
        }

        // Set the text of the TextView to the ingredients list
        ingredientsTextView.setText(ingredientsText.toString());

        // Retrieve the TextView by its ID
        TextView haramingredientsTextView = findViewById(R.id.haramIngredients);

        // Sample list of ingredients
        String[] haramingredients = {"GELATIN"};

        // Create a StringBuilder to build the text containing all ingredients
        StringBuilder haramingredientsText = new StringBuilder();

        // Append each ingredient to the StringBuilder
        for (int i = 0; i < haramingredients.length; i++) {
            if (i == haramingredients.length - 1) {
                haramingredientsText.append(haramingredients[i]).append(".");
            } else {
                haramingredientsText.append(haramingredients[i]).append(",").append("\n"); // Append each ingredient and a newline character
            }
        }

        // Set the text of the TextView to the ingredients list
        haramingredientsTextView.setText(haramingredientsText.toString());



        ImageView heart = findViewById(R.id.heart);

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heart.setSelected(!heart.isSelected());
            }
        });





    }
    public void goToHome(View v){
        Intent i = new Intent(this, home.class);
        startActivity(i);
    }

}