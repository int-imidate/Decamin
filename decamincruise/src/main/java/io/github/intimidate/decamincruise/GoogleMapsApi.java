package io.github.intimidate.decamincruise;

import io.github.intimidate.decamincruise.remote.Directions;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapsApi {

    String baseURL = "https://maps.googleapis.com/maps/api/directions/";

    @GET("json")
    Call<Directions> getDirectionsAPIResponse(@Query("origin") String origin,
                                              @Query("destination") String destination,
                                              @Query("waypoints") String waypoints,
                                              @Query("key") String key);
}
