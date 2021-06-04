package com.intern.cndd.ui.detailorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.intern.cndd.R;
import com.intern.cndd.model.Products;

import java.util.List;

public class DetailOderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Products> mProducts;

    public DetailOderAdapter(List<Products> products) {
        mProducts = products;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new DetailOderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((DetailOderViewHolder) holder).onBind(mProducts.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class DetailOderViewHolder extends RecyclerView.ViewHolder {

        private TextView mProductName;
        private TextView mProductPrice;
        private TextView mTotal;

        public DetailOderViewHolder(@NonNull View itemView) {
            super(itemView);

            mProductName = itemView.findViewById(R.id.cart_product_name);
            mProductPrice = itemView.findViewById(R.id.cart_product_price);
            mTotal = itemView.findViewById(R.id.cart_product_quantity);
        }

        public void onBind(Products products, int position) {
            mProductName.setText(products.getName());
            mTotal.setText(products.getTotal());
            mProductPrice.setText("Total Price: $" + Integer.parseInt(products.getTotal())*Integer.parseInt(products.getPrice()) + "");
        }
    }
}
