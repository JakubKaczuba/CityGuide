package com.example.cityguide;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.google.maps.model.LatLng;
import java.util.ArrayList;
import java.util.Collections;

public class ListViewActivity extends AppCompatActivity {

    private ListView listViewPlaces;
    private MyPlaceAdapter placeAdapter;
    private ArrayList<Place> places, chosenPlaces;
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
        chosenPlaces = intent.getParcelableArrayListExtra("chosenPlaces");
        sortByRating(places, 0, places.size()-1);
        Collections.reverse(places);
        listViewPlaces = (ListView) findViewById(R.id.listViewPlaces);
        placeAdapter = new MyPlaceAdapter(this, R.layout.row_of_listview, places);
        listViewPlaces.setAdapter(placeAdapter);

        listViewPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                Place place = (Place) listViewPlaces.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putDouble("placeLat", place.getLat());
                bundle.putDouble("placeLng", place.getLng());
                bundle.putString("placeName", place.getName());
                intent.putExtras(bundle);
                intent.putParcelableArrayListExtra("chosenPlaces", (ArrayList<? extends Parcelable>) chosenPlaces);
                startActivity(intent);
            }
        });

    }

    /*
    funkcja sortująca korzystająca z algorytmu sortowania szybkiego
     */

    private static void sortByRating(ArrayList<Place> places, int left, int right)
    {
        int i, j;
        Place x, y;
        i = left; j = right;
        x = places.get((left+right)/2);
        do {
            while((places.get(i).getRating() < x.getRating()) && (i < right)) i++;
            while((x.getRating() < places.get(j).getRating()) && (j > left)) j--;
            if(i <= j) {
                y = places.get(i);
                places.set(i, places.get(j));
                places.set(j, y);
                i++; j--;
            }
        } while(i <= j);
        if(left < j) sortByRating(places, left, j);
        if(i < right) sortByRating(places, i, right);
    }

}



