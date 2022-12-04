package com.example.ezev.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ezev.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class VendorList extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        SelectListener {

    RecyclerView recyclerView;
    FirebaseFirestore databaseReference;
    VendorAdaptor vendorAdapter;
    ArrayList<VendorDetails> vendordetails_list;
    ArrayList<Double> distance_calculated;
    ArrayList<VendorDetails> sorted_with_distance;
    ArrayList<VendorDetails> sorted_with_price;
    ArrayList<VendorDetails> list_to_be_shown;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_vendor_list);

            recyclerView=findViewById(R.id.vendorList);
            databaseReference= FirebaseFirestore.getInstance();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            spinner=findViewById(R.id.filter);
            ArrayAdapter<CharSequence> filter_adapter=ArrayAdapter.createFromResource(this,R.array.filters, android.R.layout.simple_spinner_dropdown_item);
            filter_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(filter_adapter);
            spinner.setOnItemSelectedListener(this);

            vendordetails_list=new ArrayList<>();
            sorted_with_distance=new ArrayList<>();
            sorted_with_price=new ArrayList<>();
            distance_calculated=new ArrayList<>();
            list_to_be_shown=new ArrayList<>();
            EventChangedListenerDistance();
            //EventChangedListenerPrice();
            //distanceSort();

    }

    private void EventChangedListenerDistance() {
        databaseReference.collection("vendors").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.e("FireStore error", error.getMessage());
                    return;
                }

                for(DocumentChange dc: value.getDocumentChanges()){
                    Log.d("Tawish","hi here");
                    //Log.d("Tawish",dc.getDocument().toObject());
                    String vendor_id=dc.getDocument().getId();
                    Log.d("Taw",dc.getDocument().getId());
                    VendorDetails obj=dc.getDocument().toObject(VendorDetails.class);
                    obj.setVendorId(vendor_id);
                    vendordetails_list.add(obj);
                }

                vendorAdapter.notifyDataSetChanged();
                //Log.d("Tawish", String.valueOf((vendordetails_list.get(0).loc)));
                //Log.d("Tawish",String.valueOf(vendordetails_list.get(1).loc));
                //Log.d("Tawish",String.valueOf(vendordetails_list.get(2).loc));
                //Log.d("Tawish",String.valueOf(vendordetails_list.get(3).loc));
                //Log.d("Tawish",String.valueOf(vendordetails_list.get(4).loc));
                Log.d("Tawish","here");
                Log.d("Tawish",Integer.toString(vendordetails_list.size()));


                //<----------------------------Distance calculation Logic starts------------------->
                for(int i=0;i<vendordetails_list.size();i++){
                    GeoPoint pnt=vendordetails_list.get(i).loc;
                    Double lat,lon;
                    Log.d("Distance", "EventChangedListener: here");
                    lat=pnt.getLatitude();
                    lon=pnt.getLongitude();
                    distance_calculated.add(distance(lat,lon));
                }


                for(int i=0;i<distance_calculated.size();i++){
                    Log.d("Distance", Double.toString(distance_calculated.get(i)));
                }
                //<----------------------------Distance calculation Logic ends------------------->

                //<---------------------------Sorting done here with distance--------------------------->
                TreeMap<Double, VendorDetails> ht1 = new TreeMap<>();
                //int j=20;
                for(int i=0;i<vendordetails_list.size();i++){
                    ht1.put(distance_calculated.get(i),vendordetails_list.get(i));
                    //j--;
                }
                Log.d("Tawish","here");
                for (Map.Entry< Double, VendorDetails> e : ht1.entrySet()) {
                    e.getValue().setDistance(e.getKey());
                    sorted_with_distance.add(e.getValue());
                }
                //<<--------------------------Sorting ends------------------------------->

                vendordetails_list=sorted_with_distance;

                //<---------------------------Sorting done here with distance--------------------------->
                TreeMap<Integer, VendorDetails> ht2 = new TreeMap<>();
                //int j=20;
                for(int i=0;i<vendordetails_list.size();i++){
                    ht2.put(vendordetails_list.get(i).getPrice(),vendordetails_list.get(i));
                    //j--;
                }
                Log.d("Tawish","here");
                for (Map.Entry< Integer, VendorDetails> e : ht2.entrySet()) {
                    sorted_with_price.add(e.getValue());
                    Log.d("Prakhar",e.getValue().getFull_name()+ Double.toString(e.getKey()));
                }
                //<<--------------------------Sorting ends------------------------------->
            }
        });
    }




    private double distance( double lat2, double lon2) {
            double lat1 = 17.544943460119097;
            double lon1 = 78.57181616128523;
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            return (dist);
        }

        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::/
        //::  This function converts decimal degrees to radians             :/

        private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::/
        //::  This function converts radians to decimal degrees             :/
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::/
        private double rad2deg(double rad) {
            return (rad * 180.0 / Math.PI);
        }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String choice=adapterView.getItemAtPosition(i).toString();
        //Toast.makeText(getApplicationContext(),choice,Toast.LENGTH_LONG).show();

        if(choice.equals("Price")){
            list_to_be_shown=sorted_with_price;
            vendorAdapter=new VendorAdaptor(this,list_to_be_shown,this);
            recyclerView.setAdapter(vendorAdapter);
            vendorAdapter.notifyDataSetChanged();
            recyclerView.invalidate();
        }else{
            list_to_be_shown=sorted_with_distance;
            vendorAdapter=new VendorAdaptor(this,list_to_be_shown,this);
            recyclerView.setAdapter(vendorAdapter);
            vendorAdapter.notifyDataSetChanged();
            recyclerView.invalidate();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(VendorDetails vendorDetails) {
        Intent intent=new Intent(VendorList.this,BookNowActivity.class);
        intent.putExtra(BookNowActivity.EXTRA_Name,vendorDetails.getVendorId());
        BookNowFragment.vid = vendorDetails.getVendorId();
        //intent.putExtra(BookNowActivity.EXTRA_Price,vendorDetails.price);
//        intent.putExtra(BookNowActivity.EXTRA_Name,vendorDetails.full_name);
        startActivity(intent);
//    Toast.makeText(this,vendorDetails.full_name,Toast.LENGTH_SHORT).show();
    }
}