package com.s23010372.agrizone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MarketplaceActivity extends AppCompatActivity {

    private TextView tvMarketplaceTitle;
    private ImageView ivBack;
    private CardView cardSeeds, cardFertilizers, cardTools, cardHarvest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        tvMarketplaceTitle = findViewById(R.id.tvMarketplaceTitle);
        ivBack = findViewById(R.id.ivBack);
        cardSeeds = findViewById(R.id.cardSeeds);
        cardFertilizers = findViewById(R.id.cardFertilizers);
        cardTools = findViewById(R.id.cardTools);
        cardHarvest = findViewById(R.id.cardHarvest);
    }

    private void setupClickListeners() {
        // Back button functionality
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            });
        }

        // Category click listeners
        if (cardSeeds != null) {
            cardSeeds.setOnClickListener(v -> {
                showToast("Seeds & Plants category - Coming Soon!");
            });
        }

        if (cardFertilizers != null) {
            cardFertilizers.setOnClickListener(v -> {
                showToast("Fertilizers & Pesticides category - Coming Soon!");
            });
        }

        if (cardTools != null) {
            cardTools.setOnClickListener(v -> {
                showToast("Farming Tools & Equipment category - Coming Soon!");
            });
        }

        if (cardHarvest != null) {
            cardHarvest.setOnClickListener(v -> {
                showToast("Harvest & Produce category - Coming Soon!");
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
