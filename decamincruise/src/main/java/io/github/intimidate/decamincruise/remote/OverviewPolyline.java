package io.github.intimidate.decamincruise.remote;

import com.google.gson.annotations.SerializedName;

public class OverviewPolyline {
    @SerializedName("points")
    String encodedPolyline;

    public OverviewPolyline(String encodedPolyline) {
        this.encodedPolyline = encodedPolyline;
    }

    public String getEncodedPolyline() {
        return encodedPolyline;
    }

    public void setEncodedPolyline(String encodedPolyline) {
        this.encodedPolyline = encodedPolyline;
    }
}
