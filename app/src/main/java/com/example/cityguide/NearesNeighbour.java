package com.example.cityguide;

import java.util.ArrayList;

public class NearesNeighbour {

    public ArrayList<Place> calculateRoute(ArrayList<Place> chosenPlaces) {
        ArrayList<Place> calculatedRoute = new ArrayList<>();

        //chosenPlaces.get(0).setDistanceInMeters(0);
        calculatedRoute.add(chosenPlaces.get(0));

        for(int i=0; i<chosenPlaces.size(); i++) {
            ArrayList<Place> checkedPlaces = new ArrayList<>();
            //checkedPlaces.add(chosenPlaces.get(i));
            //int temp = i;
            if(i == chosenPlaces.size()-1) {
                break;
            }
            for(int j=i+1; j<chosenPlaces.size(); j++){
                chosenPlaces.get(j).setDistanceInMeters(MainActivity.distance(chosenPlaces.get(i).getLat(),
                        chosenPlaces.get(j).getLat(),
                        chosenPlaces.get(i).getLng(),
                        chosenPlaces.get(j).getLng()));
                checkedPlaces.add(chosenPlaces.get(j));
            }
            sortByDistance(checkedPlaces, 0, checkedPlaces.size()-1);
            System.out.println("Iteracja: " + (i+1));
            for(int k=0; k<checkedPlaces.size(); k++) {
                System.out.println(checkedPlaces.get(k).getName() + ",   distance: " +checkedPlaces.get(k).getDistanceInMeters());
            }

            sortByDistance(chosenPlaces, 0, chosenPlaces.size()-1);
            calculatedRoute.add(checkedPlaces.get(0));
        }

        return calculatedRoute;
    }
    private void sortByDistance(ArrayList<Place> checkedPlaces, int left, int right) {
        int i, j;
        Place x, y;
        i = left; j = right;
        x = checkedPlaces.get((left+right)/2);
        do {
            while((checkedPlaces.get(i).getDistanceInMeters() < x.getDistanceInMeters()) && (i < right)) i++;
            while((x.getDistanceInMeters() < checkedPlaces.get(j).getDistanceInMeters()) && (j > left)) j--;
            if(i <= j) {
                y = checkedPlaces.get(i);
                checkedPlaces.set(i, checkedPlaces.get(j));
                checkedPlaces.set(j, y);
                i++; j--;
            }
        } while(i <= j);
        if(left < j) sortByDistance(checkedPlaces, left, j);
        if(i < right) sortByDistance(checkedPlaces, i, right);
    }
}

