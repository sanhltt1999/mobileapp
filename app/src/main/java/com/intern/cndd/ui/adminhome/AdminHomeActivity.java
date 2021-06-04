package com.intern.cndd.ui.adminhome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.intern.cndd.R;
import com.intern.cndd.prevalent.Prevalent;

public class AdminHomeActivity extends AppCompatActivity {

    public static final String ACCOUNT_KEY = "acount";

    private ImageView mDesignImageView;
    private ImageView mItemImageView;
    private Button mMaintainButton;
    private Button mCheckButton;
    private Button mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        mDesignImageView = findViewById(R.id.designImageView);
        mItemImageView = findViewById(R.id.itemImageView);
        mMaintainButton = findViewById(R.id.maintainButton);
        mCheckButton = findViewById(R.id.checkButton);
        mLogout = findViewById(R.id.checkButton);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Prevalent.currentOnlineUser = null;
            }
        });

        mDesignImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}