package com.example.marketplace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
    private Button loginButton;
    private Button signupButton;
    private EditText enterEmail, enterPass;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterEmail = findViewById(R.id.et_email);
        enterPass = findViewById(R.id.et_password);
        signupButton = (Button) findViewById(R.id.btn_signup);
        loginButton = (Button) findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Intent i = new Intent(MainActivity.this, AccountHome.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_signup_page();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = enterEmail.getText().toString().trim();
                String password = enterPass.getText().toString().trim();
                System.out.println(email.getClass().getName());
                System.out.println(password.getClass().getName());

                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    enterEmail.setError("Please enter a valid email address");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    enterPass.setError("Email is Required");
                    return;
                } else if (password.length() < 6) {
                    enterPass.setError("Password must be 6");
                    return;
                }


                progressDialog.setMessage("Checking Info");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                //authenticate the user
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, AccountHome.class);
                            startActivity(i);
                            progressDialog.dismiss();
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });


            }
        });
    }


    public void open_signup_page() {
        Intent i = new Intent(MainActivity.this, SignUp.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}

