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

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String name = intent.getStringExtra("name");

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

        // set the username and name
        usernametext.setText("Username:"+ username);
        Name.setText("Name:"+ name);

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

                // This will go to notifications
                Notifications fragment = new Notifications();

                // we will send the username to the notifications
                Bundle bundle = new Bundle();
                bundle.putString("username", usernametext.getText().toString());
                fragment.setArguments(bundle);
                replaceFragement(fragment);
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
              // This will go to Lost Pet Fragements
                Fragment_Lost_Pets fragment = new Fragment_Lost_Pets();
                replaceFragement(fragment);
            }

            else if(item.getItemId() == R.id.ask_donation){
                // This will go to Ask Donation Fragements
                Ask_Donations fragment = new Ask_Donations();
                replaceFragement(fragment);


            }
            // This will go to logout
            else if(item.getItemId() == R.id.user_pref){
                // This will go to User Preferences Fragements
                UserPreferences fragment = new UserPreferences();
                replaceFragement(fragment);
            }
            else if(item.getItemId() == R.id.logout){

                // This will go to logut and go back to login
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder .setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                            // Logout
                            finish();
                            Intent intent1 = new Intent(this, MainActivity.class);
                            startActivity(intent1);
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Do nothing
                        })
                        .show();

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