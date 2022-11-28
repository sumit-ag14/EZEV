package com.example.ezev.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ezev.model.AppRepository;
import com.google.firebase.auth.FirebaseUser;

import java.io.PushbackReader;
import java.util.HashMap;
import java.util.Map;

public class VendorHomeViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private MutableLiveData<FirebaseUser> mutableLiveData;

    public VendorHomeViewModel(@NonNull Application application) {
        super(application);
        this.appRepository = new AppRepository(application);
        mutableLiveData = appRepository.getUserMutableLiveData();
    }

    public MutableLiveData<FirebaseUser> getMutableLiveData() {
        return mutableLiveData;
    }
}
