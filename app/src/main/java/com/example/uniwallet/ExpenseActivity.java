package com.example.uniwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;

public class ExpenseActivity extends AppCompatActivity {
    Account account;
    EditText budgetText;
    EditText addText;
    EditText paycheckText;
    EditText amountText;
    EditText itemText;
    EditText costText;
    TextView totalBalanceText;
    double budget = 0.0;
    double amount = 0.0;
    double pay;
    String timeRate;
    String item;
    Spinner timeRateSpinner;
    Spinner categorySpinner;
    Spinner utilitySpinner;
    Spinner timeRateSpinner2;
    private boolean isAddingAmount = false;
    private boolean isRemovingFunds = false;
    private boolean isAddingUtility = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("account")) {
            account = (Account) intent.getSerializableExtra("account");
        }

        //Budget/Balance Buttons/Texts

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
        budgetText = findViewById(R.id.ChangeBudgetField);

        //Add Buttons/Texts

        Button submitAddButton = findViewById(R.id.add_amountButton);
        addText = findViewById(R.id.AddAmountField);

        //Paycheck Buttons/Texts

        Button paycheckButton = findViewById(R.id.add_paycheck_button);
        String[] timeRates = {"Weekly", "Biweekly", "Monthly", "Yearly"};
        paycheckText = findViewById(R.id.paycheckAmountField);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeRates);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timeRateSpinner = findViewById(R.id.paycheckTypeSpinner);
        timeRateSpinner.setAdapter(adapter);

        // String selectedTimeRate = timeRateSpinner.getSelectedItem().toString();

        //Remove Buttons/Texts

        Button removeButton = findViewById(R.id.remove_funds_button);
        String[] categories = {"Food", "Groceries", "Personal", "Custom"};
        amountText = findViewById(R.id.remove_funds_field);
        itemText = findViewById(R.id.item_field);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner = findViewById(R.id.categoryTypeSpinner);
        categorySpinner.setAdapter(adapter2);


        //Expense Buttons/Texts

        Button utilityButton = findViewById(R.id.add_utility_button);
        String[] utilities = {"Water", "Electrical", "House", "Custom"};
        String[] timeRates2 = {"Weekly", "Biweekly", "Monthly", "Yearly"};
        costText = findViewById(R.id.cost_field);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, utilities);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeRates2);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        utilitySpinner = findViewById(R.id.utilityTypeSpinner);
        utilitySpinner.setAdapter(adapter3);
        timeRateSpinner2 = findViewById(R.id.timeRateTypeSpinner);
        timeRateSpinner2.setAdapter(adapter4);

        //Generate Balance Button

        Button generateBalanceButton = findViewById(R.id.generateBalanceButton);
        totalBalanceText = findViewById(R.id.generate_balance_field);

        //General purpose buttons

        Button homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity(account);
            }
        });
        generateBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountManager accountManager = new AccountManager(ExpenseActivity.this);
                double generatedBalance = accountManager.generateBalance(account);
                double budget = accountManager.getBudgetFromFile(account);
                double budgetDecimal = budget / 100;
                double newBalance = generatedBalance * (1-budgetDecimal);
                String generatedBalanceString = String.valueOf(newBalance);
                totalBalanceText.setText(generatedBalanceString);
                balanceTextView.setText(generatedBalanceString);
            }
        });
        utilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddUtility();
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRemoveFunds();
            }
        });
        paycheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccountPaycheck();
            }
        });

        submitAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateAddAmount();

            }
        });
        submitBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccountBudget();
                double budget = accountManager.getBudgetFromFile(account);
                String budgetString = budget + "%";
                budgetTextView.setText(budgetString);
            }
        });
    }

    private void launchMainActivity(Account account) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
    private void updateAccountBudget() {
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

    private void updateAddAmount() {
        if (!isAddingAmount) { // Check if amount is already being added
            isAddingAmount = true; // Set flag to indicate that amount is being added
            String addString = addText.getText().toString();
            if (!addString.isEmpty()) {
                double amount = Double.parseDouble(addString);
                if (account != null) {
                    AccountManager accountManager = new AccountManager(ExpenseActivity.this);
                    accountManager.addMoney(account, amount); // Update Money in the file
                }
            }
            isAddingAmount = false; // Reset flag after adding amount
        }
    }

    private void updateAccountPaycheck() {
        String addString = paycheckText.getText().toString();
        if (!addString.isEmpty()) {
            double amount = Double.parseDouble(addString);
            String selectedTimeRate = timeRateSpinner.getSelectedItem().toString();
            if (account != null) {
                AccountManager accountManager = new AccountManager(ExpenseActivity.this);
                accountManager.addPaycheck(account, amount, selectedTimeRate); // Update Money in the file
            }
        }
    }

    private void updateRemoveFunds() {
        if (!isRemovingFunds) { // Check if funds are already being removed
            isRemovingFunds = true; // Set flag to indicate that funds are being removed
            String amountString = amountText.getText().toString();
            String itemString = itemText.getText().toString();
            if (!(amountString.isEmpty() && itemString.isEmpty())) {
                double amount = Double.parseDouble(amountString);
                String item = itemString;
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                if (account != null) {
                    AccountManager accountManager = new AccountManager(ExpenseActivity.this);
                    accountManager.removeFunds(account, selectedCategory, item, amount);
                }

            }
            isRemovingFunds = false;
        }
    }
    private void updateAddUtility() {
        if (!isAddingUtility) { // Check if utilities are already being added
            isAddingUtility = true; // Set flag to indicate that utilities are being added

            String utility = utilitySpinner.getSelectedItem().toString();
            String rate = timeRateSpinner2.getSelectedItem().toString();
            String amountString = costText.getText().toString();

            if (!utility.isEmpty() && !rate.isEmpty() && !amountString.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountString); // Parse amountString to a double

                    boolean isCustom = utility.equals("Custom");

                    AccountManager accountManager = new AccountManager(ExpenseActivity.this);
                    accountManager.addUtility(account, utility, rate, amount, isCustom);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            isAddingUtility = false; // Reset flag after adding utility
        }
    }

}
