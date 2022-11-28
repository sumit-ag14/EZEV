package com.example.ezev.views;

import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VendorHomeFragment extends Fragment {
    private static final String TAG = "prak" ;
    public static  String userId;
    private VendorHomeViewModel vendorHomeViewModel;
    private EditText nameTextView;
    private EditText phoneTextView;
    private EditText emailTextView;
    private Button deleteButton;
    private FirebaseFirestore firebaseFirestore;
    private Button updateButton;
    private Spinner chargerTypeSpinner;
    private Switch avaiabilityRadio;
    private EditText priceEditText;
    private EditText addressEditText;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_vendor_home, container, false);
        nameTextView =  view.findViewById(R.id.nameEdit);
//        emailTextView = view.findViewById(R.id.);
        phoneTextView = view.findViewById(R.id.phoneEdit);
        deleteButton = view.findViewById(R.id.buttonDeRegister);
        updateButton =  view.findViewById(R.id.buttonUpdate);
        chargerTypeSpinner = view.findViewById(R.id.spinnerChargerType);
        avaiabilityRadio = view.findViewById(R.id.switch3);
        priceEditText = view.findViewById(R.id.priceEdit);
         addressEditText =view.findViewById(R.id.addressEdit);


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
                        Log.d(TAG, "DocumentSnapshot data: ");
                        Object [] data = document.getData().values().toArray();
                        nameTextView.setText((String)data[0]);

                        phoneTextView.setText((String)data[1]);
//                        emailTextView.setText((String) data[2]);

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
                data.put("price",Integer.parseInt(priceEditText.getText().toString()));
                if(!avaiabilityRadio.isChecked()){
                    data.put("avaiability",(boolean)true);
                }
                else{
                    data.put("avaiability",(boolean)false);
                }
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    List<Address> results = geocoder.getFromLocationName(addressEditText.getText().toString(),1);
                    GeoPoint geoPoint = new GeoPoint(results.get(0).getLatitude(),results.get(0).getLongitude());
                    data.put("loc",geoPoint);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                docRef
                        .update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("detail");
                            }
                        });
            }
        });



//
        return view;
    }
}