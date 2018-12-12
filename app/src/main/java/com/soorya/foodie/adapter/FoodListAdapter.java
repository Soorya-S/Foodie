package com.soorya.foodie.adapter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.soorya.foodie.R;
import com.soorya.foodie.fragments.FoodDetailFragment;
import com.soorya.foodie.interfaces.AddRemoveButtonClickListener;
import com.soorya.foodie.interfaces.CartValueUpdater;
import com.soorya.foodie.model.CartItem;
import com.soorya.foodie.model.FoodItem;
import com.soorya.foodie.utils.CartDatabase;
import com.soorya.foodie.view.AddRemoveButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private Context context;
    private List<FoodItem> foodList;
    private CartValueUpdater cartValueUpdater;
    private CartDatabase cartDatabase;
    private HashMap<String,CartItem> cartItems = new HashMap<>();
    private Boolean isSync = false;

    public FoodListAdapter(Context contex, List<FoodItem> foodItems)
    {
        this.context = contex;
        this.foodList = foodItems;
        this.cartValueUpdater =(CartValueUpdater) contex;

        new Thread(new Runnable() {
            @Override
            public void run() {
                cartDatabase = Room.databaseBuilder(context,CartDatabase.class,"cartdb").build();

                for(CartItem c : cartDatabase.cartDB().getCartDetails())
                {
                    cartItems.put(c.getFoodName(),c);
                }
                notifyDataSetChanged();
                syncData();
            }
        }).start();
    }

    @NonNull
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.food_card_viewholder,parent,false);
        ViewHolder  viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.ViewHolder holder, int position) {

        if (foodList.size()>0)
        {
            String foodName = foodList.get(position).getItemName();
            holder.itemName.setText(foodName);
            holder.itemRating.setText(String.valueOf(foodList.get(position).getAverageRating()));
            holder.itemPrice.setText("Rs."+String.valueOf(foodList.get(position).getItemPrice()));
            Glide.with(context).load(foodList.get(position).getImageUrl()).thumbnail(0.3f).into(holder.itemImage);

            if (cartItems.size()>0 && cartItems.containsKey(foodName))
            {
                holder.addRemoveButton.setItemCount(cartItems.get(foodName).getCartValue());
            }
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView itemImage;
        private TextView itemPrice;
        private TextView itemRating;
        private TextView itemName;
        private AddRemoveButton addRemoveButton;

        public ViewHolder(View itemView)
        {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
            itemPrice = itemView.findViewById(R.id.item_price_text);
            itemRating = itemView.findViewById(R.id.rating_text);
            itemName = itemView.findViewById(R.id.item_name_text);
            addRemoveButton = itemView.findViewById(R.id.add_remove_button);
            addRemoveButton.setMinValue(0);
            addRemoveButton.setMaxValue(5);


            addRemoveButton.setOnAddRemoveButtonClickListener(new AddRemoveButtonClickListener() {
                @Override
                public void onAddRemoveButtonClicked(final int value) {

                    int pos = getAdapterPosition();
                    CartItem cartItem = new CartItem();
                    cartItem.setFoodName(foodList.get(pos).getItemName());
                    cartItem.setCartValue(value);
                    cartItem.setFoodPrice(foodList.get(pos).getItemPrice());
                    cartItem.setFoodRating(foodList.get(pos).getAverageRating());
                    cartItem.setFoodImageUrl(foodList.get(pos).getImageUrl());
                    updateCartValues(cartItem);
                }
            });

            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FoodDetailFragment fragment = new FoodDetailFragment();
                    Bundle data = new Bundle();
                    data.putString("IMAGE_URL",foodList.get(getAdapterPosition()).getImageUrl());
                    data.putString("ITEM_NAME",foodList.get(getAdapterPosition()).getItemName());
                    data.putFloat("ITEM_PRICE",foodList.get(getAdapterPosition()).getItemPrice());
                    data.putFloat("ITEM_RATING",foodList.get(getAdapterPosition()).getAverageRating());
                    data.putInt("ITEM_CART_COUNT",addRemoveButton.getItemCount());

                    fragment.setArguments(data);
                    FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_righ);
                    transaction.replace(android.R.id.content, fragment, "food_detail_fragment");
                    transaction.addToBackStack(null).commit();
                }
            });
        }
    }


    int pos;
    @SuppressLint("StaticFieldLeak")
    public void updateCartValues(final CartItem cartItem)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground( final Void ... params ) {
                try {
                    cartDatabase.cartDB().addCartData(cartItem);
                }
                catch (Exception e) {
                    cartDatabase.cartDB().updateCartValue(cartItem);
                }
                cartItems.put(cartItem.getFoodName(),cartItem);
                int i = 0;
                for(FoodItem item : foodList)
                {
                    if (item.getItemName().equals(cartItem.getFoodName()))
                    {
                        pos = i;
                    }
                    i++;
                }
                return null;
            }
            @Override
            protected void onPostExecute( final Void result ) {
                notifyItemChanged(pos);
            }
        }.execute();
    }


    /*
     * This function make sure that all cart items are there in the server.
     * if the item which present in cart is not available in server it will
     * delete the item from cart and show user a error message
     */
    boolean isChanged = false;
    @SuppressLint("StaticFieldLeak")
    public void syncData()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground( final Void ... params ) {
                List<String> tmp = new ArrayList<>();

                if (cartItems.size()>0 && foodList.size()>0 && !isSync)
                {
                    isSync = true;

                    for(FoodItem foodItem : foodList)
                    {
                        tmp.add(foodItem.getItemName());
                    }

                    for(CartItem c : cartItems.values())
                    {
                        if (!tmp.contains(c.getFoodName()))
                        {
                            cartDatabase.cartDB().deleteCartData(c);
                            isChanged = true;
                            Log.d("","deleted : " + c.getFoodName());
                        }
                    }
                }
                return null;
            }
            @Override
            protected void onPostExecute( final Void result ) {
                if (isChanged)
                {
                    Toast.makeText(context,"Some items in your cart is currently unavailable!",Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}