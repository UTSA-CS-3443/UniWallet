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
    // Declare variables
    Account account;
    double savings = 0.0;
    double balance = 0.0;
    EditText savingsText;
    EditText balanceText;
    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Retrieve account information from intent
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("account")) {
             account = (Account) intent.getSerializableExtra("account");

        }
        // Initialize EditText fields and buttons

        savingsText = findViewById(R.id.initialSavingsField);
        balanceText = findViewById(R.id.initialBalanceField);
        Button settingsButton = findViewById(R.id.settingsButton);
        Button expenseButton = findViewById(R.id.expenseButton);
        Button graphsButton = findViewById(R.id.graphsButton);
        Button submitBalanceButton = findViewById(R.id.submitBalanceButton);
        Button submitSavingsButton = findViewById(R.id.submitSavingsButton);

        // Initialize AccountManager
        AccountManager accountManager = new AccountManager(MainActivity.this);
        // Get initial savings and balance from file
        savings = accountManager.getSavingsFromFile(account);
         balance = accountManager.getBalanceFromFile(account);
        // Set click listener for submit balance button
        submitBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update account balance
                double balanceAmount = Double.parseDouble(balanceText.getText().toString());
                Toast.makeText(MainActivity.this, account.getUsername() + " balance updated: $" + balanceAmount, Toast.LENGTH_SHORT).show();
                updateAccountBalance();
            }
        });

        // Set click listener for submit savings button
        submitSavingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update savings balance
                double savingsAmount = Double.parseDouble(savingsText.getText().toString());
                Toast.makeText(MainActivity.this, account.getUsername() + " savings updated: " + savingsAmount + "%", Toast.LENGTH_SHORT).show();
                updateAccountSavings();
            }
        });
        // Set click listener for settings button
        settingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchSettingsActivity(account);
                    }
                     // Launch SettingsActivity
        });
        // Set click listener for expense button
        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchExpenseActivity(account);
            }
            // Launch ExpenseActivity
        });


        // Set click listener for graphs button
        graphsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGraphsActivity(account);
            }
            // Launch GraphsActivity
        });


    }
    /**
     * Updates the account balance.
     */
    private void updateAccountBalance() {
        String balanceString = balanceText.getText().toString();
        if (!balanceString.isEmpty()) {
            balance = Double.parseDouble(balanceString);
            if (account != null) {
                // Update balance in the file
                AccountManager accountManager = new AccountManager(MainActivity.this);
                accountManager.updateBalanceInFile(account, balance); // Update balance in the file
            }
        }
    }
    /**
     * Updates the account savings.
     */
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
    /**
     * Launches the ExpenseActivity.
     *
     * @param account The user's account.
     */
    private void launchExpenseActivity(Account account) {
        Intent intent = new Intent(this, ExpenseActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
    /**
     * Launches the GraphsActivity.
     *
     * @param account The user's account.
     */
    private void launchGraphsActivity(Account account) {
        Intent intent = new Intent(this, GraphsActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
    /**
     * Launches the GraphsActivity.
     *
     * @param account The user's account.
     */
    private void launchSettingsActivity(Account account) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
}