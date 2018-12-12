package com.soorya.foodie.interfaces;

import com.soorya.foodie.model.CartItem;

import java.util.List;

public interface CartValueUpdater  {
    void onCartValueChanged(CartItem cartItem);
}
