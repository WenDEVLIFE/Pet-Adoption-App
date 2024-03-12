package com.example.pet_adoption_app;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class FragementController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // This will call the home fragment
        replaceFragement(new HomeFragment());

        // Get the NavigationView reference using findViewById
        NavigationView navView = findViewById(R.id.navView);
        ColorStateList csl = ContextCompat.getColorStateList(this, R.color.Cool_gray);
        navView.setItemIconTintList(csl);
        navView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home){
                replaceFragement(new HomeFragment());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Home");
                builder.setMessage("Home Fragment");
                builder.show();
            }

            // This will go to Notification Fragements
            else if(item.getItemId() == R.id.notifications){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Notifications");
                builder.setMessage("Notifications Fragment");
                builder.show();
            }

            // This will go to Adopt Pet Fragements
            else if(item.getItemId() == R.id.adopt_pet){
                replaceFragement(new AdoptionFragments());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Adopt Pet");
                builder.setMessage("Adopt Pet Fragment");
                builder.show();
            }
            return true;
        });
    }

    // This will call the fragments
    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}