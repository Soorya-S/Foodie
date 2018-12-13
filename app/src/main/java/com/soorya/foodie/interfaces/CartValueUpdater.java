package com.soorya.foodie.interfaces;

import com.soorya.foodie.model.CartItem;


/*
 * a call back interface for updating the cart values from various classes
 */
public interface CartValueUpdater  {
    void onCartValueChanged(CartItem cartItem);
}
