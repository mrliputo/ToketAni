package com.tecmanic.gogrocer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.util.SessionManagement;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class MapboxActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnCameraIdleListener, PermissionsListener {

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private MapboxMap mapboxMap;
    private Location location;
    private LinearLayout addressLay;
    private TextView addressText;
    private SessionManagement sessionManagement;
    private ProgressBar progressBar;
    private String address;
    private boolean inPlacePredection = false;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";
    private String mapAccessToken;


    private void intialLocationEngine() {
        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(MapboxActivity.this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationEngine.getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult result) {
                if (result != null && result.getLastLocation() != null) {
                    Location locationd = result.getLastLocation();
                    location = locationd;
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(14).build()), 4000);
                    getAddress();
                }
            }

            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            mapAccessToken = getIntent().getStringExtra("map_key");
        }
        Mapbox.getInstance(this, mapAccessToken);
        setContentView(R.layout.activity_mapbox);
        sessionManagement = new SessionManagement(MapboxActivity.this);
        MapView mapView = findViewById(R.id.mapView);
        ImageView backBtn = findViewById(R.id.back_btn);
        LinearLayout searchLay = findViewById(R.id.search_lay);
        progressBar = findViewById(R.id.progressbar);
        addressText = findViewById(R.id.address_text);
        addressLay = findViewById(R.id.address_lay);
        TextView saveLoc = findViewById(R.id.save_loc);
        View searchView = findViewById(R.id.search_view);
        LinearLayout currentLoc = findViewById(R.id.current_Loc);
        searchLay.setVisibility(View.GONE);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        location = new Location("A");
        backBtn.setOnClickListener(v -> onBackPressed());
        searchView.setOnClickListener(view -> setPlaceDescription());
        saveLoc.setOnClickListener(v -> onBackPressed());

        currentLoc.setOnClickListener(view -> intialLocationEngine());

    }

    private void setPlaceDescription() {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : mapAccessToken)
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS))
                .build(MapboxActivity.this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.getUiSettings().setCompassEnabled(false);
        mapboxMap.getUiSettings().setLogoEnabled(false);
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            style.addImage(symbolIconId, BitmapFactory.decodeResource(
                    MapboxActivity.this.getResources(), R.drawable.map_default_map_marker));
            setUpSource(style);
            setupLayer(style);
        });
        intialLocationEngine();
        mapboxMap.addOnCameraIdleListener(MapboxActivity.this);
    }

    @Override
    public void onCameraIdle() {
        LatLng latLng = mapboxMap.getCameraPosition().target;
        if (latLng != null) {
            inPlacePredection = true;
            location.setLatitude(latLng.getLatitude());
            location.setLongitude(latLng.getLongitude());
            getAddress();
        }
    }

    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }

    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[]{0f, -8f})
        ));
    }

    private void getAddress() {
        new Thread(() -> {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(MapboxActivity.this, Locale.getDefault());
            DecimalFormat dFormat = new DecimalFormat("#.######");
            double latitude = Double.parseDouble(dFormat.format(location.getLatitude()));
            double longitude = Double.parseDouble(dFormat.format(location.getLongitude()));

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (!addresses.isEmpty()) {
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
            } finally {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (inPlacePredection && address != null && !address.equalsIgnoreCase("")) {
                        addressLay.setVisibility(View.VISIBLE);
                        addressText.setText(address);
                    }
                });
            }
        }).start();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }
        }
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
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Permission Needed For this!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(style -> intialLocationEngine());
        } else {
            Toast.makeText(this, "Permission Not Granted!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}