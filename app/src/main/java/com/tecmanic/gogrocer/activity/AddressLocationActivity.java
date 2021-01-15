package com.tecmanic.gogrocer.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.adapters.PlacePredictionAdapter;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.modelclass.GoogleMapModel;
import com.tecmanic.gogrocer.util.MultiTouchMapFragment;
import com.tecmanic.gogrocer.util.SessionManagement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class AddressLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, LocationListener {

    private static final int REQUEST_LOCATION_PERMISSION = 124;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 3000;
    boolean canGetLocation = false;
    private GoogleMap mMap;
    private MultiTouchMapFragment mapFragment;
    private SessionManagement sessionManagement;
    private Location location;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private EditText searchText;
    private LinearLayout searchLay;
    private LinearLayout addressLay;
    private TextView addressText;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;
    private PlacePredictionAdapter adapter;
    private ProgressBar progressBar;
    private String address = "";
    private boolean inPlacePredection = false;
    private LinearLayout progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_location);
        ImageView backBtn = findViewById(R.id.back_btn);
        RecyclerView searchViewRecy = findViewById(R.id.search_view_recy);
        searchText = findViewById(R.id.search_txt);
        searchLay = findViewById(R.id.search_lay);
        progressBar = findViewById(R.id.progressbar);
        progressBar1 = findViewById(R.id.progress_bar);
        addressText = findViewById(R.id.address_text);
        addressLay = findViewById(R.id.address_lay);
        TextView saveLoc = findViewById(R.id.save_loc);
        searchLay.setVisibility(View.GONE);
        backBtn.setOnClickListener(v -> onBackPressed());
        location = new Location("A");
        sessionManagement = new SessionManagement(AddressLocationActivity.this);
        mapFragment = (MultiTouchMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        configureCameraIdle();
        if (checkAndRequestPermissions()) {
            getLocationRequest();
        }
        getMapKey(searchViewRecy);
        saveLoc.setOnClickListener(v -> onBackPressed());
        searchText.setOnClickListener(v -> sessionToken = AutocompleteSessionToken.newInstance());
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchText.getText().toString() != null && searchText.getText().toString().length() > 0) {
                    searchLay.setVisibility(View.VISIBLE);
                    getPlacePredictions(searchText.getText().toString());
                } else {
                    searchLay.setVisibility(View.GONE);
                }

            }
        });
    }

    private void show() {
        if (progressBar1.getVisibility() == View.VISIBLE) {
            progressBar1.setVisibility(View.GONE);
        } else {
            progressBar1.setVisibility(View.VISIBLE);
        }
    }

    private void getMapKey(RecyclerView searchViewRecy) {
        show();
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.GOOGLEMAP_KEY, response -> {
            Gson mapGson = new Gson();
            GoogleMapModel mapModel = mapGson.fromJson(response, GoogleMapModel.class);
            Places.initialize(getApplicationContext(), mapModel.getData().getMapApiKey());
            placesClient = Places.createClient(this);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            searchViewRecy.setLayoutManager(layoutManager);
            adapter = new PlacePredictionAdapter(this::geocodePlaceAndDisplay);
            searchViewRecy.setAdapter(adapter);
            searchViewRecy.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
            show();
        }, error -> {
            show();
            error.printStackTrace();
        });

        RequestQueue rq = Volley.newRequestQueue(AddressLocationActivity.this);
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 90000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        rq.add(request);

    }


    private void getPlacePredictions(String query) {
//        final LocationBias bias = RectangularBounds.newInstance(
//                new LatLng(7.2, 67.8), // SW lat, lng
//                new LatLng(36.5, 93.8) // NE lat, lng
//        );
        final FindAutocompletePredictionsRequest newRequest = FindAutocompletePredictionsRequest
                .builder()
                .setSessionToken(sessionToken)
//                .setLocationBias(bias)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setQuery(query)
//                .setCountries("IN")
                .build();
        placesClient.findAutocompletePredictions(newRequest).addOnSuccessListener(response -> adapter.setPredictions(response.getAutocompletePredictions()));
    }

    private void geocodePlaceAndDisplay(AutocompletePrediction placePrediction) {
        searchText.setText("");
        inPlacePredection = true;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        searchLay.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);
        FetchPlaceRequest requestq = FetchPlaceRequest.builder(placePrediction.getPlaceId(), placeFields)
                .build();

        placesClient.fetchPlace(requestq).addOnCompleteListener(task -> {

            if (task.isSuccessful() && task.getResult() != null && task.getResult().getPlace().getLatLng() != null) {
                Location locations = new Location("point 1");
                locations.setLatitude(task.getResult().getPlace().getLatLng().latitude);
                locations.setLongitude(task.getResult().getPlace().getLatLng().longitude);
                location = locations;
                mMap.clear();
                sessionManagement.setLocationPref(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                getAddress();
            }

        }).addOnFailureListener(exception -> progressBar.setVisibility(View.GONE));
    }

    private void setSupLocation() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location1 -> {
            if (location1 != null) {
                sessionManagement.setLocationPref(String.valueOf(location1.getLatitude()), String.valueOf(location1.getLongitude()));
                mMap.clear();
                LatLng latLng = new LatLng(location1.getLatitude(), location1.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                getAddress();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setBuildingsEnabled(false);
        mapFragment.getmTouchView().setGoogleMap(mMap);
        if (!sessionManagement.getLatPref().equalsIgnoreCase("") && !sessionManagement.getLangPref().equalsIgnoreCase("")) {
            LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }
//        else {
//            LatLngBounds india = new LatLngBounds(new LatLng(7.2, 67.8), new LatLng(36.5, 93.8));
//            mMap.addMarker(new MarkerOptions().position(india.getCenter()));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(india.getCenter(), 17));
//        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraIdleListener(onCameraIdleListener);
    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public void onBackPressed() {
        setResult(22);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location locations) {
        if (!inPlacePredection) {
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    setLatLang(locations);
                }
            }).start();
        }
    }

    private void setLatLang(Location locations) {
        if (!sessionManagement.getLatPref().equalsIgnoreCase("") && !sessionManagement.getLangPref().equalsIgnoreCase("")) {
            DecimalFormat dFormat = new DecimalFormat("##.#######");
            LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
            double latitude = Double.parseDouble(dFormat.format(latLng.latitude));
            double longitude = Double.parseDouble(dFormat.format(latLng.longitude));
            Location locationA = new Location("cal 1");
            locationA.setLatitude(latitude);
            locationA.setLongitude(longitude);
            double disInMetter = locationA.distanceTo(locations);
            double disData = disInMetter / 1000;
            DecimalFormat dFormatt = new DecimalFormat("#.#");
            disData = Double.parseDouble(dFormatt.format(disData));
            if (disData > 5.0) {
                location = locations;
                getAddress();
            }
        } else {
            runOnUiThread(() -> {
                mMap.clear();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locations.getLatitude(), locations.getLongitude()), 11));
                location = locations;
            });
            getAddress();
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddressLocationActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    private void getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        long mInterval = 3;
        locationRequest.setInterval(mInterval * 1000);
        long nInterval = 1;
        locationRequest.setFastestInterval(nInterval * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        location = getLocation();
        if (location != null) {
            if (sessionManagement != null && mMap != null) {
                sessionManagement.setLocationPref(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                mMap.clear();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                getAddress();
            }
        } else {
            setSupLocation();
        }
    }

    public Location getLocation() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                setSupLocation();
            } else {
                this.canGetLocation = true;
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (isGPSEnabled && location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0) {
            Map<String, Integer> perms = new HashMap<>();
            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
            }
            if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                setLocation();
            } else {
                showNeedDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showNeedDialog() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddressLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            showDialogOK("Location Services Permission required for this app",
                    (dialog, which) -> {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            checkAndRequestPermissions();
                        }
                    });
        }
    }

    private void setLocation() {
        location = getLocation();
        if (location != null && sessionManagement != null && mMap != null) {
            sessionManagement.setLocationPref(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            mMap.clear();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            getAddress();
        } else {
            setSupLocation();
        }
    }

    private boolean checkAndRequestPermissions() {

        int locationPermission = ContextCompat.checkSelfPermission(AddressLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(AddressLocationActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_LOCATION_PERMISSION);
            Toast.makeText(AddressLocationActivity.this, "Go to settings and enable Location permissions", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void getAddress() {
        new Thread(() -> {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(AddressLocationActivity.this, Locale.getDefault());
            DecimalFormat dFormat = new DecimalFormat("#.######");
            double latitude = Double.parseDouble(dFormat.format(location.getLatitude()));
            double longitude = Double.parseDouble(dFormat.format(location.getLongitude()));

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (!addresses.isEmpty()){
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }
                    String city = addresses.get(0).getLocality();
                    sessionManagement.setLocationCity(city);
                    sessionManagement.setLocationPref(String.valueOf(latitude), String.valueOf(longitude));
                    address = returnedAddress.getAddressLine(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (inPlacePredection && address!=null &&!address.equalsIgnoreCase("")) {
                        addressLay.setVisibility(View.VISIBLE);
                        addressText.setText(address);
                    }
                });
            }
        }).start();

    }


    private void configureCameraIdle() {
        onCameraIdleListener = () -> {
            LatLng latLng = mMap.getCameraPosition().target;
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            inPlacePredection = true;
            getAddress();
        };
    }

}