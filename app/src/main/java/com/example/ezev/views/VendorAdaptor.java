package com.example.ezev.views;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezev.R;

import java.util.ArrayList;

public class VendorAdaptor extends RecyclerView.Adapter<VendorAdaptor.VendorViewHolder> {

    private Context context;
    ArrayList<VendorDetails> vendorDetailsList;

    public VendorAdaptor(Context context, ArrayList<VendorDetails> vendorDetailsList){
        this.context=context;
        this.vendorDetailsList=vendorDetailsList;
    }

    @NonNull
    @Override
    public VendorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.nearby_vendor_layout,parent,false);
        return new VendorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorViewHolder holder, int position) {

        VendorDetails vendorDetails=vendorDetailsList.get(position);
        holder.name.setText(vendorDetails.getFull_name());
        holder.price.setText(Integer.toString(vendorDetails.getPrice()));
        //Log.d("sumit", Boolean.toString(vendorDetails.getAvailability()));
        if(vendorDetails.getAvailability()== true){
            Log.d("sumit", "onBindViewHolder: hn mai idhar hu");
            holder.availablity_non_availibility.setBackgroundColor(Color.GREEN);
        }else{
            Log.d("sumit", "onBindViewHolder: hn mai idhar");
            holder.availablity_non_availibility.setBackgroundColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return vendorDetailsList.size();
    }

    public static class VendorViewHolder extends RecyclerView.ViewHolder{

        TextView name,price;
        Button availablity_non_availibility;

        public VendorViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            //address=itemView.findViewById(R.id.address);
            price=itemView.findViewById(R.id.price);
            availablity_non_availibility=itemView.findViewById(R.id.availabe_notavailable);
        }
    }


}
