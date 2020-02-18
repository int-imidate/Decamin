package io.github.intimidate.decamincruise.remote;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Route {

    @SerializedName("overview_polyline")
    OverviewPolyline overviewPolyline;

    public Route(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }
}
