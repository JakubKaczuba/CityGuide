package com.example.cityguide;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String apikey = "AIzaSyAN5D5ckDw0_G3rJEkwznCU8S6ITCFsb1U";
    private GeoApiContext geoApiContext;
    private ArrayList<Place> listOfPlaces, chosenPlaces;
    private ImageButton buttonMuseum, buttonChurch, buttonRestaurant, buttonCafe;
    private TextView textViewDistance;
    private SeekBar seekBarDistance;
    private Place chosenPlace;
    private Bundle bundle1;
    private Button buttonCalculateRoute;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonMuseum = (ImageButton) findViewById(R.id.imageButtonMuseum);
        buttonChurch = (ImageButton) findViewById(R.id.imageButtonChurch);
        buttonRestaurant = (ImageButton) findViewById(R.id.imageButtonRestaurant);
        buttonCafe = (ImageButton) findViewById(R.id.imageButtonCafe);
        buttonCalculateRoute = (Button) findViewById(R.id.buttonCalculateRoute);
        textViewDistance = (TextView) findViewById(R.id.textViewDistance);
        seekBarDistance = (SeekBar) findViewById(R.id.seekBarDistance);
        listOfPlaces = new ArrayList<Place>();
        chosenPlaces = new ArrayList<Place>();
        geoApiContext = new GeoApiContext.Builder()
                .apiKey(apikey)
                .build();



        /*
        Utworzenie tablicy dwuwymiarowej odległości między miejscami
         */

//        double[][] distances = createDistanceTable(chosenPlaces);
//        for (int i = 0; i < distances.length; i++) {
//            for (int j = 0; j < distances.length; j++) {
//                System.out.println(distances[i][j]);
//            }
//        }


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
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
            gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (network_enabled) {
            net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (gps_loc != null && net_loc != null) {

            if (gps_loc.getAccuracy() > net_loc.getAccuracy()) {
                finalLoc = net_loc;
            } else {
                finalLoc = gps_loc;
            }
        } else {
            if (gps_loc != null) {
                finalLoc = gps_loc;
            } else if (net_loc != null) {
                finalLoc = net_loc;
            }
        }

        final LatLng currentLocation = new LatLng(finalLoc.getLatitude(), finalLoc.getLongitude());

                /*
        pobieranie wybranego miejsca z MapsActivity i tworzenie listy wybranych miejsc
         */

        bundle1 = getIntent().getExtras();
        if (bundle1 == null) {
            chosenPlaces = new ArrayList<Place>();


        /*
           dodanie aktualnego położenia do listy wybranych miejsc
        */

            Place place = new Place("MyLocation", currentLocation.lat, currentLocation.lng);
            chosenPlaces.add(place);
        }

        if (bundle1 != null) {
            Intent intent = getIntent();
            chosenPlaces = intent.getParcelableArrayListExtra("chosenPlaces");
            chosenPlace = new Place(bundle1.getString("chosenPlaceName"),
                    bundle1.getDouble("chosenPlaceLat"),
                    bundle1.getDouble("chosenPlaceLng"));
//            System.out.println(chosenPlace.getName() + " this is it");

            chosenPlaces.add(chosenPlace);

//            for (int i = 0; i < chosenPlaces.size(); i++) {
//                System.out.println(chosenPlaces.get(i).getName() + " DZIALA!!!!!");
//            }
        }



        /*
        obsługa przycisku wyznaczającego trasę
         */

        buttonCalculateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0; i<chosenPlaces.size(); i++) {
                    System.out.println("Chosen place: " + chosenPlaces.get(i).getName());
                }
                Route route = new Route(chosenPlaces);
//                ArrayList<Place> calculatedRoute = new SimulatedAnnealing()
//                        .calculateRoute(SimulatedAnnealing.MAX_TEMPERATURE, route)
//                        .getPlaces();
                ArrayList<Place> calculatedRoute = new NearesNeighbour().calculateRoute(chosenPlaces);
                for(int i=0; i<calculatedRoute.size(); i++) {
                    System.out.println("Calculated route: "+ calculatedRoute.get(i).getName());
                }
                Intent intent = new Intent(getApplicationContext(), MapsActivity2.class);
                intent.putParcelableArrayListExtra("calculatedRoute", (ArrayList<? extends Parcelable>)calculatedRoute);
                startActivity(intent);
            }
        });



        /*
        obsługa kliknięcia przycisków z typami miejsc
         */

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NearbySearchRequest nearby = new NearbySearchRequest(geoApiContext);
                PlaceType placeType;

                if (v == buttonMuseum) {
                    placeType = PlaceType.MUSEUM;
                } else if (v == buttonChurch) {
                    placeType = PlaceType.CHURCH;
                } else if (v == buttonRestaurant) {
                    placeType = PlaceType.RESTAURANT;
                } else {
                    placeType = PlaceType.CAFE;
                }

                setNearbyParameters(placeType, seekBarDistance.getProgress(), nearby, currentLocation);

                nearby.setCallback(new PendingResult.Callback<PlacesSearchResponse>() {
                    @Override
                    public void onResult(PlacesSearchResponse result) {
                        for (int i = 0; i < result.results.length; i++) {
                            double distanceInMeters = distance(result.results[i].geometry.location.lat,
                                    currentLocation.lat,
                                    result.results[i].geometry.location.lng,
                                    currentLocation.lng);

                            listOfPlaces.add(new Place(result.results[i].name,
                                    result.results[i].geometry.location.lat,
                                    result.results[i].geometry.location.lng,
                                    result.results[i].rating,
                                    result.results[i].vicinity,
                                    result.results[i].placeId,
                                    distanceInMeters));
                            System.out.println(result.results[i].name + ":" + distanceInMeters);
                        }

                        Bundle bundle = new Bundle();
                        bundle.putDouble("currentLat", currentLocation.lat);
                        bundle.putDouble("currentLng", currentLocation.lng);
                        Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
                        intent.putExtras(bundle);
                        intent.putParcelableArrayListExtra("places", (ArrayList<? extends Parcelable>) listOfPlaces);
                        intent.putParcelableArrayListExtra("chosenPlaces", (ArrayList<? extends Parcelable>) chosenPlaces);
                        startActivity(intent);
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



    /*
          Metoda obliczająca dystans między dwoma lokalizacjami
    */

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

    public static double[][] createDistanceTable(ArrayList<Place> chosenPlaces) {
        double[][] tab = new double[chosenPlaces.size()][chosenPlaces.size()];

        for (int i = 0; i < chosenPlaces.size(); i++) {
            for (int j = 0; j < chosenPlaces.size(); j++) {
                double distance = distance(chosenPlaces.get(i).getLat(),
                        chosenPlaces.get(j).getLat(),
                        chosenPlaces.get(i).getLng(),
                        chosenPlaces.get(j).getLng());
                tab[i][j] = distance;
            }
        }

        return tab;
    }

    public static void printHeading(Route route) {
        String headingColumn1 = "Route";
        String remainHeadingColumns = "Distance | Temp | Func | Random # | Decision                                                                 ";
        int placesNamesLength = 0;
        for (int x = 0; x < route.getPlaces().size(); x++) {
            placesNamesLength += route.getPlaces().get(x).getName().length();
        }
        int arrayLength = placesNamesLength + route.getPlaces().size() * 2;
        int partialLength = (arrayLength - headingColumn1.length()) / 2;
        for (int x = 0; x < partialLength; x++) {
            System.out.println(" ");
        }
        System.out.println(headingColumn1);
        for (int x = 0; x < placesNamesLength; x++) {
            System.out.println(" ");
        }
        if ((arrayLength % 2) == 0) {
            System.out.println(" ");
        }
        System.out.println(" | " + remainHeadingColumns);
        placesNamesLength += remainHeadingColumns.length() + 3;
        for (int x = 0; x < placesNamesLength + route.getPlaces().size() * 2; x++) {
            System.out.println("-");
        }
        System.out.println("");
    }

}



