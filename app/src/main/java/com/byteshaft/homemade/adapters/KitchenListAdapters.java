package com.byteshaft.homemade.adapters;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.byteshaft.homemade.R;
import com.byteshaft.homemade.gettersetter.KitchenList;
import com.byteshaft.homemade.utils.AppGlobals;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by husnain on 7/26/17.
 */

public class KitchenListAdapters extends BaseAdapter {

    private ViewHolder viewHolder;
    private ArrayList<KitchenList> kitchenListArrayList;
    private Activity activity;

    private StringBuilder result;

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
            viewHolder.mKitchenImage = (CircleImageView) convertView.findViewById(R.id.kitchen_image);
            viewHolder.mKitchenLocation = (TextView) convertView.findViewById(R.id.address);
            viewHolder.mOpeningTime = (TextView) convertView.findViewById(R.id.opening_time);
            viewHolder.mClosingTime = (TextView) convertView.findViewById(R.id.closing_time);
            viewHolder.mOrderTime = (TextView) convertView.findViewById(R.id.order_time);
            viewHolder.mWorkingDays = (TextView) convertView.findViewById(R.id.working_days);
            viewHolder.mDeliverable = (TextView) convertView.findViewById(R.id.delivery_status);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        KitchenList kitchenList = kitchenListArrayList.get(position);
        viewHolder.mKitchenName.setText(kitchenList.getKitchenName());
        viewHolder.mPhoneNumber.setText(" " + kitchenList.getKitchenPhoneNumber());
        String[] latlng = kitchenList.getKitchenLocation().split(",");
        double latitude = Double.parseDouble(latlng[0]);
        double longitude = Double.parseDouble(latlng[1]);
        getAddress(latitude, longitude);
        viewHolder.mKitchenLocation.setText(" " + result.toString());
        viewHolder.mOpeningTime.setText(" " + kitchenList.getOpeningTime());
        viewHolder.mClosingTime.setText(" " + kitchenList.getClosingTime());
        viewHolder.mOrderTime.setText(" " + kitchenList.getOrderTime());
        viewHolder.mDeliverable.setText( " " + String.valueOf(kitchenList.isDeliverable()));
        viewHolder.mWorkingDays.setText(" " + kitchenList.getWorkingDays());
        Picasso.with(AppGlobals.getContext()).load(kitchenList.getKitchenImage()).into(viewHolder.mKitchenImage);


        return convertView;
    }

    private void getAddress(double latitude, double longitude) {
        result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(AppGlobals.getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append(" ").append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
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

    private class ViewHolder {
        TextView mKitchenName;
        TextView mPhoneNumber;
        TextView mKitchenLocation;
        TextView mOpeningTime;
        TextView mClosingTime;
        TextView mOrderTime;
        TextView mWorkingDays;
        TextView mDeliverable;
        CircleImageView mKitchenImage;
    }

}
