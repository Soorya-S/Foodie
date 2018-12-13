package com.soorya.foodie.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.soorya.foodie.model.CartItem;

import java.util.List;

/*
 * Database AccessObject for accessing the Room database
 */
@Dao
public interface CartDataAccessObject {

    @Insert
    void addCartData(CartItem cartItem);

    @Query("select * from cart")
    public List<CartItem> getCartDetails();

    @Update
    void updateCartValue(CartItem cartItem);

    @Delete
    void deleteCartData(CartItem cartItem);

    @Query("DELETE FROM cart")
    public void nukeTable();
}
