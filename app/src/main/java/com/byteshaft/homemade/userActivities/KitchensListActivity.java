package com.byteshaft.homemade.userActivities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.byteshaft.homemade.R;
import com.byteshaft.homemade.adapters.KitchenListAdapters;
import com.byteshaft.homemade.gettersetter.*;

import java.util.ArrayList;

/**
 * Created by husnain on 7/24/17.
 */

public class KitchensListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView mKitchenListView;
    private KitchenListAdapters mKitchenListAdapters;
    private ArrayList<KitchenList> kitchenListArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchens_list);
        mKitchenListView = (ListView) findViewById(R.id.kitchen_list);
        kitchenListArrayList = new ArrayList<>();
        mKitchenListAdapters = new KitchenListAdapters(KitchensListActivity.this, kitchenListArrayList);

        KitchenList kitchenList = new KitchenList();
        kitchenList.setKitchenName("Rice");
        kitchenList.setKitchenAllDishes("10");
        kitchenList.setKitchenLocation("abdali road cantt");
        kitchenList.setKitchenPhoneNumber("0676767");
        kitchenList.setKitchenImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        kitchenListArrayList.add(kitchenList);

        KitchenList kitchenList1 = new KitchenList();
        kitchenList1.setKitchenName("Rice");
        kitchenList1.setKitchenAllDishes("10");
        kitchenList1.setKitchenLocation("abdali road cantt");
        kitchenList1.setKitchenImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        kitchenListArrayList.add(kitchenList1);

        KitchenList kitchenList2 = new KitchenList();
        kitchenList2.setKitchenName("Rice");
        kitchenList2.setKitchenAllDishes("10");
        kitchenList2.setKitchenLocation("abdali road cantt");
        kitchenList2.setKitchenImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        kitchenListArrayList.add(kitchenList2);

        KitchenList kitchenList3 = new KitchenList();
        kitchenList3.setKitchenName("Rice");
        kitchenList3.setKitchenAllDishes("10");
        kitchenList3.setKitchenLocation("abdali road cantt");
        kitchenList3.setKitchenImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        kitchenListArrayList.add(kitchenList3);

        KitchenList kitchenList4 = new KitchenList();
        kitchenList4.setKitchenName("Rice");
        kitchenList4.setKitchenAllDishes("10");
        kitchenList4.setKitchenLocation("abdali road cantt");
        kitchenList4.setKitchenImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        kitchenListArrayList.add(kitchenList4);
        mKitchenListView.setAdapter(mKitchenListAdapters);

        mKitchenListView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, KitchenDishesDetailsActivity.class));

    }
}
