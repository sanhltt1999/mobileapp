package com.intern.cndd.ui.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.intern.cndd.R;
import com.intern.cndd.model.Products;
import com.intern.cndd.prevalent.Prevalent;
import com.intern.cndd.ui.home.HomeActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    private ImageView mProductImageView;
    private TextView mNameProduct;
    private TextView mStarTextView;
    private TextView mTotalStarTextView;
    private TextView mCostTextView;
    private TextView mTotalProductTextView;
    private Button mBuyButton;
    private Button mAddButton;
    private ImageView mSubImageView;
    private ImageView mPlusImageView;
    private TextView mCountTextView;

    private Products mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
        setContentView(R.layout.activity_detail);

        mProductImageView = findViewById(R.id.productImageView);
        mNameProduct = findViewById(R.id.nameProductTextView);
        mStarTextView = findViewById(R.id.staffTextView);
        mTotalStarTextView = findViewById(R.id.totalStarTextView);
        mCostTextView = findViewById(R.id.totalCostTextView);
        mTotalProductTextView = findViewById(R.id.totalStarTextView);
        mBuyButton = findViewById(R.id.buyButton);
        mAddButton = findViewById(R.id.addButton);
        mSubImageView = findViewById(R.id.subImageView);
        mPlusImageView = findViewById(R.id.plusImageView);
        mCountTextView = findViewById(R.id.countTextView);

        mProducts = (Products) getIntent().getExtras().getSerializable(HomeActivity.PRODUCT_KEY);

        Glide.with(this)
                .load(mProducts.getImage())
                .centerCrop()
                .into(mProductImageView);

        mNameProduct.setText(mProducts.getName());
        mStarTextView.setText(mProducts.getStar());
        mTotalStarTextView.setText(mProducts.getTotal());
        mCostTextView.setText("$" + mProducts.getPrice());
        mTotalProductTextView.setText(mProducts.getTotal());
        mCountTextView.setText("1");

        mPlusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(mCountTextView.getText().toString().trim());
                if (count > 0) {
                    mCountTextView.setText((count - 1) + "");
                    mCostTextView.setText("$" + (count - 1)*(Integer.parseInt(mProducts.getPrice())) + "");
                }
            }
        });

        mSubImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(mCountTextView.getText().toString().trim());
                mCountTextView.setText((count + 1) + "");
                mCostTextView.setText("$" + (count + 1)*(Integer.parseInt(mProducts.getPrice())) + "");
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToCartList();
            }
        });

    }

    public void addProductToCartList() {

        String saveCurrentDate, saveCurrentTime;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();

        int count = Integer.parseInt(mCountTextView.getText().toString().trim());

        cartMap.put("name", mNameProduct.getText().toString());
        cartMap.put("price", count*(Integer.parseInt(mProducts.getPrice())) + "");
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("total", mCountTextView.getText().toString());
        cartMap.put("id", mProducts.getId());
        cartMap.put("star", mProducts.getStar());
        cartMap.put("image", mProducts.getImage());
        cartMap.put("category", mProducts.getCategory());
        cartMap.put("description", mProducts.getDescription());

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(mProducts.getId())
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(mProducts.getId())
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(DetailActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
                                                startActivity(intent);

                                            }
                                        }
                                    });
                        }
                    }
                });

    }
}