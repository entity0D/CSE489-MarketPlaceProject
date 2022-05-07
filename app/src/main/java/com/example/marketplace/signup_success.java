package com.example.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class signup_success extends AppCompatActivity {


    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_success);
        loginButton = findViewById(R.id.btn_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_login();
            }
        });
    }


    public void open_login() {
        Intent i = new Intent(signup_success.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}