package com.example.cityguide;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Bundle bundle;
    private Intent intent;
    private LatLng placeLocation, userLocation;
    private String placeName;
    private Button buttonAddToPlan;
    private ArrayList<Place> chosenPlaces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bundle = getIntent().getExtras();
        placeLocation = new LatLng(bundle.getDouble("placeLat"), bundle.getDouble("placeLng"));
        placeName = bundle.getString("placeName");
        buttonAddToPlan = (Button)findViewById(R.id.buttonAddToPlan);

        buttonAddToPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Intent intent2 = getIntent();
                chosenPlaces = intent2.getParcelableArrayListExtra("chosenPlaces");
                Bundle bundle = new Bundle();
                bundle.putString("chosenPlaceName", placeName);
                bundle.putDouble("chosenPlaceLat", placeLocation.latitude);
                bundle.putDouble("chosenPlaceLng", placeLocation.longitude);
                intent.putExtras(bundle);
                intent.putParcelableArrayListExtra("chosenPlaces", (ArrayList<? extends Parcelable>) chosenPlaces);
                startActivity(intent);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(placeLocation).title(placeName));
        mMap.setMinZoomPreference(15);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
    }
}
