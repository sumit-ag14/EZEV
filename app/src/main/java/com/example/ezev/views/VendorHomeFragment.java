package com.example.ezev.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ezev.R;
import com.example.ezev.viewmodel.LoginRegisterViewModel;
import com.example.ezev.viewmodel.VendorHomeViewModel;

import java.util.HashMap;
import java.util.Map;


public class VendorHomeFragment extends Fragment {
    private static final String TAG = "prak" ;
    private VendorHomeViewModel vendorHomeViewModel;
    private EditText nameEditText;
    private String userId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         vendorHomeViewModel = new ViewModelProvider(this).get(VendorHomeViewModel.class);
        userId = vendorHomeViewModel.getUserDetails1();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_vendor_home, container, false);
        nameEditText =  view.findViewById(R.id.textView5);
//        nameEditText.setText(userId);
        Log.d(TAG,userId);
        return view;
    }
}