package io.github.intimidate.decamin;

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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.github.intimidate.decamin.bookride.BookRideFragment;
import io.github.intimidate.decamin.login.LoginActivity;
import io.github.intimidate.decamin.remote.ApiManager;
import io.github.intimidate.decamin.remote.DriverBody;
import io.github.intimidate.decamin.remote.VerifyTokenBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRideActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final int MY_LOCATION_REQUEST_CODE = 0;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    AutocompleteSupportFragment autocompleteFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LatLng finalDestination;
    private String address = "";
    private Button booknow;
    private LatLng userLocation;
    private BookRideImpl bookRide;
    BookRideFragment bottomSheet;

    Context context=this;
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
                    if (response.code() == 400) {
                        Intent intent = new Intent(BookRideActivity.this, LoginActivity.class);
                        intent.putExtra("stayAtLogin", true);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<VerifyTokenBody> call, Throwable t) {
                    Log.d("TAG", call.toString());
                    t.printStackTrace();
                }
            });
        }
        bookRide = new BookRideImpl(this, token);
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

        if (autocompleteFragment == null) {
            Log.i("TAG", "Enter pickup location");
        }

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
        booknow = findViewById(R.id.booknow);
        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 bottomSheet = new BookRideFragment(userLocation, address, true, bookRide,finalDestination,token,BookRideActivity.this);
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");

            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    Objects.equals(permissions[0], Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLocationTrackingEnabled();
            } else {
                https:
//stackoverflow.com/questions/21403496/how-to-get-current-location-in-google-map-android
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
        userLocation = latLng;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
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
            finalDestination=mMap.getCameraPosition().target;
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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bookRide.getDrivers();
                handler.postDelayed(this, 2000);
            }
        }, 2000);

    }

    public void goToLogin() {
        Intent a = new Intent(this, LoginActivity.class);
        startActivity(a);
    }

    public void addDriversToMap(List<DriverBody> drivers) {
        List<MarkerOptions> markerOptions = new ArrayList<>();
        for (int i = 0; i < drivers.size(); i++) {
            LatLng latLng = new LatLng(drivers.get(i).getPosition_lat(), drivers.get(i).getPosition_lon());
            markerOptions.add(new MarkerOptions());
            markerOptions.get(i).icon(BitmapDescriptorFactory.fromResource(R.drawable.blip));
            markerOptions.get(i).position(latLng);
            markerOptions.get(i).title(drivers.get(i).getName());

        }
        mMap.clear();

        for (int i = 0; i < markerOptions.size(); i++) {
            mMap.addMarker(markerOptions.get(i));
        }


    }
    public void close_dialog(){
         bottomSheet.dismiss();

    }

}
