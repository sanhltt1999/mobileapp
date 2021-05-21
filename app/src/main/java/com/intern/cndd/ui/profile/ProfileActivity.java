package com.intern.cndd.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.intern.cndd.R;
import com.intern.cndd.prevalent.Prevalent;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mAvatarImageView;
    private EditText mNameEditText;
    private EditText mAddressEditText;
    private EditText mPhoneEditText;
    private Button mUpdateButton;

    private DatabaseReference mProductRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAvatarImageView = findViewById(R.id.avatarImageView);
        mNameEditText = findViewById(R.id.nameEditText);
        mAddressEditText = findViewById(R.id.addressEditText);
        mPhoneEditText = findViewById(R.id.phoneEditText);
        mUpdateButton = findViewById(R.id.updateButton);

        Glide.with(this)
                .load(Prevalent.currentOnlineUser.getPicture())
                .centerCrop()
                .into(mAvatarImageView);

        mNameEditText.setText(Prevalent.currentOnlineUser.getName());
        mAddressEditText.setText(Prevalent.currentOnlineUser.getAddress());
        mPhoneEditText.setText(Prevalent.currentOnlineUser.getPhone());

    }
}