package io.github.intimidate.decamincruise.remote;

import io.github.intimidate.decamincruise.DecaApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    public static String directionsAPI =
            "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&waypoints=%s&key=%s";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DecaApi.base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static DecaApi api = retrofit.create(DecaApi.class);
}
