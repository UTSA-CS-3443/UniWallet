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
/**
 * Activity for changing the password of the user's account.
 */
public class ChangePasswordActivity extends AppCompatActivity {
    /**
     * The account whose password is being changed.
     */
    Account account;
    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("account")) {
            account = (Account) intent.getSerializableExtra("account");

        }
        //TODO add ids from xml file

        Button submit_button = findViewById(R.id.submitButton);
        Button back_button = findViewById(R.id.backbutton);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSettingsActivity();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changePassword();
            }
        });
    }
    /**
     * Performs the action of changing the password.
     */
    private void changePassword() {
        EditText new_password = findViewById(R.id.editText1);
        EditText reenter_new_password = findViewById(R.id.editText2);
        String newPass = new_password.getText().toString();
        String rNewPass = reenter_new_password.getText().toString();

        if (newPass.equals(rNewPass) && !newPass.isEmpty()) {
            if (!newPass.equals(account.getPassword())) { // Check if new password is different from old one
                AccountManager accountManager = new AccountManager(ChangePasswordActivity.this);
                accountManager.updatePasswordInFiles(account, newPass);
                Toast.makeText(ChangePasswordActivity.this, "Password changed successfully to: " +  account.getPassword(), Toast.LENGTH_SHORT).show();
                launchSettingsActivity();
            } else {
                Toast.makeText(ChangePasswordActivity.this, "New password must be different from the old one!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChangePasswordActivity.this, "Passwords must match!", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Launches the SettingsActivity.
     */
    private void launchSettingsActivity( ) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
