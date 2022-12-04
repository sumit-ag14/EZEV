package com.example.ezev.views;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ezev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookNowFragment extends Fragment  {
    public static String userId;

    String pn;
    String email;
    Double latitude;
    Double longitude;
   static String vendor_name;
   static  Long price;

    static String  vid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("bf",vid);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_book_now,container,false);
        TextView nameTextView =  view.findViewById(R.id.textView9);
        Button bookNow = (Button)view.findViewById(R.id.button2);
        TextView plugTextView = view.findViewById(R.id.textViewPlug);
        TextView costTextView = view.findViewById(R.id.textViewCost);
        TextView phoneTextView = view.findViewById(R.id.textViewPhone);
        TextView locationTextView = view.findViewById(R.id.textViewLoc);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docref = db.collection("users").document(userId);
                docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        nameTextView.setText(document.getString("full_name"));

                        email = document.getString("email");
                        pn = document.getString("phone_number");
                    } else {
                        Log.d("test", "No such document");
                    }
                } else {
                    Log.d("test", "get failed with ", task.getException());
                }
            }
        });
        DocumentReference docref1 = db.collection("vendors").document(vid);
        docref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        plugTextView.setText(document.getString("charger_type"));
                        phoneTextView.setText(document.getString("phone_number"));
                        GeoPoint test = (GeoPoint)document.get("loc");
                        longitude = test.getLongitude();
                        latitude = test.getLatitude();
                        Geocoder geocoder = new Geocoder(getContext());

                        List<Address> addresses = null;
                        try {
                            GeoPoint geo = (GeoPoint) document.get("loc");
                            addresses = geocoder.getFromLocation(geo.getLatitude(),
                                    geo.getLongitude(), 1);
                            String address = addresses.get(0).getAddressLine(0);
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String zip = addresses.get(0).getPostalCode();
                            String country = addresses.get(0).getCountryName();
                            locationTextView.setText(address + " " + city + " " + state + " " + country);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        costTextView.setText(document.get("price").toString());
                        price = (Long) document.get("price");
                        vendor_name = document.getString("full_name");
                    }
                        else {
                        Log.d("test", "No such document");
                    }
                } else {
                    Log.d("test", "get failed with ", task.getException());
                }
            }
        });


        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Checkout co = new Checkout();
                JSONObject obj = new JSONObject();
                try {
                    obj.put("name",nameTextView.getText());
                    obj.put("currency","INR");
                    obj.put("amount",price);

                    JSONObject prefill = new JSONObject();
                    prefill.put("contact",pn);
                    prefill.put("email",email);
                    obj.put("prefill",prefill);

                    co.open(getActivity(),obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
        ImageButton dir =(ImageButton) view.findViewById(R.id.imagebuttonDirections);
        dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude+","+longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

        return view;

    }



//    @Override
//    public void onPaymentSuccess(String s) {
////        Toast.makeText(getContext(),"payment sucess",Toast.LENGTH_SHORT).show();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String, Object> data = new HashMap<>();
//        CollectionReference det = db.collection("users").document(userId).collection("transactions");
//        Date date = new Date();
////        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
////        String strDate= formatter.format(date);
////        Timestamp ts=new Timestamp(date.getTime());
//        data.put("name","prakhar");
//        System.out.println("payment success ful");
//
//        det.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//
//            }
//        });
//    }

//    @Override
//    public void onPaymentError(int i, String s) {
//        Toast.makeText(getContext(),"payment failed",Toast.LENGTH_SHORT).show();
//    }

}
