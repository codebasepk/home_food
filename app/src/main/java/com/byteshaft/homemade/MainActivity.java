package com.byteshaft.homemade;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.byteshaft.homemade.accountfragments.AccountManagerActivity;
import com.byteshaft.homemade.accountfragments.ChangePassword;
import com.byteshaft.homemade.accountfragments.UpdateProfile;
import com.byteshaft.homemade.foodProvidersFragments.KitchenDishesList;
import com.byteshaft.homemade.accountfragments.Login;
import com.byteshaft.homemade.accountfragments.SignUp;
import com.byteshaft.homemade.foodProvidersFragments.AddDishDetails;
import com.byteshaft.homemade.utils.AppGlobals;
import com.byteshaft.homemade.utils.Helpers;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static MainActivity sInstance;
    public static MainActivity getInstance() {
        return sInstance;
    }

    public static MenuItem sMenuItem;
    private ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (AccountManagerActivity.getInstance() != null) {
            AccountManagerActivity.getInstance().finish();
        }
        sInstance = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View headerView;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        TextView name = (TextView) headerView.findViewById(R.id.name);
        TextView email = (TextView) headerView.findViewById(R.id.email);
        CircleImageView kitchenImage = (CircleImageView) headerView.findViewById(R.id.nav_imageView);
        name.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_KITCHEN_NAME));
        email.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL));

        if (AppGlobals.isLogin() && AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_SERVER_IMAGE) != null) {
            String url = String.format("%s" + AppGlobals
                    .getStringFromSharedPreferences(AppGlobals.KEY_SERVER_IMAGE), AppGlobals.SERVER_IP_FOR_IMAGE);
            System.out.println(url + "image server");
            Helpers.getBitMap(url, kitchenImage);
        }
        loadFragment(new KitchenDishesList());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void showHamburger() {
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_dish) {
            loadFragmentWithBackStack(new AddDishDetails());

        } else if (id == R.id.update_profile) {
            loadFragment(new UpdateProfile());

        }  else if (id == R.id.change_password) {
            loadFragment(new ChangePassword());

        }
        else if (id == R.id.admin_logout) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.confirmation_title);
            alertDialogBuilder.setMessage(R.string.logout_dialog)
                    .setCancelable(false).setPositiveButton(R.string.button_yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            AppGlobals.clearSettings();
                            loadFragment(new Login());
                        }
                    });
            alertDialogBuilder.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.container, fragment);
        tx.commit();
    }

    public void loadFragmentWithBackStack(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.container, fragment, backStateName);
        fragmentTransaction.addToBackStack(backStateName);
        fragmentTransaction.commit();
    }
}
