package com.example.uniwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;

public class MainActivity extends AppCompatActivity {

    Account account;
    double savings = 0.0;

   // double budget = 0.0;
    double balance = 0.0;
    EditText savingsText;
    EditText balanceText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("account")) {
             account = (Account) intent.getSerializableExtra("account");

        }
        savingsText = findViewById(R.id.initialSavingsField);
        balanceText = findViewById(R.id.initialBalanceField);

        Button settingsButton = findViewById(R.id.settingsButton);
        Button expenseButton = findViewById(R.id.expenseButton);
        //Button graphsButton = findViewById(R.id.graphsButton);/
        Button submitBalanceButton = findViewById(R.id.submitBalanceButton);
        Button submitSavingsButton = findViewById(R.id.submitSavingsButton);

        AccountManager accountManager = new AccountManager(MainActivity.this);

        savings = accountManager.getSavingsFromFile(account);
         //budget = accountManager.getBudgetFromFile(account);
         balance = accountManager.getBalanceFromFile(account);
        submitBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double balanceAmount = Double.parseDouble(balanceText.getText().toString());
                Toast.makeText(MainActivity.this, account.getUsername() + " balance updated: $" + balanceAmount, Toast.LENGTH_SHORT).show();
                updateAccountBalance();
            }
        });

        submitSavingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double savingsAmount = Double.parseDouble(savingsText.getText().toString());
                Toast.makeText(MainActivity.this, account.getUsername() + " savings updated: " + savingsAmount + "%", Toast.LENGTH_SHORT).show();
                updateAccountSavings();
            }
        });
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

        /*
        graphsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGraphsActivity(account);
            }
        });
        */

    }
    private void updateAccountBalance() {
        String balanceString = balanceText.getText().toString();
        if (!balanceString.isEmpty()) {
            balance = Double.parseDouble(balanceString);
            if (account != null) {
                //account.setBalance(balance);
                AccountManager accountManager = new AccountManager(MainActivity.this);
                accountManager.updateBalanceInFile(account, balance); // Update balance in the file
            }
        }
    }

    private void updateAccountSavings() {
        String savingsString = savingsText.getText().toString();
        if (!savingsString.isEmpty()) {
            savings = Double.parseDouble(savingsString);
            if (account != null) {
                account.setSavings(savings);
                AccountManager accountManager = new AccountManager(MainActivity.this);
                accountManager.updateSavingsInFile(account, savings); // Update budget in the file
            }
        }
    }
    private void launchExpenseActivity(Account account) {
        Intent intent = new Intent(this, ExpenseActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
    /*
    private void launchGraphsActivity(Account account) {
        Intent intent = new Intent(this, GraphsActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }

     */
    private void launchSettingsActivity(Account account) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
}