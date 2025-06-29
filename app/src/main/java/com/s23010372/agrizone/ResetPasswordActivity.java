package com.s23010372.agrizone;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etEmail, etNewPassword, etRePassword;
    private DatabaseActivity dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        etRePassword = findViewById(R.id.etRePassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Initialize database helper
        dbHelper = new DatabaseActivity(this);

        // Login button click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    String email = etEmail.getText().toString().trim();
                    String newPassword = etNewPassword.getText().toString().trim();
                    boolean success = dbHelper.resetPassword(email, newPassword);
                    if (success) {
                        Toast.makeText(ResetPasswordActivity.this, "Password reset successful!", Toast.LENGTH_SHORT).show();
                        // Navigate to login screen
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Email not found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateForm() {
        String email = etEmail.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String rePassword = etRePassword.getText().toString().trim();

        // Reset errors
        etEmail.setError(null);
        etNewPassword.setError(null);
        etRePassword.setError(null);

        boolean isValid = true;

        // Email validation
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            isValid = false;
        }

        // Password validation
        if (newPassword.isEmpty()) {
            etNewPassword.setError("Password is required");
            isValid = false;
        } else if (newPassword.length() < 6) {
            etNewPassword.setError("Password must be at least 6 characters");
            isValid = false;
        } else if (!newPassword.matches(".*[A-Z].*")) {
            etNewPassword.setError("Must contain at least one uppercase letter");
            isValid = false;
        } else if (!newPassword.matches(".*[a-z].*")) {
            etNewPassword.setError("Must contain at least one lowercase letter");
            isValid = false;
        } else if (!newPassword.matches(".*\\d.*")) {
            etNewPassword.setError("Must contain at least one digit");
            isValid = false;
        } else if (!newPassword.matches(".*[!@#$%^&*+=?-].*")) {
            etNewPassword.setError("Must contain at least one special character");
            isValid = false;
        }

        // Re-enter password validation
        if (rePassword.isEmpty()) {
            etRePassword.setError("Please re-enter password");
            isValid = false;
        } else if (!rePassword.equals(newPassword)) {
            etRePassword.setError("Passwords do not match");
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Optional: Add custom back navigation if needed
    }
}