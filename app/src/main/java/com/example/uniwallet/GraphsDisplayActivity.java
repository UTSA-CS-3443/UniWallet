package com.example.uniwallet;
//

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;

import androidx.appcompat.app.AppCompatActivity;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class GraphsDisplayActivity extends AppCompatActivity {

    public TextView personalBillTextView, utilitiesBillTextView, houseBillTextView, carPaymentTextView, customBillTextView;
    public TextView budgetTextView, balanceTextView, groceriesTextView;
    public TextView carPercentageTextView, balancePercentageTextView, utilitiesPercentageTextView, housePercentageTextView, customPercentageTextView, personalPercentageTextView, groceriesPercentageTextView;
    Account account;

    // Declare variables for bill values
    public double balanceValue;
    public TextView balancePercentage , utilitiesPercentage, housePercentage,
            customPercentage, personalPercentage, groceriesPercentage, carPercentage;

    //double budgetValue = 100;
    PieChart pieChart;
    Button homeButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Button homeButton, backButton;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs_display);
        pieChart = findViewById(R.id.piechart);
        personalBillTextView = findViewById(R.id.personalView);
        utilitiesBillTextView = findViewById(R.id.utilitiesView);
        houseBillTextView = findViewById(R.id.houseView);
        carPaymentTextView = findViewById(R.id.carView);
        customBillTextView = findViewById(R.id.customView);
         //budgetTextView = findViewById(R.id.remainingBudgetView);
        balanceTextView = findViewById(R.id.remainingBudgetView);
        groceriesTextView = findViewById(R.id.groceriesView);

        // Initialize the TextView variables
        utilitiesPercentageTextView = findViewById(R.id.utilitiesPercentage);
        housePercentageTextView = findViewById(R.id.housePercentage);
        customPercentageTextView = findViewById(R.id.customPercentage);
        personalPercentageTextView = findViewById(R.id.personalPercentage);
        groceriesPercentageTextView = findViewById(R.id.groceriesPercentage);
        carPercentageTextView = findViewById(R.id.carPercentage);

        utilitiesPercentage = findViewById(R.id.utilitiesPercentage);
        housePercentage = findViewById(R.id.housePercentage);
        customPercentage = findViewById(R.id.customPercentage);
        personalPercentage = findViewById(R.id.personalPercentage);
        groceriesPercentage = findViewById(R.id.groceriesPercentage);
        balancePercentage = findViewById(R.id.balancePercentage);
        carPercentage = findViewById(R.id.carPercentage);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("account")) {
            AccountManager accountManager;
            accountManager = new AccountManager(this);
            account = (Account) intent.getSerializableExtra("account");
            String dataType = intent.getStringExtra("dataType");
            assert account != null;
            double Savings = accountManager.getSavingsFromFile(account);
            double budgetValue = account.getBudget();
            double income = accountManager.getPaycheckFromFile(account);
            switch (dataType) {
                case "weekly": {
                    balanceValue = accountManager.getBalanceFromFile(account) / 4;
                    balanceTextView.setText(String.valueOf(balanceValue));
                    Double balancePercent = Double.parseDouble(String.valueOf(getPercentage(balanceValue,income)));
                    if (balancePercentageTextView != null) {
                        balancePercentageTextView.setText(String.valueOf(balancePercentage));
                    } else {
                        Log.e("GraphsDisplayActivity", "balancePercentageTextView is null");
                    }
                    //balanceTextView.setText(String.valueOf(balanceValue));
                    double personalBill = accountManager.getRemoveFundsCategory(account, "Personal");
                    String personalBillString = formatDouble(personalBill);
                    double personalPercent = Double.parseDouble(getPercentage(personalBill, income));
                    personalPercentageTextView.setText(String.valueOf(personalPercent));
                    personalBillTextView.setText(personalBillString);
                    Log.i("accounts", personalBillString);


                    String utilitiesRate = accountManager.getUtilityRate(account, "Utilities");
                    double utilitiesBill = accountManager.getUtilityRateAdjusted(account, "Utilities", utilitiesRate, false);
                    String utilitiesBillString = formatDouble(utilitiesBill);
                    utilitiesBillTextView.setText(utilitiesBillString);
                    double utilitiesPercent = Double.parseDouble(getPercentage(utilitiesBill, income));
                    utilitiesPercentageTextView.setText(String.valueOf(utilitiesPercent));
                    Log.i("accounts", utilitiesBillString);

                    String houseRate = accountManager.getUtilityRate(account, "House");
                    double houseBill = accountManager.getUtilityRateAdjusted(account, "House", houseRate, false) ;
                    String houseBillString = formatDouble(houseBill);
                    double housePercent = Double.parseDouble(getPercentage(houseBill,income));
                    housePercentageTextView.setText(String.valueOf(housePercent));
                    houseBillTextView.setText(String.valueOf(houseBill));
                    Log.i("accounts", houseBillString);

                    String carRate = accountManager.getUtilityRate(account, "Car");
                    double carBill = accountManager.getUtilityRateAdjusted(account, "Car", carRate, false);
                    String carBillString = formatDouble(carBill);
                    double carPercent = Double.parseDouble(getPercentage(carBill,income));
                    carPercentageTextView.setText(String.valueOf(carPercent));
                    carPaymentTextView.setText(carBillString);
                    Log.i("accounts", carBillString);

                    double groceriesBill = accountManager.getRemoveFundsCategory(account, "Groceries");
                    String groceriesBillString = formatDouble(groceriesBill);
                    double groceriesPercent = Double.parseDouble(getPercentage(groceriesBill, income));
                    groceriesPercentageTextView.setText(String.valueOf(groceriesPercent));
                    groceriesTextView.setText(groceriesBillString);
                    Log.i("accounts", groceriesBillString);


                    String customRate = accountManager.getUtilityRate(account, "Custom");
                    double customBill = accountManager.getUtilityRateAdjusted(account, "Custom", customRate, true);
                    String customBillString = formatDouble(customBill);
                    if (customBillString != null) {
                        // Proceed with using customBillString
                    } else {
                        // Handle the case where customBillString is null
                        Log.e("GraphsDisplayActivity", "customBillString is null");
                    }
                    double customPercent = Double.parseDouble(getPercentage(groceriesBill,income));
                    customPercentageTextView.setText(String.valueOf(customPercent));
                    customBillTextView.setText(customBillString);
                    Log.i("accounts", customBillString);

                    utilitiesPercentage.setText(String.valueOf(utilitiesPercent));
                    housePercentage.setText(String.valueOf(housePercent));
                    customPercentage.setText(String.valueOf(customPercent));
                    personalPercentage.setText(String.valueOf(personalPercent));
                    groceriesPercentage.setText(String.valueOf(groceriesPercent));
                    balancePercentage.setText(String.valueOf(balancePercent));
                    carPercentage.setText(String.valueOf(carPercent));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Utilites",
                                    (int) Double.parseDouble(utilitiesPercentage.getText().toString()), // Corrected line
                                    Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "House",
                                    (int) Double.parseDouble(housePercentage.getText().toString()),
                                    Color.parseColor("#66BB6A")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Custom",
                                    (int) Double.parseDouble(customPercentage.getText().toString()),
                                    Color.parseColor("#EF5350")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Personal",
                                    (int) Double.parseDouble(personalPercentage.getText().toString()),
                                    Color.parseColor("#29B6F6")));
                    Color.parseColor("#E3E0E0");
                    pieChart.addPieSlice( //CORRECT FORMAT
                            new PieModel(
                                    "Groceries",
                                    (int) Double.parseDouble(groceriesPercentage.getText().toString()),
                                    Color.parseColor("#800080")));
                    Color.parseColor("#E3E0E0");
                    pieChart.addPieSlice( //CORRECT FORMAT
                            new PieModel(
                                    "Balance",
                                    (int) Double.parseDouble(balancePercentage.getText().toString()),
                                    Color.parseColor("#fb7268")));
                    pieChart.addPieSlice( //CORRECT FORMAT
                            new PieModel(
                                    "Car",
                                    (int) Double.parseDouble(carPercentage.getText().toString()),
                                    Color.parseColor("#00008B"))); //CORRECT FORMAT
                    // To animate the pie chart
                    pieChart.startAnimation();
                }// Display weekly data
                break;
                case "monthly":
                    double balanceValueMonthly = accountManager.getBalanceFromFile(account);
                    balanceTextView.setText(String.valueOf(balanceValueMonthly));
                    double balancePercentMonthly = Double.parseDouble(getPercentage(balanceValueMonthly, income));
                    if (balancePercentageTextView != null) {
                        balancePercentageTextView.setText(String.valueOf(balancePercentMonthly));
                    } else {
                        Log.e("GraphsDisplayActivity", "balancePercentageTextView is null");
                    }
                    //balanceTextView.setText(String.valueOf(balanceValue));

                    double personalBillMonthly = accountManager.getRemoveFundsCategory(account, "Personal");
                    String personalBillStringMonthly = formatDouble(personalBillMonthly);
                    double personalPercentMonthly = Double.parseDouble(getPercentage(personalBillMonthly, income));
                    personalPercentageTextView.setText(String.valueOf(personalPercentMonthly));
                    personalBillTextView.setText(personalBillStringMonthly);
                    Log.i("accounts", personalBillStringMonthly);
//

                    String utilitiesRateMonthly = accountManager.getUtilityRate(account, "Utilities");
                    double utilitiesBillMonthly = accountManager.getUtilityRateAdjusted(account, "Utilities", utilitiesRateMonthly, false) * 30;
                    String utilitiesBillStringMonthly = formatDouble(utilitiesBillMonthly);
                    utilitiesBillTextView.setText(utilitiesBillStringMonthly);
                    double utilitiesPercentMonthly = Double.parseDouble(getPercentage(utilitiesBillMonthly, income));
                    utilitiesPercentageTextView.setText(String.valueOf(utilitiesPercentMonthly));
                    Log.i("accounts", utilitiesBillStringMonthly);
//

                    String houseRateMonthly = accountManager.getUtilityRate(account, "House");
                    double houseBillMonthly = accountManager.getUtilityRateAdjusted(account, "House", houseRateMonthly, false) * 30;
                    String houseBillStringMonthly = formatDouble(houseBillMonthly);
                    double housePercentMonthly = Double.parseDouble(getPercentage(houseBillMonthly, income));
                    housePercentageTextView.setText(String.valueOf(housePercentMonthly));
                    houseBillTextView.setText(String.valueOf(houseBillMonthly));
                    Log.i("accounts", houseBillStringMonthly);

                    String carRateMonthly = accountManager.getUtilityRate(account, "Car");
                    double carBillMonthly = accountManager.getUtilityRateAdjusted(account, "Car", carRateMonthly, false) * 30;
                    String carBillStringMonthly = formatDouble(carBillMonthly);
                    double carPercentMonthly = Double.parseDouble(getPercentage(carBillMonthly, income));
                    carPercentageTextView.setText(String.valueOf(carPercentMonthly));
                    carPaymentTextView.setText(carBillStringMonthly);
                    Log.i("accounts", carBillStringMonthly);

                    double groceriesBillMonthly = accountManager.getRemoveFundsCategory(account, "Groceries");
                    String groceriesBillStringMonthly = formatDouble(groceriesBillMonthly);
                    double groceriesPercentMonthly = Double.parseDouble(getPercentage(groceriesBillMonthly, income));
                    groceriesPercentageTextView.setText(String.valueOf(groceriesPercentMonthly));
                    groceriesTextView.setText(groceriesBillStringMonthly);
                    Log.i("accounts", groceriesBillStringMonthly);


                    String customRateMonthly = accountManager.getUtilityRate(account, "Custom");
                    double customBillMonthly = accountManager.getUtilityRateAdjusted(account, "Custom", customRateMonthly, true) * 30;
                    String customBillStringMonthly = formatDouble(customBillMonthly);
                    if (customBillStringMonthly != null) {
                        // Proceed with using customBillStringMonthly
                    } else {
                        // Handle the case where customBillStringMonthly is null
                        Log.e("GraphsDisplayActivity", "customBillStringMonthly is null");
                    }
                    double customPercentMonthly = Double.parseDouble(getPercentage(customBillMonthly, income));
                    customPercentageTextView.setText(String.valueOf(customPercentMonthly));
                    customBillTextView.setText(customBillStringMonthly);
                    Log.i("accounts", customBillStringMonthly);

                    // Set the percentages for each category
                    utilitiesPercentage.setText(String.valueOf(utilitiesPercentMonthly));
                    housePercentage.setText(String.valueOf(housePercentMonthly));
                    customPercentage.setText(String.valueOf(customPercentMonthly));
                    personalPercentage.setText(String.valueOf(personalPercentMonthly));
                    groceriesPercentage.setText(String.valueOf(groceriesPercentMonthly));
                    balancePercentage.setText(String.valueOf(balancePercentMonthly));
                    carPercentage.setText(String.valueOf(carPercentMonthly));

                    // Add pie slices for monthly data
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Utilities",
                                    (int) Double.parseDouble(utilitiesPercentage.getText().toString()),
                                    Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "House",
                                    (int) Double.parseDouble(housePercentage.getText().toString()),
                                    Color.parseColor("#66BB6A")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Custom",
                                    (int) Double.parseDouble(customPercentage.getText().toString()),
                                    Color.parseColor("#EF5350")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Personal",
                                    (int) Double.parseDouble(personalPercentage.getText().toString()),
                                    Color.parseColor("#29B6F6")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Groceries",
                                    (int) Double.parseDouble(groceriesPercentage.getText().toString()),
                                    Color.parseColor("#800080")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Balance",
                                    (int) Double.parseDouble(balancePercentage.getText().toString()),
                                    Color.parseColor("#fb7268")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Car",
                                    (int) Double.parseDouble(carPercentage.getText().toString()),
                                    Color.parseColor("#00008B")));
                    // To animate the pie chart
                    pieChart.startAnimation();
                    break;
                case "yearly":
                    double balanceValueYearly = accountManager.getBalanceFromFile(account)* 12;
                    balanceTextView.setText(String.valueOf(balanceValueYearly));
                    double balancePercentYearly = Double.parseDouble(getPercentage(balanceValueYearly, income));
                    if (balancePercentageTextView != null) {
                        balancePercentageTextView.setText(String.valueOf(balancePercentYearly));
                    } else {
                        Log.e("GraphsDisplayActivity", "balancePercentageTextView is null");
                    }
                    //balanceTextView.setText(String.valueOf(balanceValueYearly));
                    double personalBillYearly = accountManager.getRemoveFundsCategory(account, "Personal");
                    String personalBillStringYearly = formatDouble(personalBillYearly);
                    double personalPercentYearly = Double.parseDouble(getPercentage(personalBillYearly, income));
                    personalPercentageTextView.setText(String.valueOf(personalPercentYearly));
                    personalBillTextView.setText(personalBillStringYearly);
                    Log.i("accounts", personalBillStringYearly);


                    String utilitiesRateYearly = accountManager.getUtilityRate(account, "Utilities");
                    double utilitiesBillYearly = accountManager.getUtilityRateAdjusted(account, "Utilities", utilitiesRateYearly, false) * 365;
                    String utilitiesBillStringYearly = formatDouble(utilitiesBillYearly);
                    utilitiesBillTextView.setText(utilitiesBillStringYearly);
                    double utilitiesPercentYearly = Double.parseDouble(getPercentage(utilitiesBillYearly, income));
                    utilitiesPercentageTextView.setText(String.valueOf(utilitiesPercentYearly));
                    Log.i("accounts", utilitiesBillStringYearly);


                    String houseRateYearly = accountManager.getUtilityRate(account, "House");
                    double houseBillYearly = accountManager.getUtilityRateAdjusted(account, "House", houseRateYearly, false) * 365;
                    String houseBillStringYearly = formatDouble(houseBillYearly);
                    double housePercentYearly = Double.parseDouble(getPercentage(houseBillYearly, income));
                    housePercentageTextView.setText(String.valueOf(housePercentYearly));
                    houseBillTextView.setText(String.valueOf(houseBillYearly));
                    Log.i("accounts", houseBillStringYearly);

                    String carRateYearly = accountManager.getUtilityRate(account, "Car");
                    double carBillYearly = accountManager.getUtilityRateAdjusted(account, "Car", carRateYearly, false) * 365;
                    String carBillStringYearly = formatDouble(carBillYearly);
                    double carPercentYearly = Double.parseDouble(getPercentage(carBillYearly, income));
                    carPercentageTextView.setText(String.valueOf(carPercentYearly));
                    carPaymentTextView.setText(carBillStringYearly);
                    Log.i("accounts", carBillStringYearly);

                    double groceriesBillYearly = accountManager.getRemoveFundsCategory(account, "Groceries");
                    String groceriesBillStringYearly = formatDouble(groceriesBillYearly);
                    double groceriesPercentYearly = Double.parseDouble(getPercentage(groceriesBillYearly, income));
                    groceriesPercentageTextView.setText(String.valueOf(groceriesPercentYearly));
                    groceriesTextView.setText(groceriesBillStringYearly);
                    Log.i("accounts", groceriesBillStringYearly);



                    String customRateYearly = accountManager.getUtilityRate(account, "Custom");
                    double customBillYearly = accountManager.getUtilityRateAdjusted(account, "Custom", customRateYearly, true) * 365;
                    String customBillStringYearly = formatDouble(customBillYearly);
                    if (customBillStringYearly != null) {
                        // Proceed with using customBillStringYearly
                    } else {
                        // Handle the case where customBillStringYearly is null
                        Log.e("GraphsDisplayActivity", "customBillStringYearly is null");
                    }
                    double customPercentYearly = Double.parseDouble(getPercentage(customBillYearly, income));
                    customPercentageTextView.setText(String.valueOf(customPercentYearly));
                    customBillTextView.setText(customBillStringYearly);
                    Log.i("accounts", customBillStringYearly);

                    // Set the percentages for each category
                    utilitiesPercentage.setText(String.valueOf(utilitiesPercentYearly));
                    housePercentage.setText(String.valueOf(housePercentYearly));
                    customPercentage.setText(String.valueOf(customPercentYearly));
                    personalPercentage.setText(String.valueOf(personalPercentYearly));
                    groceriesPercentage.setText(String.valueOf(groceriesPercentYearly));
                    balancePercentage.setText(String.valueOf(balancePercentYearly));
                    carPercentage.setText(String.valueOf(carPercentYearly));

                    // Add pie slices for yearly data
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Utilities",
                                    (int) Double.parseDouble(utilitiesPercentage.getText().toString()),
                                    Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "House",
                                    (int) Double.parseDouble(housePercentage.getText().toString()),
                                    Color.parseColor("#66BB6A")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Custom",
                                    (int) Double.parseDouble(customPercentage.getText().toString()),
                                    Color.parseColor("#EF5350")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Personal",
                                    (int) Double.parseDouble(personalPercentage.getText().toString()),
                                    Color.parseColor("#29B6F6")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Groceries",
                                    (int) Double.parseDouble(groceriesPercentage.getText().toString()),
                                    Color.parseColor("#800080")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Balance",
                                    (int) Double.parseDouble(balancePercentage.getText().toString()),
                                    Color.parseColor("#fb7268")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    "Car",
                                    (int) Double.parseDouble(carPercentage.getText().toString()),
                                    Color.parseColor("#00008B")));
                    // To animate the pie chart
                    pieChart.startAnimation();
                    break;

            }
        }

        homeButton = findViewById(R.id.homeButton);
        backButton = findViewById(R.id.backButton);


        homeButton.setOnClickListener(v -> {
            launchHomeActivity();
        });

        backButton.setOnClickListener(v -> onBackPressed());

        pieChart = findViewById(R.id.piechart);
    }
    public String getPercentage(double value, double income) {
        // Handle potential division by zero
        if (income == 0) {
            return String.valueOf(0.0); // Or return a specific value to indicate no budget
        }
        // Calculate the percentage without the % symbol
        double percentage = (value * 100) / income;
        return String.valueOf(Double.parseDouble(String.format("%.2f", percentage))); // Return with 2 decimal places
    }

    private void launchHomeActivity( ) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private String formatDouble(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP); // Optional: Adjust rounding mode as needed
        return df.format(value);
    } }
