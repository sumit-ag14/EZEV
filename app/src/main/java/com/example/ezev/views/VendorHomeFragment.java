package com.example.ezev.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ezev.R;
import com.example.ezev.viewmodel.LoginRegisterViewModel;
import com.example.ezev.viewmodel.VendorHomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class VendorHomeFragment extends Fragment {
    private static final String TAG = "prak" ;
    public static  String userId;
    private VendorHomeViewModel vendorHomeViewModel;
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private Button deleteButton;
    private FirebaseFirestore firebaseFirestore;
    private Button updateButton;
    private Spinner chargerTypeSpinner;
    private RadioButton avaiabilityRadio;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         vendorHomeViewModel = new ViewModelProvider(this).get(VendorHomeViewModel.class);
         vendorHomeViewModel.getMutableLiveData().observe(this, new Observer<FirebaseUser>() {
             @Override
             public void onChanged(FirebaseUser firebaseUser) {

                 if(firebaseUser!=null){
                     System.out.println(firebaseUser);
                 }
             }
         });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_vendor_home, container, false);
        nameTextView =  view.findViewById(R.id.textView5);
        emailTextView = view.findViewById(R.id.textView6);
        phoneTextView = view.findViewById(R.id.textView4);
        deleteButton = view.findViewById(R.id.buttonDeRegister);
        updateButton =  view.findViewById(R.id.buttonUpdate);
        chargerTypeSpinner = view.findViewById(R.id.spinnerChargerType);
        avaiabilityRadio = view.findViewById(R.id.radioButton);


        firebaseFirestore  = FirebaseFirestore.getInstance();
        System.out.println(userId);
        DocumentReference docRef = firebaseFirestore.collection("vendors").document(userId);
//        System.out.println(docRef);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " );
                        Object [] data = document.getData().values().toArray();
                        nameTextView.setText((String)data[0]);
                        phoneTextView.setText((String)data[1]);
                        emailTextView.setText((String) data[2]);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    docRef
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
                Navigation.findNavController(getView()).navigate(R.id.action_vendorHomeFragment_to_loginRegisterFragment);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> data = new HashMap<>();
                data.put("charger_type",chargerTypeSpinner.getSelectedItem().toString() );
                if(avaiabilityRadio.isChecked()){
                    data.put("avaiability",(boolean)true);
                }
                else{
                    data.put("avaiability",(boolean)false);
                }
                docRef.collection("details")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });



//
        return view;
    }
}