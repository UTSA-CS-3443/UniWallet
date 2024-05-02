package com.example.uniwallet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

/**
 * The activity for managing user settings.
 */
public class SettingsActivity extends AppCompatActivity {
//Declare account
    Account account;
    private static final String MODE_PREF_KEY = "mode_preference";
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
        SwitchCompat switch_mode = findViewById(R.id.mode_switch);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isDarkModeEnabled = sharedPreferences.getBoolean(MODE_PREF_KEY, false); // Default to false (light mode)
        // Initialize the switch based on the mode preference
        switch_mode.setChecked(isDarkModeEnabled);
        // Set listener for theme switch
        switch_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(MODE_PREF_KEY, isChecked);
                editor.apply();

                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    setTheme(R.style.Base_Theme_UniWallet);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    setTheme(R.style.Base_Theme_UniWallet);
                }
                recreate();
            }
        });
        // Set click listener for delete account button
        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Revert theme then delete account and return to home activity
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                setTheme(R.style.Base_Theme_UniWallet);
                recreate();
                switch_mode.setChecked(false);
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
                // Revert theme then logout and return to home activity
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                setTheme(R.style.Base_Theme_UniWallet);
                recreate();
                switch_mode.setChecked(false);
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
