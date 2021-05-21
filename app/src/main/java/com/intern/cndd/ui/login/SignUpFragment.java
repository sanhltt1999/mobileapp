package com.intern.cndd.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.intern.cndd.R;

import java.util.HashMap;

public class SignUpFragment extends Fragment {

    private EditText mPhoneEditText;
    private EditText mPassEditText;
    private EditText mConfirmEditText;
    private Button mSignUpButton;
    private ProgressDialog loadingBar;
    private callAnotherFragment mOnListener;

    public void setOnListener(callAnotherFragment onListener) {
        mOnListener = onListener;
    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mOnListener = (callAnotherFragment) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhoneEditText = view.findViewById(R.id.nameEditText);
        mPassEditText = view.findViewById(R.id.passEditText);
        mConfirmEditText = view.findViewById(R.id.confirmEditText);
        mSignUpButton = view.findViewById(R.id.signUpButton);
        loadingBar = new ProgressDialog(getActivity());

        mSignUpButton.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        String name = mPhoneEditText.getText().toString().trim();
        String pass = mPassEditText.getText().toString().trim();
        String confirm = mConfirmEditText.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Name!", Toast.LENGTH_SHORT).show();
        } else if (pass.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter pass!", Toast.LENGTH_SHORT).show();
        } else if (confirm.isEmpty()) {
            Toast.makeText(getActivity(), "Please confirm pass!", Toast.LENGTH_SHORT).show();
        } else {
            if (!pass.equals(confirm)) {
                Toast.makeText(getActivity(), "pass is wrong", Toast.LENGTH_SHORT).show();
            } else {
                loadingBar.setTitle("Create Account");
                loadingBar.setMessage("Please wait, while we are checking.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                validatePhoneNumber(name, pass);
            }
        }

    }

    private void validatePhoneNumber (String phone, String pass){
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phone).exists())) {
                    HashMap<String, Object> user = new HashMap<>();
                    user.put("phone", phone);
                    user.put("password", pass);
                    user.put("name", "");
                    user.put("address", "");
                    user.put("picture", "");

                    rootRef.child("Users").child(phone).updateChildren(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Congratulation, your account has been created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        mOnListener.callBack();
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(getActivity(), "This " + phone + " already exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(getActivity(), "Please try again using another phone number", Toast.LENGTH_SHORT).show();
                    mOnListener.callBack();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface callAnotherFragment {
        void callBack();
    }

}