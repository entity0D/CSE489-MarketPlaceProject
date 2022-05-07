package com.example.marketplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdAdapter extends ArrayAdapter<Ads> {


    private static final String TAG = "AdListAdapter";
    private Context mContext;
    int mResource;

    public AdAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ads> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String id = getItem(position).getProduct_ID();
        String name = getItem(position).getProduct_Name();
        String purl = getItem(position).getProducts_Picture_URL();
        int price = getItem(position).getProduct_Price();




        Ads ads = new Ads(id,   price,  name, purl);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView showid = (TextView) convertView.findViewById(R.id.showID);
        TextView showname = (TextView) convertView.findViewById(R.id.nametext);
        TextView showprice = (TextView) convertView.findViewById(R.id.emailtext);
        ImageView showimg = (ImageView)  convertView.findViewById(R.id.img1);



        showid.setText(id);
        showname.setText(name);
        showprice.setText(price +"/=");
        Glide.with(getContext()).load(purl).into(showimg);

        return convertView;
    }

}