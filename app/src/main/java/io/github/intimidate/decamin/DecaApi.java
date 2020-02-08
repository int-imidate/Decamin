package io.github.intimidate.decamin;

import java.util.List;

import io.github.intimidate.decamin.remote.BookingBody;
import io.github.intimidate.decamin.remote.DriverBody;
import io.github.intimidate.decamin.remote.LoginBody;
import io.github.intimidate.decamin.remote.VerifyTokenBody;
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

    @GET("getNearestDriver")
    Call<LoginBody> getDriver();

    @FormUrlEncoded
    @POST("getAllDrivers")
    Call<List<DriverBody>> getAllDrivers(@Field("token") int token);

    @FormUrlEncoded
    @POST("book")
    Call<BookingBody> bookDriver(@Field("token") int token,
                                 @Field("userEmail") String userEmail,
                                 @Field("from_lat")double from_lat,
                                 @Field("from_lon") double from_lon,
                                 @Field("to_lat")double to_lat,
                                 @Field("to_lon") double to_lon,
                                 @Field("noOfSeats") int noOfSeats);

}
