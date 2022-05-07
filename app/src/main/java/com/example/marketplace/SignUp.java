package com.example.marketplace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

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
    private boolean imageUploaded = false;
    private Button signUp_button;
    private CircleImageView enterImage;
    private EditText enterName, enterEmail, enterPass, enterMobile;
    private TextView backButton;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUp_button = findViewById(R.id.btn_signup_2);

        enterImage = findViewById(R.id.btn_profile_picture);

        enterEmail = findViewById(R.id.et_email);
        enterPass = findViewById(R.id.et_password);
        enterMobile = findViewById(R.id.et_mobile);
        enterName = findViewById(R.id.et_name);

        backButton = findViewById(R.id.txt_backButton);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        //if already logged in
        if (mAuth.getCurrentUser() != null) {
            // doNothing();
        }


        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = enterEmail.getText().toString().trim();
                String password = enterPass.getText().toString().trim();
                String mobile = enterMobile.getText().toString().trim();
                String name = enterName.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    enterName.setError("Enter Full Name");
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    enterEmail.setError("Email is Required");
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    enterEmail.setError("Please enter a valid email address");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    enterPass.setError("Enter Password");
                    return;
                } else if (password.length() < 6) {
                    enterPass.setError("Password must be 6 character");
                    return;
                } else if (TextUtils.isEmpty(mobile)) {
                    enterMobile.setError("Enter Mobile Number");
                    return;
                }

                progressDialog.setMessage("Registering");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if (imageUploaded) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(SignUp.this, "User Created", Toast.LENGTH_SHORT).show();

                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId); //old structure
                                /*userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(name); //new structure*/
                                HashMap userInfo = new HashMap();
                                userInfo.put("User_ID", currentUserId);
                                userInfo.put("Name", name);
                                userInfo.put("Email", email);
                                userInfo.put("Password", password);
                                userInfo.put("Mobile", mobile);


                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(SignUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            mAuth.getInstance().signOut();
                                        }
                                        progressDialog.dismiss();
                                        finish();

                                    }
                                });
                                if (resultUri != null) {
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("User Profile Images").child(currentUserId);
                                    Bitmap bitmap = null;

                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();

                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUp.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("Profile_Picture_URL", imageUrl);

                                                        userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(SignUp.this, "Image URL added to database successfully", Toast.LENGTH_SHORT).show();
                                                                    System.out.println("---------------doing");

                                                                } else {
                                                                    Toast.makeText(SignUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        finish();
                                                    }
                                                });
                                            }

                                        }
                                    });

                                    progressDialog.dismiss();
                                    mAuth.signOut();
                                    Intent i = new Intent(SignUp.this, signup_success.class);
                                    System.out.println("---------------Page opened");
                                    startActivity(i);

                                }

                            }

                        }
                    });
                } else {

                    Toast.makeText(SignUp.this, "Image koi!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;

                }


            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginPage();
            }


        });

        enterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");

                startActivityForResult(i, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            resultUri = data.getData();
            enterImage.setImageURI(resultUri);
            if(resultUri!=null)
            {
                imageUploaded = true;
                System.out.println("Image Selected");
            }
        }
    }

    private void openLoginPage() {
        Intent i = new Intent(SignUp.this, MainActivity.class);
        startActivity(i);
        finishAffinity();
    }

}
