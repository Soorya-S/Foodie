package com.soorya.foodie.interfaces;

import com.soorya.foodie.model.CartItem;

public interface CartValueUpdater  {
    void onCartValueChanged(CartItem cartItem);
}
