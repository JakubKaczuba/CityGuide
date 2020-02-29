package com.example.cityguide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;

public class MainActivity extends AppCompatActivity {

    private final  String apikey = "AIzaSyDbplIQW6ZSSZ3ZggrsSqT1uxCO2syPbGM";
    private GeoApiContext geoApiContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Places.initialize(getApplicationContext(), apikey );
        //PlacesClient placesClient = Places.createClient(this);

        geoApiContext = new GeoApiContext.Builder()
                .apiKey(apikey)
                .build();

        NearbySearchRequest nearby = new NearbySearchRequest(geoApiContext);
        
    }
}
