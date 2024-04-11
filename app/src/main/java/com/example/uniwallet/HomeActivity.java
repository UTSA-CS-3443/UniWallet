package com.example.uniwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button signup_button = findViewById(R.id.signup_btn);
        Button login_button = findViewById(R.id.login_btn);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launchSignupActivity();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launchLoginActivity();
            }
        });
    }

//    private void launchSignupActivity( ) {
//        Intent intent = new Intent(this, SignupActivity.class);
//        startActivity(intent);
//    }
//
//    private void launchLoginActivity( ) {
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//    }
}
