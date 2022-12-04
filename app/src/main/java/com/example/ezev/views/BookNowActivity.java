package com.example.ezev.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.ezev.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.PaymentResultListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookNowActivity extends AppCompatActivity implements PaymentResultListener  {
    public static final String EXTRA_Name="name";

    @Override
    public void onPaymentSuccess(String s) {
//            System.out.println("testign payment");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        CollectionReference det = db.collection("users").document(BookNowFragment.userId).collection("transactions");
        Date date = new Date();

        Timestamp ts =new Timestamp(new Date());
        data.put("vendor_name",BookNowFragment.vendor_name);
        data.put("price",BookNowFragment.price);
        data.put("date",ts);

        System.out.println("payment success ful");

        det.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent=getIntent();
//        String uid=intent.getStringExtra(EXTRA_Name);
        setContentView(R.layout.activity_book_now);
//        BookNowFragment fragment=
//                (BookNowFragment) getSupportFragmentManager().findFragmentById(R.id.book_now_frag);
//        Button book =  findViewById(R.id.button2);
//        fragment.setUID(uid);



    }

}
