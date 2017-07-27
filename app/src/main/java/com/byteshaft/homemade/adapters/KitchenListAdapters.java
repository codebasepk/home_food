package com.byteshaft.homemade.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.byteshaft.homemade.R;
import com.byteshaft.homemade.gettersetter.KitchenList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by husnain on 7/26/17.
 */

public class KitchenListAdapters extends BaseAdapter{

    private ViewHolder viewHolder;
    private ArrayList<KitchenList> kitchenListArrayList;
    private Activity activity;

    public KitchenListAdapters(Activity activity, ArrayList<KitchenList> kitchenListArrayList) {
        this.activity = activity;
        this.kitchenListArrayList = kitchenListArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.delegate_kitchen_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mKitchenName = (TextView) convertView.findViewById(R.id.kitchen_name);
            viewHolder.mPhoneNumber = (TextView) convertView.findViewById(R.id.contact_number);
            viewHolder.mKitchenAllDishes = (TextView) convertView.findViewById(R.id.all_dishes_number);
            viewHolder.mDishImage = (CircleImageView) convertView.findViewById(R.id.kitchen_image);
            viewHolder.mKitchenLocation = (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(viewHolder);

        } else {
            KitchenList kitchenList = kitchenListArrayList.get(position);
            viewHolder.mKitchenName.setText(kitchenList.getKitchenName());
            viewHolder.mPhoneNumber.setText(kitchenList.getKitchenPhoneNumber());
            viewHolder.mKitchenAllDishes.setText(kitchenList.getKitchenAllDishes());
            viewHolder.mKitchenLocation.setText(kitchenList.getKitchenLocation());
            viewHolder.mDishImage.setImageBitmap(kitchenList.getKitchenImage());
        }

        return convertView;
    }


    @Override
    public int getCount() {
        return kitchenListArrayList.size();
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
        TextView mKitchenName;
        TextView mPhoneNumber;
        TextView mKitchenAllDishes;
        TextView mKitchenLocation;
        CircleImageView mDishImage;
    }
}
