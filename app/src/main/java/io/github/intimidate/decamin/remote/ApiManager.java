package io.github.intimidate.decamin.remote;

import io.github.intimidate.decamin.DecaApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DecaApi.base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static DecaApi api = retrofit.create(DecaApi.class);
}
