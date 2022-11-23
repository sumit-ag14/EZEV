package com.example.ezev.model;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AppRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private Map<String,String> userDetails;
    private FirebaseFirestore firebaseFirestore;
    private String userId;
    private static final String TAG = "success";
    public AppRepository(Application application) {
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        userMutableLiveData = new MutableLiveData<>();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }
    public void login(String email,String password,boolean isVendor){
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                        }
                        else{
                            Toast.makeText(application, "sign in fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void register(String email, String password,String name,String phoneNumber,boolean isVendor){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, String.valueOf(isVendor));
                        if(task.isSuccessful()){

                            if(isVendor == false){
                                userId = firebaseAuth.getCurrentUser().getUid();

                                DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);
                                HashMap<String,Object> hash = new HashMap<>();
                                hash.put("full_name",name);
                                hash.put("phone_number",phoneNumber);
                                hash.put("email",email);

                                documentReference.set(hash).addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.i(TAG, "onSuccess: ");
                                            }
                                        }
                                );
                            }
                            else{

                                userId = firebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = firebaseFirestore.collection("vendors").document(userId);
                                HashMap<String,Object> hash = new HashMap<>();
                                hash.put("full_name",name);
                                hash.put("phone_number",phoneNumber);
                                hash.put("email",email);
                                userDetails = new HashMap<>();
                                userDetails.put("full_name",name);
                                userDetails.put("phone_number",phoneNumber);
                                userDetails.put("email",email);
                                documentReference.set(hash).addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.i(TAG, "onSuccess: ");
                                            }
                                        }
                                );

                            }
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                        }
                        else{
                            Toast.makeText(application, "registration fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public String getUserDetails() {
        return userId;
    }
}
