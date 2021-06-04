package com.intern.cndd.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.intern.cndd.R;
import com.intern.cndd.ui.ItemClickListener;


public class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView productImageView;
    public TextView starTextView;
    public TextView nameTextView;
    public ItemClickListener listener;

    public ProductsViewHolder(@NonNull View itemView) {
        super(itemView);

        productImageView = itemView.findViewById(R.id.productImageView);
        starTextView = itemView.findViewById(R.id.staffTextView);
        nameTextView = itemView.findViewById(R.id.nameProductTextView);

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}

