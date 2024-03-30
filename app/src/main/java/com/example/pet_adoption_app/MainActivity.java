package com.example.pet_adoption_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    ProgressDialog progressDialog;

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

            // This will start the intent
            Intent intent = new Intent(MainActivity.this, Signin_Activity.class);
            startActivity(intent);

        });


        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Signing up...");


        // Sign up button
        Sign_up = findViewById(R.id.button);
        Sign_up.setOnClickListener(v ->{


            // This will start the intent
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
                progressDialog.dismiss();
            } else {
                // Check if the username and password fields are not empty
                progressDialog.show();
                LoginVerification(username, password);
            }



        });

        // This is our checkbox for the password
        checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                // Show the password
                passwordfield.setTransformationMethod(null);

            }else{
                //hide password
                passwordfield.setTransformationMethod(new PasswordTransformationMethod());

            }
        });



    }

    public void LoginVerification(String username, String password){

        // This is how to login call the document reference
        DocumentReference docRef = db.collection("users").document(username);

        // This will check if the username exists
        db.collection("users").whereEqualTo("Username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {

                            // This will get the hashed password and compare it
                            // with the password entered by the user
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String hashedPassword = documentSnapshot.getString("Password");
                            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
                            if (result.verified) {

                                // Retrieve the username and name
                                String retrievedUsername = documentSnapshot.getString("Username");
                                String name = documentSnapshot.getString("Name");

                                // The start the intent
                                Intent intent = new Intent(MainActivity.this, FragementController.class);
                                intent.putExtra("username", retrievedUsername);
                                intent.putExtra("name", name);
                                startActivity(intent);

                                // close the current intent
                                finish();
                                // Dismiss the progress dialog
                                progressDialog.dismiss();
                                // This will alert the login success
                                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                                // Dismiss the progress dialog
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(this, "This User don't exist", Toast.LENGTH_SHORT).show();
                            // Dismiss the progress dialog
                            progressDialog.dismiss();
                        }
                    } else {
                        Log.d(TAG, "onFailure: " + task.getException().toString());
                        // Dismiss the progress dialog
                        progressDialog.dismiss();
                    }
                });

    }
}