package io.github.intimidate.decamincruise.remote;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

class second {
    @SerializedName("legs")
    ArrayList<third> legs;

    public second(ArrayList<third> legs) {
        this.legs = legs;
    }

    public ArrayList<third> getLegs() {
        return legs;
    }

    public void setLegs(ArrayList<third> legs) {
        this.legs = legs;
    }
}
