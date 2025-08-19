package com.s23010372.agrizone;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SmartDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_data);

        initializeViews();
        showWelcomeMessage();
    }

    private void initializeViews() {
        // Initialize back button with professional animation
        ImageView backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                // Add smooth animation
                v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100)
                    .withEndAction(() -> {
                        v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100);
                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    });
            });
        }

        // Initialize search button
        ImageView searchButton = findViewById(R.id.search_button);
        if (searchButton != null) {
            searchButton.setOnClickListener(v -> {
                animateButton(v);
                showProfessionalToast("Search functionality coming soon!");
            });
        }
    }

    private void animateButton(android.view.View button) {
        button.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100)
            .withEndAction(() -> button.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100));
    }

    private void showProfessionalToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showWelcomeMessage() {
        showProfessionalToast("Welcome to Smart Data System! 🌱 6 crops available");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
