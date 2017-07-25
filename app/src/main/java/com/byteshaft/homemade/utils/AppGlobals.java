package com.byteshaft.homemade.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

/**
 * Created by husnain on 5/11/17.
 */

public class AppGlobals extends Application {

    public static final String SERVER_IP = "http://46.101.15.119:8000/";
    public static final String BASE_URL = String.format("%sapi/", SERVER_IP);
    private static Context sContext;
    public static final String KEY_LOGIN = "login";
    public static final String KEY_RESTAURANT_NAME = "full_name";
    public static final String KEY_USER_ID = "id";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ACCOUNT_TYPE = "account_type";
    public static final String KEY_TABLE_LOCATION = "location";
    public static final String KEY_TABLE_NUMBER = "table_number";
    public static final String KEY_TABLE_STATUS = "serviceable";
    public static final String KEY_TABLE_BOOKINGS = "bookings";
    public static final String KEY_TABLE_ID = "id";
    public static final String KEY_RESTAURANT_ID = "restaurant";
    public static final String KEY_TABLE_BOOKING_TIME = "minimum_booking_time";
    public static final String KEY_TABLE_CHAIRS = "number_of_chairs";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_OPENING_TIME = "opening_time";
    public static final String KEY_CLOSING_TIME = "closing_time";
    public static final String USER_ACTIVATION_KEY = "activation_key";
    public static final String KEY_FCM_TOKEN = "token";
    public static final int LOCATION_ENABLE = 3;


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getPreferenceManager() {
        return getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE);
    }

    public static void clearSettings() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().clear().apply();
    }

    public static void saveDataToSharedPreferences(String key, String value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static String getStringFromSharedPreferences(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(key, "");
    }

    public static void loginState(boolean type) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(KEY_LOGIN, type).apply();
    }

    public static boolean isLogin() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(KEY_LOGIN, false);
    }

    public static void alertDialog(Activity activity, String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
