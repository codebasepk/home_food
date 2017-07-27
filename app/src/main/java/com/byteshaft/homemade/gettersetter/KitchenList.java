package com.byteshaft.homemade.gettersetter;

import android.graphics.Bitmap;


/**
 * Created by husnain on 7/26/17.
 */

public class KitchenList {

    private String kitchenName;
    private String kitchenPhoneNumber;
    private String kitchenLocation;
    private String kitchenAllDishes;
    private Bitmap kitchenImage;

    public Bitmap getKitchenImage() {
        return kitchenImage;
    }

    public void setKitchenImage(Bitmap kitchenImage) {
        this.kitchenImage = kitchenImage;
    }

    public String getKitchenName() {
        return kitchenName;
    }

    public void setKitchenName(String kitchenName) {
        this.kitchenName = kitchenName;
    }

    public String getKitchenPhoneNumber() {
        return kitchenPhoneNumber;
    }

    public void setKitchenPhoneNumber(String kitchenPhoneNumber) {
        this.kitchenPhoneNumber = kitchenPhoneNumber;
    }

    public String getKitchenLocation() {
        return kitchenLocation;
    }

    public void setKitchenLocation(String kitchenLocation) {
        this.kitchenLocation = kitchenLocation;
    }

    public String getKitchenAllDishes() {
        return kitchenAllDishes;
    }

    public void setKitchenAllDishes(String kitchenAllDishes) {
        this.kitchenAllDishes = kitchenAllDishes;
    }
}
