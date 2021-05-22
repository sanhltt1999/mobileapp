package com.intern.cndd.ui.shipping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.intern.cndd.R;
import com.intern.cndd.model.Products;

import java.util.List;

public class ShipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<Products> mProductsList;

    public ShipAdapter(Context context, List<Products> productsList) {
        mContext = context;
        mProductsList = productsList;
    }

    public List<Products> getProductsList() {
        return mProductsList;
    }

    public void setProductsList(List<Products> productsList) {
        mProductsList = productsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gr_item_product_ship, parent, false);
        return new ShippingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ShippingViewHolder) holder).onBind(mProductsList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mProductsList.size();
    }

    public class ShippingViewHolder extends RecyclerView.ViewHolder {

        private ImageView mProductImageView;
        private TextView mNameTextView;
        private TextView mCostTextView;
        private TextView mTotalTextView;

        public ShippingViewHolder(@NonNull  View itemView) {
            super(itemView);

            mProductImageView = itemView.findViewById(R.id.productImageView);
            mNameTextView = itemView.findViewById(R.id.nameTextView);
            mCostTextView = itemView.findViewById(R.id.costTextView);
            mTotalTextView = itemView.findViewById(R.id.totalTextView);

        }

        public void onBind(Products products, int position) {
            int price = Integer.parseInt(products.getPrice());
            int total = Integer.parseInt(products.getTotal());

            Glide.with(mContext)
                    .load(products.getImage())
                    .placeholder(R.drawable.loadingimage)
                    .optionalCenterInside()
                    .into(mProductImageView);

            mNameTextView.setText(products.getName());
            mCostTextView.setText("$" + price*total);
            mTotalTextView.setText("x" + products.getTotal());
        }
    }

}
