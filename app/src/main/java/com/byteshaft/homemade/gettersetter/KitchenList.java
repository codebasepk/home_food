package com.byteshaft.homemade.gettersetter;

import android.graphics.Bitmap;
import android.widget.TextView;


/**
 * Created by husnain on 7/26/17.
 */

public class KitchenList {

    private int kitchenId;
    private String kitchenName;
    private String kitchenPhoneNumber;
    private String kitchenLocation;
    private String kitchenAllDishes;
    private String kitchenImage;
    private String openingTime;
    private String closingTime;
    private String orderTime;
    private String workingDays;
    private boolean deliverable;

    public int getKitchenId() {

        return kitchenId;
    }

    public void setKitchenId(int kitchenId) {
        this.kitchenId = kitchenId;
    }

    public String getKitchenImage() {
        return kitchenImage;
    }

    public void setKitchenImage(String kitchenImage) {
        this.kitchenImage = kitchenImage;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public boolean isDeliverable() {
        return deliverable;
    }

    public void setDeliverable(boolean deliverable) {
        this.deliverable = deliverable;
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
