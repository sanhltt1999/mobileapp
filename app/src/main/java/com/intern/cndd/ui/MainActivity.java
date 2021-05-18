package com.intern.cndd.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.intern.cndd.R;
import com.intern.cndd.ui.login.LoginFragment;
import com.intern.cndd.ui.login.SignUpFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ColorStateList def;
    TextView item1;
    TextView item2;
    TextView select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        select = findViewById(R.id.select);
        def = item2.getTextColors();

        getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new LoginFragment()).commit();

    }

    private void init() {

        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item1){
            select.animate().x(0).setDuration(100);
            item1.setTextColor(Color.WHITE);
            item2.setTextColor(def);
            getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new LoginFragment()).commit();
        } else if (view.getId() == R.id.item2){
            item1.setTextColor(def);
            item2.setTextColor(Color.WHITE);
            int size = item2.getWidth();
            select.animate().x(size).setDuration(100);
            getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new SignUpFragment()).commit();
        }
    }
}