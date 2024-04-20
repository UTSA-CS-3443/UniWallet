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

public class SignUpActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button submitButton = findViewById(R.id.submit_btn);
        EditText usernameEditText = findViewById(R.id.editTextUsername);
        EditText passwordEditText = findViewById(R.id.editTextPassword);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();


                AccountManager accountManager = new AccountManager(SignUpActivity.this);

                try {
                    Account signedUpAccount = accountManager.signUp(username, password);

                    if (signedUpAccount != null){
                        launchActivity(signedUpAccount);
                        Toast.makeText(SignUpActivity.this, "valid sign up parameters", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Invalid sign up parameters", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

   private void launchActivity(Account account) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
   }
}
