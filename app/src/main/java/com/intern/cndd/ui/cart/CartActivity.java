package com.intern.cndd.ui.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.intern.cndd.R;
import com.intern.cndd.model.Orders;
import com.intern.cndd.model.Products;
import com.intern.cndd.prevalent.Prevalent;
import com.intern.cndd.ui.profile.ProfileActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private List<Products> mProductsList;
    private TextView mCostTextView;
    private RecyclerView mProductsRecyclerView;
    private CartAdapter mCartAdapter;
    private DatabaseReference ProductRef;
    private Button mBuyButton;
    private AlertDialog mAlertDialog;
    private AlertDialog.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ProductRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
        .child(Prevalent.currentOnlineUser.getId()).child("Products");

        mCostTextView = findViewById(R.id.costTextView);
        mBuyButton = findViewById(R.id.buyButton);

        mProductsList = new ArrayList<>();
        mProductsRecyclerView = findViewById(R.id.productsRecyclerView);
        mProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCartAdapter = new CartAdapter(this, mProductsList);
        mProductsRecyclerView.setAdapter(mCartAdapter);

        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(getResources().getString(R.string.buy))
                .setMessage(R.string.confirm_buy)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        buyProduct();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        mAlertDialog = mBuilder.create();
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

        mBuyButton.setOnClickListener(v -> mAlertDialog.show());

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

    public void buyProduct() {
        if (Prevalent.currentOnlineUser.getName().isEmpty() || Prevalent.currentOnlineUser.getAddress().isEmpty() ||
                Prevalent.currentOnlineUser.getPhone().isEmpty()) {
            startActivity(new Intent(CartActivity.this, ProfileActivity.class));
            Toast.makeText(this, "Please update your information", Toast.LENGTH_SHORT).show();
        } else {
            final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
            final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View")
                    .child(Prevalent.currentOnlineUser.getId()).child("Products");

            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(Prevalent.currentOnlineUser.getId()).exists()) {
                        Toast.makeText(CartActivity.this, "Your order is being shipped, Please order after receiving the goods", Toast.LENGTH_SHORT).show();
                    } else {
                        List<Products> productsList = new ArrayList<>();
                        for (Products products : mProductsList) {
                            if (!products.getTotal().equals("0")) {
                                productsList.add(products);
                            }
                        }

                        String saveCurrentDate, saveCurrentTime;

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                        saveCurrentDate = currentDate.format(calendar.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                        saveCurrentTime = currentTime.format(calendar.getTime());

                        cartRef.setValue(productsList).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(CartActivity.this, "Upload successfully", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Orders orders = new Orders(Prevalent.currentOnlineUser.getId(), Prevalent.currentOnlineUser.getName(),
                                Prevalent.currentOnlineUser.getPhone(), Prevalent.currentOnlineUser.getAddress(),
                                "No shipped", saveCurrentDate, saveCurrentTime, mCostTextView.getText().toString().substring(1));

                        orderRef.child(Prevalent.currentOnlineUser.getId()).                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(CartActivity.this, "Add order successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


}