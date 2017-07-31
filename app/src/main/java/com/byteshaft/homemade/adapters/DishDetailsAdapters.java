package com.byteshaft.homemade.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.byteshaft.homemade.R;
import com.byteshaft.homemade.gettersetter.DishDetails;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by husnain on 7/26/17.
 */

public class DishDetailsAdapters extends ArrayAdapter<String> {

    private ViewHolder viewHolder;
    private ArrayList<DishDetails> dishDetailsArrayList;
    private Activity activity;

    public DishDetailsAdapters(Context context, Activity activity, ArrayList<DishDetails> dishDetailsArrayList) {
        super(context, R.layout.delegate_dish_details);
        this.activity = activity;
        this.dishDetailsArrayList = dishDetailsArrayList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.delegate_dish_details, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mDishName = (TextView) convertView.findViewById(R.id.dish_name);
            viewHolder.mDishPrice = (TextView) convertView.findViewById(R.id.dish_price);
            viewHolder.mDishImage = (CircleImageView) convertView.findViewById(R.id.dish_image);
            convertView.setTag(viewHolder);

        } else {
            DishDetails dishDetails = dishDetailsArrayList.get(position);
            viewHolder.mDishName.setText(dishDetails.getDishName());
            viewHolder.mDishPrice.setText(dishDetails.getDishPrice());
            viewHolder.mDishQuantity.setText(dishDetails.getDishQuantity());
            viewHolder.mDishImage.setImageBitmap(dishDetails.getDishImage());
        }

        return convertView;
    }


    @Override
    public int getCount() {
        return dishDetailsArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView mDishName;
        TextView mDishPrice;
        TextView mDishQuantity;
        CircleImageView mDishImage;
    }
}
