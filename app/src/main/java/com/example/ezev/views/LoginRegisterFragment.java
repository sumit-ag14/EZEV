package com.example.ezev.views;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.ezev.R;
import com.example.ezev.viewmodel.LoginRegisterViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginRegisterFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private EditText phoneNumberEditText;
    private EditText nameEditText;
    private LoginRegisterViewModel loginRegisterViewModel;
    private Switch vendorSwitch;
    private boolean isVendor;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser!=null){
                    if(isVendor == false)
                    Navigation.findNavController(getView()).navigate(R.id.action_loginRegisterFragment_to_loggedInFragment);
                    else if(isVendor == true)
                        Navigation.findNavController(getView()).navigate(R.id.action_loginRegisterFragment_to_vendorHomeFragment);

                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_loginregister,container,false);

        emailEditText = view.findViewById(R.id.fragment_loginregister_email);
        passwordEditText = view.findViewById(R.id.fragment_loginregister_password);
        loginButton = view.findViewById(R.id.fragment_loginregister_login);
        registerButton = view.findViewById(R.id.fragment_loginregister_register);
        phoneNumberEditText = view.findViewById(R.id.fragment_loginregister_phone);
        nameEditText = view.findViewById(R.id.fragment_loginregister_name);
        vendorSwitch = view.findViewById(R.id.switch1);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                isVendor = vendorSwitch.isChecked();

                if (email.length() > 0 && password.length() > 0) {
                    loginRegisterViewModel.login(email, password,isVendor);
                } else {
                    Toast.makeText(getContext(), "Email Address and Password Must Be Entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String name = nameEditText.getText().toString();
                isVendor = vendorSwitch.isChecked();
                if (email.length() > 0 && password.length() > 0 && name.length()>0 && phoneNumber.length()>0) {
                        loginRegisterViewModel.register(email, password,name,phoneNumber,isVendor);
                } else {
                    Toast.makeText(getContext(), "Email Address and Password Must Be Entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
