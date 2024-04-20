package com.example.uniwallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uniwallet.model.Account;

public class ExpenseActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("account")) {
            Account account = (Account) intent.getSerializableExtra("account");
        }
    }
}
