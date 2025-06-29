package com.s23010372.agrizone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    // UI Components
    private ImageView ivFarmingCalendar;
    private ImageView ivDiseaseMap;
    private ImageView ivSmartDataSystem;
    private ImageView navMarket;
    private ImageView navTraining;
    private ImageView navProfile;
    private ImageView ivSensor;
    private ImageView ivExpertSupport;
    private ImageView ivLaborManagement;
    private ImageView ivCommunity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        // Feature cards
        ivFarmingCalendar = findViewById(R.id.ivFarmingCalendar);
        ivDiseaseMap = findViewById(R.id.ivDiseaseMap);
        ivSmartDataSystem = findViewById(R.id.ivSmartDataSystem);
        ivExpertSupport = findViewById(R.id.ivExpertSupport);
        ivLaborManagement = findViewById(R.id.ivLaborManagement);
        ivCommunity = findViewById(R.id.ivCommunity);
        ivSensor = findViewById(R.id.ivSensor);

        // Navigation
        navMarket = findViewById(R.id.nav_market);
        navTraining = findViewById(R.id.ivTraining);
        navProfile = findViewById(R.id.nav_profile);
    }

    private void setupClickListeners() {
        // Feature cards click listeners - Link to actual activities
        if (ivFarmingCalendar != null) {
            ivFarmingCalendar.setOnClickListener(v -> {
                showToast("Opening Farming Calendar");
                startActivity(new Intent(HomeActivity.this, FarmingCalendarActivity.class));
            });
        }

        if (ivDiseaseMap != null) {
            ivDiseaseMap.setOnClickListener(v -> {
                showToast("Opening Disease Map");
                startActivity(new Intent(HomeActivity.this, DiseaseMapActivity.class));
            });
        }

        if (ivExpertSupport != null) {
            ivExpertSupport.setOnClickListener(v -> {
                showToast("Opening Expert Support");
                startActivity(new Intent(HomeActivity.this, ExpertSupportActivity.class));
            });
        }

        // Link Floating Sensor Button to SensorActivity
        if (ivSensor != null) {
            ivSensor.setOnClickListener(v -> {
                showToast("Opening Sensor Dashboard");
                startActivity(new Intent(HomeActivity.this, SensorActivity.class));
            });
        }

        // Features not yet implemented - show coming soon message
        setFeatureClickListener(ivSmartDataSystem, "Smart Data System");
        setFeatureClickListener(ivLaborManagement, "Labor Management");
        setFeatureClickListener(ivCommunity, "Community");

        // Bottom navigation click listeners
        if (navTraining != null) {
            navTraining.setOnClickListener(v -> {
                showToast("Opening Training Modules");
                startActivity(new Intent(HomeActivity.this, TrainingModulesActivity.class));
            });
        }

        if (navMarket != null) {
            navMarket.setOnClickListener(v -> {
                showToast("Opening Marketplace");
                startActivity(new Intent(HomeActivity.this, MarketplaceActivity.class));
            });
        }

        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                showToast("Profile - Coming Soon!");
            });
        }
    }

    private void setFeatureClickListener(ImageView imageView, String featureName) {
        if (imageView != null) {
            imageView.setOnClickListener(v -> {
                showToast("Opening " + featureName + " - Coming Soon!");
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
