package com.example.uniwallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;


import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;

import java.io.IOException;
/**
 * Activity for user sign-up process.
 */
public class SignUpActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Initialize UI components
        Button submitButton = findViewById(R.id.submit_btn);
        Button backButton = findViewById(R.id.back_btn);
        EditText usernameEditText = findViewById(R.id.editTextUsername);
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        // Set click listener for submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        // Retrieve username and password input
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

        // Create an AccountManager instance
                AccountManager accountManager = new AccountManager(SignUpActivity.this);
                // Check if username or password is empty
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Username or password cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // Attempt to sign up the user
                        Account signedUpAccount = accountManager.signUp(username, password);
                        // Display appropriate message based on sign-up success or failure
                        if (signedUpAccount != null) {
                            // If sign-up is successful, launch LoginActivity
                            launchActivity(signedUpAccount);
                            Toast.makeText(SignUpActivity.this, "valid sign up parameters", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign-up fails, display error message
                            Toast.makeText(SignUpActivity.this, "Invalid sign up parameters", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        // Throw runtime exception if IOException occurs
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        // Set click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to HomeActivity
                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    /**
     * Launches the LoginActivity.
     *
     * @param account The signed-up account.
     */
   private void launchActivity(Account account) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
   }
}
