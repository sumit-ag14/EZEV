package com.example.ezev.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ezev.model.AppRepository;
import com.example.ezev.model.AppRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private MutableLiveData<FirebaseUser> mutableLiveData;
    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);
        this.appRepository = new AppRepository(application);
        mutableLiveData = appRepository.getUserMutableLiveData();
    }

    public void login(String email,String password){
        appRepository.login(email,password);
    }
    public void register(String email,String password){
        appRepository.register(email,password);

    }

    public MutableLiveData<FirebaseUser> getMutableLiveData() {
        return mutableLiveData;
    }
}
