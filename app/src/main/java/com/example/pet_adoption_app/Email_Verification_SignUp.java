package com.example.pet_adoption_app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Email_Verification_SignUp extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView Timer;

    Button sendcode;

    String CodeReceiver;
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
        Timer = findViewById(R.id.textView10);

        // Start the countdown timer
        startCountdownTimer();


        // We will receive the intent
        Intent intent = getIntent();
        String UserName = intent.getStringExtra("Username");
        String Password = intent.getStringExtra("Password");
        String Email = intent.getStringExtra("Email");
        String Name = intent.getStringExtra("Name");
        String code = intent.getStringExtra("Code");
        CodeReceiver = code;

        EditText editText = findViewById(R.id.idcode);
        EditText editText1 = findViewById(R.id.Email);
        editText1.setText(Email);
        editText1.setEnabled(false);

        sendcode = findViewById(R.id.sendcode);
        sendcode.setOnClickListener(v -> {
           try{

               if(sendcode.isEnabled()){
                   // Send the code to the user's email
                   String codes = generateCode();
                   CodeReceiver = codes;
                   sendEmail(Email, codes);
               }
                else{
                     AlertDialog alertDialog = new AlertDialog.Builder(Email_Verification_SignUp.this).create();
                     alertDialog.setTitle("Alert");
                     alertDialog.setMessage("You can only send the code once the timer is done.");
                     alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> dialog.dismiss());
                     alertDialog.show();
               }
           } catch (Exception e){
                e.printStackTrace();

           }
        });
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            String codeEntered = editText.getText().toString();

            if (codeEntered.equals(CodeReceiver)) {
                SignUpToDatabase(UserName, Password, Email, Name);
            } else {
                Toast.makeText(Email_Verification_SignUp.this, "Invalid code", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // This method will sign the user to the database
    public void SignUpToDatabase( String UserName, String Password, String Email, String Name) {

        String bcryptPassHashing = BCrypt.withDefaults().hashToString(12, Password.toCharArray());
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Name", Name);
        user.put("Username", UserName);
        user.put("Password", bcryptPassHashing);
        user.put("Email", Email);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {

                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                    // Alert Success
                    Toast.makeText(Email_Verification_SignUp.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Email_Verification_SignUp.this, Signin_Activity.class);
                    startActivity(intent);


                })
                .addOnFailureListener(e -> {


                    // Alert Errors
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(Email_Verification_SignUp.this, "Sign up failed", Toast.LENGTH_SHORT).show();

                });
    }

    private void startCountdownTimer() {
        new CountDownTimer(60000, 1000) { // 60000 milliseconds = 60 seconds

            public void onTick(long millisUntilFinished) {
                Timer.setText("Time remaining: " + millisUntilFinished / 1000 + " seconds");

            sendcode.setEnabled(false);

            }

            public void onFinish() {
                Timer.setText("");
                sendcode.setEnabled(true);

                if(Timer.getText().toString().isEmpty()){
                    AlertDialog alertDialog = new AlertDialog.Builder(Email_Verification_SignUp.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("You can now resend the code");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> dialog.dismiss());
                    alertDialog.show();
                }
                else{
                    Timer.setText("Resend code");
                }

            }
        }.start();
    }


    private void sendEmail(String email, String code) throws MessagingException {

        JavaMailAPI mailAPI = new JavaMailAPI(Email_Verification_SignUp.this, email, "The Verification Code", "The verification code is:" + code, code);
        Toast.makeText(this, " Email Sended", Toast.LENGTH_SHORT).show();
        mailAPI.execute();


        // Start the countdown timer
        startCountdownTimer();


    }

    private String generateCode() {
        // Implement code generation logic here
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // This will generate a random 6-digit number
        return String.valueOf(code);
    }




}