package com.intern.cndd.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.intern.cndd.R;
import com.intern.cndd.model.Products;
import com.intern.cndd.prevalent.Prevalent;
import com.intern.cndd.ui.cart.CartActivity;
import com.intern.cndd.ui.detail.DetailActivity;
import com.intern.cndd.ui.profile.ProfileActivity;
import com.intern.cndd.ui.shipping.ShippingActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public static final String PRODUCT_KEY = "product";

    private ShapeableImageView mAvatarImageView;
    private TextView mTitleTextView;
    private ImageView mHomeImageView;
    private ImageView mCartImageView;
    private ImageView mShipImageView;
    private ImageView mProfileImageView;
    private ImageView mItemDecorationImageView;
    private ImageView mItemDesignImageView;
    private EditText mSearchEditText;
    private RecyclerView mProductsRecyclerView;
    private ProductsAdapter mProductsAdapter;
    private List<Products> mProducts = new ArrayList<>();


    private DatabaseReference ProductRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        mAvatarImageView = findViewById(R.id.avatarHomeImageView);
        mTitleTextView = findViewById(R.id.titleTextView);

        mHomeImageView = findViewById(R.id.homeImageView);
        mCartImageView = findViewById(R.id.cartImageView);
        mShipImageView = findViewById(R.id.shipImageView);
        mProfileImageView = findViewById(R.id.profileImageView);
        mProductsRecyclerView = findViewById(R.id.productsRecyclerView);
        mItemDesignImageView = findViewById(R.id.itemDesignImageView);
        mItemDecorationImageView = findViewById(R.id.itemDecorationImageView);
        mSearchEditText = findViewById(R.id.searchEditText);

        mProductsRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL    ));
        mProductsAdapter = new ProductsAdapter(this, mProducts);
        mProductsRecyclerView.setAdapter(mProductsAdapter);

        mTitleTextView.setText("Hi, " + Prevalent.currentOnlineUser.getName() + "!");

        Glide.with(this)
                .load(Prevalent.currentOnlineUser.getImage())
                .override(60, 60)
                .centerCrop()
                .into(mAvatarImageView);

        mHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveProductInfoToDatabase();
            }
        });


        loadProduct("design");

        mItemDesignImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProduct("design");
                mItemDesignImageView.setBackgroundResource(R.drawable.gradient_bg_select);
                mItemDecorationImageView.setBackgroundResource(R.drawable.gradient_bg_type_home_no_select);
            }
        });

        mItemDecorationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProduct("item");
                mItemDesignImageView.setBackgroundResource(R.drawable.gradient_bg_type_home_no_select);
                mItemDecorationImageView.setBackgroundResource(R.drawable.gradient_bg_select);
            }
        });

        mProductsAdapter.setOnListener((products, position) -> {
            Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
            intent.putExtra(PRODUCT_KEY, products);
            startActivity(intent);
        });

        mProfileImageView.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        mShipImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ShippingActivity.class));
            }
        });

        mCartImageView.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void loadProduct(String type) {
        ProductRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProducts.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Products product = postSnapshot.getValue(Products.class);
                    if (product.getCategory().equals(type)) {
                        mProducts.add(product);
                    }
                }
                mProductsAdapter.setProducts(mProducts);
                mProductsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SaveProductInfoToDatabase() {

        String saveCurrentDate, saveCurrentTime, productRandomKey;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("id", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", "Den cua Sanh v2");
        productMap.put("category", "item");
        productMap.put("price", "5");
        productMap.put("name", "Den v2");
        productMap.put("image", "https://i.pinimg.com/236x/60/c3/f3/60c3f33fcd50e2695db40db6f1fca974.jpg");
        productMap.put("total", "10");
        productMap.put("star", "4.5");

        DatabaseReference ProductRef;
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");


        ProductRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(HomeActivity.this, "Product is added successfully...", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(HomeActivity.this, "Error " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void filter(String newText) {
        List<Products> products = new ArrayList<>();

        for (Products item : mProducts) {
            if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                products.add(item);
            }
        }

        if (products.isEmpty()) {
        } else {
            mProductsAdapter.filterList(products);
        }
    }

}