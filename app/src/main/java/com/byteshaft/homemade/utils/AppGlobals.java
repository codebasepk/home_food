package com.byteshaft.homemade.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import com.byteshaft.homemade.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class AppGlobals extends Application {

    public static final String SERVER_IP = "http://178.62.67.242:8000/";
    public static final String SERVER_IP_FOR_IMAGE = "http://178.62.67.242:8000";
    public static final String BASE_URL = String.format("%sapi/", SERVER_IP);
    private static Context sContext;
    public static final String KEY_LOGIN = "login";
    public static final String KEY_KITCHEN_NAME = "name";
    public static final String KEY_DISH_NAME = "name";
    public static final String KEY_USER_ID = "id";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CONTACT_NUMBER = "contact_number";
    public static final String KEY_KITCHEN_IMAGE = "image";
    public static final String KEY_SERVER_IMAGE = "photo";
    public static final String KEY_KITCHEN_PROVIDERS_ID = "id";
    public static final String KEY_DELIVERY_STATUS = "delivery";
    public static final String KEY_TIME_TO_FINISH = "time_to_finish";
    public static final String KEY_WORKING_DAYS = "working_days";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_OPENING_TIME = "opening_time";
    public static final String KEY_CLOSING_TIME = "closing_time";
    public static final String KEY_SWITCH_STATE = "switch";
    public static final String USER_ACTIVATION_KEY = "activation_key";
    public static final int LOCATION_ENABLE = 3;
    public static final String KEY_SEEK_BAR_VALUE = "seek_bar_value";
    public static ImageLoader sImageLoader;


    @Override
    public void onCreate() {
        super.onCreate();
        sImageLoader = ImageLoader.getInstance();
        sImageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
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

    public static void saveSeekBarvalueToSharedPreferences(String key, int value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static int getSeekBarValueFromSharedPreferences(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getInt(key, 10);
    }

    public static void loginState(boolean type) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(KEY_LOGIN, type).apply();
    }

    public static boolean isLogin() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(KEY_LOGIN, false);
    }

    public static void saveSwitchState(boolean type) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(KEY_SWITCH_STATE, type).apply();
    }

    public static boolean getSwitchValue() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(KEY_SWITCH_STATE, false);
    }

    public static void alertDialog(Activity activity, String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg).setCancelable(false).setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
