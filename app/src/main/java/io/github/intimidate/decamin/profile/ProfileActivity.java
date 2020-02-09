package io.github.intimidate.decamin.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.model.PaymentApp;

import io.github.intimidate.decamin.R;
import io.github.intimidate.decamin.User;
import io.github.intimidate.decamin.qrcode.QrActivity;
import io.github.intimidate.decamin.remote.ApiManager;
import io.github.intimidate.decamin.remote.BookingBody;
import io.github.intimidate.decamin.remote.LoginBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView email,name,dues;
        Button pay;
        email=findViewById(R.id.profileemail);
        email.setText(User.email);
        name=findViewById(R.id.profilename);
        name.setText(User.name);
        dues=findViewById(R.id.dues);
        pay=findViewById(R.id.pay);
        final EasyUpiPayment[] easyUpiPayment = new EasyUpiPayment[1];
        int z=PreferenceManager.getDefaultSharedPreferences(this).getInt("token",-1);

        Log.d("TAGQ",String.valueOf(z));
        Call<LoginBody> call = ApiManager.api.getDues(z);
        call.enqueue(new Callback<LoginBody>() {
            @Override
            public void onResponse(Call<LoginBody> call, Response<LoginBody> response) {
                float d=response.body().getDue();
                String x=String.valueOf(d);
                dues.setText(x);
                easyUpiPayment[0] = new EasyUpiPayment.Builder()
                        .with(ProfileActivity.this)
                        .setPayeeVpa("sidvenu@ybl")
                        .setPayeeName("Siddharth Venu")
                        .setTransactionId("20190603022401")
                        .setTransactionRefId("0120192019060302240")
                        .setDescription("For Today's Food")
                        .setAmount(x)
                        .build();
            }

            @Override
            public void onFailure(Call<LoginBody> call, Throwable t) {

            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyUpiPayment[0].setDefaultPaymentApp(PaymentApp.GOOGLE_PAY);
                easyUpiPayment[0].startPayment();
            }
        });
    }
}
