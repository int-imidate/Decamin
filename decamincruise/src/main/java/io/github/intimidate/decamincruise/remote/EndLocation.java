package io.github.intimidate.decamincruise.remote;

import com.google.gson.annotations.SerializedName;

class EndLocation {
    @SerializedName("lat")
    double lat;
    @SerializedName("lng")
    double lng;

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

    public EndLocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
