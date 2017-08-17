package com.byteshaft.homemade.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.byteshaft.homemade.R;
import com.byteshaft.homemade.gettersetter.KitchenDishesDetails;
import com.byteshaft.homemade.userActivities.ProductImageView;
import com.byteshaft.homemade.utils.AppGlobals;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by husnain on 7/26/17.
 */

public class KitchenDishesDetailsAdapters extends BaseAdapter {

    private ViewHolder viewHolder;
    private ArrayList<KitchenDishesDetails> kitchenDetailsArrayList;
    private Activity activity;
    private ProductImageView productImageView;

    public KitchenDishesDetailsAdapters(Activity activity, ArrayList<KitchenDishesDetails> kitchenDetailsArrayList) {
        this.activity = activity;
        this.kitchenDetailsArrayList = kitchenDetailsArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.delegate_kitchen_dishes_details, parent, false);
            productImageView = new ProductImageView();
            viewHolder = new ViewHolder();
            viewHolder.mDishName = (TextView) convertView.findViewById(R.id.dish_name);
            viewHolder.mDishPrice = (TextView) convertView.findViewById(R.id.dish_price);
            viewHolder.mDishImage = (CircleImageView) convertView.findViewById(R.id.dish_image);
            viewHolder.mDishDescriptions = (TextView) convertView.findViewById(R.id.dish_description);
            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }
        final KitchenDishesDetails kitchenDetails = kitchenDetailsArrayList.get(position);
        viewHolder.mDishName.setText(kitchenDetails.getDishName());
        viewHolder.mDishPrice.setText(kitchenDetails.getDishPrice());
        viewHolder.mDishDescriptions.setText(kitchenDetails.getDishDescriptions());
        Picasso.with(AppGlobals.getContext()).load(kitchenDetails.getDishImage()).into(viewHolder.mDishImage);
        viewHolder.mDishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppGlobals.getContext(), productImageView.getClass());
                intent.putExtra("url", kitchenDetails.getDishImage());
                AppGlobals.getContext().startActivity(intent);
            }
        });

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

    private class ViewHolder {
        TextView mDishName;
        TextView mDishDescriptions;
        TextView mDishPrice;
        CircleImageView mDishImage;
    }
}
