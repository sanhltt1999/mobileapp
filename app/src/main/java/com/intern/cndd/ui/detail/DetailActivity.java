package com.intern.cndd.ui.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intern.cndd.R;
import com.intern.cndd.model.Products;
import com.intern.cndd.prevalent.Prevalent;
import com.intern.cndd.ui.home.HomeActivity;

public class DetailActivity extends AppCompatActivity {

    private ImageView mProductImageView;
    private TextView mNameProduct;
    private TextView mStarTextView;
    private TextView mTotalStarTextView;
    private TextView mCostTextView;
    private TextView mTotalProductTextView;
    private Button mBuyButton;
    private Button mAddButton;

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

        mProducts = (Products) getIntent().getExtras().getSerializable(HomeActivity.PRODUCT_KEY);

        Glide.with(this)
                .load(mProducts.getImage())
                .centerCrop()
                .into(mProductImageView);

        mNameProduct.setText(mProducts.getName());
        mStarTextView.setText(mProducts.getStar());
        mTotalStarTextView.setText(mProducts.getTotal());
        mCostTextView.setText(mProducts.getPrice());
        mTotalProductTextView.setText("1");

    }
}