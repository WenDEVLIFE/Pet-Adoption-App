package com.example.pet_adoption_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pet_adoption_app.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class FragementController extends AppCompatActivity {

     ActivityMainBinding binding;
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

        replaceFragement(new HomeFragment());

        // Get the NavigationView reference using findViewById
        NavigationView navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home){
                replaceFragement(new HomeFragment());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Home");
                builder.setMessage("Home Fragment");
                builder.show();
            }
            else if(item.getItemId() == R.id.notifications){
                replaceFragement(new NotificationFragment());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Notifications");
                builder.setMessage("Notifications Fragment");
                builder.show();
            }
            else if(item.getItemId() == R.id.adopt_pet){
                replaceFragement(new NotificationFragment());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Adopt Pet");
                builder.setMessage("Adopt Pet Fragment");
                builder.show();
            }
            return true;
        });
    }

    private void replaceFragement(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}