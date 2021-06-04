package com.intern.cndd.showorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.intern.cndd.R;
import com.intern.cndd.model.Orders;
import com.intern.cndd.model.Products;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Orders> mOrdersList;
    private OnListener mOnListener;

    public void setOnListener(OnListener onListener) {
        mOnListener = onListener;
    }

    public void setOrdersList(List<Orders> ordersList) {
        mOrdersList = ordersList;
    }

    public OrderAdapter(List<Orders> ordersList) {
        mOrdersList = ordersList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gr_item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((OrderViewHolder) holder).onBind(mOrdersList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mOrdersList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
        public Button ShowOrdersBtn;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            ShowOrdersBtn = itemView.findViewById(R.id.show_all_products_btn);

        }

        public void onBind(Orders model, int position) {
            userName.setText("Name: " + model.getName());
            userPhoneNumber.setText("Phone: " + model.getPhone());
            userTotalPrice.setText("Total Amount: " + model.getCost());
            userDateTime.setText("Orders: " + model.getDate() + " " + model.getTime());
            userShippingAddress.setText("Shipping Address: " + model.getAddress());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnListener.onClick(model, position);
                }
            });

            ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnListener.onClick(model, position);
                }
            });

        }

    }

    public interface OnListener {
        void onClick(Orders products, int position);
        void onShow(Orders products, int position);
    }

}
