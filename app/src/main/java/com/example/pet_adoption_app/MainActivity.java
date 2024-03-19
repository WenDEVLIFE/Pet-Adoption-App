package com.example.pet_adoption_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText usernamefield,passwordfield;

    Button Sign_up;

    TextView Sign_in;

    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // our edit text fields
        usernamefield = findViewById(R.id.username);
        passwordfield = findViewById(R.id.password);

        // Sign in button
        Sign_in = findViewById(R.id.sigin);
        Sign_in.setOnClickListener(v -> {

            // The start the intent
            Intent intent = new Intent(MainActivity.this, Signin_Activity.class);
            startActivity(intent);

            // close the current intent
            finish();
        });

        // Sign up button
        Sign_up = findViewById(R.id.button);
        Sign_up.setOnClickListener(v ->{
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Sign up successful");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();

            // The start the intent
            Intent intent = new Intent(MainActivity.this, FragementController.class);
            startActivity(intent);

            // close the current intent
            finish();



        });

        // This is our checkbox for the password
        checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                // Set to show the password
                passwordfield.setInputType(0);
            } else {

                // Set to hide the password
                passwordfield.setInputType(129);
            }
        });



    }

}