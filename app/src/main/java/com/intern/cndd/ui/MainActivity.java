package com.intern.cndd.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.intern.cndd.R;
import com.intern.cndd.ui.login.LoginFragment;
import com.intern.cndd.ui.login.SignUpFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ColorStateList def;
    TextView mLoginTextView;
    TextView mSignUpTextView;
    TextView mSelectTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mSelectTextView = findViewById(R.id.select);
        def = mSignUpTextView.getTextColors();

        getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new LoginFragment()).commit();

    }

    private void init() {

        mLoginTextView = findViewById(R.id.item1);
        mSignUpTextView = findViewById(R.id.item2);
        mLoginTextView.setOnClickListener(this);
        mSignUpTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item1){
            mSelectTextView.animate().x(0).setDuration(100);
            mLoginTextView.setTextColor(Color.WHITE);
            mSignUpTextView.setTextColor(def);
            getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new LoginFragment()).commit();
        } else if (view.getId() == R.id.item2){
            mLoginTextView.setTextColor(def);
            mSignUpTextView.setTextColor(Color.WHITE);
            int size = mSignUpTextView.getWidth();
            mSelectTextView.animate().x(size).setDuration(100);
            getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new SignUpFragment()).commit();
        }
    }
}