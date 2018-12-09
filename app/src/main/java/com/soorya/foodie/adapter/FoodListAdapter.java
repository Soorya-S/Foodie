package com.soorya.foodie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.soorya.foodie.R;
import com.soorya.foodie.model.FoodItem;
import com.soorya.foodie.view.AddRemoveButton;

import java.util.ArrayList;
import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private Context contex;
    private List<FoodItem> foodList;

    public FoodListAdapter(Context contex, List<FoodItem> foodItems)
    {
        this.contex = contex;
        this.foodList = foodItems;
    }

    @NonNull
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(contex).inflate(R.layout.food_card_layout,parent,false);
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
            Glide.with(contex).load(foodList.get(position).getImageUrl()).into(holder.itemImage);
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
        }
    }
}
