package com.example.uniwallet;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


        import android.content.Intent;
        import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
        import android.widget.TextView;
//import android.widget.Toast;
        import org.eazegraph.lib.charts.PieChart;
        import org.eazegraph.lib.models.PieModel;
        import com.example.uniwallet.model.Account;
import com.example.uniwallet.model.AccountManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class GraphsDisplayActivity extends AppCompatActivity {
    // Create the object of TextView
    // and PieChart class
    Account account;
    String dataType;
    Button homeButton, backButton;
    TextView waterBillTextView, electricalBillTextView, carPaymentTextView,
             budget, balance,
            carPercentage, electricalPercentage, waterPercentage, houseBillTextView,
            housePercentage, customBillTextView, customPercentage, personalBillTextView,
            personalPercentage, luxuryBillTextView, luxuryPercentage, foodBillTextView,
            foodPercentage, groceriesBillTextView, groceriesPercentage;
    PieChart pieChart;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs_display);



        AccountManager accountManager = new AccountManager(GraphsDisplayActivity.this);

        Intent intent = getIntent();
            if (intent != null && intent.hasExtra("account")) {
                account = (Account) intent.getSerializableExtra("account");
                dataType = intent.getStringExtra("dataType");
                assert account != null;


                    // Customize the display based on the dataType
                    switch (dataType) {
                            case "weekly":
                                waterBillTextView = findViewById(R.id.waterBillView);
                                String waterRate = accountManager.getUtilityRate(account, "Water");
                                double waterBill = accountManager.getUtilityRateAdjusted(account, "Water", waterRate, false) * 7;
                                String waterBillString = formatDouble(waterBill);
                                waterBillTextView.setText(waterBillString);
                                Log.i("accounts", waterBillString);

                                electricalBillTextView = findViewById(R.id.electricalBillView);
                                String electricalRate = accountManager.getUtilityRate(account, "Electrical");
                                double electricalBill = accountManager.getUtilityRateAdjusted(account, "Electrical", electricalRate, false) * 7;
                                String electricalBillString = formatDouble(electricalBill);
                                electricalBillTextView.setText(electricalBillString);
                                Log.i("accounts", electricalBillString);

                                houseBillTextView = findViewById(R.id.houseBillView);
                                String houseRate = accountManager.getUtilityRate(account, "House");
                                double houseBill = accountManager.getUtilityRateAdjusted(account, "House", houseRate, false) * 7;
                                String houseBillString = formatDouble(houseBill);
                                houseBillTextView.setText(houseBillString);
                                Log.i("accounts", houseBillString);

                                carPaymentTextView = findViewById(R.id.carPaymentView);
                                String carRate = accountManager.getUtilityRate(account, "Car");
                                double carBill = accountManager.getUtilityRateAdjusted(account, "Car", carRate, false) * 7;
                                String carBillString = formatDouble(carBill);
                                carPaymentTextView.setText(carBillString);
                                Log.i("accounts", carBillString);

                                customBillTextView = findViewById(R.id.customBillView);
                                String customRate = accountManager.getUtilityRate(account, "Custom");
                                double customBill = accountManager.getUtilityRateAdjusted(account, "Custom", customRate, true) * 7;
                                String customBillString = formatDouble(customBill);
                                customBillTextView.setText(customBillString);
                                Log.i("accounts", customBillString);
                                    // Display weekly data
                                    break;
                            case "monthly":
                                waterBillTextView = findViewById(R.id.waterBillView);
                                String waterRateMonthly = accountManager.getUtilityRate(account, "Water");
                                double waterBillMonthly = accountManager.getUtilityRateAdjusted(account, "Water", waterRateMonthly, false) * 30;
                                String waterBillStringMonthly = formatDouble(waterBillMonthly);
                                waterBillTextView.setText(waterBillStringMonthly);
                                Log.i("accounts", waterBillStringMonthly);

                                electricalBillTextView = findViewById(R.id.electricalBillView);
                                String electricalRateMonthly = accountManager.getUtilityRate(account, "Electrical");
                                double electricalBillMonthly = accountManager.getUtilityRateAdjusted(account, "Electrical", electricalRateMonthly, false) * 30;
                                String electricalBillStringMonthly = formatDouble(electricalBillMonthly);
                                electricalBillTextView.setText(electricalBillStringMonthly);
                                Log.i("accounts", electricalBillStringMonthly);

                                houseBillTextView = findViewById(R.id.houseBillView);
                                String houseRateMonthly = accountManager.getUtilityRate(account, "House");
                                double houseBillMonthly = accountManager.getUtilityRateAdjusted(account, "House", houseRateMonthly, false) * 30;
                                String houseBillStringMonthly = formatDouble(houseBillMonthly);
                                houseBillTextView.setText(houseBillStringMonthly);
                                Log.i("accounts", houseBillStringMonthly);

                                carPaymentTextView = findViewById(R.id.carPaymentView);
                                String carRateMonthly = accountManager.getUtilityRate(account, "Car");
                                double carBillMonthly = accountManager.getUtilityRateAdjusted(account, "Car", carRateMonthly, false) * 30;
                                String carBillStringMonthly = formatDouble(carBillMonthly);
                                carPaymentTextView.setText(carBillStringMonthly);
                                Log.i("accounts", carBillStringMonthly);

                                customBillTextView = findViewById(R.id.customBillView);
                                String customRateMonthly = accountManager.getUtilityRate(account, "Custom");
                                double customBillMonthly = accountManager.getUtilityRateAdjusted(account, "Custom", customRateMonthly, true) * 30;
                                String customBillStringMonthly = formatDouble(customBillMonthly);
                                customBillTextView.setText(customBillStringMonthly);
                                    // Display monthly data
                                    break;
                            case "yearly":
                                waterBillTextView = findViewById(R.id.waterBillView);
                                String waterRateYearly = accountManager.getUtilityRate(account, "Water");
                                double waterBillYearly = accountManager.getUtilityRateAdjusted(account, "Water", waterRateYearly, false) * 365;
                                String waterBillStringYearly = formatDouble(waterBillYearly);
                                waterBillTextView.setText(waterBillStringYearly);
                                Log.i("accounts", waterBillStringYearly);

                                electricalBillTextView = findViewById(R.id.electricalBillView);
                                String electricalRateYearly = accountManager.getUtilityRate(account, "Electrical");
                                double electricalBillYearly = accountManager.getUtilityRateAdjusted(account, "Electrical", electricalRateYearly, false) * 365;
                                String electricalBillStringYearly = formatDouble(electricalBillYearly);
                                electricalBillTextView.setText(electricalBillStringYearly);
                                Log.i("accounts", electricalBillStringYearly);

                                houseBillTextView = findViewById(R.id.houseBillView);
                                String houseRateYearly = accountManager.getUtilityRate(account, "House");
                                double houseBillYearly = accountManager.getUtilityRateAdjusted(account, "House", houseRateYearly, false) * 365;
                                String houseBillStringYearly = formatDouble(houseBillYearly);
                                houseBillTextView.setText(houseBillStringYearly);
                                Log.i("accounts", houseBillStringYearly);

                                carPaymentTextView = findViewById(R.id.carPaymentView);
                                String carRateYearly = accountManager.getUtilityRate(account, "Car");
                                double carBillYearly = accountManager.getUtilityRateAdjusted(account, "Car", carRateYearly, false) * 365;
                                String carBillStringYearly = formatDouble(carBillYearly);
                                carPaymentTextView.setText(carBillStringYearly);
                                Log.i("accounts", carBillStringYearly);

                                customBillTextView = findViewById(R.id.customBillView);
                                String customRateYearly = accountManager.getUtilityRate(account, "Custom");
                                double customBillYearly = accountManager.getUtilityRateAdjusted(account, "Custom", customRateYearly, true) * 365;
                                String customBillStringYearly = formatDouble(customBillYearly);
                                customBillTextView.setText(customBillStringYearly);
                                    // Display yearly data
                                    break;
                            default:
                                    // Handle unknown dataType
                                    break;
                    }
            }

        homeButton = (Button) findViewById(R.id.homeButton);



        homeButton.setOnClickListener(v -> {
            launchMainActivity(account);
            });

            homeButton.setOnClickListener(v -> onBackPressed());
            // Link those objects with their
            // respective id's that
            // we have given in .XML file



        foodBillTextView = findViewById(R.id.foodBillView);
        double foodBill = accountManager.getRemoveFundsCategory(account, "Food");
        String foodBillString = String.valueOf(foodBill);
        foodBillTextView.setText(foodBillString);

        Log.i("accounts", foodBillString);

        groceriesBillTextView = findViewById(R.id.groceriesBillView);
        double groceriesBill = accountManager.getRemoveFundsCategory(account, "Groceries");
        String groceriesBillString = String.valueOf(groceriesBill);
        groceriesBillTextView.setText(groceriesBillString);

        Log.i("accounts", groceriesBillString);

        personalBillTextView = findViewById(R.id.personalView);
        double personalBill = accountManager.getRemoveFundsCategory(account, "Personal");
        String personalBillString = String.valueOf(personalBill);
        personalBillTextView.setText(personalBillString);

        Log.i("accounts", personalBillString);

        luxuryBillTextView = findViewById(R.id.luxuryBillView);
        double luxuryBill = accountManager.getRemoveFundsCategory(account, "Luxury");
        String luxuryBillString = String.valueOf(luxuryBill);
        luxuryBillTextView.setText(luxuryBillString);

        Log.i("accounts", luxuryBillString);

        /*
        paycheckTextView = findViewById(R.id.paycheckTextView);
        double paycheck = accountManager.getPaycheckFromFile(account);
        String paycheckString = String.valueOf(paycheck);
        paycheckTextView.setText(paycheckString);

        savingsTextView = findViewById(R.id.savingsTextView);
        double savings = accountManager.getSavingsFromFile(account);
        String savingsString = String.valueOf(savings);
        savingsTextView.setText(savingsString);
*/
        //Generate budget (Add goes into budget)
        //savings




        budget = findViewById(R.id.budgetView);
        //balance = findViewById(R.id.remainingBudgetView);
        pieChart = findViewById(R.id.pieChart);
        carPercentage = findViewById((R.id.carPercentageValueView));
        waterPercentage = findViewById((R.id.waterPercentageValueView));
        electricalPercentage = findViewById((R.id.electricalPercentageValueView));
        housePercentage = findViewById(R.id.housePercentageValueView);
        //String houseString = housePercentage.getText().toString();
        customPercentage = findViewById(R.id.customPercentageValueView);
        foodPercentage = findViewById(R.id.foodPercentageValueView);
        groceriesPercentage = findViewById(R.id.groceriesPercentageValueView);
        personalPercentage = findViewById((R.id.personalPercentageValueView));
        luxuryPercentage = findViewById(R.id.luxuryPercentageValueView);


  // Creating a method setData()
            // to set the text in text view and pie chart
            setData();
        }


    private void setData ()
    {


        // Set the percentage of language used
        waterPercentage.setText(Integer.toString(25));
        electricalPercentage.setText(Integer.toString(25));
        housePercentage.setText(Integer.toString(25));
        carPercentage.setText(Integer.toString(25));
        customPercentage.setText(Integer.toString(25));
        foodPercentage.setText(Integer.toString(25));
        groceriesPercentage.setText(Integer.toString(25));
        personalPercentage.setText(Integer.toString(25));
        luxuryPercentage.setText(Integer.toString(25));

       // rent.setText(Integer.toString(60));
       // carPayment.setText(Integer.toString(10));
       // utilities.setText(Integer.toString(5));
      //  personal.setText(Integer.toString(25));
        //totalBudget.setText(Integer.toString(2500)); (test)
        //String budgetString = budget.getText().toString();
       // double newBudget = Double.parseDouble(budgetString);
       // newBudget = account.getBudget();

        //budget = accountManager.getBudgetFromFile(account);
        //remainingBudget.setText(Integer.toString(100)); (test)
        //balance = accountManager.getBalanceFromFile(account);
        // Set the data and color to the pie chart
        pieChart.setInnerPadding(10);
        pieChart.setLegendTextSize(15);
        pieChart.addPieSlice(
                new PieModel(
                        "House bill",
                        Integer.parseInt(housePercentage.getText().toString()),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Car payment",
                        Integer.parseInt(carPercentage.getText().toString()),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "Utilities",
                        Integer.parseInt(waterPercentage.getText().toString()),
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "Personal",
                        Integer.parseInt(personalPercentage.getText().toString()),
                        Color.parseColor("#29B6F6")));
        pieChart.addPieSlice(
                new PieModel(


                )
        );

        pieChart.startAnimation();

    }
    private void launchMainActivity(Account account){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
       private String formatDouble(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP); // Optional: Adjust rounding mode as needed
        return df.format(value);
    }
}


