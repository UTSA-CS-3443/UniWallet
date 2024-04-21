package com.example.uniwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("account")) {
            account = (Account) intent.getSerializableExtra("account");

        }

        //TODO add ids from xml file
        Button delete_account = findViewById(R.id.deleteAccountButton);
        Button change_password = findViewById(R.id.changePasswordButton);
        Button logout = findViewById(R.id.logoutAccountButton);
        Button home_button = findViewById(R.id.homeButton);

        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add/change logic for account deletion
                //AccountManager.deleteAccount();
                launchHomeActivity();
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchChangePasswordActivity();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeActivity();
            }
        });
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity();
            }
        });
    }

    private void launchChangePasswordActivity() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
    private void launchHomeActivity( ) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void launchMainActivity( ) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
}
