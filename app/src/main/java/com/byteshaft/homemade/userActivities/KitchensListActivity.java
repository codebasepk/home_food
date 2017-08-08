package com.byteshaft.homemade.userActivities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.homemade.R;
import com.byteshaft.homemade.accountfragments.SignUp;
import com.byteshaft.homemade.adapters.KitchenListAdapters;
import com.byteshaft.homemade.gettersetter.*;
import com.byteshaft.homemade.utils.AppGlobals;
import com.byteshaft.homemade.utils.Helpers;
import com.byteshaft.requests.HttpRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by husnain on 7/24/17.
 */

public class KitchensListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        HttpRequest.OnErrorListener, HttpRequest.OnReadyStateChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private ListView mKitchenListView;
    private KitchenListAdapters mKitchenListAdapters;
    private ArrayList<KitchenList> kitchenListArrayList;
    private KitchenList mKitchenList;

    private HttpRequest request;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private int locationCounter = 0;
    private static final int LOCATION_PERMISSION = 4;

    private int mRadiusString = 10;
    private String mLocationString;

    private int currentArraySize = 0;
    public static boolean updated = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchens_list);
        mKitchenListView = (ListView) findViewById(R.id.kitchen_list);
        mKitchenListView.setOnItemClickListener(this);
        locationMethod();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Helpers.locationEnabled()) {
                        new LocationTask().execute();
                    } else {
                        Helpers.dialogForLocationEnableManually(this);
                    }
                } else {
                    Helpers.showSnackBar(findViewById(android.R.id.content), R.string.permission_denied);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mKitchenList = kitchenListArrayList.get(position);
        Intent intent = new Intent(KitchensListActivity.this, KitchenDishesDetailsActivity.class);
        intent.putExtra("id", mKitchenList.getKitchenId());
        intent.putExtra("contact_number", mKitchenList.getKitchenPhoneNumber());
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.redius, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.radius:
                Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
                radiusDialog();
                break;
            case R.id.refresh:
                locationMethod();
                getAllKitchens(mLocationString, String.valueOf(mRadiusString));
                currentArraySize = kitchenListArrayList.size();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void radiusDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setCancelable(false);

        dialog.show();
        SeekBar seekbar = (SeekBar) dialog.findViewById(R.id.seek_bar);
        final TextView seekBarValue = (TextView) dialog.findViewById(R.id.value_text_view);
        final TextView cancelTextView = (TextView) dialog.findViewById(R.id.cancel_text_view);
        final TextView doneTextView = (TextView) dialog.findViewById(R.id.done_text_view);
        seekbar.setMax(100);
        seekbar.setKeyProgressIncrement(1);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        doneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadiusString = Integer.valueOf(seekBarValue.getText().toString());
                locationMethod();
                getAllKitchens(mLocationString, String.valueOf(mRadiusString));
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onReadyStateChange(HttpRequest httpRequest, int readyState) {
        switch (readyState) {
            case HttpRequest.STATE_DONE:
                Helpers.dismissProgressDialog();
                switch (httpRequest.getStatus()) {
                    case HttpURLConnection.HTTP_OK:
                        System.out.println(httpRequest.getResponseText() + "working ");
                        kitchenListArrayList = new ArrayList<>();
                        mKitchenListAdapters = new KitchenListAdapters(KitchensListActivity.this, kitchenListArrayList);
                        mKitchenListView.setAdapter(mKitchenListAdapters);
                        try {
                            JSONArray jsonArray = new JSONArray(httpRequest.getResponseText());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                KitchenList kitchenList = new KitchenList();
                                kitchenList.setKitchenId(jsonObject.getInt("id"));
                                kitchenList.setKitchenPhoneNumber(jsonObject.getString("contact_number"));
                                kitchenList.setKitchenLocation(jsonObject.getString("location"));
                                kitchenList.setKitchenName(jsonObject.getString("name"));
                                kitchenList.setOrderTime(jsonObject.getString("time_to_finish"));
                                kitchenList.setOpeningTime(jsonObject.getString("opening_time"));
                                kitchenList.setClosingTime(jsonObject.getString("closing_time"));
                                kitchenList.setDeliverable(jsonObject.getBoolean("delivery"));
                                kitchenList.setWorkingDays(jsonObject.getString("working_days"));
                                kitchenList.setKitchenImage(jsonObject.getString("photo"));

                                kitchenListArrayList.add(kitchenList);
                                mKitchenListAdapters.notifyDataSetChanged();
                            }
                            currentArraySize = kitchenListArrayList.size();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        System.out.println(httpRequest.getResponseText() + "working ");
                        break;
                }
        }

    }

    @Override
    public void onError(HttpRequest httpRequest, int readyState, short i1, Exception exception) {
        switch (readyState) {
            case HttpRequest.ERROR_CONNECTION_TIMED_OUT:
                Helpers.showSnackBar(findViewById(android.R.id.content), "connection time out");
                break;
            case HttpRequest.ERROR_NETWORK_UNREACHABLE:
                Helpers.showSnackBar(findViewById(android.R.id.content), exception.getLocalizedMessage());
                break;
        }

    }

    private void getAllKitchens(String baseLocation, String radius) {
        request = new HttpRequest(this);
        request.setOnReadyStateChangeListener(this);
        request.setOnErrorListener(this);
        request.open("GET", String.format("%sfood-providers/?base_location=%s&radius=%s", AppGlobals.BASE_URL, baseLocation, radius));
        request.send();
    }


    @Override
    public void onLocationChanged(Location location) {
        locationCounter++;
        if (locationCounter > 1) {
            stopLocationUpdate();
            mLocationString = location.getLatitude() + "," + location.getLongitude();
            System.out.println(mLocationString);
            if (mLocationString != null && !mLocationString.trim().isEmpty())
                getAllKitchens(mLocationString, String.valueOf(mRadiusString));
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class LocationTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("task");
            Helpers.showProgressDialog(KitchensListActivity.this, "Loading Kitchens...");
        }

        @Override
        protected String doInBackground(String... strings) {
            buildGoogleApiClient();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(AppGlobals.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }


    protected void createLocationRequest() {
        long INTERVAL = 1000;
        long FASTEST_INTERVAL = 1000;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void locationMethod() {
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
                    ActivityCompat.requestPermissions(KitchensListActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
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
                new LocationTask().execute();
            } else {
                Helpers.dialogForLocationEnableManually(this);
            }
        }
    }
}

