package com.byteshaft.homemade.gettersetter;

import android.graphics.Bitmap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by husnain on 7/26/17.
 */

public class DishDetails {

    private String dishName;
    private String dishQuantity;
    private String dishPrice;
    private Bitmap dishImage;

    public String getDishQuantity() {
        return dishQuantity;
    }

    public void setDishQuantity(String dishQuantity) {
        this.dishQuantity = dishQuantity;
    }

    public String getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(String dishPrice) {
        this.dishPrice = dishPrice;
    }


    public Bitmap getDishImage() {
        return dishImage;
    }

    public void setDishImage(Bitmap dishImage) {
        this.dishImage = dishImage;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }
}
