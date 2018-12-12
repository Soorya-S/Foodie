package com.soorya.foodie.utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.soorya.foodie.interfaces.CartDataAccessObject;
import com.soorya.foodie.model.CartItem;

@Database(entities = {CartItem.class},version = 2)
public abstract class CartDatabase extends RoomDatabase {
    public abstract CartDataAccessObject cartDB();
}