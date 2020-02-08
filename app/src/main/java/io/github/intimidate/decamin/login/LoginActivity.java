package io.github.intimidate.decamin.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.airbnb.lottie.LottieAnimationView;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.github.intimidate.decamin.BookRideActivity;
import io.github.intimidate.decamin.R;
import io.github.intimidate.decamin.User;
import io.github.intimidate.decamin.remote.ApiManager;
import io.github.intimidate.decamin.remote.LoginBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Login {

    Button login;
    LottieAnimationView loginAnim;
    MaterialEditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int token = PreferenceManager.getDefaultSharedPreferences(this).getInt("token", -1);
        User.email=PreferenceManager.getDefaultSharedPreferences(this).getString("email",null);
        User.name=PreferenceManager.getDefaultSharedPreferences(this).getString("name",null);

        if ((token != -1 && User.email != null && User.name != null) && !getIntent().getBooleanExtra("stayAtLogin", false)) {
            startActivity(new Intent(LoginActivity.this, BookRideActivity.class));
            finish();
        }

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

        Call<LoginBody> call = ApiManager.api.loginUser(email, password);
        call.enqueue(new Callback<LoginBody>() {
            @Override
            public void onResponse(Call<LoginBody> call, Response<LoginBody> response) {
                Log.d("TAG", response.toString());
                login.setVisibility(View.GONE);
                loginAnim.setVisibility(View.VISIBLE);
                loginAnim.playAnimation();
                if (response.body() != null) {
                    PreferenceManager
                            .getDefaultSharedPreferences(LoginActivity.this)
                            .edit()
                            .putInt("token", response.body().getToken())
                            .apply();
                    PreferenceManager
                            .getDefaultSharedPreferences(LoginActivity.this)
                            .edit()
                            .putString("email", response.body().getEmail())
                            .apply();
                    PreferenceManager
                            .getDefaultSharedPreferences(LoginActivity.this)
                            .edit()
                            .putString("name", response.body().getName())
                            .apply();
                    User.email=response.body().getEmail();
                    User.name=response.body().getName();
                }
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
