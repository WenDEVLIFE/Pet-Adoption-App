package com.example.pet_adoption_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Signin_Activity extends AppCompatActivity {

     Button Register , Back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         Register = findViewById(R.id.button);
         Register.setOnClickListener(v-> {
             AlertDialog alertDialog = new AlertDialog.Builder(Signin_Activity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Sign up successful");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();


        });

            Back = findViewById(R.id.buttonback);
            Back.setOnClickListener(v -> {
                Intent intent = new Intent(Signin_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });



    }
}