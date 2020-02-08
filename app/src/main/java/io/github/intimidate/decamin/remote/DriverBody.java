package io.github.intimidate.decamin.remote;

import com.google.gson.annotations.SerializedName;

public class DriverBody {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("gender")
    private String gender;
    @SerializedName("name")
    private String name;
    @SerializedName("currentCar")
    private int currentCar;
    @SerializedName("isActive")
    private int isActive;
    @SerializedName("noOfPassengers")
    private int noOfPassengers;
    @SerializedName("position_lat")
    private double position_lat;
    @SerializedName("position_lon")
    private double position_lon;

    public DriverBody(String email, String password, String gender, String name, int currentCar, int isActive, int noOfPassengers, double position_lat, double position_lon) {
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.name = name;
        this.currentCar = currentCar;
        this.isActive = isActive;
        this.noOfPassengers = noOfPassengers;
        this.position_lat = position_lat;
        this.position_lon = position_lon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentCar() {
        return currentCar;
    }

    public void setCurrentCar(int currentCar) {
        this.currentCar = currentCar;
    }

    public int getActive() {
        return isActive;
    }

    public void setActive(int active) {
        isActive = active;
    }

    public int getNoOfPassengers() {
        return noOfPassengers;
    }

    public void setNoOfPassengers(int noOfPassengers) {
        this.noOfPassengers = noOfPassengers;
    }

    public double getPosition_lat() {
        return position_lat;
    }

    public void setPosition_lat(double position_lat) {
        this.position_lat = position_lat;
    }

    public double getPosition_lon() {
        return position_lon;
    }

    public void setPosition_lon(double position_lon) {
        this.position_lon = position_lon;
    }
}
