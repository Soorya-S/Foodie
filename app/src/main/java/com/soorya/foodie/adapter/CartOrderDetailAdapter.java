package com.soorya.foodie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.soorya.foodie.model.CartItem;
import com.soorya.foodie.model.FoodItem;

import java.util.List;

public class CartOrderDetailAdapter extends RecyclerView.Adapter<CartOrderDetailAdapter.ViewHolder> {

    private Context context;
    private List<CartItem> cartItems;

    public CartOrderDetailAdapter(Context context, List<CartItem> cartItems) {

        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}