package com.example.pet_adoption_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoadingApp extends AppCompatActivity {
    ProgressBar loading;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading_app);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Our loading bar
        loading = findViewById(R.id.progressBar);
        loading.setMax(100);
        loading.setProgress(0);

        // This will load the value of the progress bar
        thread = new Thread(() -> {
            try {
                for (int i = 0; i < 100; i+=5) {
                    // Set the progress bar value
                    loading.setProgress(i);
                    Thread.sleep(50);
                }
                // This will start and go to next page
                Intent intent = new Intent(LoadingApp.this, MainActivity.class);
                startActivity(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start(); // Don't forget to start the thread
    }
}