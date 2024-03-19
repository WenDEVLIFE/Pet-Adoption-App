package com.example.pet_adoption_app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Signin_Activity extends AppCompatActivity {


    // Constructors
     Button Register , Back;

     CheckBox see_the_password;

     EditText username, email, name, password, confirm_password;

     // FireStore Instance

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

        // our edit text fields
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirmpassword);

        // This is register button
         Register = findViewById(R.id.button);
         Register.setOnClickListener(v-> {

             // Our Constructors for the fields
             String UserName = username.getText().toString();
             String Email = email.getText().toString();
             String Name = name.getText().toString();
             String Password = password.getText().toString();
             String Confirm_Password = confirm_password.getText().toString();

             // If the fields are empty
             if(UserName.isEmpty() || Email.isEmpty() || Name.isEmpty() || Password.isEmpty() || Confirm_Password.isEmpty()) {

                 // Alert Errors
                 AlertDialog alertDialog = new AlertDialog.Builder(Signin_Activity.this).create();
                 alertDialog.setTitle("Alert");
                 alertDialog.setMessage("All fields are required");
                 alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                         (dialog, which) -> dialog.dismiss());
                 alertDialog.show();
             }

             // else if its not empty
             else {

                 // This will check if password and confirm password is equal
                 if (Password.equals(Confirm_Password)) {

                     // Then this will check if email containt with @ and .
                     if(Email.contains("@") && Email.contains(".")){

                         // This will check if password has a special character and an uppercase letter
                         Boolean hasSpecialCharacter = hasSpecialCharacters(Password);
                         Boolean hasUpperCase = HasUpperCase(Password);

                         // Then if the password has special characters and has upper case
                            if (hasSpecialCharacter && hasUpperCase) {

                                // Then call the sign up method to add the user to the database
                                Intent intent = new Intent(Signin_Activity.this, Email_Verification_SignUp.class);
                                intent.putExtra("UserName", UserName);
                                intent.putExtra("Email", Email);
                                intent.putExtra("Name", Name);
                                intent.putExtra("Password", Password);
                                startActivity(intent);

                            } else {

                                // Alert Errors
                                AlertDialog alertDialog = new AlertDialog.Builder(Signin_Activity.this).create();
                                alertDialog.setTitle("Alert");
                                alertDialog.setMessage("Password must contain a special character and an uppercase letter");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        (dialog, which) -> dialog.dismiss());
                                alertDialog.show();
                            }
                     }
                     else {

                         // Alert Errors
                         AlertDialog alertDialog = new AlertDialog.Builder(Signin_Activity.this).create();
                         alertDialog.setTitle("Alert");
                         alertDialog.setMessage("Invalid Email");
                         alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                 (dialog, which) -> dialog.dismiss());
                         alertDialog.show();
                     }

                 }

             }




        });


         // This will go back to login
            Back = findViewById(R.id.buttonback);
            Back.setOnClickListener(v -> {
                Intent intent = new Intent(Signin_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });


            // find the checkbox
            see_the_password = findViewById(R.id.checkBox);
            see_the_password.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked){
                    //show password
                    password.setInputType(0);
                    confirm_password.setInputType(0);

                }else{
                    //hide password
                    password.setInputType(129);
                    confirm_password.setInputType(129);
                }
            });


    }
    // This will check if it has a special character
    public boolean hasSpecialCharacters(String password1) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(password1);
        return matcher.find();
    }


    // This will check if it has an uppercase letter
    public boolean HasUpperCase(String password1){
        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Matcher uppercaseMatcher = uppercasePattern.matcher(password1);
        boolean hasUppercase = uppercaseMatcher.find();

        return  hasUppercase;
    }



}