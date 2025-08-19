package com.s23010372.agrizone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    // Add this field for the parent layout
    private View navProfileLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get username from intent (if available)
        String username = getIntent().getStringExtra("username");

        initializeViews();
        setupClickListeners(username);
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

        // Navigation (use correct IDs from layout)
        navMarket = findViewById(R.id.nav_market);
        navTraining = findViewById(R.id.ivTraining);
        navProfile = findViewById(R.id.nav_profile);
        // Fix: Only get parent if navProfile is not null
        navProfileLayout = navProfile != null ? (View) navProfile.getParent() : null;

        // Defensive null checks for all views
        if (ivFarmingCalendar == null) throw new RuntimeException("ivFarmingCalendar not found in layout");
        if (ivDiseaseMap == null) throw new RuntimeException("ivDiseaseMap not found in layout");
        if (ivSmartDataSystem == null) throw new RuntimeException("ivSmartDataSystem not found in layout");
        if (ivExpertSupport == null) throw new RuntimeException("ivExpertSupport not found in layout");
        if (ivLaborManagement == null) throw new RuntimeException("ivLaborManagement not found in layout");
        if (ivCommunity == null) throw new RuntimeException("ivCommunity not found in layout");
        if (ivSensor == null) throw new RuntimeException("ivSensor not found in layout");
        if (navMarket == null) throw new RuntimeException("nav_market not found in layout");
        if (navTraining == null) throw new RuntimeException("ivTraining not found in layout");
        if (navProfile == null) throw new RuntimeException("nav_profile not found in layout");
        // Remove navProfileLayout null check, not needed for crash fix
    }

    // Pass username to click listeners
    private void setupClickListeners(String username) {
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

        if (ivSmartDataSystem != null) {
            ivSmartDataSystem.setOnClickListener(v -> {
                showToast("Opening Smart Data System");
                startActivity(new Intent(HomeActivity.this, SmartDataActivity.class));
            });
        }

        // Link Floating Sensor Button to SensorActivity
        if (ivSensor != null) {
            ivSensor.setOnClickListener(v -> {
                showToast("Opening Sensor Dashboard");
                startActivity(new Intent(HomeActivity.this, SensorActivity.class));
            });
        }

        // Link Community to CommunityActivity - ENHANCED
        if (ivCommunity != null) {
            ivCommunity.setOnClickListener(v -> {
                animateButton(v);
                showToast("Opening Community - Share & Learn Together! 🌱");
                Intent intent = new Intent(HomeActivity.this, CommunityActivity.class);
                if (username != null) {
                    intent.putExtra("username", username);
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            });
        }

        // Features not yet implemented - show coming soon message
        setFeatureClickListener(ivLaborManagement, "Labor Management");

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

        // Profile navigation: open profile screen from LinearLayout
        LinearLayout navProfileLayout = findViewById(R.id.nav_profile_layout);
        if (navProfileLayout != null) {
            navProfileLayout.setOnClickListener(v -> {
                try {
                    showToast("Opening Profile");
                    Intent intent = new Intent(HomeActivity.this, profileActivity.class);
                    // Always pass username if available
                    if (username != null && !username.isEmpty()) {
                        intent.putExtra("username", username);
                    } else {
                        intent.putExtra("username", "Guest");
                    }
                    startActivity(intent);
                } catch (Exception e) {
                    showToast("Error opening profile: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }

    private void setFeatureClickListener(ImageView imageView, String featureName) {
        if (imageView != null) {
            imageView.setOnClickListener(v -> {
                animateButton(v);
                showToast("Opening " + featureName + " - Coming Soon!");
            });
        }
    }

    private void animateButton(View button) {
        button.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100)
            .withEndAction(() -> button.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
