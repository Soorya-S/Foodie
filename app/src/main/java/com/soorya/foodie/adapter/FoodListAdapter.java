package com.soorya.foodie.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
import com.soorya.foodie.model.FoodItem;
import com.soorya.foodie.view.AddRemoveButton;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private Context context;
    private List<FoodItem> foodList;
    private CartValueUpdater cartValueUpdater;

    public FoodListAdapter(Context contex, List<FoodItem> foodItems)
    {
        this.context = contex;
        this.foodList = foodItems;
        this.cartValueUpdater =(CartValueUpdater) contex;
    }

    @NonNull
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.food_card_layout,parent,false);
        ViewHolder  viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.ViewHolder holder, int position) {

        if (foodList.size()>0)
        {
            holder.itemName.setText(foodList.get(position).getItemName());
            holder.itemRating.setText(String.valueOf(foodList.get(position).getAverageRating()));
            holder.itemPrice.setText("Rs."+String.valueOf(foodList.get(position).getItemPrice()));
            Glide.with(context).load(foodList.get(position).getImageUrl()).thumbnail(0.3f).into(holder.itemImage);
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
                public void onAddRemoveButtonClicked(int value) {
                    Toast.makeText(context,"CLicked on + " + getAdapterPosition() + " value is " + value,Toast.LENGTH_SHORT).show();
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
}