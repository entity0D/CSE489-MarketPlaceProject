package com.example.marketplace;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class myadapter extends FirebaseRecyclerAdapter<model, myadapter.myviewholder> {


    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final model model) {
        holder.nametext.setText(model.getProduct_Name());
        holder.emailtext.setText(model.getProduct_Price());
        holder.idtext.setText(model.getProduct_ID());
        Glide.with(holder.img1.getContext()).load(model.getProducts_Picture_URL()).into(holder.img1);

        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new descfragment(model.getProduct_Name(), model.getProduct_Price(), model.getProducts_Picture_URL(), model.getProduct_ID())).addToBackStack(null).commit();
            }
        });


    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single_row_design, parent, false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView img1;
        TextView nametext, emailtext, idtext;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.img1);
            nametext = itemView.findViewById(R.id.nametext);
            emailtext = itemView.findViewById(R.id.emailtext);
            idtext = itemView.findViewById(R.id.showID);

        }
    }

}
