package com.byteshaft.homemade.accountfragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.byteshaft.homemade.AddDishDetails;
import com.byteshaft.homemade.R;
import com.byteshaft.homemade.adapters.DishDetailsAdapters;
import com.byteshaft.homemade.gettersetter.DishDetails;
import com.byteshaft.homemade.userActivities.KitchenDishesDetailsActivity;

import java.util.ArrayList;

/**
 * Created by husnain on 7/26/17.
 */

public class KitchenDishesList extends Fragment implements AdapterView.OnItemClickListener {

    private View mBaseView;
    private ListView mDishesListView;
    private DishDetailsAdapters dishDetailsAdapters;
    private ArrayList<DishDetails> dishDetailsArrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_kitchen_dishes_list, container, false);
        mDishesListView = (ListView) mBaseView.findViewById(R.id.dish_list_view);
        dishDetailsArrayList = new ArrayList<>();
        dishDetailsAdapters = new DishDetailsAdapters(getActivity().getApplicationContext(),
                getActivity(), dishDetailsArrayList);
        DishDetails dishDetails = new DishDetails();
        dishDetails.setDishName("Rice");
        dishDetails.setDishPrice("100");
        dishDetails.setDishQuantity("10");
        dishDetails.setDishImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        dishDetailsArrayList.add(dishDetails);
        DishDetails dish = new DishDetails();
        dish.setDishName("BBQ");
        dish.setDishPrice("200");
        dish.setDishQuantity("20");
        dish.setDishImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        dishDetailsArrayList.add(dishDetails);
        DishDetails dish1 = new DishDetails();
        dish1.setDishName("BBQ");
        dish1.setDishPrice("200");
        dish1.setDishQuantity("20");
        dish1.setDishImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        dishDetailsArrayList.add(dishDetails);
        DishDetails dish2 = new DishDetails();
        dish2.setDishName("BBQ");
        dish2.setDishPrice("200");
        dish2.setDishQuantity("20");
        dish2.setDishImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        dishDetailsArrayList.add(dishDetails);
        DishDetails dish3 = new DishDetails();
        dish3.setDishName("BBQ");
        dish3.setDishPrice("200");
        dish3.setDishQuantity("20");
        dish3.setDishImage(BitmapFactory.decodeResource(getResources(), R.drawable.change_password));
        dishDetailsArrayList.add(dishDetails);
        mDishesListView.setAdapter(dishDetailsAdapters);

        mDishesListView.setOnItemClickListener(this);
        return mBaseView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(), AddDishDetails.class));
    }
}
