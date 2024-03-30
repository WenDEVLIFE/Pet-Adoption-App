package com.example.pet_adoption_app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Signin_Activity extends AppCompatActivity {


    // Constructors
     Button Register , Back;

     CheckBox see_the_password;

     EditText username, email, name, password, confirm_password;

     // FireStore Instance
     FirebaseFirestore db = FirebaseFirestore.getInstance();

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

                     // Regex pattern for basic email validation
                     String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

                     // Then this will check if email containt with @ and .
                     if (Email.matches(emailPattern)) {
                         // Email is valid
                         // This will check if password has a special character and an uppercase letter
                         Boolean hasSpecialCharacter = hasSpecialCharacters(Password);
                         Boolean hasUpperCase = HasUpperCase(Password);

                         // Then if the password has special characters and has upper case
                            if (hasSpecialCharacter && hasUpperCase) {
                                String code = generateCode();
                            try{
                                // Then call the sign up method to add the user to the database

                            }catch (Exception e) {
                                e.printStackTrace();
                            }


                            CheckUser(Email, UserName, Name, Password, code);

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

            });


            // find the checkbox
            see_the_password = findViewById(R.id.checkBox);
            see_the_password.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked){
                    // Show the password
                    password.setTransformationMethod(null);
                    confirm_password.setTransformationMethod(null);
                }else{
                    //hide password
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    confirm_password.setTransformationMethod(new PasswordTransformationMethod());
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



    private String generateCode() {
        // Implement code generation logic here
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // This will generate a random 6-digit number
        return String.valueOf(code);
    }


    private void SendMail(String Email, String code){
        JavaMailAPI mailAPI = new JavaMailAPI(Signin_Activity.this, Email, "The Verification Code", "The verification code is:" + code, code);
        Toast.makeText(this, " Email Sended", Toast.LENGTH_SHORT).show();
        mailAPI.execute();


    }

    private void DoneLoading( String UserName, String Email, String Name, String Password, String code){
        Intent intent = new Intent(Signin_Activity.this, Email_Verification_SignUp.class);
        intent.putExtra("Username", UserName);
        intent.putExtra("Email", Email);
        intent.putExtra("Name", Name);
        intent.putExtra("Password", Password);
        intent.putExtra("Code"  , code);
        startActivity(intent);
    }

    public void CheckUser(String Email, String UserName, String Name, String Password, String code){
      db.collection("users").whereEqualTo("Username" ,  UserName).get().addOnSuccessListener(queryDocumentSnapshots -> {
          if(queryDocumentSnapshots.isEmpty()){
              SendMail(Email, code);
              DoneLoading(UserName, Email, Name, Password, code);

          }
          else{
              // Alert Errors
              AlertDialog alertDialog = new AlertDialog.Builder(Signin_Activity.this).create();
              alertDialog.setTitle("Alert");
              alertDialog.setMessage("Username already exists");
              alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                      (dialog, which) -> dialog.dismiss());
              alertDialog.show();
          }
      });

    }

}