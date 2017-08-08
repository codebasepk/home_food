package com.byteshaft.homemade.foodProvidersFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.byteshaft.homemade.MainActivity;
import com.byteshaft.homemade.R;
import com.byteshaft.homemade.adapters.DishDetailsAdapters;
import com.byteshaft.homemade.gettersetter.DishDetails;
import com.byteshaft.homemade.utils.AppGlobals;
import com.byteshaft.homemade.utils.Helpers;
import com.byteshaft.requests.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;


/**
 * Created by husnain on 7/26/17.
 */

public class KitchenDishesList extends Fragment implements AdapterView.OnItemClickListener,
        HttpRequest.OnReadyStateChangeListener, HttpRequest.OnErrorListener, AdapterView.OnItemLongClickListener {

    private View mBaseView;
    private ListView mDishesListView;
    private DishDetailsAdapters dishDetailsAdapters;
    private ArrayList<DishDetails> dishDetailsArrayList;

    private HttpRequest request;

    private int positionOfDelete;
    private int currentArraySize = 0;
    private boolean isLongPress = false;
    public static boolean updated = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_kitchen_dishes_list, container, false);
        setHasOptionsMenu(true);
        mDishesListView = (ListView) mBaseView.findViewById(R.id.dish_list_view);
        dishDetailsArrayList = new ArrayList<>();
        dishDetailsAdapters = new DishDetailsAdapters(getActivity().getApplicationContext(),
                getActivity(), dishDetailsArrayList);
        mDishesListView.setAdapter(dishDetailsAdapters);
        gettingDishes();
        mDishesListView.setOnItemClickListener(this);
        mDishesListView.setOnItemLongClickListener(this);

        return mBaseView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isLongPress) {
            DishDetails dishDetails = dishDetailsArrayList.get(position);
            AddDishDetails addDishDetails = new AddDishDetails();
            Bundle bundle = new Bundle();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            bundle.putInt("id", dishDetails.getId());
            bundle.putString("name", dishDetails.getDishName());
            bundle.putString("price", dishDetails.getDishPrice());
            bundle.putString("description", dishDetails.getDescriptions());
            System.out.println(dishDetails.getDishImage() + "dish image");
            bundle.putString("image", dishDetails.getDishImage());
            addDishDetails.setArguments(bundle);
            fragmentTransaction.addToBackStack("update");
            fragmentTransaction.replace(R.id.container, addDishDetails);
            fragmentTransaction.commit();

        } else {
            isLongPress = false;
        }

    }

    private void gettingDishes() {
        request = new HttpRequest(getActivity());
        request.setOnReadyStateChangeListener(this);
        request.setOnErrorListener(this);
        request.open("GET", String.format("%sdishes/", AppGlobals.BASE_URL));
        request.setRequestHeader("Authorization", "Token " +
                AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_TOKEN));
        request.send();
    }

    @Override
    public void onError(HttpRequest httpRequest, int readyState, short i1, Exception exception) {
        switch (readyState) {
            case HttpRequest.ERROR_CONNECTION_TIMED_OUT:
                Helpers.showSnackBar(getView(), "connection time out");
                break;
            case HttpRequest.ERROR_NETWORK_UNREACHABLE:
                Helpers.showSnackBar(getView(), exception.getLocalizedMessage());
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("All Dishes");
        isLongPress = false;
        if (dishDetailsArrayList.size() > currentArraySize || updated) {
            dishDetailsAdapters.notifyDataSetChanged();
            if (updated) {
                updated = false;
            } else {
                currentArraySize = dishDetailsArrayList.size();
            }
        }
    }

    @Override
    public void onReadyStateChange(HttpRequest httpRequest, int readyState) {
        switch (readyState) {
            case HttpRequest.STATE_DONE:
                Helpers.dismissProgressDialog();
                switch (request.getStatus()) {
                    case HttpURLConnection.HTTP_OK:
                        System.out.println(request.getResponseText() + "working ");
                        try {
                            JSONArray jsonArray = new JSONArray(request.getResponseText());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                DishDetails dishDetails = new DishDetails();
                                dishDetails.setId(jsonObject.getInt("id"));
                                dishDetails.setDishName(jsonObject.getString("name"));
                                dishDetails.setDishImage(jsonObject.getString("image"));
                                dishDetails.setDishPrice(jsonObject.getString("price"));
                                dishDetails.setDescriptions(jsonObject.getString("description"));
                                dishDetailsArrayList.add(dishDetails);
                                dishDetailsAdapters.notifyDataSetChanged();
                            }
                            currentArraySize = dishDetailsArrayList.size();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        isLongPress = true;
        positionOfDelete = position;
        final DishDetails dishDetails = dishDetailsArrayList.get(position);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Do you want to delete this dish?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteTable(dishDetails.getId());
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return true;
    }

    private void deleteTable(int id) {
        Log.i("TAG", "id " + id);
        HttpRequest request = new HttpRequest(getActivity().getApplicationContext());
        request.setOnReadyStateChangeListener(new HttpRequest.OnReadyStateChangeListener() {
            @Override
            public void onReadyStateChange(HttpRequest httpRequest, int i) {
                switch (i) {
                    case HttpRequest.STATE_DONE:
                        Helpers.dismissProgressDialog();
                        switch (httpRequest.getStatus()) {
                            case HttpURLConnection.HTTP_NO_CONTENT:
                                Log.i("TAG", "204");
                                dishDetailsArrayList.remove(positionOfDelete);
                                dishDetailsAdapters.notifyDataSetChanged();
                                Helpers.showSnackBar(getView(), R.string.dish_delete);
                                break;
                        }
                }
            }
        });
        request.setOnErrorListener(this);
        request.open("DELETE", String.format("%sdishes/%s/", AppGlobals.BASE_URL, id));
        request.setRequestHeader("Authorization", "Token " +
                AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_TOKEN));
        request.send();
    }
}
