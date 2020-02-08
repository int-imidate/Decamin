package io.github.intimidate.decamin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.github.intimidate.decamin.R;
import io.github.intimidate.decamin.DecaApi;
import io.github.intimidate.decamin.remote.LoginBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements Login {

    Button login;
    LottieAnimationView loginAnim;
    MaterialEditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginAnim=findViewById(R.id.loginAnim);
        login=findViewById(R.id.login);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               doLogin("m.shyam.tnj","blee");
            }
        });
    }

    @Override
    public void doLogin(String email, String password) {
        DecaApi api;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DecaApi.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(DecaApi.class);

        Call<Boolean> call = api.loginUser(email,password);
       call.enqueue(new Callback<Boolean>() {
           @Override
           public void onResponse(Call<Boolean> call, Response<Boolean> response) {
               Log.d("fuck",response.toString());
           }

           @Override
           public void onFailure(Call<Boolean> call, Throwable t) {
               Log.d("fuck",call.toString());
               t.printStackTrace();
           }
       });
    }
}
