package com.example.uniwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uniwallet.model.Account;

public class GraphsActivity extends AppCompatActivity {
    Account account;

    /**
     * Sets up the GraphsActivity UI and handles button clicks.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("account")) {
            account = (Account) intent.getSerializableExtra("account");
        }

        Button weeklyButton = findViewById(R.id.weeklyButton);
        Button monthlyButton = findViewById(R.id.monthlyButton);
        Button yearlyButton = findViewById(R.id.yearlyButton);
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity(account);
            }
        });

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGraphsDisplayActivity("weekly");
            }
        });
        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGraphsDisplayActivity("monthly");
            }
        });
        yearlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGraphsDisplayActivity("yearly");
            }
        });
    }
    /**
     * Launches the MainActivity.
     *
     * @param account The account data to pass to the MainActivity.
     */
    private void launchMainActivity(Account account){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
    /**
     * Launches the GraphsDisplayActivity with the specified data type.
     *
     * @param dataType The type of data to display in the graphs.
     */
    private void launchGraphsDisplayActivity(String dataType){
        Intent intent = new Intent(this, GraphsDisplayActivity.class);
        intent.putExtra("account", account);
        intent.putExtra("dataType", dataType);
        startActivity(intent);
    }
    /**
     * Launches the SettingsActivity.
     *
     * @param account The account data to pass to the SettingsActivity.
     */
    private void launchSettingsActivity(Account account) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
}
