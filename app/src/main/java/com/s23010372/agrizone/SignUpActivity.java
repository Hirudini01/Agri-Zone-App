package com.s23010372.agrizone;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String selectedUserType = "Farmer"; // Default selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        EditText etFullName = findViewById(R.id.etFullName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        TextView tvPasswordHint = findViewById(R.id.tvPasswordHint);
        EditText etUsername = findViewById(R.id.etUsername);
        Spinner spinnerUserType = findViewById(R.id.spinnerUserType);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        TextView tvLogin = findViewById(R.id.tvLogin);

        // Setup password hint visibility
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvPasswordHint.setVisibility(View.VISIBLE);
                } else {
                    tvPasswordHint.setVisibility(View.GONE);
                }
            }
        });

        // Setup user type spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.user_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(adapter);
        spinnerUserType.setOnItemSelectedListener(this);

        // Create Account button click - UPDATED AS REQUESTED
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    // Get user data
                    String fullName = ((EditText) findViewById(R.id.etFullName)).getText().toString().trim();
                    String email = ((EditText) findViewById(R.id.etEmail)).getText().toString().trim();
                    String password = ((EditText) findViewById(R.id.etPassword)).getText().toString().trim();
                    String username = ((EditText) findViewById(R.id.etUsername)).getText().toString().trim();

                    // Pass user data to AccountCreatedActivity
                    Intent intent = new Intent(SignUpActivity.this, AccountCreatedActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // Already have account? Click to login
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private boolean validateForm() {
        EditText etFullName = findViewById(R.id.etFullName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etUsername = findViewById(R.id.etUsername);

        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String username = etUsername.getText().toString().trim();

        // Reset errors
        etFullName.setError(null);
        etEmail.setError(null);
        etPassword.setError(null);
        etUsername.setError(null);

        boolean isValid = true;

        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            isValid = false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            isValid = false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            isValid = false;
        } else {
            if (password.length() < 6) {
                etPassword.setError("Password must be at least 6 characters");
                isValid = false;
            }
            if (!password.matches(".*[A-Z].*")) {
                etPassword.setError("Must contain at least one uppercase letter");
                isValid = false;
            }
            if (!password.matches(".*[a-z].*")) {
                etPassword.setError("Must contain at least one lowercase letter");
                isValid = false;
            }
            if (!password.matches(".*\\d.*")) {
                etPassword.setError("Must contain at least one digit");
                isValid = false;
            }
            if (!password.matches(".*[!@#$%^&*+=?-].*")) {
                etPassword.setError("Must contain at least one special character");
                isValid = false;
            }
        }

        if (username.isEmpty()) {
            etUsername.setError("Username is required");
            isValid = false;
        } else if (username.length() < 4) {
            etUsername.setError("Username must be at least 4 characters");
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedUserType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedUserType = "Farmer"; // Default if nothing selected
    }
}