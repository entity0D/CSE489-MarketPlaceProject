package com.example.marketplace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Add_Product extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private boolean imageUploaded = false;

    private Button addProductButton, showAllProductButton;
    private EditText enterProductName, enterPrice;
    private ImageView showImage, addImage;
    private Uri imageUri;

    private DatabaseReference userReference;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    //Calculation
    private int accountBalance = 0;
    private int productQuantity = 1;

    private int productPrice = 0;

    private boolean exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        addImage = findViewById(R.id.img_addImage);
        addProductButton = findViewById(R.id.btn_addProduct);
        showAllProductButton = findViewById(R.id.btn_showProduct);
        showImage = findViewById(R.id.img_showImage);

        enterProductName = findViewById(R.id.et_productName);
        enterPrice = findViewById(R.id.et_price);

        firebaseAuth = FirebaseAuth.getInstance();

        //button activation

        //opens gallery
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });


        //add product press
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uuid = UUID.randomUUID().toString().replace("-", "");//generates product id
                /*System.out.println("uuid = " + uuid);*/

                String productName = enterProductName.getText().toString().trim();

                try {
                    productPrice = Integer.parseInt(enterPrice.getText().toString());
                } catch (Exception e) {
                    productPrice = 0;
                }


                String currentUserId = firebaseAuth.getCurrentUser().getUid();//gets current user ID
                userReference = FirebaseDatabase.getInstance().getReference().child("Products");

                progressDialog = new ProgressDialog(Add_Product.this);

                if (TextUtils.isEmpty(productName)) {
                    enterProductName.setError("Enter Product Name");
                    return;
                } else if (productPrice == 0) {
                    enterPrice.setError("Enter Price");
                    return;
                }


                progressDialog.setMessage("Adding Product");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


                if (imageUploaded) {
                    HashMap userInfo = new HashMap();
                    userInfo.put("Product_ID", uuid);


                    userReference = FirebaseDatabase.getInstance().getReference().child("Products").child(uuid);//changed


                    userInfo.put("Added By", currentUserId);
                    userInfo.put("Product_Name", productName);
                    userInfo.put("Product_Price", productPrice);
                    userInfo.put("Product_Quantity", productQuantity);

                    userReference.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Add_Product.this, "Added Product Info", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Add_Product.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                firebaseAuth.getInstance().signOut();
                            }
                            /*finish();*/
                            if (imageUri != null) {
                                final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("User Product Images").child(currentUserId);
                                Bitmap bitmap = null;

                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), imageUri);
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
                                        Toast.makeText(Add_Product.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
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
                                                    newImageMap.put("Products_Picture_URL/", imageUrl);

                                                    userReference.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(Add_Product.this, "Added Image To Database", Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(Add_Product.this, "Product Added", Toast.LENGTH_SHORT).show();


                                                            } else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(Add_Product.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    });


                                                }
                                            });
                                        }

                                    }
                                });


                            }


                        }
                    });
                } else {
                    Toast.makeText(Add_Product.this, "Product Image koi!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }


            }
        });



    }


    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            System.out.println("--------------------------" + imageUri);
            Picasso.with(this).load(imageUri).into(showImage);
            if (imageUri != null) {
                imageUploaded = true;
            }
        }
    }
}