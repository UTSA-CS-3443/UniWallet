package com.example.uniwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;
import com.google.android.material.button.MaterialButtonToggleGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
/**
 * The activity for managing user settings.
 */
public class SettingsActivity extends AppCompatActivity {
//Declare account
    Account account;
    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Retrieve account information from intent
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("account")) {
            account = (Account) intent.getSerializableExtra("account");

        }
        // Initialize buttons and button toggle group
        Button delete_account = findViewById(R.id.deleteAccountButton);
        Button change_password = findViewById(R.id.changePasswordButton);
        Button logout = findViewById(R.id.logoutAccountButton);
        Button home_button = findViewById(R.id.homeButton);
        MaterialButtonToggleGroup buttonToggleGroup = findViewById(R.id.btg_theme_switch);
        // Set listener for theme switch toggle group
        buttonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                // Change app theme based on selection
                if (checkedId == R.id.btnLight) {
                    // Set app theme to light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (checkedId == R.id.btnDark) {
                    // Set app theme to dark mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });
        // Set click listener for delete account button
        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete account and return to home activity
                deleteAccount();
                launchHomeActivity();
            }
        });
        // Set click listener for change password button
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch ChangePasswordActivity
                launchChangePasswordActivity();
            }
        });
        // Set click listener for logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout and return to home activity
                launchHomeActivity();
            }
        });
        // Set click listener for home button
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to main activity
                launchMainActivity();
            }
        });
    }

    /**
     * Deletes the user account.
     */
    private void deleteAccount(){
        // Delete account from file
        AccountManager accountManager = new AccountManager(SettingsActivity.this);
            accountManager.deleteAccount(account);
    }
    /**
     * Launches the ChangePasswordActivity.
     */
    private void launchChangePasswordActivity() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
    /**
     * Launches the HomeActivity.
     */
    private void launchHomeActivity( ) {
        Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * Launches the MainActivity.
     */
    private void launchMainActivity( ) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
}
