package com.example.pet_adoption_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    EditText usernamefield,passwordfield;

    Button Sign_up;

    TextView Sign_in;

    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernamefield = findViewById(R.id.username);
        passwordfield = findViewById(R.id.password);

        Sign_in = findViewById(R.id.sigin);
        Sign_in.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        });

        Sign_up = findViewById(R.id.button);
        Sign_up.setOnClickListener(v ->{
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Sign up successful");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();

            Intent intent = new Intent(MainActivity.this, FragementController.class);
            startActivity(intent);

        });

        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                passwordfield.setInputType(0);
            } else {
                passwordfield.setInputType(129);
            }
        });



    }

}