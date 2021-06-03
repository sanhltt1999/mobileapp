package com.intern.cndd.ui.adminhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.intern.cndd.R;
import com.intern.cndd.prevalent.Prevalent;
import com.intern.cndd.ui.adminproduct.AdminProductActivity;

public class AdminHomeActivity extends AppCompatActivity {

    public static final String CATEGORY_KEY = "category";

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
        mLogout = findViewById(R.id.logoutButton);

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
                Intent intent = new Intent(AdminHomeActivity.this, AdminProductActivity.class);
                intent.putExtra(CATEGORY_KEY, "design");
                startActivity(intent);
            }
        });

        mItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminProductActivity.class);
                intent.putExtra(CATEGORY_KEY, "item");
                startActivity(intent);
            }
        });

        mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}