package com.byteshaft.homemade.accountfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.byteshaft.homemade.MainActivity;
import com.byteshaft.homemade.R;
import com.byteshaft.homemade.utils.AppGlobals;
import com.byteshaft.homemade.utils.Helpers;
import com.byteshaft.requests.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;


public class AccountActivationCode extends Fragment implements View.OnClickListener,
        HttpRequest.OnReadyStateChangeListener, HttpRequest.OnErrorListener {

    private View mBaseView;

    private EditText mEmail;
    private EditText mVerificationCode;
    private Button mLoginButton;

    private String mEmailString;
    private String mVerificationCodeString;

    private HttpRequest request;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_account_activation_code, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(getResources().getString(R.string.account_activation));
        setHasOptionsMenu(true);
        mEmail = (EditText) mBaseView.findViewById(R.id.email_edit_text);
        mVerificationCode = (EditText) mBaseView.findViewById(R.id.verification_code);
        mLoginButton = (Button) mBaseView.findViewById(R.id.button_activate);

        mEmail.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL));
        mEmailString = AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL);
        mEmail.setEnabled(false);

        mLoginButton.setOnClickListener(this);
        return mBaseView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_activate:
                if (validate()) {
                    activateUser(mEmailString, mVerificationCodeString);
                }

                break;

        }
    }

    public boolean validate() {
        boolean valid = true;
        mEmailString = mEmail.getText().toString();
        mVerificationCodeString = mVerificationCode.getText().toString();

        System.out.println(mEmailString);
        System.out.println(mVerificationCodeString);

        if (mEmailString.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailString).matches()) {
            mEmail.setError(getString(R.string.provide_email));
            valid = false;
        } else {
            mEmail.setError(null);
        }
        if (mVerificationCodeString.trim().isEmpty() || mVerificationCodeString.length() < 6) {
            mVerificationCode.setError(getString(R.string.verification_code_length));
            valid = false;
        } else {
            mVerificationCode.setError(null);
        }
        return valid;
    }

    private void activateUser(String email, String emailOtp) {
        request = new HttpRequest(getActivity());
        request.setOnReadyStateChangeListener(this);
        request.setOnErrorListener(this);
        request.open("POST", String.format("%sactivate", AppGlobals.BASE_URL));
        request.send(getUserActivationData(email, emailOtp));
        Helpers.showProgressDialog(getActivity(), getString(R.string.activating_user));
    }


    private String getUserActivationData(String email, String emailOtp) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("email_otp", emailOtp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void onReadyStateChange(HttpRequest request, int readyState) {
        switch (readyState) {
            case HttpRequest.STATE_DONE:
                Log.i("TAG", "dismisss");
                Helpers.dismissProgressDialog();
                switch (request.getStatus()) {
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        Toast.makeText(getActivity(), R.string.correct_account_verification_code, Toast.LENGTH_LONG).show();
                        break;
                    case HttpRequest.ERROR_NETWORK_UNREACHABLE:
                        AppGlobals.alertDialog(getActivity(), getString(R.string.sending_failed), getString(R.string.check_internet));
                        break;
                    case HttpURLConnection.HTTP_FORBIDDEN:
                        AppGlobals.alertDialog(getActivity(), getString(R.string.acticvtion_failed), getString(R.string.deactiveed_by_admin));
                        break;
                    case HttpURLConnection.HTTP_OK:
                        try {
                            JSONObject jsonObject = new JSONObject(request.getResponseText());
                            String userId = jsonObject.getString(AppGlobals.KEY_USER_ID);
                            String token = jsonObject.getString(AppGlobals.KEY_TOKEN);
                            String email = jsonObject.getString(AppGlobals.KEY_EMAIL);
                            String kitchenName = jsonObject.getString(AppGlobals.KEY_KITCHEN_NAME);
                            String contactNumber = jsonObject.getString(AppGlobals.KEY_CONTACT_NUMBER);
                            String openingTime = jsonObject.getString(AppGlobals.KEY_OPENING_TIME);
                            String closingTime = jsonObject.getString(AppGlobals.KEY_CLOSING_TIME);
                            boolean deliveryStatus = jsonObject.getBoolean(AppGlobals.KEY_DELIVERY_STATUS);
                            String KitchenDetailsId = jsonObject.getString(AppGlobals.KEY_KITCHEN_PROVIDERS_ID);
                            String location = jsonObject.getString(AppGlobals.KEY_LOCATION);
                            String KitchenImage = jsonObject.getString(AppGlobals.KEY_SERVER_IMAGE);
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
                            AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_SERVER_IMAGE, KitchenImage);
                            AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_TIME_TO_FINISH, timeToFinish);
                            AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_WORKING_DAYS, workingDays);
                            AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_TOKEN, token);
                            Log.i("token", " " + AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_TOKEN));
                            AppGlobals.loginState(true);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
        }

    }

    @Override
    public void onError(HttpRequest request, int readyState, short error, Exception exception) {
        Helpers.dismissProgressDialog();
        switch (readyState) {
            case HttpRequest.ERROR_CONNECTION_TIMED_OUT:
                Helpers.showSnackBar(getView(), getString(R.string.connection_time_out));
                break;
            case HttpRequest.ERROR_NETWORK_UNREACHABLE:
                Helpers.showSnackBar(getView(), exception.getLocalizedMessage());
                break;
        }
    }
}
