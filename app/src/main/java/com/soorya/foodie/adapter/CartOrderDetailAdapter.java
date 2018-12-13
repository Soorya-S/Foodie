package com.soorya.foodie.adapter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soorya.foodie.R;
import com.soorya.foodie.interfaces.AddRemoveButtonClickListener;
import com.soorya.foodie.interfaces.CartValueUpdater;
import com.soorya.foodie.model.CartItem;
import com.soorya.foodie.utils.CartDatabase;
import com.soorya.foodie.view.AddRemoveButton;

import java.util.List;

public class CartOrderDetailAdapter extends RecyclerView.Adapter<CartOrderDetailAdapter.ViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private CartDatabase cartDatabase;
    private CartValueUpdater cartValueUpdater;
    public CartOrderDetailAdapter(Context context, List<CartItem> cartItems) {

        this.context = context;
        this.cartItems = cartItems;
        this.cartValueUpdater = (CartValueUpdater) context;
        initDatabase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cart_viewholder, parent, false);
        CartOrderDetailAdapter.ViewHolder viewHolder = new CartOrderDetailAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (cartItems.size() > 0) {
            holder.itemName.setText(cartItems.get(position).getFoodName());
            holder.perItemRate.setText(String.valueOf(cartItems.get(position).getFoodPrice()));
            holder.addRemoveButton.setItemCount(cartItems.get(position).getCartValue());
            float total = cartItems.get(position).getFoodPrice() * holder.addRemoveButton.getItemCount();
            holder.itemPrice.setText(String.valueOf(total));
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemPrice;
        private TextView itemName;
        private AddRemoveButton addRemoveButton;
        private TextView perItemRate;

        public ViewHolder(View itemView) {
            super(itemView);
            itemPrice = itemView.findViewById(R.id.item_total);
            itemName = itemView.findViewById(R.id.item_name_text);
            addRemoveButton = itemView.findViewById(R.id.add_remove_button);
            addRemoveButton.setMaxValue(5);
            addRemoveButton.setMinValue(0);
            perItemRate = itemView.findViewById(R.id.per_item_rate);

            addRemoveButton.setOnAddRemoveButtonClickListener(new AddRemoveButtonClickListener() {
                @Override
                public void onAddRemoveButtonClicked(int value) {
                    cartItems.get(getAdapterPosition()).setCartValue(value);
                    updateCartValues(cartItems.get(getAdapterPosition()), getAdapterPosition());
                    cartValueUpdater.onCartValueChanged(cartItems.get(getAdapterPosition()));
                }
            });
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void initDatabase()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground( final Void ... params ) {
                cartDatabase = Room.databaseBuilder(context,CartDatabase.class,"cartdb").build();
                return null;
            }
            @Override
            protected void onPostExecute( final Void result ) {

            }
        }.execute();
    }


    /*
     * this function will add/remove/update a single data entity 'CartItem' to the database
     */
    @SuppressLint("StaticFieldLeak")
    public void updateCartValues(final CartItem cartItem,final int pos)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground( final Void ... params ) {
                if (cartDatabase!=null) {
                    try {
                        if (cartItem.getCartValue() < 1) {
                            cartDatabase.cartDB().deleteCartData(cartItem);
                            Log.d("","deleted : " + cartItem.getFoodName());

                        } else {
                            cartDatabase.cartDB().addCartData(cartItem);
                        }
                    } catch (Exception e) {
                        Log.d("","Exception : " + cartItem.getFoodName() + " err : " + e.getLocalizedMessage());

                        cartDatabase.cartDB().updateCartValue(cartItem);
                    }
                }

                return null;
            }
            @Override
            protected void onPostExecute( final Void result ) {
                notifyItemChanged(pos);
            }
        }.execute();
    }
}