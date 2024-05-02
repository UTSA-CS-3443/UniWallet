package com.example.uniwallet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/**
 * This activity serves as the home screen of the application.
 * It provides options for signing up or logging in.
 */
public class HomeActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Initialize buttons
        Button signup_button = findViewById(R.id.signup_btn);
        Button login_button = findViewById(R.id.login_btn);
        // Set click listeners for buttons
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignupActivity();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });
    }
    /**
     * Launches the sign-up activity.
     */
   private void launchSignupActivity( ) {
      Intent intent = new Intent(this, SignUpActivity.class);
      startActivity(intent);
  }

    /**
     * Launches the login activity.
     */
    private void launchLoginActivity( ) {
       Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
