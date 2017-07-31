package com.byteshaft.homemade.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.byteshaft.homemade.R;
import com.byteshaft.homemade.gettersetter.KitchenDishesDetails;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by husnain on 7/26/17.
 */

public class KitchenDishesDetailsAdapters extends BaseAdapter {

    private ViewHolder viewHolder;
    private ArrayList<KitchenDishesDetails> kitchenDetailsArrayList;
    private Activity activity;

    public KitchenDishesDetailsAdapters(Activity activity, ArrayList<KitchenDishesDetails> kitchenDetailsArrayList) {
        this.activity = activity;
        this.kitchenDetailsArrayList = kitchenDetailsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.delegate_kitchen_dishes_details, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mDishName = (TextView) convertView.findViewById(R.id.dish_name);
            viewHolder.mDishPrice = (TextView) convertView.findViewById(R.id.dish_price);
            viewHolder.mDishImage = (CircleImageView) convertView.findViewById(R.id.dish_image);
            viewHolder.mDishDescriptions = (TextView) convertView.findViewById(R.id.dish_description);
            convertView.setTag(viewHolder);

        } else {
            KitchenDishesDetails kitchenDetails = kitchenDetailsArrayList.get(position);
            viewHolder.mDishName.setText(kitchenDetails.getDishName());
            viewHolder.mDishPrice.setText(kitchenDetails.getDishPrice());
            viewHolder.mDishDescriptions.setText(kitchenDetails.getDishDescriptions());
            viewHolder.mDishImage.setImageBitmap(kitchenDetails.getKitchenImage());
        }

        return convertView;
    }


    @Override
    public int getCount() {
        return kitchenDetailsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView mDishName;
        TextView mDishDescriptions;
        TextView mDishQuantity;
        TextView mDishPrice;
        CircleImageView mDishImage;
    }
}
