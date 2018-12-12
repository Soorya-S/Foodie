package com.soorya.foodie.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.soorya.foodie.R;
import com.soorya.foodie.activities.CartActivity;
import com.soorya.foodie.activities.MainActivity;
import com.soorya.foodie.interfaces.AddRemoveButtonClickListener;
import com.soorya.foodie.interfaces.CartValueUpdater;
import com.soorya.foodie.model.CartItem;
import com.soorya.foodie.view.AddRemoveButton;

public class FoodDetailFragment extends Fragment {

    private ImageView itemImage;
    private TextView itemPrice;
    private TextView itemRating;
    private TextView itemName;
    private AddRemoveButton addRemoveButton;
    private RelativeLayout gotoCart;
    private RelativeLayout addMoreItem;
    private ImageView backButton;
    private CartValueUpdater cartValueUpdater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.food_description_layout,container,false);

        itemImage = view.findViewById(R.id.item_image);
        itemPrice = view.findViewById(R.id.item_price_text);
        itemRating =view.findViewById(R.id.rating_text);
        itemName = view.findViewById(R.id.item_name_text);
        backButton = view.findViewById(R.id.back);
        gotoCart = view.findViewById(R.id.cart_button);
        addMoreItem = view.findViewById(R.id.add_more_button);
        addRemoveButton = view.findViewById(R.id.add_remove_button);
        addRemoveButton.setMinValue(0);
        addRemoveButton.setMaxValue(5);

       final  Bundle bundle = getArguments();

        Glide.with(getContext()).load(bundle.getString("IMAGE_URL")).into(itemImage);
        itemPrice.setText(String.valueOf(bundle.getFloat("ITEM_PRICE")));
        itemRating.setText(String.valueOf(bundle.getFloat("ITEM_RATING")) + "(/5 (500+ reviews))");
        itemName.setText(bundle.getString("ITEM_NAME"));
        addRemoveButton.setItemCount(bundle.getInt("ITEM_CART_COUNT"));

        addRemoveButton.setOnAddRemoveButtonClickListener(new AddRemoveButtonClickListener() {
            @Override
            public void onAddRemoveButtonClicked(int value) {

                CartItem cartItem = new CartItem();
                cartItem.setFoodName(bundle.getString("ITEM_NAME"));
                cartItem.setCartValue(value);
                cartItem.setFoodPrice(bundle.getFloat("ITEM_PRICE"));
                cartItem.setFoodRating(bundle.getFloat("ITEM_RATING"));
                cartItem.setFoodImageUrl(bundle.getString("IMAGE_URL"));


                cartValueUpdater.onCartValueChanged(cartItem);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        addMoreItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CartActivity.class));
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cartValueUpdater = (CartValueUpdater) context;
    }
}
