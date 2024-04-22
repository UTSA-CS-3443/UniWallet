package com.example.uniwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;

public class MainActivity extends AppCompatActivity {

    Account account;
    double budget = 0.0;
    double balance = 0.0;
    EditText budgetText;
    EditText balanceText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("account")) {
             account = (Account) intent.getSerializableExtra("account");

        }
        budgetText = findViewById(R.id.initialBudgetField);
        balanceText = findViewById(R.id.initialBalanceField);

        Button settingsButton = findViewById(R.id.settingsButton);
        Button expenseButton = findViewById(R.id.expenseButton);
        //Button graphsButton = findViewById(R.id.graphsButton);/
        Button submitBalanceButton = findViewById(R.id.submitBalanceButton);
        Button submitBudgetButton = findViewById(R.id.submitBudgetButton);

        AccountManager accountManager = new AccountManager(MainActivity.this);

         budget = accountManager.getBudgetFromFile(account);
         balance = accountManager.getBalanceFromFile(account);
        submitBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateAccountBalance();
            }
        });

        submitBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccountBudget();
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

    private void updateAccountBudget() {
        String budgetString = budgetText.getText().toString();
        if (!budgetString.isEmpty()) {
            budget = Double.parseDouble(budgetString);
            if (account != null) {
                account.setBudget(budget);
                AccountManager accountManager = new AccountManager(MainActivity.this);
                accountManager.updateBudgetInFile(account, budget); // Update budget in the file
            }
        }
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