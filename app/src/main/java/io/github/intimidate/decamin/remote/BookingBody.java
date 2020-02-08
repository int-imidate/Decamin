package io.github.intimidate.decamin.remote;

import com.google.gson.annotations.SerializedName;

public class BookingBody {
    @SerializedName("id")
    int id;

    @SerializedName("status")
    int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BookingBody(int id, int status) {
        this.id = id;
        this.status = status;
    }




}
