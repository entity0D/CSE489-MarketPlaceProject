package com.example.marketplace;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Feed extends AppCompatActivity {

    private DatabaseReference adRef;
    private FirebaseAuth mAuth;

    private ListView AdListView;
    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private ArrayList<Ads> ads = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mAuth = FirebaseAuth.getInstance();
        AdListView = findViewById(R.id.AdListView);

    }



    @Override
    protected void onResume() {
        super.onResume();

        adRef = FirebaseDatabase.getInstance().getReference().child("Ad");
        adRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ads.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Ads ad1 = postSnapshot.getValue(Ads.class);
                    ads.add(postSnapshot.getValue(Ads.class));
/*                    if (!ad1.getCreated_by().toString().equals(currentUserId)) {

                    }*/
                }


                AdAdapter AdAdapter = new AdAdapter(Feed.this, R.layout.ad_adapter_view, ads);
                AdListView.setAdapter(AdAdapter);

//                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
