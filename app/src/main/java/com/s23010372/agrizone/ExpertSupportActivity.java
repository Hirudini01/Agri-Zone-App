package com.s23010372.agrizone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpertSupportActivity extends AppCompatActivity {

    private MaterialCardView ivBack;
    private ImageView ivPhotoPreview;
    private Button btnTakePhoto;
    private Button btnSelectPhoto;
    private Button btnUploadProblem;
    private Button btnEmergencyHelp;
    private Spinner spinnerProblemCategory;
    private EditText etProblemDescription;
    private EditText etLocation;
    private EditText etContactNumber;

    private DatabaseActivity dbHelper;
    private Bitmap capturedPhoto;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int STORAGE_PERMISSION_CODE = 102;

    // Activity result launchers
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expertsupport);

        // Initialize database helper
        dbHelper = new DatabaseActivity(this);

        // Initialize views
        initializeViews();

        // Setup spinner with hardcoded values
        setupProblemCategorySpinner();

        // Setup activity result launchers
        setupActivityLaunchers();

        // Setup click listeners
        setupClickListeners();

        Toast.makeText(this, "💬 Expert Support - Submit your farming problems", Toast.LENGTH_SHORT).show();
    }

    private void setupProblemCategorySpinner() {
        // Create professional problem categories with emojis
        String[] problemCategories = {
            "Select Problem Category",
            "🦠 Plant Disease & Infection",
            "🐛 Pest & Insect Issues",
            "🌱 Growth & Development Problems",
            "🍂 Leaf Discoloration & Damage",
            "🌾 Low Crop Yield Issues",
            "💧 Irrigation & Water Problems",
            "🌡️ Weather & Climate Issues",
            "🧪 Soil Quality & Nutrition",
            "🌿 Weed Management",
            "❓ Other Agricultural Issues"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, problemCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProblemCategory.setAdapter(adapter);
    }

    private void initializeViews() {
        ivBack = findViewById(R.id.ivBack);
        ivPhotoPreview = findViewById(R.id.ivPhotoPreview);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
        btnUploadProblem = findViewById(R.id.btnUploadProblem);
        btnEmergencyHelp = findViewById(R.id.btnEmergencyHelp);
        spinnerProblemCategory = findViewById(R.id.spinnerProblemCategory);
        etProblemDescription = findViewById(R.id.etProblemDescription);
        etLocation = findViewById(R.id.etLocation);
        etContactNumber = findViewById(R.id.etContactNumber);
    }

    private void setupActivityLaunchers() {
        // Camera launcher
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        capturedPhoto = (Bitmap) extras.get("data");
                        if (capturedPhoto != null) {
                            ivPhotoPreview.setImageBitmap(capturedPhoto);
                            Toast.makeText(this, "📷 Photo captured successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        );

        // Gallery launcher
        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    if (selectedImage != null) {
                        ivPhotoPreview.setImageURI(selectedImage);
                        Toast.makeText(this, "🖼️ Photo selected from gallery!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }

    private void setupClickListeners() {
        // Back button
        ivBack.setOnClickListener(v -> finish());

        // Take photo button
        btnTakePhoto.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                openCamera();
            } else {
                requestCameraPermission();
            }
        });

        // Select photo button
        btnSelectPhoto.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                openGallery();
            } else {
                requestStoragePermission();
            }
        });

        // Upload problem button
        btnUploadProblem.setOnClickListener(v -> uploadProblemToDatabase());

        // Emergency help button
        btnEmergencyHelp.setOnClickListener(v -> {
            Toast.makeText(this, "🚨 Connecting to emergency agricultural hotline...", Toast.LENGTH_LONG).show();
            showEmergencyDialog();
        });
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(cameraIntent);
        } else {
            Toast.makeText(this, "Camera not available on this device", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private void uploadProblemToDatabase() {
        // Enhanced validation with professional messages
        String category = spinnerProblemCategory.getSelectedItem().toString();
        String description = etProblemDescription.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String contact = etContactNumber.getText().toString().trim();

        if (category.equals("Select Problem Category")) {
            Toast.makeText(this, "⚠️ Please select a problem category", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty() || description.length() < 10) {
            etProblemDescription.setError("Please provide a detailed description (minimum 10 characters)");
            etProblemDescription.requestFocus();
            return;
        }

        if (location.isEmpty()) {
            etLocation.setError("Farm location is required for expert visit");
            etLocation.requestFocus();
            return;
        }

        if (contact.isEmpty() || contact.length() < 10) {
            etContactNumber.setError("Valid phone number is required (minimum 10 digits)");
            etContactNumber.requestFocus();
            return;
        }

        // Show professional loading message
        Toast.makeText(this, "🔄 Submitting your request to agricultural expert...", Toast.LENGTH_SHORT).show();

        // Create problem record
        boolean success = saveProblemToDatabase(category, description, location, contact);

        if (success) {
            showSuccessDialog();
            clearForm();
        } else {
            Toast.makeText(this, "❌ Submission failed. Please check your connection and try again.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean saveProblemToDatabase(String category, String description, String location, String contact) {
        try {
            // Generate timestamp
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            // Create problem ID
            String problemId = "PROB_" + System.currentTimeMillis();

            // Save to database
            boolean success = dbHelper.addProblem(problemId, category, description, location, contact, timestamp);

            if (success) {
                // Log the problem
                System.out.println("=== EXPERT SUPPORT PROBLEM SUBMITTED ===");
                System.out.println("Problem ID: " + problemId);
                System.out.println("Category: " + category);
                System.out.println("Description: " + description);
                System.out.println("Location: " + location);
                System.out.println("Contact: " + contact);
                System.out.println("Timestamp: " + timestamp);
                System.out.println("Photo: " + (capturedPhoto != null ? "Yes" : "No"));
                System.out.println("========================================");
            }

            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void clearForm() {
        etProblemDescription.setText("");
        etLocation.setText("");
        etContactNumber.setText("");
        spinnerProblemCategory.setSelection(0);
        ivPhotoPreview.setImageResource(android.R.drawable.ic_menu_camera);
        capturedPhoto = null;
    }

    private void showSuccessDialog() {
        Toast.makeText(this,
            "✅ SUCCESS! Your problem has been submitted to our expert team.\n\n" +
            "📞 Dr. Perera will contact you within 2-4 hours\n" +
            "📧 You'll receive SMS updates on your request\n" +
            "🏥 For emergencies, use the red emergency button",
            Toast.LENGTH_LONG).show();
    }

    private void showEmergencyDialog() {
        Toast.makeText(this,
            "🚨 AGRICULTURAL EMERGENCY HOTLINE\n\n" +
            "📞 24/7 Helpline: 1920\n" +
            "🏥 Plant Disease Emergency: 1919\n" +
            "📱 WhatsApp Support: +94 77 123 4567\n\n" +
            "⚡ For immediate assistance with crop disasters",
            Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Camera permission required to take photos", Toast.LENGTH_SHORT).show();
                }
                break;

            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, "Storage permission required to select photos", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

// All UI logic matches the professional layout.
