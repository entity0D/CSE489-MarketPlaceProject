package com.example.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button logoutButton, addProductButton, showAllProductButton,searchProductButton;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav_view;

    private CircleImageView nav_profile_image;
    private TextView nav_fullname, nav_email, nav_type;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;


    DataSnapshot Psnapshot;
    String name,email,imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_home);




        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Options");

        nav_view = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(AccountHome.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_closed);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        nav_profile_image = nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_fullname = nav_view.getHeaderView(0).findViewById(R.id.nav_user_name);
        nav_email = nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);


        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mAuth = FirebaseAuth.getInstance();

        showAllProductButton = findViewById(R.id.btn_showProduct);
        addProductButton = findViewById(R.id.btn_addProduct);


        showAllProductButton.setVisibility(View.INVISIBLE);
        addProductButton.setVisibility(View.INVISIBLE);

       String currentUserId = mAuth.getCurrentUser().getUid();
        System.out.println("=============================");


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference pp = ref.child("Users").child(currentUserId).child("Name");

        pp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                if(name.trim().equals("admin"))
                {
                    showAllProductButton.setVisibility(View.INVISIBLE);
                    addProductButton.setVisibility(View.VISIBLE);
                    System.out.println("=======add======= "+name);
                }
                else
                {
                    showAllProductButton.setVisibility(View.VISIBLE);
                    addProductButton.setVisibility(View.INVISIBLE);
                    System.out.println("========show======== "+name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name = snapshot.child("Name").getValue().toString();
                    nav_fullname.setText(name);

                    email = snapshot.child("Email").getValue().toString();
                    nav_email.setText(email);

                    imageUrl = snapshot.child("Profile_Picture_URL").getValue().toString();
                    Glide.with(getBaseContext()).load(imageUrl).into(nav_profile_image);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountHome.this, Add_Product.class);
                startActivity(i);

            }
        });

        showAllProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountHome.this, Show_Products.class);
                startActivity(i);
            }
        });



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                Intent i = new Intent(AccountHome.this, MainActivity.class);
                startActivity(i);
                mAuth.signOut();
                finish();
        }

        return false;
    }
}