package com.example.cityguide;

public class SimulatedAnnealing {

    public static final double RATE_OF_COOLING = 0.005;
    public static final double MAX_TEMPERATURE = 999;
    public static final double MIN_TEMPERATURE = 0.99;

    public Route calculateRoute(double temperature, Route currentRoute) {
        Route shortestRoute = new Route(currentRoute);
        Route nearbyRoute;
        int numberOfIteration = 0;
        while(temperature > MIN_TEMPERATURE) {
            nearbyRoute = obtainNearbyRoute(new Route(currentRoute));
            if (currentRoute.getTotalDistance() < shortestRoute.getTotalDistance()) {
                shortestRoute = new Route(currentRoute);
            }
            if(acceptRoute(currentRoute.getTotalDistance(), nearbyRoute.getTotalDistance(), temperature)) {
                currentRoute = new Route(nearbyRoute);
            }
            temperature *= 1-RATE_OF_COOLING;
            numberOfIteration ++;
        }
        System.out.println("Liczba iteracji algorytmu dla 12 miejsc: " + numberOfIteration);
        return shortestRoute;
    }

    private boolean acceptRoute(double currentDistance, double adjacentDistance, double temperature) {

        boolean acceptRouteFlag = false;
        double acceptanceProbability = 1.0;
        if(adjacentDistance >= currentDistance) {
            acceptanceProbability = Math.exp(-(adjacentDistance-currentDistance)/temperature);
        }
        double random = Math.random();
        if(acceptanceProbability >= random) {
            acceptRouteFlag = true;
        }
        return acceptRouteFlag;
    }

    private Route obtainNearbyRoute(Route route) {
        int x1=0;
        int x2=0;

        while(x1 == x2) {
            x1 = (int) (route.getPlaces().size() * Math.random());
            x2 = (int) (route.getPlaces().size() * Math.random());
        }
        Place place1 = route.getPlaces().get(x1);
        Place place2 = route.getPlaces().get(x2);
        route.getPlaces().set(x2, place1);
        route.getPlaces().set(x1, place2);
        return route;
    }
}



