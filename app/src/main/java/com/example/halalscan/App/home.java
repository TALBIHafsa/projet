package com.example.halalscan.App;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.halalscan.R;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class home extends AppCompatActivity {
    Button btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        btn_scan = findViewById(R.id.button4);
        btn_scan.setOnClickListener(v -> {
            scanCode();
        });

    }
    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
        ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{
            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                builder.setTitle("Result");
                builder.setMessage(result.getContents());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }


                }).show();
            }});

    public void goToFavourits(View v){
        Intent i = new Intent(this, favorits.class);
        startActivity(i);
    }
    public void goToNotifications(View v){
        Intent i = new Intent(this, notification.class);
        startActivity(i);
    }
    public void goToProduct(View v){
        Intent i = new Intent(this, products.class);
        startActivity(i);
    }
    public void goToSearch(View v){
        Intent i = new Intent(this, productList.class);
        startActivity(i);
    }

}