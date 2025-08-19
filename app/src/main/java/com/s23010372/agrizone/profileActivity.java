package com.s23010372.agrizone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class profileActivity extends AppCompatActivity {

    private static final int ANIMATION_DURATION = 200;
    private static final float SCALE_PRESSED = 0.96f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_profile);

            // Initialize views with null safety
            TextView idView = findViewById(R.id.profile_id);
            TextView nameView = findViewById(R.id.profile_name);
            TextView usernameView = findViewById(R.id.profile_username);
            TextView emailView = findViewById(R.id.profile_email);
            TextView phoneView = findViewById(R.id.profile_phone);
            TextView userTypeView = findViewById(R.id.profile_user_type);
            Button editBtn = findViewById(R.id.profile_edit_btn);
            Button settingsBtn = findViewById(R.id.profile_settings_btn);
            Button logoutBtn = findViewById(R.id.profile_logout_btn);
            ImageView avatarView = findViewById(R.id.profile_avatar);
            ImageView backButton = findViewById(R.id.back_button);

            // Enhanced back button with professional animation
            if (backButton != null) {
                setupBackButton(backButton);
            }

            // Defensive null checks with professional error handling
            if (idView == null || nameView == null || usernameView == null ||
                emailView == null || phoneView == null || userTypeView == null) {
                showProfessionalError("❌ Profile interface initialization failed");
                return;
            }

            // Get username from intent
            String username = getIntent().getStringExtra("username");
            loadUserProfile(username, idView, nameView, usernameView, emailView, phoneView, userTypeView);

            // Setup professional button interactions
            setupProfessionalButtons(editBtn, settingsBtn, logoutBtn, avatarView);

        } catch (Exception e) {
            showProfessionalError("⚠️ Error loading profile: " + e.getMessage());
            finish();
        }
    }

    private void setupBackButton(ImageView backButton) {
        backButton.setOnClickListener(v -> {
            // Professional scale animation
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, SCALE_PRESSED, 1.0f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.0f, SCALE_PRESSED, 1.0f);
            scaleX.setDuration(ANIMATION_DURATION);
            scaleY.setDuration(ANIMATION_DURATION);

            scaleX.start();
            scaleY.start();

            // Navigate back after animation
            v.postDelayed(() -> {
                Intent intent = new Intent(profileActivity.this, HomeActivity.class);
                String username = getIntent().getStringExtra("username");
                if (username != null) {
                    intent.putExtra("username", username);
                }
                startActivity(intent);
                finish();
                // Professional transition
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }, ANIMATION_DURATION / 2);
        });
    }

    private void setupProfessionalButtons(Button editBtn, Button settingsBtn, Button logoutBtn, ImageView avatarView) {
        // Edit profile button with enhanced interaction
        if (editBtn != null) {
            editBtn.setOnClickListener(v -> {
                animateButtonPress(v);
                v.postDelayed(() -> showFeatureComingSoon("✏️ Profile Editor", "Advanced profile editing capabilities"), 100);
            });
        }

        // Settings button with professional feedback
        if (settingsBtn != null) {
            settingsBtn.setOnClickListener(v -> {
                animateButtonPress(v);
                v.postDelayed(() -> showFeatureComingSoon("⚙️ Account Settings", "Comprehensive account management tools"), 100);
            });
        }

        // Logout button with confirmation
        if (logoutBtn != null) {
            logoutBtn.setOnClickListener(v -> {
                animateButtonPress(v);
                v.postDelayed(this::showProfessionalLogoutConfirmation, 100);
            });
        }

        // Avatar with professional interaction
        if (avatarView != null) {
            avatarView.setOnClickListener(v -> {
                animateAvatarPress(v);
                v.postDelayed(() -> showFeatureComingSoon("📸 Avatar Customization", "Upload and customize your profile picture"), 150);
            });
        }
    }

    private void loadUserProfile(String username, TextView idView, TextView nameView, TextView usernameView, TextView emailView, TextView phoneView, TextView userTypeView) {
        if (username != null && !username.isEmpty() && !username.equals("Guest")) {
            DatabaseActivity db = null;
            Cursor cursor = null;
            try {
                showLoadingState(true);
                db = new DatabaseActivity(this);
                cursor = db.getUserByUsername(username);

                if (cursor != null && cursor.moveToFirst()) {
                    // Get user data from database with better error handling
                    int idIndex = cursor.getColumnIndex(DatabaseActivity.COL_ID);
                    int emailIndex = cursor.getColumnIndex(DatabaseActivity.COL_EMAIL);
                    int phoneIndex = cursor.getColumnIndex(DatabaseActivity.COL_PHONE);
                    int userTypeIndex = cursor.getColumnIndex(DatabaseActivity.COL_USER_TYPE);

                    int id = idIndex >= 0 ? cursor.getInt(idIndex) : 0;
                    String email = emailIndex >= 0 ? cursor.getString(emailIndex) : "";
                    String phone = phoneIndex >= 0 ? cursor.getString(phoneIndex) : "";
                    String userType = userTypeIndex >= 0 ? cursor.getString(userTypeIndex) : "Farmer";

                    // Null safety
                    if (email == null) email = "";
                    if (phone == null) phone = "";
                    if (userType == null || userType.isEmpty()) userType = "Farmer";

                    // Get user activity stats from community
                    int postCount = db.getUserPostCount(username);
                    int commentCount = db.getUserCommentCount(username);

                    // Professional display formatting
                    String idText = String.format(Locale.getDefault(),
                        "🆔 ID: #%04d • 📝 %d posts • 💬 %d replies", id, postCount, commentCount);
                    idView.setText(idText);

                    // Enhanced name display
                    nameView.setText(String.format("👤 %s", username));
                    usernameView.setText("@" + username.toLowerCase(Locale.ROOT));

                    emailView.setText(!email.isEmpty() ? "📧 " + email : "📧 No email provided");
                    phoneView.setText(!phone.isEmpty() ? "📱 " + phone : "📱 No phone number provided");

                    // Professional user type styling
                    userTypeView.setText(userType);
                    setProfessionalUserTypeStyle(userTypeView, userType);

                    // Success feedback
                    showProfessionalToast("✅ Profile loaded successfully");

                } else {
                    showProfessionalError("❌ User data not found in database");
                    setDefaultProfile(username, idView, nameView, usernameView, emailView, phoneView, userTypeView);
                }
            } catch (Exception e) {
                showProfessionalError("⚠️ Database error: " + e.getMessage());
                setDefaultProfile(username, idView, nameView, usernameView, emailView, phoneView, userTypeView);
            } finally {
                showLoadingState(false);
                // Proper resource cleanup
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                if (db != null) {
                    db.close();
                }
            }
        } else {
            setDefaultProfile("Guest User", idView, nameView, usernameView, emailView, phoneView, userTypeView);
        }
    }

    private void setDefaultProfile(String username, TextView idView, TextView nameView, TextView usernameView, TextView emailView, TextView phoneView, TextView userTypeView) {
        idView.setText("🆔 ID: Not assigned • 📝 0 posts • 💬 0 replies");
        nameView.setText(String.format("👤 %s", username != null ? username : "Unknown User"));
        usernameView.setText("@" + (username != null ? username.toLowerCase(Locale.ROOT) : "unknown"));
        emailView.setText("📧 No email provided");
        phoneView.setText("📱 No phone number provided");
        userTypeView.setText("Guest");
        setProfessionalUserTypeStyle(userTypeView, "Guest");
    }

    private void setProfessionalUserTypeStyle(TextView userTypeView, String userType) {
        int color;
        int icon;
        String emoji;

        switch (userType) {
            case "Expert":
            case "Agricultural Expert":
                color = ContextCompat.getColor(this, R.color.button_green);
                icon = android.R.drawable.ic_dialog_info;
                emoji = "🔬 ";
                break;
            case "Admin":
                color = ContextCompat.getColor(this, R.color.button_red);
                icon = android.R.drawable.ic_menu_manage;
                emoji = "👑 ";
                break;
            case "Farmer":
                color = ContextCompat.getColor(this, R.color.button_green);
                icon = android.R.drawable.ic_menu_myplaces;
                emoji = "🌾 ";
                break;
            default:
                color = ContextCompat.getColor(this, R.color.hint_text);
                icon = android.R.drawable.ic_menu_help;
                emoji = "👤 ";
                break;
        }

        // Create professional rounded background
        GradientDrawable background = new GradientDrawable();
        background.setColor(color);
        background.setCornerRadius(20f);

        userTypeView.setBackground(background);
        userTypeView.setText(emoji + userType);
        userTypeView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        userTypeView.setPadding(24, 12, 24, 12);

        // Add subtle animation
        userTypeView.setAlpha(0f);
        userTypeView.animate()
            .alpha(1f)
            .setDuration(300)
            .start();
    }

    private void animateButtonPress(View button) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1.0f, SCALE_PRESSED, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1.0f, SCALE_PRESSED, 1.0f);
        scaleX.setDuration(ANIMATION_DURATION);
        scaleY.setDuration(ANIMATION_DURATION);
        scaleX.start();
        scaleY.start();
    }

    private void animateAvatarPress(View avatar) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(avatar, "rotation", 0f, 5f, -5f, 0f);
        ObjectAnimator scale = ObjectAnimator.ofFloat(avatar, "scaleX", 1.0f, 1.1f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(avatar, "scaleY", 1.0f, 1.1f, 1.0f);

        rotation.setDuration(300);
        scale.setDuration(300);
        scaleY.setDuration(300);

        rotation.start();
        scale.start();
        scaleY.start();
    }

    private void showFeatureComingSoon(String title, String description) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(description + "\n\n🚀 Coming in the next update!")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Got it", null)
                .show();
    }

    private void showProfessionalToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showProfessionalError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showLoadingState(boolean show) {
        // You can add a progress bar or loading indicator here
        // For now, just a toast
        if (show) {
            showProfessionalToast("🔄 Loading profile...");
        }
    }

    private void showProfessionalLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("🚪 Sign Out")
                .setMessage("Are you sure you want to sign out?\n\nYou'll need to log in again to access your account.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Sign Out", (d, w) -> {
                    try {
                        showProfessionalToast("👋 Successfully signed out");

                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } catch (Exception e) {
                        showProfessionalError("❌ Error during sign out: " + e.getMessage());
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(profileActivity.this, HomeActivity.class);
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            intent.putExtra("username", username);
        }
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}