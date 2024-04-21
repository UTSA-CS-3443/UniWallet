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

public class LoginActivity extends AppCompatActivity {

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);


            EditText usernameEditText = findViewById(R.id.editTextUsername);
            EditText passwordEditText = findViewById(R.id.editTextPassword);

            Button submitButton = findViewById(R.id.btnSubmit);
            Button backButton = findViewById(R.id.btnBack);


            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();


                    AccountManager accountManager = new AccountManager(LoginActivity.this);

                    Account loginAccount = accountManager.login(username, password);

                    if (loginAccount != null){
                        Toast.makeText(LoginActivity.this, "Valid Login parameters", Toast.LENGTH_SHORT).show();
                        launchActivity(loginAccount);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Login parameters", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
        private void launchActivity(Account account) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("account", account);
        launchActivity(account);
    }

}
