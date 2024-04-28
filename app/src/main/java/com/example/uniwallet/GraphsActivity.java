package com.example.uniwallet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.widget.Button;
        import android.widget.TextView;
//import android.widget.Toast;
        import androidx.appcompat.app.AppCompatActivity;
        import org.eazegraph.lib.charts.PieChart;
        import org.eazegraph.lib.models.PieModel;
        import com.example.uniwallet.model.Account;
        import com.example.uniwallet.model.AccountManager;


/*
public class GraphsActivity extends AppCompatActivity {
    // Create the object of TextView
    // and PieChart class
    Account account;
    Button homeButton, backButton;
    TextView rent, carPayment, personal, utilities, budget, balance, rentPercentage, carPercentage, utilPercentage, personalPercentage;
    PieChart pieChart;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);




        Intent intent = getIntent();
            if (intent != null && intent.hasExtra("account")) {
                account = (Account) intent.getSerializableExtra("account");
            }

        homeButton = (Button) findViewById(R.id.homeButton);
        backButton = (Button) findViewById(R.id.backButton);


        backButton.setOnClickListener(v -> {
                launchHomeActivity(account);
            });

            homeButton.setOnClickListener(v -> onBackPressed());
            // Link those objects with their
            // respective id's that
            // we have given in .XML file
            rent = findViewById(R.id.rentView);
            carPayment = findViewById(R.id.carPayView);
            personal = findViewById(R.id.personalView);
            utilities = findViewById(R.id.utilitiesView);
            budget = findViewById(R.id.budgetView);
            balance = findViewById(R.id.remainingBudgetView);
            pieChart = findViewById(R.id.piechart);
            rentPercentage = findViewById((R.id.rentPercentage));
            carPercentage = findViewById((R.id.carPercentage));
            utilPercentage = findViewById((R.id.utilPercentage));
            personalPercentage = findViewById((R.id.personalPercentage));

            // Creating a method setData()
            // to set the text in text view and pie chart
            setData();
        }




    }
    private void setData ()
    {

        // Set the percentage of language used
        rentPercentage.setText(Integer.toString(25));
        carPercentage.setText(Integer.toString(25));
        utilPercentage.setText(Integer.toString(25));
        personalPercentage.setText(Integer.toString(25));
        rent.setText(Integer.toString(60));
        carPayment.setText(Integer.toString(10));
        utilities.setText(Integer.toString(5));
        personal.setText(Integer.toString(25));
        //totalBudget.setText(Integer.toString(2500)); (test)
        String budgetString = budget.getText().toString();
        double newBudget = Double.parseDouble(budgetString);
        newBudget = account.getBudget();

        //budget = accountManager.getBudgetFromFile(account);
        //remainingBudget.setText(Integer.toString(100)); (test)
        //balance = accountManager.getBalanceFromFile(account);
        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "Rent",
                        Integer.parseInt(rentPercentage.getText().toString()),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Car payment",
                        Integer.parseInt(carPercentage.getText().toString()),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "Utilities",
                        Integer.parseInt(utilPercentage.getText().toString()),
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "Personal",
                        Integer.parseInt(personalPercentage.getText().toString()),
                        Color.parseColor("#29B6F6")));


    }
}
*/
