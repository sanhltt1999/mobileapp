package com.intern.cndd.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.intern.cndd.R;
import com.intern.cndd.model.Users;
import com.intern.cndd.prevalent.Prevalent;
import com.intern.cndd.ui.home.HomeActivity;

public class LoginFragment extends Fragment {

    private EditText mPhoneEditText;
    private EditText mPassEditText;
    private Button mLoginButton;
    private ProgressDialog loadingBar;


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPhoneEditText = view.findViewById(R.id.phoneEditText);
        mPassEditText = view.findViewById(R.id.passEditText);
        mLoginButton = view.findViewById(R.id.loginButton);
        loadingBar = new ProgressDialog(getActivity());

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void loginUser() {

        String phone = mPhoneEditText.getText().toString().trim();
        String pass = mPassEditText.getText().toString().trim();

        if (phone.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter your phone", Toast.LENGTH_SHORT).show();
        } else if (pass.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter your pass work", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            checkLogin(phone, pass);

        }

    }

    private void checkLogin(String phone, String pass) {

        final DatabaseReference roofRef;
        roofRef = FirebaseDatabase.getInstance().getReference();

        roofRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(phone).exists()) {
                    Users user = snapshot.child("Users").child(phone).getValue(Users.class);

                    if (user.getPhone().equals(phone)) {
                        if (user.getPassword().equals(pass)) {
                            Prevalent.currentOnlineUser = user;
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getActivity(), "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Account with this" + phone + " number do not exits", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}