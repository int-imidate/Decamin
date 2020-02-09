package io.github.intimidate.decamincruise.remote;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class firstlevel {
    public ArrayList<second> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<second> routes) {
        this.routes = routes;
    }

    public firstlevel(ArrayList<second> routes) {
        this.routes = routes;
    }

    @SerializedName("routes")
    ArrayList<second> routes;
}
