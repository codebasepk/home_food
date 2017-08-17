package com.byteshaft.homemade.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.byteshaft.homemade.R;
import com.byteshaft.homemade.gettersetter.DishDetails;
import com.byteshaft.homemade.userActivities.ProductImageView;
import com.byteshaft.homemade.utils.AppGlobals;
import com.squareup.picasso.Picasso;

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
    private ProductImageView productImageView;

    public DishDetailsAdapters(Context context, Activity activity, ArrayList<DishDetails> dishDetailsArrayList) {
        super(context, R.layout.delegate_dish_details);
        this.activity = activity;
        this.dishDetailsArrayList = dishDetailsArrayList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.delegate_dish_details, parent, false);
            productImageView = new ProductImageView();
            viewHolder = new ViewHolder();
            viewHolder.mDishName = (TextView) convertView.findViewById(R.id.dish_name);
            viewHolder.mDishPrice = (TextView) convertView.findViewById(R.id.dish_price);
            viewHolder.mDishImage = (CircleImageView) convertView.findViewById(R.id.dish_image);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
          }

        final DishDetails dishDetails = dishDetailsArrayList.get(position);
        viewHolder.mDishName.setText(dishDetails.getDishName());
        viewHolder.mDishPrice.setText(dishDetails.getDishPrice());
        Picasso.with(AppGlobals.getContext()).load(dishDetails.getDishImage()).into(viewHolder.mDishImage);
        viewHolder.mDishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppGlobals.getContext(), productImageView.getClass());
                intent.putExtra("url", dishDetails.getDishImage());
                AppGlobals.getContext().startActivity(intent);
            }
        });

        return convertView;
    }





    @Override
    public int getCount() {
        return dishDetailsArrayList.size();
    }


    private class ViewHolder{
        TextView mDishName;
        TextView mDishPrice;
        CircleImageView mDishImage;
    }

}
