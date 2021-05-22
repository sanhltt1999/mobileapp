package com.intern.cndd.ui.shipping;

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

public class ShippingActivity extends AppCompatActivity {

    public static final String SHIP_KEY = "ship";

    private TextView mEditTextView;
    private TextView mNameTextView;
    private TextView mPhoneTextView;
    private TextView mAddressTextView;
    private TextView mStatusShippingTextView;
    private RecyclerView mProductsRecyclerView;
    private Button mConfirmButton;
    private Orders mOrders;
    private List<Products> mProductsList;
    private ShipAdapter mShipAdapter;
    private AlertDialog mAlertDialog;
    private AlertDialog.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        mEditTextView = findViewById(R.id.editTextView);
        mNameTextView = findViewById(R.id.nameTextView);
        mPhoneTextView = findViewById(R.id.phoneTextView);
        mAddressTextView = findViewById(R.id.addressTextView);
        mProductsRecyclerView = findViewById(R.id.productsRecyclerView);
        mConfirmButton = findViewById(R.id.confirmButton);
        mStatusShippingTextView = findViewById(R.id.statusShippingTextView);

        mProductsList = new ArrayList<>();

        mShipAdapter = new ShipAdapter(this, mProductsList);
        mProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProductsRecyclerView.setAdapter(mShipAdapter);

        loadInformationShipping();

        loadProduct();

        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(getResources().getString(R.string.pay))
                .setMessage(R.string.confirm_pay)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (mOrders.getState().equals("Shipped")) {

                            final DatabaseReference removeProductAdminRef = FirebaseDatabase.getInstance().getReference();
                            final DatabaseReference removeProductUserRef = FirebaseDatabase.getInstance().getReference();
                            final DatabaseReference removeOrderAdminRef = FirebaseDatabase.getInstance().getReference();
                            final DatabaseReference addProductAdminRef = FirebaseDatabase.getInstance().getReference();

                            removeProductAdminRef.child("Cart List").child("Admin View")
                                    .child(Prevalent.currentOnlineUser.getId())
                                    .removeValue();
                            removeProductUserRef.child("Cart List").child("User View")
                                    .child(Prevalent.currentOnlineUser.getId())
                                    .removeValue();
                            removeOrderAdminRef.child("Orders")
                                    .child(Prevalent.currentOnlineUser.getId())
                                    .removeValue();

                            String saveCurrentDate, saveCurrentTime;

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                            saveCurrentDate = currentDate.format(calendar.getTime());

                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                            saveCurrentTime = currentTime.format(calendar.getTime());

                            mOrders.setId(saveCurrentDate + saveCurrentTime);
                            mOrders.setDate(saveCurrentDate);
                            mOrders.setTime(saveCurrentTime);

                            addProductAdminRef.child("History").child(Prevalent.currentOnlineUser.getId())
                                    .child(mOrders.getId())
                                    .setValue(mOrders).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ShippingActivity.this, "Confirm success", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        mAlertDialog = mBuilder.create();

        mEditTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrders.getState().equals("No shipped")) {
                    Intent intent = new Intent(ShippingActivity.this, ProfileActivity.class);
                    intent.putExtra(SHIP_KEY, mOrders);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.show();
            }
        });

    }

    public void loadInformationShipping() {

        final DatabaseReference shipRef =  FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getId());
        shipRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mOrders = snapshot.getValue(Orders.class);

                if (mOrders != null) {
                    mNameTextView.setText(mOrders.getName());
                    mPhoneTextView.setText(mOrders.getPhone());
                    mAddressTextView.setText(mOrders.getAddress());
                    mStatusShippingTextView.setText("Status shipping: " + mOrders.getState());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadProduct() {
        final DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(Prevalent.currentOnlineUser.getId()).child("Products");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProductsList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Products product = postSnapshot.getValue(Products.class);
                    mProductsList.add(product);
                }
                mShipAdapter.setProductsList(mProductsList);
                mShipAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}