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

import at.favre.lib.crypto.bcrypt.BCrypt;


public class MainActivity extends AppCompatActivity {

    EditText usernamefield,passwordfield;

    Button Sign_up;

    TextView Sign_in;

    CheckBox checkBox;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

            String username = usernamefield.getText().toString();
            String password = passwordfield.getText().toString();

            // Check if the username and password fields are empty
            if (username.isEmpty() || password.isEmpty()) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Please fill in all the fields");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            } else {
                // Check if the username and password fields are not empty

                LoginVerification(username, password);
            }


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

    public void LoginVerification(String username, String password){

        DocumentReference docRef = db.collection("users").document(username);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String hashedPassword = documentSnapshot.getString("Password");
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
                if (result.verified) {
                    // Retrieve the username and name
                    String retrievedUsername = documentSnapshot.getString("Username");
                    String name = documentSnapshot.getString("Name");

                    // The start the intent
                    Intent intent = new Intent(MainActivity.this, Signin_Activity.class);
                    intent.putExtra("username", retrievedUsername);
                    intent.putExtra("name", name);
                    startActivity(intent);

                    // close the current intent
                    finish();

                    // This will alert the login success
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Sign in successful");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> dialog.dismiss());
                    alertDialog.show();

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Invalid username or password");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> dialog.dismiss());
                    alertDialog.show();
                }
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Invalid username or password");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: " + e.toString());
        });

    }
}