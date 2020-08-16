package com.example.cityguide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Route {
    private ArrayList<Place> places = new ArrayList<>();

    public Route(ArrayList<Place> places) {
        this.places.addAll(places);
        Collections.shuffle(this.places);
    }

    public Route(Route route) {
        this.places.addAll(route.places);
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public double getTotalDistance() {
        double totalDistance = 0;
        for(int i=0; i<this.places.size()-1; i++) {
            totalDistance += MainActivity.distance(this.places.get(i).getLat(),
                    this.places.get(i+1).getLat(),
                    this.places.get(i).getLng(),
                    this.places.get(i+1).getLng());
        }
        return totalDistance;
    }
    public String getTotalStringDistance() {
        String returnValue = String.format("%.2f", this.getTotalDistance());
        if(returnValue.length() == 7) {
            returnValue = " "+returnValue;
        }
        return returnValue;
    }
    public String toString() {
        return Arrays.toString(places.toArray());
    }
}
