package com.example.ezev.views;

import static android.graphics.Color.rgb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezev.R;
import com.google.firebase.Timestamp;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VendorAdaptor extends RecyclerView.Adapter<VendorAdaptor.VendorViewHolder> {

    private Context context;
    ArrayList<VendorDetails> vendorDetailsList;
    private SelectListener listener;
    public VendorAdaptor(Context context, ArrayList<VendorDetails> vendorDetailsList,
                         SelectListener listener){
        this.context=context;
        this.vendorDetailsList=vendorDetailsList;
        this.listener=listener;
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
        holder.disance.setText(Double.toString(vendorDetails.getDistance()).substring(0,7)+" km");
        //Log.d("sumit", Boolean.toString(vendorDetails.getAvailability()));



        //<--------------------------------------Button Color Changer Logic---------------------------------------------->
        Timestamp timestamp=vendorDetails.getStart_time();
        Date ds = timestamp.toDate();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String strTime = dateFormat.format(ds);

        Timestamp timestamp2=vendorDetails.getEnd_time();
        Date ds2 = timestamp2.toDate();
        DateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        String endTime = dateFormat2.format(ds2);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        Log.d("tawstr", strTime);
        String curr_time=dtf.format(now);
        //Timestamp curr_timestamp = new Timestamp(System.currentTimeMillis());
        Log.d("timestr", String.valueOf(timestamp.toDate().getTime()));
        if(curr_time.compareTo(strTime)>=0&&curr_time.compareTo(endTime)<=0){
            if(vendorDetails.isAvaiability()){
                Log.d("sumit", "onBindViewHolder: hn mai idhar hu");
                holder.availablity_non_availibility.setBackgroundColor(rgb(178, 255, 89));
            }else{
                Log.d("sumit", "onBindViewHolder: hn mai idhar");
                holder.availablity_non_availibility.setBackgroundColor(rgb(230,74,25));
            }
            vendorDetails.setWithin_time(true);
        }else{
            vendorDetails.setWithin_time(false);
            holder.availablity_non_availibility.setBackgroundColor(Color.GRAY);
        }

        //<--------------------------------------Button Color Changer Logic----------------------------------------------->



        //<----------------------------------------Map Implementation----------------------------------------------------->
        holder.imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri gmmIntentUri =
                        Uri.parse("google.navigation:q="+vendorDetails.getLoc().getLatitude()+","+vendorDetails.getLoc().getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });
        //<----------------------------------------Map Implementation----------------------------------------------------->


        //<----------------------------------------Address Implementation------------------------------------------------->
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(vendorDetails.getLoc().getLatitude(),
                    vendorDetails.getLoc().getLongitude(), 1);
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String zip = addresses.get(0).getPostalCode();
        String country = addresses.get(0).getCountryName();
        holder.address.setText(address+" "+city+" "+state+" "+country);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //<----------------------------------------Address Implementation------------------------------------------------->
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(vendorDetailsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendorDetailsList.size();
    }

    public static class VendorViewHolder extends RecyclerView.ViewHolder{

        TextView name,price,disance,address;
        Button availablity_non_availibility;
        ImageButton imbtn;
        public CardView cardView;
        public VendorViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            //address=itemView.findViewById(R.id.address);
            price=itemView.findViewById(R.id.price);
            disance=itemView.findViewById(R.id.distance);
            availablity_non_availibility=itemView.findViewById(R.id.availabe_notavailable);
            imbtn=itemView.findViewById(R.id.directions);
            address=itemView.findViewById(R.id.address);
            cardView=itemView.findViewById(R.id.card_view_listing);
        }
    }


}
