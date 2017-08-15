package com.byteshaft.homemade.userActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.byteshaft.homemade.R;
import com.byteshaft.homemade.adapters.KitchenDishesDetailsAdapters;
import com.byteshaft.homemade.gettersetter.KitchenDishesDetails;
import com.byteshaft.homemade.utils.AppGlobals;
import com.byteshaft.homemade.utils.Helpers;
import com.byteshaft.requests.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by husnain on 7/24/17.
 */

public class KitchenDishesDetailsActivity extends AppCompatActivity implements
        HttpRequest.OnReadyStateChangeListener, HttpRequest.OnErrorListener {

    private ListView mKitchenDishesDetailsListView;
    private KitchenDishesDetailsAdapters kitchenDishesDetailsAdapters;
    private ArrayList<com.byteshaft.homemade.gettersetter.KitchenDishesDetails> kitchenDishesDetailsArrayList;
    private String mKitchenContact;
    private int mKitchenId;

    private HttpRequest request;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_details);
        mKitchenDishesDetailsListView = (ListView) findViewById(R.id.kitchen_details_list_view);
        kitchenDishesDetailsArrayList = new ArrayList<>();
        kitchenDishesDetailsAdapters = new KitchenDishesDetailsAdapters(KitchenDishesDetailsActivity.this, kitchenDishesDetailsArrayList);
        mKitchenDishesDetailsListView.setAdapter(kitchenDishesDetailsAdapters);

        mKitchenId = getIntent().getIntExtra("id", 0);
        mKitchenContact = getIntent().getStringExtra("contact_number");
        getAllKitchenDishes(mKitchenId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.phone_number:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mKitchenContact));
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAllKitchenDishes(int kitchenId) {
        request = new HttpRequest(this);
        request.setOnReadyStateChangeListener(this);
        request.setOnErrorListener(this);
        request.open("GET", String.format("%sfood-providers/%s/menu/", AppGlobals.BASE_URL, kitchenId));
        request.send();
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
                                KitchenDishesDetails kitchenDishesDetails = new KitchenDishesDetails();
                                kitchenDishesDetails.setDishPrice(jsonObject.getString("price"));
                                kitchenDishesDetails.setDishName(jsonObject.getString("name"));
                                kitchenDishesDetails.setDishImage(jsonObject.getString("image"));
                                kitchenDishesDetails.setDishDescriptions(jsonObject.getString("description"));
                                kitchenDishesDetailsArrayList.add(kitchenDishesDetails);
                                kitchenDishesDetailsAdapters.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
        }

    }
}
