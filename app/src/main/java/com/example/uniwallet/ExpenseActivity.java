package com.example.uniwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ExpenseActivity extends AppCompatActivity {
    Account account;
   // EditText budgetText;
    EditText savingsText;
    EditText addText;
    EditText paycheckText;
    EditText amountText;
    EditText itemText;
    EditText costText;
    TextView balanceTextView,budgetTextView,incomeTextView,savingsValueTextView,savingsPercentTextView;
    double savings;
    double budget = 0.0;
    double balance = 0.0;
    double income = 0.0;
    double amount = 0.0;
    //double paycheck = 0.1;
    double pay;
    String timeRate;
    String item;
    String savingsString;
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

         balanceTextView = findViewById(R.id.balanceTextView);
         budgetTextView = findViewById(R.id.generate_budget_field);
         incomeTextView = findViewById(R.id.incomeTextView);
         savingsText = findViewById(R.id.ChangeSavingsField);
         savingsPercentTextView = findViewById(R.id.savingsPercentTextView);
         savingsValueTextView = findViewById(R.id.savingsValueTextView);


        AccountManager accountManager = new AccountManager(ExpenseActivity.this);
        assert account != null;

        balance = accountManager.getBalanceFromFile(account);
        String formattedBalance = formatDouble(balance);
        //String balanceString = String.valueOf(formattedBalance);
        balanceTextView.setText(formattedBalance);

        savings = accountManager.getSavingsFromFile(account);
        String savingsString = formatDouble(savings);
        savingsPercentTextView.setText(savingsString);

        income = accountManager.getPaycheckFromFile(account);
        String incomeString = formatDouble(income);
        incomeTextView.setText(incomeString);

        double savingsDecimal = savings / 100;
        double savingsValue = income * savingsDecimal;
        String savingsValueString = formatDouble(savingsValue);
        savingsValueTextView.setText(savingsValueString);

        budget = accountManager.generateBudget(account);
        String budgetString = formatDouble(budget);
        budgetTextView.setText(budgetString);

        Button generateBudgetButton = findViewById(R.id.generateBudgetButton);
        //totalBudgetText = findViewById(R.id.generate_budget_field);

        //General purpose buttons

        Button homeButton = findViewById(R.id.homeButton);
        Button changeSavingsButton = findViewById(R.id.change_SavingsButton);
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
/*
         changeSavingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExpenseActivity.this, account.getUsername() + "savings updated: " + account.getSavings(), Toast.LENGTH_SHORT).show();
                updateAccountSavings();
            }
        });
  */
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity(account);
            }
        });
        generateBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccountBudget();
                AccountManager accountManager = new AccountManager(ExpenseActivity.this);
                double generatedBudget = accountManager.generateBudget(account);
                savings = accountManager.getSavingsFromFile(account);
                //double newBalance = generatedBudget * (1-savingsDecimal);
                //String generatedBalanceString = String.valueOf(newBalance);
                String generatedBudgetString = formatDouble(generatedBudget);

                if(generatedBudget <= 0){
                    Toast.makeText(ExpenseActivity.this, account.getUsername() + " Budget Value Exceeded: " + generatedBudgetString, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ExpenseActivity.this, account.getUsername() + " budget updated: " + generatedBudgetString, Toast.LENGTH_SHORT).show();
                }
                budgetTextView.setText(generatedBudgetString);

                // updateAccountSavings();
               // savingsValueTextView.setText(savingsValueString);
            }
        });
        utilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateAddUtility();
                double addedAmount = Double.parseDouble(costText.getText().toString());
                String utility = utilitySpinner.getSelectedItem().toString();
                Toast.makeText(ExpenseActivity.this, "Utility added: " + utility + ", Amount: " + addedAmount, Toast.LENGTH_SHORT).show();

            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateRemoveFunds();
                double removedAmount = Double.parseDouble(amountText.getText().toString());
                String removedItem = itemText.getText().toString();
                Toast.makeText(ExpenseActivity.this, "Funds removed: " + removedItem + ", Amount: " + removedAmount, Toast.LENGTH_SHORT).show();

            }
        });
        paycheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateAccountPaycheck();
                double paycheckAmount = Double.parseDouble(paycheckText.getText().toString());
                Toast.makeText(ExpenseActivity.this, "Paycheck added: $" + paycheckAmount, Toast.LENGTH_SHORT).show();
                double paycheck = account.getPay();
                String paycheckString = formatDouble(paycheck);
                incomeTextView.setText(paycheckString);
                //updateAccountSavings();
                //savingsValueTextView.setText(savingsValueString);
                //updateAccountSavings();
                //savingsValueTextView.setText(savingsValueString);


            }
        });

        submitAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateAddAmount();
                double addedAmount = Double.parseDouble(addText.getText().toString());
                Toast.makeText(ExpenseActivity.this, "Funds added: $" + addedAmount, Toast.LENGTH_SHORT).show();

            }
        });
        changeSavingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateAccountSavings();
                double updatedSavings = Double.parseDouble(savingsText.getText().toString());
                Toast.makeText(ExpenseActivity.this, "Savings updated: " + updatedSavings + "%", Toast.LENGTH_SHORT).show();

                double savings = accountManager.getSavingsFromFile(account);
                 String savingsString = savings + "%";
                //String savingsString = String.valueOf(savings);
                savingsPercentTextView.setText(savingsString);
                double savingsDecimal = savings / 100;
                double savingsValue = income * savingsDecimal;
                String savingsValueString = formatDouble(savingsValue);
                savingsValueTextView.setText(savingsValueString);

            }
        });
    }

    private void launchMainActivity(Account account) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }

    private void updateAccountSavings() {
        String savingsString = savingsText.getText().toString();
        if (!savingsString.isEmpty()) {
            try {
                // Remove the percent symbol if present
                savingsString = savingsString.replace("%", "");
                savings = Double.parseDouble(savingsString);
                if (account != null) {
                    account.setSavings(savings);
                    AccountManager accountManager = new AccountManager(ExpenseActivity.this);
                    accountManager.updateSavingsInFile(account, savings); // Update savings in the file

                    // Calculate savings value based on income and savings
                    double savingsDecimal = savings / 100;
                    double savingsValue = income * savingsDecimal;
                    String savingsValueString = formatDouble(savingsValue);

                    // Update the TextView with the new savings value
                    savingsValueTextView.setText(savingsValueString);

                    // Update the savings variable with the new value
                    this.savings = savings;

                    // Update the savingsPercentTextView with the new savings percentage
                    savingsPercentTextView.setText(savingsString);
                }
            } catch (NumberFormatException e) {
                // Handle the NumberFormatException gracefully
                // For example, display an error message to the user
                Toast.makeText(ExpenseActivity.this, "Invalid savings value", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    private void updateAccountBudget() {
        String budgetString = budgetTextView.getText().toString();
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

                // Recalculate savings value based on the updated paycheck amount
                double paycheck = account.getPay();
                double savingsDecimal = savings / 100;
                double savingsValue = paycheck * savingsDecimal;
                String savingsValueString = formatDouble(savingsValue);

                // Update the TextView with the new savings value
                savingsValueTextView.setText(savingsValueString);
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
    private String formatDouble(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP); // Optional: Adjust rounding mode as needed
        return df.format(value);
    }

}
