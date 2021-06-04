package com.intern.cndd.ui.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.intern.cndd.R;
import com.intern.cndd.model.Products;
import com.intern.cndd.prevalent.Prevalent;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private List<Products> mProductsList;
    private TextView mCostTextView;
    private RecyclerView mProductsRecyclerView;
    private CartAdapter mCartAdapter;
    private DatabaseReference ProductRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ProductRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
        .child(Prevalent.currentOnlineUser.getId()).child("Products");

        mCostTextView = findViewById(R.id.costTextView);

        mProductsList = new ArrayList<>();
        mProductsRecyclerView = findViewById(R.id.productsRecyclerView);
        mProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCartAdapter = new CartAdapter(this, mProductsList);
        mProductsRecyclerView.setAdapter(mCartAdapter);

        loadProduct();

        mCartAdapter.setOnListener(new CartAdapter.OnListener() {
            @Override
            public void onClick(Products products, int position, int count) {

            }

            @Override
            public void resetCount(int count) {
                mCostTextView.setText("$" + count);
            }
        });

    }

    public void loadProduct() {
        ProductRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProductsList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Products product = postSnapshot.getValue(Products.class);
                    mProductsList.add(product);
                }

                mCartAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}