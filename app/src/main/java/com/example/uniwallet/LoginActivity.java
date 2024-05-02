package com.example.uniwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;

import java.io.IOException;
/**
 * This activity allows users to log in to their account.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            // Initialize EditText fields and buttons
            EditText usernameEditText = findViewById(R.id.editTextUsername);
            EditText passwordEditText = findViewById(R.id.editTextPassword);
            Button submitButton = findViewById(R.id.btnSubmit);
            Button backButton = findViewById(R.id.btnBack);

            // Set click listener for submit button
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Retrieve username and password from EditText fields
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    // Initialize AccountManager
                    AccountManager accountManager = new AccountManager(LoginActivity.this);
                    // Attempt login
                    Account loginAccount = accountManager.login(username, password);
                    // Check if login is successful
                    if (loginAccount != null){
                        // Display success message and launch main activity
                        Toast.makeText(LoginActivity.this, "Valid Login parameters", Toast.LENGTH_SHORT).show();
                        launchActivity(loginAccount);
                    } else {
                        // Display error message for invalid login
                        Toast.makeText(LoginActivity.this, "Invalid Login parameters", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            // Set click listener for back button
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate back to HomeActivity
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    /**
     * Launches the main activity with the logged-in account.
     *
     * @param account The logged-in account.
     */
        private void launchActivity(Account account) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }

}
