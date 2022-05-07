package com.example.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Show_Products extends AppCompatActivity {

    Button refreshButton;
    Button searchProductButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);


        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recfragment()).commit();

        refreshButton = findViewById(R.id.btn_refresh);
        searchProductButton = findViewById(R.id.btn_searchProduct);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        searchProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Show_Products.this, Search.class);
                startActivity(i);
            }
        });

    }
    private void refresh() {
        Intent i = new Intent(Show_Products.this, Show_Products.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Show_Products.this, AccountHome.class);
        startActivity(i);
        finish();
    }





}