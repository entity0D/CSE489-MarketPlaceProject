package com.example.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Search extends AppCompatActivity
{
    DatabaseReference reference;

    private ListView AdListView;
    private DatabaseReference adRef;
    private FirebaseAuth mAuth;
    private Button searchbutton;
    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private ArrayList<Ads> ads = new ArrayList<>();

    String SearchTextValue;
    private TextInputEditText SearchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        searchbutton = findViewById(R.id.SearchButton);
        AdListView = findViewById(R.id.AdListView);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SearchText = findViewById(R.id.SearchText);
                SearchTextValue = SearchText.getText().toString();
                adRef = FirebaseDatabase.getInstance().getReference().child("Products");
                adRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ads.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Ads ad1 = postSnapshot.getValue(Ads.class);
                            if(TextUtils.isEmpty(SearchTextValue)){
                                ads.add(postSnapshot.getValue(Ads.class));

                                System.out.println("------------------GG");

                            }
                            else if( ad1.getProduct_Name().toLowerCase().contains(SearchTextValue.toLowerCase())){
                                ads.add(postSnapshot.getValue(Ads.class));
                                System.out.println("---------------GG2");
                            }

                        }

                        AdAdapter AdAdapter = new AdAdapter(Search.this, R.layout.ad_adapter_view, ads);
                        AdListView.setAdapter(AdAdapter);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });




    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Search.this, Show_Products.class);
        startActivity(i);
        finish();
    }





}