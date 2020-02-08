package io.github.intimidate.decamin.remote;

import com.google.gson.annotations.SerializedName;

public class BookingBody {
    @SerializedName("userEmail")
    String userEmail;

    @SerializedName("driverEmail")
    String driverEmail;

    @SerializedName("from_lat")
    double from_lat;

    @SerializedName("from_lon")
    double from_lon;

    @SerializedName("to_lat")
    double to_lat;

    @SerializedName("to_lon")
    double to_lon;

    @SerializedName("noOfSeats")
    int noOfSeats;

    @SerializedName("timeStamp")
    String timeStamp;

    @SerializedName("status")
    int status;

    @SerializedName("id")
    double id;

    public BookingBody(
            String userEmail,
            String driverEmail,
            double from_lat,
            double from_lon,
            double to_lat,
            double to_lon,
            int noOfSeats,
            String timeStamp,
            int status,
            double id
    ) {
        this.userEmail = userEmail;
        this.driverEmail = driverEmail;
        this.from_lat = from_lat;
        this.from_lon = from_lon;
        this.to_lat = to_lat;
        this.to_lon = to_lon;
        this.noOfSeats = noOfSeats;
        this.timeStamp = timeStamp;
        this.status = status;
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public double getFrom_lat() {
        return from_lat;
    }

    public void setFrom_lat(double from_lat) {
        this.from_lat = from_lat;
    }

    public double getFrom_lon() {
        return from_lon;
    }

    public void setFrom_lon(double from_lon) {
        this.from_lon = from_lon;
    }

    public double getTo_lat() {
        return to_lat;
    }

    public void setTo_lat(double to_lat) {
        this.to_lat = to_lat;
    }

    public double getTo_lon() {
        return to_lon;
    }

    public void setTo_lon(double to_lon) {
        this.to_lon = to_lon;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(int noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }
}

