package io.github.intimidate.decamin.qrcode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import io.github.intimidate.decamin.BookRideActivity;
import io.github.intimidate.decamin.R;
import io.github.intimidate.decamin.User;
import io.github.intimidate.decamin.remote.ApiManager;
import io.github.intimidate.decamin.remote.BookingBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.github.intimidate.decamin.CurrentBooking.destination;

public class QrActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private final int CAMERA_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            init();
        }

    }

    private void init() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> QrActivity.this.runOnUiThread(
                () ->updateCode(result.getText()))

        );
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
        mCodeScanner.startPreview();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                Toast.makeText(this, "Camera permission denied. Please allow the permission to continue", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCodeScanner != null)
            mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCodeScanner != null)
            mCodeScanner.releaseResources();
    }
    private void updateCode(String demail){
        int x= PreferenceManager.getDefaultSharedPreferences(this).getInt("bookingStatus",1);
        int y=PreferenceManager.getDefaultSharedPreferences(this).getInt("bookingId",1);
        int z=PreferenceManager.getDefaultSharedPreferences(this).getInt("token",-1);
        if(x==1){
            x=2;
        }
        else{
            x=3;
        }
        Log.d("TAGQ",String.valueOf(x+y+z));
        Call<BookingBody> call = ApiManager.api.updateBook(z,demail,x,y);
        int finalX = x;
        call.enqueue(new Callback<BookingBody>() {
            @Override
            public void onResponse(Call<BookingBody> call, Response<BookingBody> response) {
                Log.d("TAGQ","Started ride");
                PreferenceManager.getDefaultSharedPreferences(QrActivity.this).edit().putInt("bookingStatus", finalX).apply();
            }

            @Override
            public void onFailure(Call<BookingBody> call, Throwable t) {

            }
        });
    }
}