package io.github.intimidate.decamincruise.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.airbnb.lottie.LottieAnimationView;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.github.intimidate.decamincruise.DriverMapActivity;
import io.github.intimidate.decamincruise.R;
import io.github.intimidate.decamincruise.remote.ApiManager;
import io.github.intimidate.decamincruise.remote.LoginBody;
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
        if (token != -1) {
            startActivity(new Intent(LoginActivity.this, DriverMapActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        loginAnim = findViewById(R.id.loginAnim);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login.setOnClickListener(v -> doLogin(String.valueOf(email.getText()), String.valueOf(password.getText())));
    }

    @Override
    public void doLogin(String email, String password) {

        Call<LoginBody> call = ApiManager.api.loginUser(email, password);
        call.enqueue(new Callback<LoginBody>() {
            @Override
            public void onResponse(Call<LoginBody> call, Response<LoginBody> response) {
                Log.d("TAG", response.toString());
                if (response.body() != null) {
                    PreferenceManager
                            .getDefaultSharedPreferences(LoginActivity.this)
                            .edit()
                            .putInt("token", response.body().getToken())
                            .apply();
                }
                startActivity(new Intent(LoginActivity.this, DriverMapActivity.class));
                finish();
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
