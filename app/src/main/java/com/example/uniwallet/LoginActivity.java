package com.example.uniwallet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;

public class LoginActivity extends AppCompatActivity {

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);


            EditText username = findViewById(R.id.editTextUsername);
            EditText password = findViewById(R.id.editTextPassword);

            Button Submit = findViewById(R.id.btnSubmit);
            Button back = findViewById(R.id.btnBack);

            //AccountManager accountManager = new AccountManager(getApplicationContext(), username.toString(), password.toString());

           // accountManager.login(username.toString(), password.toString());
        }
}
