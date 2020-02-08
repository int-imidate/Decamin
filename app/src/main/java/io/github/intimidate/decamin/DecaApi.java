package io.github.intimidate.decamin;

import io.github.intimidate.decamin.remote.LoginBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DecaApi {

    String base_url = "http://10.1.96.113:3000/";

    @FormUrlEncoded
    @POST("login")
    Call<LoginBody> loginUser(
            @Field("email") String email,
            @Field("password") String password

    );


}
