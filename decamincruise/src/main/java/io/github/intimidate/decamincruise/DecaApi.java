package io.github.intimidate.decamincruise;

import java.util.ArrayList;

import io.github.intimidate.decamincruise.remote.BookingBody;
import io.github.intimidate.decamincruise.remote.LoginBody;
import io.github.intimidate.decamincruise.remote.VerifyTokenBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DecaApi {

    String base_url = "http://10.1.96.113:3000/";

    @FormUrlEncoded
    @POST("login")
    Call<LoginBody> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("verifyToken")
    Call<VerifyTokenBody> verifyToken(
            @Field("token") int token
    );

    @FormUrlEncoded
    @POST("driver/updatePosition")
    Call<Boolean> updatePosition(
            @Field("token") int token,
            @Field("position_lat") double latitude,
            @Field("position_lon") double longitude
    );

    @FormUrlEncoded
    @POST("driver/updatePosition")
    Call<Boolean> updateCurrentCar(
            @Field("token") int token,
            @Field("currentCar") String currentCar
    );

    @FormUrlEncoded
    @POST("driver/getPassengers")
    Call<ArrayList<BookingBody>> getPassengers(
            @Field("token") int token
    );

}
