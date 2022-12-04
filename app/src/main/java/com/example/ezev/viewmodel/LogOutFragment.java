package com.example.ezev.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ezev.model.AppRepository;
import com.google.firebase.auth.FirebaseUser;

public class LogOutFragment extends AndroidViewModel {
    private AppRepository appRepository;
    private MutableLiveData<FirebaseUser> mutableLiveData;
    private MutableLiveData<Boolean> logOutMutableLiveData;
    public LogOutFragment(@NonNull Application application) {
        super(application);
        this.appRepository = new AppRepository(application);
        mutableLiveData = appRepository.getUserMutableLiveData();
        logOutMutableLiveData = appRepository.getLogOutMutableLiveData();
    }
    public void logOut(){
        appRepository.logOut();
    }
    public MutableLiveData<FirebaseUser> getMutableLiveData() {
        return mutableLiveData;
    }

    public MutableLiveData<Boolean> getLogOutMutableLiveData() {
        return logOutMutableLiveData;
    }
}
