package io.github.intimidate.decamincruise.remote;

import com.google.gson.annotations.SerializedName;

class third {
    @SerializedName("end_location")
    EndLocation endLocation;
    @SerializedName("start_location")
    StartLocation start_location;

    public EndLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocation endLocation) {
        this.endLocation = endLocation;
    }

    public StartLocation getStart_location() {
        return start_location;
    }

    public void setStart_location(StartLocation start_location) {
        this.start_location = start_location;
    }

    public third(EndLocation endLocation, StartLocation start_location) {
        this.endLocation = endLocation;
        this.start_location = start_location;
    }
}
