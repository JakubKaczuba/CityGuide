package com.example.cityguide;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

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

    private final String apikey = "AIzaSyDbplIQW6ZSSZ3ZggrsSqT1uxCO2syPbGM";
    private GeoApiContext geoApiContext;
    private ArrayList<Place> listOfPlaces;
    private int maxDistance = 6000;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfPlaces = new ArrayList<Place>();
        geoApiContext = new GeoApiContext.Builder()
                .apiKey(apikey)
                .build();



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

                return;
            }
            System.out.println("dupa");
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
        pobieranie danych miejsc i dodanie ich do listy
         */

        NearbySearchRequest nearby = new NearbySearchRequest(geoApiContext);
        nearby.radius(maxDistance);
        nearby.rankby(RankBy.PROMINENCE);
        nearby.type(PlaceType.RESTAURANT);
        nearby.location(currentLocation);



        nearby.setCallback(new PendingResult.Callback<PlacesSearchResponse>() {
            @Override
            public void onResult(PlacesSearchResponse result) {
                for(int i=0; i<result.results.length; i++) {
                    double distanceinMeters = distance(result.results[i].geometry.location.lat,
                            currentLocation.lat,
                            result.results[i].geometry.location.lng,
                            currentLocation.lng);

                    System.out.println(result.results[i].name); //sprawdzenie, czy aplikacja pobiera dane

                }
            }

            @Override
            public void onFailure(Throwable e) {
                System.out.println(e.toString());
            }
        });

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
}
