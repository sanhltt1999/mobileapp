package com.intern.cndd.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.intern.cndd.R;
import com.intern.cndd.model.Products;
import com.intern.cndd.prevalent.Prevalent;
import com.intern.cndd.ui.home.ProductsAdapter;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Products> mProducts;
    private OnListener mOnListener;

    private int count = 0;

    public void setProducts(List<Products> products) {
        mProducts = products;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public CartAdapter(Context context, List<Products> products) {
        mContext = context;
        mProducts = products;
    }

    public void setOnListener(OnListener onListener) {
        mOnListener = onListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gr_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CartViewHolder) holder).onBind(mProducts.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private ImageView mProductImageView;
        private CheckBox selectCheckBox;
        private TextView mNameProductTextView;
        private TextView mCostTextView;
        private TextView mTotalTextView;
        private ImageView mPlusImageView;
        private ImageView mSubImageView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            mProductImageView = itemView.findViewById(R.id.productImageView);
            selectCheckBox = itemView.findViewById(R.id.selectCheckBox);
            mNameProductTextView = itemView.findViewById(R.id.nameProductTextView);
            mCostTextView = itemView.findViewById(R.id.costTextView);
            mTotalTextView = itemView.findViewById(R.id.totalTextView);
            mPlusImageView = itemView.findViewById(R.id.plusImageView);
            mSubImageView = itemView.findViewById(R.id.subImageView);

        }

        public void onBind(Products products, int position) {

            int price = Integer.parseInt(products.getPrice());
            int total = Integer.parseInt(products.getTotal());

            Glide.with(mContext)
                    .load(products.getImage())
                    .centerCrop()
                    .into(mProductImageView);

            selectCheckBox.setSelected(true);
            mNameProductTextView.setText(products.getName());
            mCostTextView.setText("$" + price*total);
            mTotalTextView.setText(total + "");

            if (selectCheckBox.isChecked()) {
                count = count + price * total;
            }

            if (position == mProducts.size() - 1) {
                mOnListener.resetCount(count);
            }

            mPlusImageView.setOnClickListener(v -> {
                int price12 = Integer.parseInt(products.getPrice());
                int total12 = Integer.parseInt(products.getTotal());
                mCostTextView.setText(price12 *(total12 + 1) + "");
                mTotalTextView.setText((total12 + 1) + "");
                mProducts.get(position).setTotal((total12 + 1) + "");
                count = 0;
                notifyDataSetChanged();
            });

            mSubImageView.setOnClickListener(v -> {
                int price1 = Integer.parseInt(products.getPrice());
                int total1 = Integer.parseInt(products.getTotal());
                mCostTextView.setText(price1 *(total1 - 1) + "");
                mTotalTextView.setText((total1 - 1) + "");
                mProducts.get(position).setTotal((total1 - 1) + "");
                count = 0;
                notifyDataSetChanged();
            });

            selectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    count = 0;
                    notifyDataSetChanged();
                }
            });
        }

    }

    public interface OnListener {
        void onClick(Products products, int position, int count);
        void resetCount(int count);
    }


}
