package com.byteshaft.homemade;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.byteshaft.homemade.accountfragments.AccountManagerActivity;
import com.byteshaft.homemade.userActivities.KitchensListActivity;
import com.byteshaft.homemade.utils.AppGlobals;
import com.byteshaft.homemade.utils.Helpers;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by husnain on 7/24/17.
 */

public class SelectUser extends AppCompatActivity implements View.OnClickListener{

    private Button mUserButton;
    private Button mFoodProviderButton;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private int locationCounter = 0;
    private static final int LOCATION_PERMISSION = 4;

    private String mLocationString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        mUserButton = (Button) findViewById(R.id.user_button);
        mFoodProviderButton = (Button) findViewById(R.id.food_provider_button);
        mUserButton.setOnClickListener(this);
        mFoodProviderButton.setOnClickListener(this);

        locationCounter = 0;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getResources().getString(R.string.permission_dialog_title));
            alertDialogBuilder.setMessage(getResources().getString(R.string.permission_dialog_message))
                    .setCancelable(false).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(SelectUser.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            LOCATION_PERMISSION);
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        } else {
            if (Helpers.locationEnabled()) {
            } else {
                Helpers.dialogForLocationEnableManually(this);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_button:
                locationCounter = 0;
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle(getResources().getString(R.string.permission_dialog_title));
                    alertDialogBuilder.setMessage(getResources().getString(R.string.permission_dialog_message))
                            .setCancelable(false).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(SelectUser.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    LOCATION_PERMISSION);
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                } else {
                    if (Helpers.locationEnabled()) {
                        startActivity(new Intent(this, KitchensListActivity.class));
                    } else {
                        Helpers.dialogForLocationEnableManually(this);
                    }
                }

                break;
            case R.id.food_provider_button:
                startActivity(new Intent(this, AccountManagerActivity.class));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Helpers.locationEnabled()) {
                        System.out.println("");
                    } else {
                        Helpers.dialogForLocationEnableManually(this);
                    }
                } else {
                    Helpers.showSnackBar(findViewById(android.R.id.content), R.string.permission_denied);
                }

                break;
        }
    }

}
