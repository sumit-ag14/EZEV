package com.example.ezev.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ezev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransactionList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionList.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionList newInstance(String param1, String param2) {
        TransactionList fragment = new TransactionList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView recyclerView;
    ArrayList<TransactionDetails> transactionDetailsList;
    FirebaseFirestore db;
    static String userID;
    TransactionAdaptor transactionAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_transactions,container,false);
        recyclerView = view.findViewById(R.id.transactionList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = FirebaseFirestore.getInstance();
        transactionDetailsList=new ArrayList<>();
        transactionAdaptor=new TransactionAdaptor(getContext(),transactionDetailsList);
        EventChangedListener();

        recyclerView.setAdapter(transactionAdaptor);

        return view;
    }

    private void EventChangedListener(){

        CollectionReference coref = db.collection("users").document(userID).collection("transactions");

//        .addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if(error!=null){
//                    Log.e("FireStore error", error.getMessage());
//                    return;
//                }
//                Log.d("transaction", userID);
//                for(DocumentChange dc: value.getDocumentChanges()){
//                    if(dc.getType()== DocumentChange.Type.ADDED){
//                        transactionDetailsList.add(dc.getDocument().toObject(TransactionDetails.class));
//                    }
//                }
//                transactionAdaptor.notifyDataSetChanged();
//            }
//        });
        coref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots){

                        TransactionDetails temp = new TransactionDetails();
                        temp.date = snapshot.getTimestamp("date");
                        temp.price= (long) snapshot.get("price");
                        temp.vendor_name=snapshot.getString("vendor_name");
                        transactionDetailsList.add(temp);
                        System.out.println(snapshot);
                    }
                    transactionAdaptor.notifyDataSetChanged();

                } else {
                    Log.d("tag test", "Cached get failed: ", task.getException());
                }
            }
        });
    }
}
//Log.d("Tawish","hi here");
//Log.d("Tawish",dc.getDocument().toObject());
//String vendor_id=dc.getDocument().getId();
//Log.d("Taw",dc.getDocument().getId());
//TransactionDetails obj=dc.getDocument().toObject(TransactionDetails.class);

//obj.setVendorId(vendor_id);
//Timestamp timestamp=obj.getTimestamp();
//Date ds = timestamp.toDate();
//SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY:MM:DD");
//String trans_time= dateFormat.format(ds);
//obj.setDate(trans_time);