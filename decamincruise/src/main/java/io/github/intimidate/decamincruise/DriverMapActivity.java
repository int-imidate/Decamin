package io.github.intimidate.decamincruise;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;

import java.util.ArrayList;
import java.util.Objects;

import io.github.intimidate.decamincruise.login.LoginActivity;
import io.github.intimidate.decamincruise.remote.ApiManager;
import io.github.intimidate.decamincruise.remote.BookingBody;
import io.github.intimidate.decamincruise.remote.BookingStatus;
import io.github.intimidate.decamincruise.remote.VerifyTokenBody;
import io.github.intimidate.decamincruise.remote.Directions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private static final int MY_LOCATION_REQUEST_CODE = 0;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private GoogleMap mMap;
    private LocationManager locationManager;
    LatLng currentLatLng = new LatLng(10.7599579, 78.81339988);
    int flag = 0;

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
                        Intent intent = new Intent(DriverMapActivity.this, LoginActivity.class);
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
        setContentView(R.layout.activity_driver_map);
        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        currentLatLng = latLng;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
        Call<Boolean> call = ApiManager.api.updatePosition(
                PreferenceManager.getDefaultSharedPreferences(this).getInt("token", -1),
                latLng.latitude,
                latLng.longitude
        );
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d("TAG", response.toString());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("TAG", call.toString());
                t.printStackTrace();
            }
        });
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

    private ArrayList<LatLng> decodePoly(String encoded) {

        Log.i("Location", "String received: "+encoded);
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            //LatLng p = new LatLng((int) (((double) lat /1E5)* 1E6), (int) (((double) lng/1E5   * 1E6)));
            LatLng p = new LatLng((((double) lat / 1E5)),(((double) lng / 1E5)));
            poly.add(p);
        }

        for(int i=0;i<poly.size();i++){
            Log.i("Location", "Point sent: Latitude: "+poly.get(i).latitude+" Longitude: "+poly.get(i).longitude);
        }
        return poly;
    }

    private void getPassengersForRickshaw() {
        Log.v("TAG", String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getInt("token", -1)));
        Call<ArrayList<BookingBody>> call = ApiManager.api.getPassengers(
                PreferenceManager.getDefaultSharedPreferences(this).getInt("token", -1)
        );
        call.enqueue(new Callback<ArrayList<BookingBody>>() {
            @Override
            public void onResponse(Call<ArrayList<BookingBody>> call, Response<ArrayList<BookingBody>> response) {
                if (response.body() != null) {
                    ArrayList<LatLng> locations = new ArrayList<>();
                    locations.add(currentLatLng);
                    LatLng ending = new LatLng(1.0, 1.0);
                    StringBuilder waypoints = new StringBuilder();

                    for (BookingBody b : response.body()) {
                        int status = b.getStatus();
                        if (status == BookingStatus.booked) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(b.getFrom_lat(), b.getFrom_lon()))
                                    .title(b.getUserEmail()));
                            locations.add(new LatLng(b.getFrom_lat(), b.getFrom_lon()));
                            waypoints.append(b.getFrom_lat()).append(",").append(b.getFrom_lon()).append("|");
                            waypoints.append(b.getTo_lat()).append(",").append(b.getTo_lon()).append("|");
                            ending = new LatLng(b.getTo_lat(), b.getTo_lon());
                        }
                    }

                    waypoints.deleteCharAt(waypoints.length() - 1);
                    String url = String.format(ApiManager.directionsAPI, currentLatLng.latitude + "," + currentLatLng.longitude,
                            ending.latitude + "," + ending.longitude,
                            waypoints.toString(),
                            getResources().getString(R.string.google_maps_key));
                    Log.v("TAG", url);
                    Call<Directions> googleMapsCall = ApiManager.googleMapsApi
                            .getDirectionsAPIResponse(currentLatLng.latitude + "," + currentLatLng.longitude,
                                    ending.latitude + "," + ending.longitude,
                                    waypoints.toString(),
                                    getResources().getString(R.string.google_maps_key)
                            );
                    googleMapsCall.enqueue(new Callback<Directions>() {
                        @Override
                        public void onResponse(Call<Directions> call, Response<Directions> response) {
                             mMap.addPolyline(new PolylineOptions()
                                    .addAll(decodePoly(response.body().getRoutes().get(0).getOverviewPolyline().getEncodedPolyline())));
                        }

                        @Override
                        public void onFailure(Call<Directions> call, Throwable t) {
                            Log.d("TAG", call.toString());
                            t.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BookingBody>> call, Throwable t) {
                Log.d("TAG", call.toString());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
                getPassengersForRickshaw();
                handler.postDelayed(this, 2000);
            }
        }, 2000);


    }
}
