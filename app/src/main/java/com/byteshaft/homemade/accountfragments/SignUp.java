package com.byteshaft.homemade.accountfragments;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.byteshaft.homemade.R;
import com.byteshaft.homemade.utils.AppGlobals;
import com.byteshaft.homemade.utils.Helpers;
import com.byteshaft.requests.HttpRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SignUp extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private View mBaseView;
    private EditText mRestaurantName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mVerifyPassword;
    private EditText mAddress;
    private EditText mOpeningTime;
    private EditText mClosingTime;
    private TextView mPickForCurrentLocation;

    private String mEmailAddressString;
    private String mPasswordString;
    private String mVerifyPasswordString;
    private String mRestaurantNameString;
    private String mAddressString;
    private String mOpeningTimeString;
    private String mClosingTimeString;
    private String mLocationString;
    private String mAccountType = String.valueOf(2);

    private Button mSignUpButton;
    private TextView mLoginTextView;

    private HttpRequest request;

    private boolean isClosingTime = false;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    private int locationCounter = 0;
    private static final int LOCATION_PERMISSION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBaseView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(getResources().getString(R.string.sign_up));
        setHasOptionsMenu(true);
        mEmail = (EditText) mBaseView.findViewById(R.id.email_edit_text);
        mPassword = (EditText) mBaseView.findViewById(R.id.password_edit_text);
//        mVerifyPassword = (EditText) mBaseView.findViewById(R.id.verify_password_edit_text);
//        mRestaurantName = (EditText) mBaseView.findViewById(R.id.restaurant_name_edit_text);
//        mAddress = (EditText) mBaseView.findViewById(R.id.address_edit_text);
//        mOpeningTime = (EditText) mBaseView.findViewById(R.id.opening_time_edit_text);
//        mClosingTime = (EditText) mBaseView.findViewById(R.id.closing_time_edit_text);
//        mSignUpButton = (Button) mBaseView.findViewById(R.id.sign_up_button);
//        mLoginTextView = (TextView) mBaseView.findViewById(R.id.login_text_view);
//        mPickForCurrentLocation = (TextView) mBaseView.findViewById(R.id.pick_for_current_location);

        mSignUpButton.setOnClickListener(this);
        mLoginTextView.setOnClickListener(this);
        mOpeningTime.setOnClickListener(this);
        mClosingTime.setOnClickListener(this);
        mPickForCurrentLocation.setOnClickListener(this);
        return mBaseView;
    }

//    if (mLocationString == null) {
//        LatLng location = getLocationFromAddress(AppGlobals.getContext(), mAddressString);
//        mLocationString = String.format("%s,%s", location.latitude, location.longitude);
//        System.out.println(mLocationString + "location");
//        registerUser(mPasswordString, mEmailAddressString, mAccountType, mAddressString,
//                mClosingTimeString, mRestaurantNameString, mOpeningTimeString, mLocationString);
//
//    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.sign_up_button:
//                System.out.println(validateEditText());
//                if (validateEditText()) {
//                    registerUser(mPasswordString, mEmailAddressString, mAccountType, mAddressString,
//                            mClosingTimeString, mRestaurantNameString, mOpeningTimeString, mLocationString);
//                }
//                break;
//            case R.id.login_text_view:
//                AccountManagerActivity.getInstance().loadFragment(new Login());
//                break;
//            case R.id.pick_for_current_location:
//                locationCounter = 0;
//                if (ContextCompat.checkSelfPermission(getActivity(),
//                        Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//                    alertDialogBuilder.setTitle(getResources().getString(R.string.permission_dialog_title));
//                    alertDialogBuilder.setMessage(getResources().getString(R.string.permission_dialog_message))
//                            .setCancelable(false).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.dismiss();
//                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                                    LOCATION_PERMISSION);
//                        }
//                    });
//                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    });
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
//
//                } else {
//                    if (Helpers.locationEnabled()) {
//                        new LocationTask().execute();
//                    } else {
//                        Helpers.dialogForLocationEnableManually(getActivity());
//                    }
//                }
//                break;
//        }

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
                        Helpers.dialogForLocationEnableManually(getActivity());
                    }
                } else {
                    Helpers.showSnackBar(getView(), R.string.permission_denied);
                }

                break;
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private void getAddress(double latitude, double longitude) {
        final StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(AppGlobals.getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append(" ").append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAddress.setText(result.toString());
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        locationCounter++;
        if (locationCounter > 1) {
            stopLocationUpdate();
            mLocationString = location.getLatitude() + "," + location.getLongitude();
            System.out.println("Lat: " + location.getLatitude() + "Long: " + location.getLongitude());
            getAddress(location.getLatitude(), location.getLongitude());
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
            Helpers.showSnackBar(getView(), R.string.acquiring_location);
        }

        @Override
        protected String doInBackground(String... strings) {
            buildGoogleApiClient();
            return null;
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        createLocationRequest();
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
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

    public boolean validateEditText() {
        boolean valid = true;
        mEmailAddressString = mEmail.getText().toString();
        mPasswordString = mPassword.getText().toString();
        mVerifyPasswordString = mVerifyPassword.getText().toString();
        mAddressString = mAddress.getText().toString();
        mRestaurantNameString = mRestaurantName.getText().toString();
        mOpeningTimeString = mOpeningTime.getText().toString();
        mClosingTimeString = mClosingTime.getText().toString();

        System.out.println(mEmailAddressString);
        System.out.println(mClosingTimeString);

        if (mEmailAddressString.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailAddressString).matches()) {
            mEmail.setError("please provide a valid email");
            valid = false;
        } else {
            mEmail.setError(null);
        }
        if (mRestaurantNameString.trim().isEmpty()) {
            mRestaurantName.setError("please provide restaurant name");
            valid = false;
        } else {
            mRestaurantName.setError(null);
        }
        if (mAddressString.trim().isEmpty()) {
            mAddress.setError(getString(R.string.enter_address));
            Helpers.showSnackBar(getView(), R.string.enter_address);
            valid = false;
        } else {
            mAddress.setError(null);
        }
        if (mOpeningTimeString.trim().isEmpty()) {
            mOpeningTime.setError("please provide restaurant opening time");
            valid = false;
        } else {
            mOpeningTime.setError(null);
        }
        if (mClosingTimeString.trim().isEmpty()) {
            mClosingTime.setError("please provide restaurant closing time");
            valid = false;
        } else {
            mClosingTime.setError(null);
        }
        if (mPasswordString.trim().isEmpty() || mPasswordString.length() < 4) {
            mPassword.setError("enter at least 4 characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (mVerifyPasswordString.trim().isEmpty() || mVerifyPasswordString.length() < 4 ||
                !mVerifyPasswordString.equals(mPasswordString)) {
            mVerifyPassword.setError("password does not match");
            valid = false;
        } else {
            mVerifyPassword.setError(null);
        }
        return valid;
    }

    private void timePickerDialog() {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String AMPM ;
                if(selectedHour < 12) {
                    AMPM = " AM";
                } else {
                    AMPM = " PM";
                }
                if (!isClosingTime) {
                    mOpeningTime.setText(selectedHour + ":" + selectedMinute + AMPM);
                    isClosingTime = true;
                } else {
                    mClosingTime.setText(selectedHour + ":" + selectedMinute + AMPM);
                    isClosingTime = false;
                }
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    private void registerUser(String password, String email, String accountType, String address,
                              String closingTime, String fullName, String openingTime, String location) {
        request = new HttpRequest(getActivity());
        request.setOnReadyStateChangeListener(new HttpRequest.OnReadyStateChangeListener() {
            @Override
            public void onReadyStateChange(HttpRequest request, int readyState) {
                switch (readyState) {
                    case HttpRequest.STATE_DONE:
                        System.out.println(request.getResponseText());
                        Helpers.dismissProgressDialog();
                        switch (request.getStatus()) {
                            case HttpRequest.ERROR_NETWORK_UNREACHABLE:
                                AppGlobals.alertDialog(getActivity(), getString(R.string.login_failed), getString(R.string.check_internet));
                                break;
                            case HttpURLConnection.HTTP_BAD_REQUEST:
                                AppGlobals.alertDialog(getActivity(), getString(R.string.register_failed), "Email already in use");
                                break;
                            case HttpURLConnection.HTTP_UNAUTHORIZED:
                                AppGlobals.alertDialog(getActivity(), getString(R.string.register_failed), getString(R.string.check_password));
                                break;
                            case HttpURLConnection.HTTP_CREATED:
                                Toast.makeText(getActivity(), "Activation code has been sent to you! Please check your Email", Toast.LENGTH_SHORT).show();
                                System.out.println(request.getResponseText() + "working ");
                                try {
                                    JSONObject jsonObject = new JSONObject(request.getResponseText());
                                    String accountType = jsonObject.getString(AppGlobals.KEY_ACCOUNT_TYPE);
                                    String userId = jsonObject.getString(AppGlobals.KEY_USER_ID);
                                    String email = jsonObject.getString(AppGlobals.KEY_EMAIL);

                                    String location = jsonObject.getString(AppGlobals.KEY_LOCATION);
                                    String address = jsonObject.getString(AppGlobals.KEY_ADDRESS);
                                    String restaurantName = jsonObject.getString(AppGlobals.KEY_RESTAURANT_NAME);
                                    String openingTime = jsonObject.getString(AppGlobals.KEY_OPENING_TIME);
                                    String closingTime = jsonObject.getString(AppGlobals.KEY_CLOSING_TIME);

                                    //saving values
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_ACCOUNT_TYPE, accountType);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_EMAIL, email);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_USER_ID, userId);

                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_LOCATION, location);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_ADDRESS, address);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_RESTAURANT_NAME, restaurantName);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_OPENING_TIME, openingTime);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_CLOSING_TIME, closingTime);
                                    Log.i("closingTime", " " + AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_CLOSING_TIME));
                                    AccountManagerActivity.getInstance().loadFragment(new AccountActivationCode());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                        }
                }

            }
        });
        request.setOnErrorListener(new HttpRequest.OnErrorListener() {
            @Override
            public void onError(HttpRequest request, int readyState, short error, Exception exception) {
                Helpers.dismissProgressDialog();
                switch (readyState) {
                    case HttpRequest.ERROR_CONNECTION_TIMED_OUT:
                        Helpers.showSnackBar(getView(), "connection time out");
                        break;
                    case HttpRequest.ERROR_NETWORK_UNREACHABLE:
                        Helpers.showSnackBar(getView(), exception.getLocalizedMessage());
                        break;
                }

            }
        });
        request.open("POST", String.format("%sregister", AppGlobals.BASE_URL));
        request.send(getRegisterData(password, email, accountType, address, closingTime,
                fullName, openingTime, location));
        Helpers.showProgressDialog(getActivity(), "Registering user..");
    }


    private String getRegisterData(String password, String email, String accountType, String address,
                                   String closingTime, String fullName, String openingTime, String location) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("account_type", accountType);
            jsonObject.put("password", password);
            jsonObject.put("address", address);
            jsonObject.put("closing_time", closingTime);
            jsonObject.put("full_name", fullName);
            jsonObject.put("opening_time", openingTime);
            jsonObject.put("location", location);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }

}
