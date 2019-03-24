package com.example.shoppingcart.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoppingcart.Interface.ItemClickListener;
import com.example.shoppingcart.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName, productDescription, productPrice;
    public ImageView productImage;
    public ItemClickListener listener;

    public ProductViewHolder(View itemView) {
        super(itemView);

        productName = (TextView) itemView.findViewById(R.id.product_name);
        productDescription = (TextView) itemView.findViewById(R.id.product_description);
        productPrice = (TextView) itemView.findViewById(R.id.product_price);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.OnClick(v, getAdapterPosition(), false);
    }
}
