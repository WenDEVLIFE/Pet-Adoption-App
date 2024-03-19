package com.example.pet_adoption_app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Email_Verification_SignUp extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_verification_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = getIntent();
        String UserName = intent.getStringExtra("username");
        String Password = intent.getStringExtra("password");
        String Email = intent.getStringExtra("email");
        String Name = intent.getStringExtra("name");

        String code = intent.getStringExtra("code");

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            EditText editText = findViewById(R.id.idcode);
            String codeEntered = editText.getText().toString();

            if (codeEntered.equals(code)) {
                SignUpToDatabase(UserName, Password, Email, Name);
            } else {
                Toast.makeText(Email_Verification_SignUp.this, "Invalid code", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void SignUpToDatabase( String UserName, String Password, String Email, String Name) {

        String bcryptPassHashing = BCrypt.withDefaults().hashToString(12, Password.toCharArray());
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", Name);
        user.put("username", UserName);
        user.put("password", bcryptPassHashing);
        user.put("email", Email);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                    // Alert Success
                    Toast.makeText(Email_Verification_SignUp.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {

                    // Alert Errors
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(Email_Verification_SignUp.this, "Sign up failed", Toast.LENGTH_SHORT).show();

                });
    }


}