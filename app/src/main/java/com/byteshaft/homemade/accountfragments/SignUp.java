package com.byteshaft.homemade.accountfragments;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.byteshaft.homemade.R;
import com.byteshaft.homemade.utils.AppGlobals;
import com.byteshaft.homemade.utils.Helpers;
import com.byteshaft.homemade.utils.MultiSelectionSpinner;
import com.byteshaft.homemade.utils.RotateUtil;
import com.byteshaft.requests.FormData;
import com.byteshaft.requests.HttpRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class SignUp extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,
        CompoundButton.OnCheckedChangeListener {

    private View mBaseView;
    private CircleImageView mProfilePicture;
    private EditText mKitchenName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mVerifyPassword;
    private EditText mAddress;
    private EditText mPhoneNumber;
    private EditText mOpeningTime;
    private EditText mClosingTime;
    private Switch mDeliverySwitch;
    private TextView mPickForCurrentLocation;
    private EditText mDeliveryTime;

    private String mEmailAddressString;
    private String mKitchenNameString;
    private String mPasswordString;
    private String mVerifyPasswordString;
    private String mPhoneNumberString;
    private String mOpeningTimeString;
    private String mClosingTimeString;
    private String mLocationString;
    //    private String mWorkingDaysString;
    private String mDeliveryTimeString;
    private String mAddressString;

    private ArrayList<String> mWeekDays;
    private MultiSelectionSpinner mWeekDaysSpinner;

    private ArrayList<String> titleArrayList;

    private Button mSignUpButton;
    private HttpRequest request;
    private boolean isClosingTime = false;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private int locationCounter = 0;
    private static final int STORAGE_CAMERA_PERMISSION = 1;
    private static final int SELECT_FILE = 2;
    private static final int LOCATION_PERMISSION = 4;
    private static final int REQUEST_CAMERA = 3;

    private File destination;
    private Uri selectedImageUri;
    private static String imageUrl = "";
    private Bitmap profilePic;

    private String method = "POST";
    private String url;

    private List<String> savedWorkingDyas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(getResources().getString(R.string.sign_up));
        setHasOptionsMenu(true);
        titleArrayList = new ArrayList<>();
        savedWorkingDyas = new ArrayList<>();
        mEmail = (EditText) mBaseView.findViewById(R.id.email_edit_text);
        mKitchenName = (EditText) mBaseView.findViewById(R.id.kitchen_name_edit_text);
        mProfilePicture = (CircleImageView) mBaseView.findViewById(R.id.kitchen_image);
        mPassword = (EditText) mBaseView.findViewById(R.id.password_edit_text);
        mVerifyPassword = (EditText) mBaseView.findViewById(R.id.verify_password_edit_text);
        mPhoneNumber = (EditText) mBaseView.findViewById(R.id.mobile_number_edit_text);
        mDeliverySwitch = (Switch) mBaseView.findViewById(R.id.delivery_switch);
        mWeekDaysSpinner = (MultiSelectionSpinner) mBaseView.findViewById(R.id.days_spinner);
        mAddress = (EditText) mBaseView.findViewById(R.id.address_edit_text);
        mOpeningTime = (EditText) mBaseView.findViewById(R.id.opening_time_edit_text);
        mClosingTime = (EditText) mBaseView.findViewById(R.id.closing_time_edit_text);
        mDeliveryTime = (EditText) mBaseView.findViewById(R.id.delivery_time_edit_text);
        mSignUpButton = (Button) mBaseView.findViewById(R.id.sign_up_button);
        mPickForCurrentLocation = (TextView) mBaseView.findViewById(R.id.pick_for_current_location);

        mSignUpButton.setOnClickListener(this);
        mOpeningTime.setOnClickListener(this);
        mClosingTime.setOnClickListener(this);
        mProfilePicture.setOnClickListener(this);
        mDeliverySwitch.setOnCheckedChangeListener(this);
        mPickForCurrentLocation.setOnClickListener(this);

        mDeliverySwitch.setChecked(AppGlobals.getSwitchValue());

        mWeekDays = new ArrayList<>();
        mWeekDays.add("Mon");
        mWeekDays.add("Tue");
        mWeekDays.add("Wed");
        mWeekDays.add("Thu");
        mWeekDays.add("Fri");
        mWeekDays.add("Sat");
        mWeekDays.add("Sun");
        mWeekDaysSpinner.setItems(mWeekDays);
        mWeekDaysSpinner.setSelection(new ArrayList<String>());
        mWeekDaysSpinner.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {

            }

            @Override
            public void selectedStrings(List<String> strings) {
                System.out.println(strings + "values");
                savedWorkingDyas = strings;


            }
        });
        mWeekDaysSpinner.setSelection(savedWorkingDyas);

        if (AppGlobals.isLogin()) {
            method = "PUT";
            mKitchenName.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_KITCHEN_NAME));
            mEmail.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL));
            mEmail.setEnabled(false);
            mPhoneNumber.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_CONTACT_NUMBER));
            mPhoneNumber.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_CONTACT_NUMBER));
            mAddress.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_ADDRESS));
            mOpeningTime.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_OPENING_TIME));
            mClosingTime.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_CLOSING_TIME));
            mDeliveryTime.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_TIME_TO_FINISH));
            mDeliverySwitch.setChecked(AppGlobals.getSwitchValue());
            String url = AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_KITCHEN_IMAGE);
            Helpers.getBitMap(url, mProfilePicture);
            String[] selected = AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_WORKING_DAYS).replace("[", "").replace("]", "").split(",");
            for (String selectedDay : selected) {
                savedWorkingDyas.add(selectedDay);
            }
            mWeekDaysSpinner.setSelection(savedWorkingDyas);
            mSignUpButton.setText("UPDATE");

        }
        return mBaseView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up_button:
                mOpeningTimeString = mOpeningTime.getText().toString();
                mClosingTimeString = mClosingTime.getText().toString();
                mDeliveryTimeString = mDeliveryTime.getText().toString();

                if (validateEditText() && !AppGlobals.isLogin()) {
                    registerUser(mEmailAddressString, mKitchenNameString, mPasswordString, mPhoneNumberString
                            , mLocationString, imageUrl, savedWorkingDyas.toString(), mOpeningTimeString, mClosingTimeString,
                            AppGlobals.getSwitchValue(), mDeliveryTimeString);
                    System.out.println("POST" + !AppGlobals.isLogin());
                } else {
                    mEmailAddressString = mEmail.getText().toString();
                    mPasswordString = mPassword.getText().toString();
                    mVerifyPasswordString = mVerifyPassword.getText().toString();
                    mPhoneNumberString = mPhoneNumber.getText().toString();
                    mKitchenNameString = mKitchenName.getText().toString();
                    mAddressString = mAddress.getText().toString();

                    registerUser("", mKitchenNameString, mPasswordString, mPhoneNumberString
                            , mLocationString, imageUrl, savedWorkingDyas.toString(), mOpeningTimeString, mClosingTimeString,
                            AppGlobals.getSwitchValue(), mDeliveryTimeString);
                    System.out.println("PUT" + AppGlobals.isLogin());
                }


                break;
            case R.id.pick_for_current_location:
                locationCounter = 0;
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle(getResources().getString(R.string.permission_dialog_title));
                    alertDialogBuilder.setMessage(getResources().getString(R.string.permission_dialog_message))
                            .setCancelable(false).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
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
                        Helpers.dialogForLocationEnableManually(getActivity());
                    }
                }
                break;
            case R.id.opening_time_edit_text:
                isClosingTime = false;
                timePickerDialog();

                break;
            case R.id.closing_time_edit_text:
                isClosingTime = true;
                timePickerDialog();
                break;
            case R.id.kitchen_image:
                checkPermissions();
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            AppGlobals.saveSwitchState(true);
            System.out.println(AppGlobals.getSwitchValue());
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
                        new LocationTask().execute();
                    } else {
                        Helpers.dialogForLocationEnableManually(getActivity());
                    }
                } else {
                    Helpers.showSnackBar(getView(), R.string.permission_denied);
                }

                break;
            case STORAGE_CAMERA_PERMISSION:
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                showDialogOK("Camera and Storage Permission required for this app",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        checkPermissions();
                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        // proceed with logic by disabling the related features or quit the app.
                                                        break;
                                                }
                                            }
                                        });
                            }
                            //permission is denied (and never ask again is  checked)
                            //shouldShowRequestPermissionRationale will return false
                            else {
                                Toast.makeText(getActivity(), "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                        .show();
                                //                            //proceed with logic by disabling the related features or quit the
                                Helpers.showSnackBar(getView(), R.string.permission_denied);
                            }
                        }
                        break;

                    }

                }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    public void checkPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(
                    new String[listPermissionsNeeded.size()]), STORAGE_CAMERA_PERMISSION);
        }

        if (listPermissionsNeeded.size() > 0) {
            System.out.println("working image");
            selectImage();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        System.out.println("show");
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                imageUrl = destination.getAbsolutePath();
                FileOutputStream fileOutputStream;
                try {
                    destination.createNewFile();
                    fileOutputStream = new FileOutputStream(destination);
                    fileOutputStream.write(bytes.toByteArray());
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profilePic = Helpers.getBitMapOfProfilePic(destination.getAbsolutePath());
                Bitmap orientedBitmap = RotateUtil.rotateBitmap(destination.getAbsolutePath(), profilePic);
                mProfilePicture.setImageBitmap(orientedBitmap);
            } else if (requestCode == SELECT_FILE) {
                selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                profilePic = Helpers.getBitMapOfProfilePic(selectedImagePath);
                Bitmap orientedBitmap = RotateUtil.rotateBitmap(selectedImagePath, profilePic);
                mProfilePicture.setImageBitmap(orientedBitmap);
                imageUrl = String.valueOf(selectedImagePath);
            }
        }
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

    public boolean validateEditText() {
        boolean valid = true;

        mEmailAddressString = mEmail.getText().toString();
        mPasswordString = mPassword.getText().toString();
        mVerifyPasswordString = mVerifyPassword.getText().toString();
        mPhoneNumberString = mPhoneNumber.getText().toString();
        mKitchenNameString = mKitchenName.getText().toString();
        mAddressString = mAddress.getText().toString();

        if (mEmailAddressString.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailAddressString).matches()) {
            mEmail.setError("please provide a valid email");
            valid = false;
        } else {
            mEmail.setError(null);
        }
        if (mKitchenNameString.trim().isEmpty()) {
            mKitchenName.setError("please provide restaurant name");
            valid = false;
        } else {
            mKitchenName.setError(null);
        }
        if (mPhoneNumberString.trim().isEmpty()) {
            mPhoneNumber.setError(getString(R.string.enter_phone));
            valid = false;
        } else {
            mPhoneNumber.setError(null);
        }
        if (mAddressString.trim().isEmpty()) {
            mAddress.setError(getString(R.string.enter_address));
            valid = false;
        } else {
            mAddress.setError(null);
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
                String AMPM;
                if (selectedHour < 12) {
                    AMPM = " AM";
                } else {
                    AMPM = " PM";
                }
                if (!isClosingTime) {
                    mOpeningTime.setText(selectedHour + ":" + selectedMinute);
                    isClosingTime = true;
                } else {
                    mClosingTime.setText(selectedHour + ":" + selectedMinute);
                    isClosingTime = false;
                }
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    private void registerUser(String email, String kitchenName, String password,
                              String contactNumber, String location, String kitchenPhoto,
                              String workingDays, String openingTime, String closingTime,
                              boolean deliveryStatus, String deliveryTime) {
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
                                System.out.println(request.getResponseText() + "HTTP_BAD_REQUEST");
                                break;
                            case HttpURLConnection.HTTP_UNAUTHORIZED:
                                AppGlobals.alertDialog(getActivity(), getString(R.string.register_failed), getString(R.string.check_password));
                                break;
                            case HttpURLConnection.HTTP_CREATED:
                                Toast.makeText(getActivity(), "Activation code has been sent to you! Please check your Email", Toast.LENGTH_SHORT).show();
                                System.out.println(request.getResponseText() + "working ");
                                try {
                                    JSONObject jsonObject = new JSONObject(request.getResponseText());
                                    String userId = jsonObject.getString(AppGlobals.KEY_USER_ID);
                                    String email = jsonObject.getString(AppGlobals.KEY_EMAIL);
                                    String kitchenName = jsonObject.getString(AppGlobals.KEY_KITCHEN_NAME);
                                    String contactNumber = jsonObject.getString(AppGlobals.KEY_CONTACT_NUMBER);
                                    String openingTime = jsonObject.getString(AppGlobals.KEY_OPENING_TIME);
                                    String closingTime = jsonObject.getString(AppGlobals.KEY_CLOSING_TIME);
                                    boolean deliveryStatus = jsonObject.getBoolean(AppGlobals.KEY_DELIVERY_STATUS);
                                    String KitchenDetailsId = jsonObject.getString(AppGlobals.KEY_KITCHEN_PROVIDERS_ID);
                                    String location = jsonObject.getString(AppGlobals.KEY_LOCATION);
                                    String KitchenImage = jsonObject.getString(AppGlobals.KEY_KITCHEN_IMAGE);
                                    String timeToFinish = jsonObject.getString(AppGlobals.KEY_TIME_TO_FINISH);
                                    String workingDays = jsonObject.getString(AppGlobals.KEY_WORKING_DAYS);

                                    //saving values
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_EMAIL, email);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_USER_ID, userId);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_KITCHEN_NAME, kitchenName);

                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_CONTACT_NUMBER, contactNumber);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_OPENING_TIME, openingTime);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_CLOSING_TIME, closingTime);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_DELIVERY_STATUS, String.valueOf(deliveryStatus));
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_KITCHEN_PROVIDERS_ID, KitchenDetailsId);

                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_LOCATION, location);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_KITCHEN_IMAGE, AppGlobals.SERVER_IP_FOR_IMAGE + KitchenImage);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_TIME_TO_FINISH, timeToFinish);
                                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_WORKING_DAYS, workingDays);
                                    Log.i("closingTime", " " + AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_CLOSING_TIME));
                                    AccountManagerActivity.getInstance().loadFragment(new AccountActivationCode());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case HttpURLConnection.HTTP_OK:
                                System.out.println(request.getResponseText() + "working ");
                                break;
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
        String dialogtext = "Registering kitchen...";
        if (method.equals("POST")) {
            url = String.format("%sregister", AppGlobals.BASE_URL);
            System.out.println("POST" + AppGlobals.isLogin());
            request.open(method, url);
        } else {
            dialogtext = "Updating...";
            url = String.format("%sme", AppGlobals.BASE_URL);
            request.open(method, url);
            request.setRequestHeader("Authorization", "Token " +
                    AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_TOKEN));

        }
        request.send(getRegisterData(email, kitchenName, password, contactNumber, location,
                kitchenPhoto, workingDays, openingTime, closingTime, deliveryStatus, deliveryTime));
        Helpers.showProgressDialog(getActivity(), dialogtext);
    }

    private FormData getRegisterData(String email, String kitchenName, String password,
                                     String contactNumber, String location, String kitchenPhoto,
                                     String workingDays, String openingTime, String closingTime,
                                     boolean deliveryStatus, String deliveryTime) {
        FormData formData = new FormData();
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            formData.append(FormData.TYPE_CONTENT_FILE, "photo", kitchenPhoto);
        }
        if (savedWorkingDyas != null && !savedWorkingDyas.isEmpty()) {
            formData.append(FormData.TYPE_CONTENT_TEXT, "working_days", workingDays);
        }

        formData.append(FormData.TYPE_CONTENT_TEXT, "opening_time", openingTime);
        formData.append(FormData.TYPE_CONTENT_TEXT, "closing_time", closingTime);
        formData.append(FormData.TYPE_CONTENT_TEXT, "delivery", String.valueOf(deliveryStatus));
        formData.append(FormData.TYPE_CONTENT_TEXT, "contact_number", contactNumber);
        formData.append(FormData.TYPE_CONTENT_TEXT, "location", location);
        formData.append(FormData.TYPE_CONTENT_TEXT, "time_to_finish", deliveryTime);
        formData.append(FormData.TYPE_CONTENT_TEXT, "type", "3");
        formData.append(FormData.TYPE_CONTENT_TEXT, "email", email);
        formData.append(FormData.TYPE_CONTENT_TEXT, "name", kitchenName);
        formData.append(FormData.TYPE_CONTENT_TEXT, "password", password);
        return formData;

    }
}
