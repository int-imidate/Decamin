package io.github.intimidate.decamin;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.github.intimidate.decamin.remote.ApiManager;
import io.github.intimidate.decamin.remote.DriverBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRideImpl {
    List<DriverBody> drivers = new ArrayList<>();
    private BookRideActivity bookRideActivity;
    private Context context;
    private int token;

    public BookRideImpl(BookRideActivity bookRideActivity, int token) {
        this.bookRideActivity = bookRideActivity;
        this.context = bookRideActivity.getApplicationContext();
        this.token = token;

    }

    public void getDrivers() {
        Call<List<DriverBody>> call = ApiManager.api.getAllDrivers(token);
        call.enqueue(new Callback<List<DriverBody>>() {
            @Override
            public void onResponse(Call<List<DriverBody>> call, Response<List<DriverBody>> response) {
                Log.d("LOL",response.body().toString());

                bookRideActivity.addDriversToMap(response.body());
                drivers.clear();
                drivers = response.body();

            }

            @Override
            public void onFailure(Call<List<DriverBody>> call, Throwable t) {
                Log.d("TAGg", call.toString());
                t.printStackTrace();
            }
        });

    }

    public void getDriver(LatLng user, LatLng destination) {


    }
}
