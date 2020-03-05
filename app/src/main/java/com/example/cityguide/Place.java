package com.example.cityguide;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.maps.model.LatLng;

public class Place implements Parcelable {

    private String name;
    private double lat;
    private double lng;
    private float rating;
    private String address;
    private String placeId;

    public Place(String name, double lat, double lng, float rating, String address, String placeId) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.name);
        dest.writeFloat(this.rating);
        dest.writeString(this.address);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.placeId);
    }


    protected Place(Parcel in) {
        this.name = in.readString();
        this.rating = in.readFloat();
        this.address = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.placeId = in.readString();
    }

    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

}

