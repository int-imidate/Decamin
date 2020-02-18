package io.github.intimidate.decamincruise.remote;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Directions {
    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public Directions(ArrayList<Route> routes) {
        this.routes = routes;
    }

    @SerializedName("routes")
    ArrayList<Route> routes;
}
