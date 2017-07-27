package com.byteshaft.homemade.userActivities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.byteshaft.homemade.AddDishDetails;
import com.byteshaft.homemade.R;
import com.byteshaft.homemade.adapters.KitchenDishesDetailsAdapters;

import java.util.ArrayList;

/**
 * Created by husnain on 7/24/17.
 */

public class KitchenDishesDetailsActivity extends AppCompatActivity {

    private ListView mKitchenDishesDetailsListView;
    private KitchenDishesDetailsAdapters kitchenDishesDetailsAdapters;
    private ArrayList<com.byteshaft.homemade.gettersetter.KitchenDishesDetails> kitchenDishesDetailsArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_details);
        mKitchenDishesDetailsListView = (ListView) findViewById(R.id.kitchen_details_list_view);
        kitchenDishesDetailsArrayList = new ArrayList<>();
        kitchenDishesDetailsAdapters = new KitchenDishesDetailsAdapters(KitchenDishesDetailsActivity.this, kitchenDishesDetailsArrayList);
        com.byteshaft.homemade.gettersetter.KitchenDishesDetails kitchenDishesDetails = new com.byteshaft.homemade.gettersetter.KitchenDishesDetails();
        kitchenDishesDetails.setDishName("Rice");
        kitchenDishesDetails.setDishPrice("100");
        kitchenDishesDetails.setDishQuantity("10");
        kitchenDishesDetails.setKitchenImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        kitchenDishesDetailsArrayList.add(kitchenDishesDetails);

        com.byteshaft.homemade.gettersetter.KitchenDishesDetails kitchenDishesDetails1 = new com.byteshaft.homemade.gettersetter.KitchenDishesDetails();
        kitchenDishesDetails1.setDishName("BBQ");
        kitchenDishesDetails1.setDishPrice("200");
        kitchenDishesDetails1.setDishQuantity("20");
        kitchenDishesDetails1.setKitchenImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        kitchenDishesDetailsArrayList.add(kitchenDishesDetails1);

        com.byteshaft.homemade.gettersetter.KitchenDishesDetails kitchenDishesDetails2 = new com.byteshaft.homemade.gettersetter.KitchenDishesDetails();
        kitchenDishesDetails2.setDishName("BBQ");
        kitchenDishesDetails2.setDishPrice("200");
        kitchenDishesDetails2.setDishQuantity("20");
        kitchenDishesDetails2.setKitchenImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        kitchenDishesDetailsArrayList.add(kitchenDishesDetails2);

        com.byteshaft.homemade.gettersetter.KitchenDishesDetails kitchenDishesDetails3 = new com.byteshaft.homemade.gettersetter.KitchenDishesDetails();
        kitchenDishesDetails3.setDishName("BBQ");
        kitchenDishesDetails3.setDishPrice("200");
        kitchenDishesDetails3.setDishQuantity("20");
        kitchenDishesDetails3.setKitchenImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        kitchenDishesDetailsArrayList.add(kitchenDishesDetails3);

        com.byteshaft.homemade.gettersetter.KitchenDishesDetails kitchenDishesDetails4 = new com.byteshaft.homemade.gettersetter.KitchenDishesDetails();
        kitchenDishesDetails4.setDishName("BBQ");
        kitchenDishesDetails4.setDishPrice("200");
        kitchenDishesDetails4.setDishQuantity("20");
        kitchenDishesDetails4.setKitchenImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        kitchenDishesDetailsArrayList.add(kitchenDishesDetails3);
        mKitchenDishesDetailsListView.setAdapter(kitchenDishesDetailsAdapters);
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
                intent.setData(Uri.parse("tel:+923120676767"));
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
