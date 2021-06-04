package com.intern.cndd.showorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
<<<<<<< HEAD
=======
import com.intern.cndd.ui.detailorder.DetailOrderActivity;
>>>>>>> 2f76b5688ded0286a8f86d81b2b8c7f9de917be5

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowOrderActivity extends AppCompatActivity {

    private RecyclerView mOrdersRecyclerView;
    private OrderAdapter mOrderAdapter;
    private List<Orders> mOrders;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);

        mOrders = new ArrayList<>();
        mOrdersRecyclerView = findViewById(R.id.orderRecyclerView);
        mOrderAdapter = new OrderAdapter(mOrders);
        mOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOrdersRecyclerView.setAdapter(mOrderAdapter);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        loadData();

        mOrderAdapter.setOnListener(new OrderAdapter.OnListener() {
            @Override
            public void onClick(Orders products, int position) {
                CharSequence options[] = new CharSequence[]
                        {
                                "Yes",
                                "No"
                        };
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowOrderActivity.this);
                builder.setTitle("Have you shipped this order products ?");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            String uID = products.getId();
                            HashMap<String, Object> ordersMap = new HashMap<>();
                            ordersMap.put("state", "Shipped");

                            ordersRef.child(uID).updateChildren(ordersMap).addOnCompleteListener(task -> Toast.makeText(ShowOrderActivity.this, "This order is shipped", Toast.LENGTH_SHORT).show());

                        } else {
                            finish();
                        }
                    }
                });

                builder.show();

            }

            @Override
            public void onShow(Orders products, int position) {
<<<<<<< HEAD
                Intent intent = new Intent(ShowOrderActivity.this, ShowOrderActivity.class);
=======
                Intent intent = new Intent(ShowOrderActivity.this, DetailOrderActivity.class);
>>>>>>> 2f76b5688ded0286a8f86d81b2b8c7f9de917be5
                intent.putExtra("uid", products.getId());
                startActivity(intent);
            }
        });

    }

    private void loadData() {

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mOrders.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Orders orders = postSnapshot.getValue(Orders.class);
<<<<<<< HEAD
                    mOrders.add(orders);
=======
                    if (!orders.getState().equals("Shipped")){
                        mOrders.add(orders);
                    }
>>>>>>> 2f76b5688ded0286a8f86d81b2b8c7f9de917be5
                }
                mOrderAdapter.setOrdersList(mOrders);
                mOrderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}