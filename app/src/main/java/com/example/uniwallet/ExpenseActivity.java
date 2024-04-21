package com.example.uniwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;

public class ExpenseActivity extends AppCompatActivity {
    Account account;
    EditText budgetText;
    double budget = 0.0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("account")) {
             account = (Account) intent.getSerializableExtra("account");
        }
            TextView balanceTextView = findViewById(R.id.balanceTextView);
            TextView budgetTextView = findViewById(R.id.budgetTextView);
            AccountManager accountManager = new AccountManager(ExpenseActivity.this);
            assert account != null;

            double balance = accountManager.getBalanceFromFile(account);
            String balanceString = String.valueOf(balance);
            balanceTextView.setText(balanceString);

            double budget = accountManager.getBudgetFromFile(account);
            String budgetString = String.valueOf(budget);
            budgetTextView.setText(budgetString);

            Button submitBudgetButton = findViewById(R.id.change_budgetButton);
            budgetText = findViewById(R.id.initialBudgetField);

            submitBudgetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateAccountBudget();
                    double budget = accountManager.getBudgetFromFile(account);
                    String budgetString = String.valueOf(budget);
                    budgetTextView.setText(budgetString);
                }
            });
        }
        private void updateAccountBudget () {
            String budgetString = budgetText.getText().toString();
            if (!budgetString.isEmpty()) {
                budget = Double.parseDouble(budgetString);
                if (account != null) {
                    account.setBudget(budget);
                    AccountManager accountManager = new AccountManager(ExpenseActivity.this);
                    accountManager.updateBudgetInFile(account, budget); // Update budget in the file
                }
            }
        }
        }
