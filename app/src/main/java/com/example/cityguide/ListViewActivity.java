package com.example.cityguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.maps.model.LatLng;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    private ListView listViewPlaces;
    private MyPlaceAdapter placeAdapter;
    private ArrayList<Place> places;
    private Intent intent;
    private LatLng currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Bundle bundle = getIntent().getExtras();
        currentPosition = new LatLng(bundle.getDouble("currentLat"), bundle.getDouble("currentLng"));

        intent = getIntent();

        places = intent.getParcelableArrayListExtra("places");


        listViewPlaces = (ListView)findViewById(R.id.listViewPlaces);
        placeAdapter = new MyPlaceAdapter(this, R.layout.row_of_listview, places);
        listViewPlaces.setAdapter(placeAdapter);

        listViewPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                Place place = (Place)listViewPlaces.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putDouble("placeLat", place.getLat());
                bundle.putDouble("placeLng", place.getLng());
                bundle.putString("placeName", place.getName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}
