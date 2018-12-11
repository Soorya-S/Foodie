package com.soorya.foodie.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoodItem {


    @SerializedName("average_rating")
    @Expose
    private Float averageRating;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("item_name")
    @Expose
    private String itemName;

    @SerializedName("item_price")
    @Expose
    private Float itemPrice;


    public void FoodItem(Float averageRating, String imageUrl, String itemName, Float itemPrice)
    {
        this.averageRating = averageRating;
        this.imageUrl = imageUrl;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }
}
