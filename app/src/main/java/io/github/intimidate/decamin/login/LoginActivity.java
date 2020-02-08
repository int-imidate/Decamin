package io.github.intimidate.decamin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.github.intimidate.decamin.BookRideActivity;
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
    MaterialEditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginAnim = findViewById(R.id.loginAnim);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login.setOnClickListener(v -> {
            doLogin(String.valueOf(email.getText()), String.valueOf(password.getText()));
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

        Call<LoginBody> call = api.loginUser(email, password);
        call.enqueue(new Callback<LoginBody>() {
            @Override
            public void onResponse(Call<LoginBody> call, Response<LoginBody> response) {
                Log.d("TAG", response.toString());
                login.setVisibility(View.GONE);
                loginAnim.setVisibility(View.VISIBLE);
                loginAnim.playAnimation();
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(LoginActivity.this, BookRideActivity.class));
                    finish();
                }, 650);
            }

            @Override
            public void onFailure(Call<LoginBody> call, Throwable t) {
                Log.d("TAG", call.toString());
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
