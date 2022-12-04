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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class AppRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private FirebaseFirestore firebaseFirestore;
    private String currUser;
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
//                        Log.d(TAG, String.valueOf(isVendor));
                        if(task.isSuccessful()){

                            if(isVendor == false){
                                currUser = firebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = firebaseFirestore.collection("users").document(currUser);
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

                                currUser = firebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = firebaseFirestore.collection("vendors").document(currUser);
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


}
