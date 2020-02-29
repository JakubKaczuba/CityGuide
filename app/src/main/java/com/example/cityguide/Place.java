package com.example.cityguide;

import com.google.maps.model.LatLng;

public class Place {

    private String name;
    private LatLng latlng;
    private float rating;
    private String address;
    private String placeId;
    private double distance;

    public Place(String name, LatLng latlng, float rating, String address, String placeId, double distance) {
        this.name = name;
        this.latlng = latlng;
        this.rating = rating;
        this.address = address;
        this.placeId = placeId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

