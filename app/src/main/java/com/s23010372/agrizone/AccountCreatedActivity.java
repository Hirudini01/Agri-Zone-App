package com.s23010372.agrizone;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AccountCreatedActivity extends AppCompatActivity {

    private DatabaseActivity dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_created);

        // If your database helper is named DatabaseHelper, use that instead:
        // dbHelper = new DatabaseHelper(this);
        dbHelper = new DatabaseActivity(this);

        // Get user data from intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

        // Store user in database if not already present
        if (username != null && email != null && password != null) {
            boolean inserted = dbHelper.addUser(username, email, password);
            if (inserted) {
                Toast.makeText(this, "Account created and saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Account already exists or error occurred.", Toast.LENGTH_SHORT).show();
            }
        }

        Button btnLogin = findViewById(R.id.btnLogin);
        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to LoginActivity and clear back stack
                    Intent intent = new Intent(AccountCreatedActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to sign up screen
        Intent intent = new Intent(AccountCreatedActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}