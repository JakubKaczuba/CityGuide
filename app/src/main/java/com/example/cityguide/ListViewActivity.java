package com.example.cityguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    private ListView listViewPlaces;
    private MyPlaceAdapter placeAdapter;
    private ArrayList<Place> places;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        intent = getIntent();

        places = intent.getParcelableArrayListExtra("places");

        listViewPlaces = (ListView)findViewById(R.id.listViewPlaces);
        placeAdapter = new MyPlaceAdapter(this, R.layout.row_of_listview, places);
        listViewPlaces.setAdapter(placeAdapter);

    }
}
