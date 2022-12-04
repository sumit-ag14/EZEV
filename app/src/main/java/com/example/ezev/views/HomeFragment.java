package com.example.ezev.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.ezev.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view=getView();
//        ImageButton b1=(ImageButton) view.findViewById(R.id.findCharging);
//        ImageButton b2=(ImageButton) view.findViewById(R.id.becomeVender);
//        ImageButton b3=(ImageButton)  view.findViewById(R.id.transactions);
//        ImageButton b4=(ImageButton)  view.findViewById(R.id.aboutUs);
//        View view=getView();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
         ImageButton book = view.findViewById(R.id.findCharging);
         book.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //Goto Book Fragment

//                 Fragment fragment = null;
//                 Class fragmentClass=BookNowFragment.class;
//                 FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                 try {
//                     fragment = (Fragment) fragmentClass.newInstance();
//                 } catch (Exception e) {
//                     e.printStackTrace();
//                 }
//                 fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
//             }
                 Intent intent=new Intent(view.getContext(),VendorList.class);
                 startActivity(intent);

             }
         });


        ImageButton becomeVendor = view.findViewById(R.id.becomeVender);
        becomeVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(view.getContext(),MainActivity.class);
               startActivity(intent);
            }

        });
    // about us
        ImageButton aboutUs = view.findViewById(R.id.aboutUs);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                 Class fragmentClass=About_us.class;
                 FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                 try {
                     fragment = (Fragment) fragmentClass.newInstance();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
             }
        });
        ImageButton transactionBT = view.findViewById(R.id.transactions);
        transactionBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                Class fragmentClass=TransactionList.class;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });
        return view;
    }

}