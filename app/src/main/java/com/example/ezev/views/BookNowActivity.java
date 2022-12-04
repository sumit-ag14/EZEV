package com.example.ezev.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ezev.R;

public class BookNowActivity extends AppCompatActivity {
    public static final String EXTRA_Name="name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        String uid=intent.getStringExtra(EXTRA_Name);
        setContentView(R.layout.activity_book_now);
        BookNowFragment fragment=
                (BookNowFragment) getSupportFragmentManager().findFragmentById(R.id.book_now_frag);
        fragment.setUID(uid);
    }
}