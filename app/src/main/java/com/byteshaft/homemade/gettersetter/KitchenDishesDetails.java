package com.byteshaft.homemade.gettersetter;

import android.graphics.Bitmap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by husnain on 7/26/17.
 */

public class KitchenDishesDetails {

    private String dishName;
    private String dishDescriptions;
    private String dishQuantity;
    private String kitchenPhoneNumber;
    private String dishPrice;
    private Bitmap kitchenImage;

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


    public Bitmap getKitchenImage() {
        return kitchenImage;
    }

    public void setKitchenImage(Bitmap kitchenImage) {
        this.kitchenImage = kitchenImage;
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
