package com.example.registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword;
    Button buttonRegister;
    LinearLayout registrationLayout, welcomeLayout;
    TextView welcomeText;

    public static final String SHARED_PREFS = "userPrefs";
    public static final String NAME_KEY = "name";
    public static final String EMAIL_KEY = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Contains both layouts

        // Initialize Views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        registrationLayout = findViewById(R.id.registrationLayout);
        welcomeLayout = findViewById(R.id.welcomeLayout);
        welcomeText = findViewById(R.id.welcomeText);

        // Read saved user data
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String savedName = sharedPref.getString(NAME_KEY, "");

        // Check if app launched with intent to show welcome
        boolean showWelcome = getIntent().getBooleanExtra("showWelcome", false);

        if (!TextUtils.isEmpty(savedName) && showWelcome) {
            showWelcomeLayout(savedName);
        } else if (!TextUtils.isEmpty(savedName)) {
            // User already registered, automatically redirect with intent
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra("showWelcome", true);
            startActivity(intent);
            finish(); // Prevent going back
        }

        // Handle Register Button
        buttonRegister.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Save user info
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(NAME_KEY, name);
                editor.putString(EMAIL_KEY, email);
                editor.apply();

                // Redirect with intent to show welcome
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("showWelcome", true);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showWelcomeLayout(String name) {
        registrationLayout.setVisibility(View.GONE);
        welcomeLayout.setVisibility(View.VISIBLE);
        welcomeText.setText("Welcome, " + name + "!");
    }
}
