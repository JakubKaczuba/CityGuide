package com.example.cityguide;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MyPlaceAdapter extends ArrayAdapter<Place> {

    private ArrayList<Place> places;
    private Context context;
    private int layoutRes;

    private static class ViewHolder {
        TextView tvName;
        TextView tvAddress;
        TextView tvRating;
        TextView tvDistance;
    }

    public MyPlaceAdapter(Context context, int layoutRes, ArrayList<Place> places) {
        super(context, layoutRes, places);
        this.layoutRes = layoutRes;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            v = layoutInflater.inflate(R.layout.row_of_listview, null);
        }

        Place p = getItem(position);

        if(p != null) {
            TextView textViewName = (TextView)v.findViewById(R.id.textViewName);
            TextView textViewAddress = (TextView)v.findViewById(R.id.textViewAddress);
            TextView textViewRating = (TextView)v.findViewById(R.id.textViewRating);
            TextView textViewDistance = (TextView)v.findViewById(R.id.textViewDistance);

            if(textViewName != null) {
                textViewName.setText(p.getName());
            }
            if(textViewAddress != null) {
                textViewAddress.setText(p.getAddress());
            }
            if(textViewRating != null) {
                textViewRating.setText(String.valueOf(p.getRating()));
            }

        }
        return v;
    }
}
