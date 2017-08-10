package com.byteshaft.homemade.gettersetter;

import android.graphics.Bitmap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by husnain on 7/26/17.
 */

public class KitchenDishesDetails {

    private String dishName;
    private String dishDescriptions;
    private String kitchenPhoneNumber;
    private String dishPrice;
    private String dishImage;

    public String getDishImage() {
        return dishImage;
    }

    public void setDishImage(String dishImage) {
        this.dishImage = dishImage;
    }
    public String getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(String dishPrice) {
        this.dishPrice = dishPrice;
    }



    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishDescriptions() {
        return dishDescriptions;
    }
    public String getKitchenPhoneNumber() {
        return kitchenPhoneNumber;
    }

    public void setKitchenPhoneNumber(String kitchenPhoneNumber) {
        this.kitchenPhoneNumber = kitchenPhoneNumber;
    }

    public void setDishDescriptions(String dishDescriptions) {
        this.dishDescriptions = dishDescriptions;
    }


}
