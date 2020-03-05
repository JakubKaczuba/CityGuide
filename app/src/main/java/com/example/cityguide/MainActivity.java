package com.example.cityguide;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String apikey = "AIzaSyAMr-S8M46kUZRjzXBhtfl9hmpgZcjqPYU";
    private GeoApiContext geoApiContext;
    private ArrayList<Place> listOfPlaces;
    private ImageButton buttonMuseum, buttonChurch, buttonRestaurant, buttonCafe;
    private TextView textViewDistance;
    private SeekBar seekBarDistance;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonMuseum = (ImageButton)findViewById(R.id.imageButtonMuseum);
        buttonChurch = (ImageButton)findViewById(R.id.imageButtonChurch);
        buttonRestaurant = (ImageButton)findViewById(R.id.imageButtonRestaurant);
        buttonCafe = (ImageButton)findViewById(R.id.imageButtonCafe);
        textViewDistance = (TextView)findViewById(R.id.textViewDistance);
        seekBarDistance = (SeekBar)findViewById(R.id.seekBarDistance);

        listOfPlaces = new ArrayList<Place>();
        geoApiContext = new GeoApiContext.Builder()
                .apiKey(apikey)
                .build();

        NearbySearchRequest nearby = new NearbySearchRequest(geoApiContext);

        /*
        obsługa seekBar
         */

        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                refreshSeekBarText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*
        wyznaczanie lokalizacji użytkownika
         */

        boolean gps_enabled = false;
        boolean network_enabled = false;

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location net_loc = null, gps_loc = null, finalLoc = null;

        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
            gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if(network_enabled) {
            net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if(gps_loc != null && net_loc != null) {

            if(gps_loc.getAccuracy() > net_loc.getAccuracy()) {
                finalLoc = net_loc;
            }
            else {
                finalLoc = gps_loc;
            }
        }
        else {
            if(gps_loc != null) {
                finalLoc = gps_loc;
            }
            else if(net_loc != null) {
                finalLoc = net_loc;
            }
        }

        final LatLng currentLocation = new LatLng(finalLoc.getLatitude(), finalLoc.getLongitude());



        /*
        obsługa kliknięcia przycisków
         */

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlaceType placeType;

                if(v == buttonMuseum) {
                    placeType = PlaceType.MUSEUM;
                }
                else if(v == buttonChurch) {
                    placeType = PlaceType.CHURCH;
                }
                else if(v == buttonRestaurant) {
                    placeType = PlaceType.RESTAURANT;
                }
                else {
                    placeType = PlaceType.CAFE;
                }

                setNearbyParameters(placeType, seekBarDistance.getProgress(), nearby, currentLocation);

                nearby.setCallback(new PendingResult.Callback<PlacesSearchResponse>() {
                    @Override
                    public void onResult(PlacesSearchResponse result) {
                        for(int i=0; i<result.results.length; i++) {
                            double distanceinMeters = distance(result.results[i].geometry.location.lat,
                                    currentLocation.lat,
                                    result.results[i].geometry.location.lng,
                                    currentLocation.lng);

                            //System.out.println(result.results[i].vicinity); //sprawdzenie, czy aplikacja pobiera dane


                            listOfPlaces.add(new Place(result.results[i].name,
                                    result.results[i].geometry.location.lat,
                                    result.results[i].geometry.location.lng,
                                    result.results[i].rating,
                                    result.results[i].vicinity,
                                    result.results[i].placeId));
                        }

                        Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
                        startActivity(new Intent(getApplicationContext(), ListViewActivity.class)
                                .putParcelableArrayListExtra("places", (ArrayList<? extends Parcelable>) listOfPlaces));


                    }
                    @Override
                    public void onFailure(Throwable e) {

                        System.out.println(e.toString());
                    }
                });

            }
        };

        buttonMuseum.setOnClickListener(onClick);
        buttonChurch.setOnClickListener(onClick);
        buttonRestaurant.setOnClickListener(onClick);
        buttonCafe.setOnClickListener(onClick);
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return distance;
    }

    public static void setNearbyParameters(PlaceType placeType, int maxDistance, NearbySearchRequest nearby, LatLng currentLocation) {
        nearby.radius(maxDistance);
        nearby.rankby(RankBy.PROMINENCE);
        nearby.type(placeType);
        nearby.location(currentLocation);
    }

    public void refreshSeekBarText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewDistance.setText("Set radius of search: " + seekBarDistance.getProgress() + " meters");
                textViewDistance.invalidate();
            }
        });
    }
}
