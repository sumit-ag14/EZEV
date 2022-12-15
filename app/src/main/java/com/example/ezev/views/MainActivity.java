package com.example.ezev.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.ezev.R;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Toolbar toolbar = findViewById(R.id.mtoolbar);
//        setSupportActionBar(toolbar);
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}