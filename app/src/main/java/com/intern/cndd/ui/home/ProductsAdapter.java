package com.intern.cndd.ui.home;

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
import com.intern.cndd.ui.ItemClickListener;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Products> mProducts;
    private OnListener mOnListener;

    public void setOnListener(OnListener onListener) {
        mOnListener = onListener;
    }

    public ProductsAdapter(Context context, List<Products> products) {
        mContext = context;
        mProducts = products;
    }

    public void setProducts(List<Products> products) {
        mProducts = products;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gr_item_decoration, parent, false);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ProductsViewHolder) holder).onBind(mProducts.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public void filterList(List<Products> filterllist) {
        mProducts = filterllist;
        notifyDataSetChanged();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {

        public ImageView productImageView;
        public TextView starTextView;
        public TextView nameTextView;


        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            starTextView = itemView.findViewById(R.id.staffTextView);
            nameTextView = itemView.findViewById(R.id.nameProductTextView);
        }

        public void onBind(Products products, int position) {
            Glide.with(mContext)
                    .load(products.getImage())
                    .placeholder(R.drawable.loadingimage)
                    .into(productImageView);

            nameTextView.setText(products.getName());
            starTextView.setText(products.getStar());

            itemView.setOnClickListener(v -> mOnListener.onClick(products, position));
        }

    }

    public interface OnListener {
        void onClick(Products products, int position);
    }

}
