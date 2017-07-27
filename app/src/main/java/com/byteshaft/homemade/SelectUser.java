package com.byteshaft.homemade;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.byteshaft.homemade.accountfragments.AccountManagerActivity;
import com.byteshaft.homemade.accountfragments.Login;
import com.byteshaft.homemade.userActivities.KitchensListActivity;

/**
 * Created by husnain on 7/24/17.
 */

public class SelectUser extends AppCompatActivity implements View.OnClickListener{

    private Button mUserButton;
    private Button mFoodProviderButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        mUserButton = (Button) findViewById(R.id.user_button);
        mFoodProviderButton = (Button) findViewById(R.id.food_provider_button);
        mUserButton.setOnClickListener(this);
        mFoodProviderButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_button:
                startActivity(new Intent(this, KitchensListActivity.class));
                break;
            case R.id.food_provider_button:
                startActivity(new Intent(this, AccountManagerActivity.class));
                break;
        }
    }
}
