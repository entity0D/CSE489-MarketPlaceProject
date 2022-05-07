package com.example.marketplace;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link descfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class descfragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String Product_Name;
    String Product_Price;
    String Products_Picture_URL;
    String Product_ID;
    Button buyButton;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference customerRef;
    DatabaseReference reference;
    private ProgressDialog progressDialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public descfragment() {

        // Required empty public constructor
    }

    public descfragment(String Product_Name, String Product_Price, String Products_Picture_URL, String Product_ID) {
        this.Product_Name = Product_Name;
        this.Product_Price = Product_Price;
        this.Product_ID = Product_ID;
        this.Products_Picture_URL = Products_Picture_URL;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment descfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static descfragment newInstance(String param1, String param2) {
        descfragment fragment = new descfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_descfragment, container, false);

        ImageView productImageHolder = view.findViewById(R.id.imagegholder);
        TextView productNameHolder = view.findViewById(R.id.nameholder);
        TextView productPriceHolder = view.findViewById(R.id.priceHolder);
        TextView productIDHolder = view.findViewById(R.id.idHolder);

        buyButton = view.findViewById(R.id.btn_buyProduct);
        progressDialog = new ProgressDialog(getContext());
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Buying.......");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                delete(Product_ID);
            }
        });


        productNameHolder.setText(Product_Name);
        productPriceHolder.setText(Product_Price);
        productIDHolder.setText(Product_ID);
        Glide.with(getContext()).load(Products_Picture_URL).into(productImageHolder);

        return view;
    }

    private void delete(String username) {


        reference = FirebaseDatabase.getInstance().getReference("Products");
        reference.child(username).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    System.out.println("Done");
                    System.out.println(Product_Name);
                    String currentUserId = mAuth.getCurrentUser().getUid();
                    customerRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Products_Purchased").child(Product_ID);
                    HashMap customer = new HashMap();
                    customer.put("Purchased By", currentUserId);
                    customer.put("Product_Name", Product_Name);
                    customer.put("Product_Price", Product_Price);

                    customerRef.updateChildren(customer).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Product Purchased Successfully", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                            progressDialog.dismiss();

                        }
                    });


                } else {


                }

            }
        });
    }

    public void onBackPressed() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recfragment()).addToBackStack(null).commit();

    }
}