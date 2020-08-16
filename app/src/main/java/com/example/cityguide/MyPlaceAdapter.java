package com.example.cityguide;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class MyPlaceAdapter extends ArrayAdapter<Place> {

    private ArrayList<Place> places;
    private Context context;
    private int layoutRes;

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
            TextView textViewDistance = (TextView)v.findViewById(R.id.textViewDistanceInMeters);

            if(textViewName != null) {
                textViewName.setText(p.getName());
            }
            if(textViewAddress != null) {
                textViewAddress.setText(p.getAddress());
            }
            if(textViewRating != null) {
                textViewRating.setText("Rating: " + String.valueOf(p.getRating())+ "/5");
            }
            if(textViewDistance != null) {
                textViewDistance.setText("Distance: " + String.valueOf((int)p.getDistanceInMeters())+ "m");
            }

        }
        return v;
    }
}
