package com.example.ezev.views;

import android.app.TimePickerDialog;
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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ezev.R;
import com.example.ezev.viewmodel.LoginRegisterViewModel;
import com.example.ezev.viewmodel.VendorHomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;


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
    private ImageButton pickStartTime;
    private ImageButton pickEndTime;
    private EditText startTime;
    private EditText endTime;
    private Timestamp timestampStart;
    private Timestamp timestampEnd;
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
        startTime= view.findViewById(R.id.editTextStartTime);
       endTime= view.findViewById(R.id.editTextEndTime);

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
//                        Object [] data = Objects.requireNonNull(document.getData()).values().toArray();
//                        System.out.println((String)data[0]);
                        System.out.println(document.getString("full_name"));
                        nameTextView.setText(document.getString("full_name"));
////                        System.out.println(data[0);
                        phoneTextView.setText(document.getString("phone_number"));
//                        emailTextView.setText((String) data[2])
                        if(document.getBoolean("avaiability")!=null){
                            avaiabilityRadio.setChecked(!document.getBoolean("avaiability"));
                        }
                        if(document.get("price")!=null){
                            priceEditText.setText(document.get("price").toString());
                        }
                        if(document.get("loc")!=null){
                            Geocoder geocoder = new Geocoder(getContext());

                            List<Address> addresses  = null;
                            try {
                                GeoPoint geo = (GeoPoint) document.get("loc");
                                addresses = geocoder.getFromLocation(geo.getLatitude(),
                                        geo.getLongitude(), 1);
                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String zip = addresses.get(0).getPostalCode();
                                String country = addresses.get(0).getCountryName();
                                addressEditText.setText(address+" "+city+" "+state+" "+country);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        Log.d(TAG, "No such document");
                        System.out.println(""+userId);
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
                data.put("full_name",nameTextView.getText().toString());
                data.put("phone_number",phoneTextView.getText().toString());
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
                data.put("start_time",timestampStart);
                data.put("end_time",timestampEnd);

                docRef
                        .update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("detail");
                            }
                        });
            }
        });
        pickStartTime=(ImageButton) view.findViewById(R.id.pick_time_button);
        pickEndTime=(ImageButton) view.findViewById(R.id.end_time_button);
        pickStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int hours=calendar.get(Calendar.HOUR_OF_DAY);
                int min=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity(),
                        com.razorpay.R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,i);
                        c.set(Calendar.MINUTE,i1);
                        c.setTimeZone(TimeZone.getDefault());
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd kk:mm");
                        SimpleDateFormat format1=new SimpleDateFormat("kk:mm");
                        String time=format1.format(c.getTime());
                        startTime.setText(time);
                        timestampStart = new Timestamp(c.getTime());
                        System.out.print(timestampStart.toString());
                    }
                },hours,min,false);
                timePickerDialog.show();
            }
        });
        pickEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int hours=calendar.get(Calendar.HOUR_OF_DAY);
                int min=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity(),
                        com.razorpay.R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,i);
                        c.set(Calendar.MINUTE,i1);
                        c.setTimeZone(TimeZone.getDefault());
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd kk:mm");
                        SimpleDateFormat format1=new SimpleDateFormat("kk:mm");
                        String time=format1.format(c.getTime());
                        endTime.setText(time);
                        timestampEnd = new Timestamp(c.getTime());
                        System.out.print(timestampEnd.toString());
                    }
                },hours,min,false);
                timePickerDialog.show();
            }
        });

//
        return view;
    }
}