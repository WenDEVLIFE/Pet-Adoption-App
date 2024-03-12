package com.example.pet_adoption_app;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.TextView;

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

    TextView usernametext, Name;
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
        usernametext = navView.getHeaderView(0).findViewById(R.id.username);
        Name = navView.getHeaderView(0).findViewById(R.id.name);
        ColorStateList csl = ContextCompat.getColorStateList(this, R.color.white);
        navView.setItemIconTintList(csl);
        navView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home){
                // This will go to Home Fragements
                HomeFragment fragment = new HomeFragment();
                replaceFragement(fragment);

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
                AdoptionFragments fragment = new AdoptionFragments();

                // Step 2: Create a new Bundle
                Bundle bundle = new Bundle();

                // Step 3: Put the data into the Bundle
                bundle.putString("username", usernametext.getText().toString());

                // Step 4: Set the arguments for the fragment with the Bundle
                fragment.setArguments(bundle);

                // Step 5: Use the FragmentManager to start the fragment
                replaceFragement(fragment);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Adopt Pet");
                builder.setMessage("Adopt Pet Fragment");
                builder.show();
            }

            else if(item.getItemId() == R.id.lostpet){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Lost Pet");
                builder.setMessage("Lost Pet Fragment");
                builder.show();

            }

            else if(item.getItemId() == R.id.ask_donation){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Ask Donation");
                builder.setMessage("Ask Donation Fragment");
                builder.show();


            }
            // This will go to logout
            else if(item.getItemId() == R.id.logout){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder .setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                            // Logout
                            finish();
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Do nothing
                        })
                        .show();

            }
            return true;
        });

        // set the username and name
        usernametext.setText("Null");
        Name.setText("Null");

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