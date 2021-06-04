package com.intern.cndd.ui.detailorder;

<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.intern.cndd.R;

public class DetailOrderActivity extends AppCompatActivity {

=======
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.intern.cndd.R;
import com.intern.cndd.model.Products;

import java.util.ArrayList;
import java.util.List;

public class DetailOrderActivity extends AppCompatActivity {

    private RecyclerView productsList;
    private DatabaseReference cartListRef;
    private DetailOderAdapter mDetailOderAdapter;
    private String userID = "";
    private List<Products> mProducts = new ArrayList<>();

>>>>>>> 2f76b5688ded0286a8f86d81b2b8c7f9de917be5
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
<<<<<<< HEAD
=======

        userID = getIntent().getExtras().getString("uid");

        productsList = findViewById(R.id.products_list);
        productsList.setLayoutManager(new LinearLayoutManager(this));
        mDetailOderAdapter = new DetailOderAdapter(mProducts);
        productsList.setAdapter(mDetailOderAdapter);


        cartListRef = FirebaseDatabase.getInstance().getReference();

        cartListRef.child("Cart List").child("Admin View").child(userID)
                .child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProducts.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Products product = postSnapshot.getValue(Products.class);
                    mProducts.add(product);
                }

                mDetailOderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

>>>>>>> 2f76b5688ded0286a8f86d81b2b8c7f9de917be5
    }
}