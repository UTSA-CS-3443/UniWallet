package com.example.uniwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.uniwallet.model.Account;

public class MainActivity extends AppCompatActivity {

    Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("account")) {
             account = (Account) intent.getSerializableExtra("account");

        }
        Button settingsButton = findViewById(R.id.settingsButton);
        Button expenseButton = findViewById(R.id.expenseButton);
        Button graphsButton = findViewById(R.id.graphsButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSettingsActivity(account);
            }
        });

        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchExpenseActivity(account);
            }
        });

        graphsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGraphsActivity(account);
            }
        });
    }
    private void launchExpenseActivity(Account account) {
        Intent intent = new Intent(this, ExpenseActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
    private void launchGraphsActivity(Account account) {
        Intent intent = new Intent(this, GraphsActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
    private void launchSettingsActivity(Account account) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
}