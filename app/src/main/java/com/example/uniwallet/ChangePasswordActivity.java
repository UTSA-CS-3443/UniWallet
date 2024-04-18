package com.example.uniwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        //TODO add ids from xml file
        EditText new_password = findViewById(R.id.editText1);
        EditText reenter_new_password = findViewById(R.id.editText2);
        Button submit_button = findViewById(R.id.submitButton);
        Button back_button = findViewById(R.id.backbutton);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSettingsActivity();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = new_password.getText().toString();
                String rNewPass = reenter_new_password.getText().toString();
                if(newPass.equals(rNewPass) && !newPass.equals("")){
                    //TODO add logic to check if new password is different from old password
                    launchSettingsActivity();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Passwords must match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void launchSettingsActivity( ) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
