package io.github.intimidate.decamin;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.github.intimidate.decamin.login.LoginActivity;
import io.github.intimidate.decamin.remote.ApiManager;
import io.github.intimidate.decamin.remote.LoginBody;
import io.github.intimidate.decamin.remote.VerifyTokenBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRideActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private static final int MY_LOCATION_REQUEST_CODE = 0;
    private LocationManager locationManager;
    AutocompleteSupportFragment autocompleteFragment;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int token = PreferenceManager.getDefaultSharedPreferences(this).getInt("token", -1);
        if (token != -1) {
            Call<VerifyTokenBody> call = ApiManager.api.verifyToken(token);
            call.enqueue(new Callback<VerifyTokenBody>() {
                @Override
                public void onResponse(Call<VerifyTokenBody> call, Response<VerifyTokenBody> response) {
                    Log.d("TAG", response.toString());
                }

                @Override
                public void onFailure(Call<VerifyTokenBody> call, Throwable t) {
                    Log.d("TAG", call.toString());
                    t.printStackTrace();
                    startActivity(new Intent(BookRideActivity.this, LoginActivity.class));
                    finish();
                }
            });
        }
        setContentView(R.layout.activity_book_ride);
        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    Log.i("TAG", "Name: " + place.getName() + "Place: " + place.getAddress() + ", " + (place.getLatLng() != null ? place.getLatLng().toString() : ""));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 17);
                    mMap.animateCamera(cameraUpdate);
                }

                @Override
                public void onError(@NonNull Status status) {
                    Log.i("TAG", "An error occurred: " + status);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    Objects.equals(permissions[0], Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLocationTrackingEnabled();
            } else {
                Toast.makeText(
                        this,
                        "Permission denied. App cannot work. Please approve permissions in settings",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private void setLocationTrackingEnabled() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }

    private String getAddress(LatLng position) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            return addresses.size() > 0 ? addresses.get(0).getAddressLine(0) : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(() -> {
            String address = getAddress(mMap.getCameraPosition().target);
            if (!Objects.equals(BookRideActivity.this.address, address)) {
                BookRideActivity.this.address = address;
                autocompleteFragment.setText(address);
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            setLocationTrackingEnabled();
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
        }
    }
}
