package com.example.ezev.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.ezev.model.AppRepository;

import java.io.PushbackReader;
import java.util.HashMap;
import java.util.Map;

public class VendorHomeViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private String userId;

    public VendorHomeViewModel(@NonNull Application application) {
        super(application);
        this.appRepository = new AppRepository(application);
        userId = appRepository.getUserDetails();
    }

    public  String getUserDetails1() {
        return userId;
    }
}
