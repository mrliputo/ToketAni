package com.tecmanic.toketani.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.fragments.CartFragment;
import com.tecmanic.toketani.fragments.CategoryFragment;
import com.tecmanic.toketani.fragments.ContactUsFragment;
import com.tecmanic.toketani.fragments.EditProfileFragment;
import com.tecmanic.toketani.fragments.HomeeeFragment;
import com.tecmanic.toketani.fragments.NotificationFragment;
import com.tecmanic.toketani.fragments.RewardFragment;
import com.tecmanic.toketani.fragments.SearchFragment;
import com.tecmanic.toketani.fragments.BisnisFragment;
import com.tecmanic.toketani.fragments.TermsAndConditionFragment;
import com.tecmanic.toketani.fragments.WalletFragment;
import com.tecmanic.toketani.modelclass.ForgotEmailModel;
import com.tecmanic.toketani.modelclass.MapSelectionModel;
import com.tecmanic.toketani.modelclass.MapboxModel;
import com.tecmanic.toketani.modelclass.NewPendingDataModel;
import com.tecmanic.toketani.modelclass.NotifyModelUser;
import com.tecmanic.toketani.modelclass.PaymentVia;
import com.tecmanic.toketani.network.ApiInterface;
import com.tecmanic.toketani.util.DatabaseHandler;
import com.tecmanic.toketani.util.FragmentClickListner;
import com.tecmanic.toketani.util.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.tecmanic.toketani.config.BaseURL.CURRENCY_API;
import static com.tecmanic.toketani.config.BaseURL.SUPPORT_URL;
import static com.tecmanic.toketani.config.BaseURL.TERMS_URL;
import static com.tecmanic.toketani.config.BaseURL.USERBLOCKAPI;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 3000;
    BottomNavigationView navigation;
    int padding = 0;
    LinearLayout myOrder;
    LinearLayout myReward;
    LinearLayout myWalllet;
    LinearLayout myCart;
    NavigationView navigationView;
    LinearLayout viewpa;
    TextView username;
    Toolbar toolbar;
    ImageView bell;
    boolean canGetLocation = true;
    private LocationManager locationManager;
    private DatabaseHandler dbcart;
    private SessionManagement sessionManagement;
    private Menu navMenu;
    private FusedLocationProviderClient mFusedLocationClient;
    private SharedPreferences pref;
    private DrawerLayout drawer;
    private TextView addres;
    private Location location;
    private boolean enterInFirst = false;
    private FragmentClickListner fragmentClickListner;
    private LinearLayout progressBar;
    private String cardQtyKey = "cardqnty";
    private String decimalRefKey = "##.#######";
    private String titleKey = "title";
    private String statusKey = "status";

    private void show() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void setColorFilter(@NonNull Drawable drawable, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
        } else {
            drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManagement = new SessionManagement(MainActivity.this);
        dbcart = new DatabaseHandler(this);
        pref = getSharedPreferences("toketani", Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(this);
        navigation = findViewById(R.id.nav_view12);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(view -> Log.i("TAG", "not work"));
        drawer = findViewById(R.id.drawer_layout);
        bell = findViewById(R.id.bell);
        ImageView notifications = findViewById(R.id.navigation_notifications12);
        addres = findViewById(R.id.address);
        navigationView = findViewById(R.id.nav_view);
        location = new Location("A");
        new Thread(this::checkMapSelection).start();
        checkAndRequestPermissions(false);
        int badgeCount = pref.getInt(cardQtyKey, 0);
        if (badgeCount > 0) {
            navigation.getOrCreateBadge(R.id.navigation_notifications123).setNumber(badgeCount);
        } else {
            navigation.removeBadge(R.id.navigation_notifications123);
        }

        notifications.setOnClickListener(v -> notificationsClick());
        fragmentClickListner = new FragmentClickListner() {
            @Override
            public void onFragmentClick(boolean open) {
                if (open) {
                    navigation.setSelectedItemId(R.id.navigation_notifications123);
                    loadFragment(new CartFragment());
                }
            }

            @Override
            public void onChangeHome(boolean open) {
                DecimalFormat dFormat = new DecimalFormat(decimalRefKey);
                LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
                double latitude = Double.parseDouble(dFormat.format(latLng.latitude));
                double longitude = Double.parseDouble(dFormat.format(latLng.longitude));
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                getAddress();
                navigation.setSelectedItemId(R.id.navigation_home);
                loadFragment(new HomeeeFragment(fragmentClickListner));
            }

            @Override
            public void onLocationPermission(boolean check) {
                checkAndRequestPermissions(true);
            }
        };

//        addres.setOnClickListener(v -> {
//            if (sessionManagement.getMapSelection().equalsIgnoreCase("mapbox")) {
//                getMapKey();
//            } else {
//                startActivityForResult(new Intent(MainActivity.this, AddressLocationActivity.class), 22);
//            }
//        });

        addres.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkAndRequestPermissions(true);
            }else {
                if (sessionManagement.getMapSelection().equalsIgnoreCase("mapbox")) {
                    getMapKey();
                } else {
                    startActivityForResult(new Intent(MainActivity.this, AddressLocationActivity.class), 22);
                }
            }
        });

        ImageView menuSlider = findViewById(R.id.sliderr);
        menuSlider.setOnClickListener(v -> drawer.openDrawer(GravityCompat.START));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.getBackground().setColorFilter(new PorterDuffColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY));
        navigationView.setNavigationItemSelectedListener(this);
        navMenu = navigationView.getMenu();
        View header = navigationView.getHeaderView(0);
        viewpa = header.findViewById(R.id.viewpa);
        if (sessionManagement.isLoggedIn()) {
            viewpa.setVisibility(View.VISIBLE);
        }


        myOrder = header.findViewById(R.id.my_orders);
        myReward = header.findViewById(R.id.my_reward);
        myWalllet = header.findViewById(R.id.my_wallet);
        myCart = header.findViewById(R.id.my_cart);
        ImageView ivProfile = header.findViewById(R.id.iv_header_img);
        username = header.findViewById(R.id.tv_header_name);

        myOrder.setOnClickListener(v -> myOrderClick());
        myReward.setOnClickListener(v -> myRewardClick());
        myWalllet.setOnClickListener(v -> myWalletClick());

        myCart.setOnClickListener(v -> myCartClick());
        ivProfile.setOnClickListener(view -> myProfileClick());
        /*sideMenu();*/
        new Thread(this::getCurrency).start();
        new Thread(this::checkOtpStatus).start();
        new Thread(this::checkUserNotify).start();
        new Thread(this::checkUserPayNotify).start();

        if (savedInstanceState == null) {
            loadFragment(new HomeeeFragment(fragmentClickListner));
        }
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            try {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                Fragment fr = getSupportFragmentManager().findFragmentById(R.id.contentPanel);
                if (fr != null) {
                    backStackListner(fr, toggle);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
        initComponent();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void myOrderClick() {
        drawer.closeDrawer(GravityCompat.START);
        if (sessionManagement.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
            startActivityForResult(intent, 4);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void myProfileClick() {
        if (sessionManagement.isLoggedIn()) {
            EditProfileFragment fm = new EditProfileFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        } else {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            overridePendingTransition(0, 0);
        }
    }

    private void myCartClick() {
        if (dbcart.getCartCount() > 0) {
            navigation.setSelectedItemId(R.id.navigation_notifications123);
            CartFragment favouriteFragment = new CartFragment();
            FragmentManager manager1 = getSupportFragmentManager();
            FragmentTransaction transaction1 = manager1.beginTransaction();
            transaction1.replace(R.id.contentPanel, favouriteFragment);
            transaction1.addToBackStack(null);
            transaction1.commit();
        } else {
            Toast.makeText(MainActivity.this, "No Item in Cart", Toast.LENGTH_SHORT).show();
        }
    }

    private void myWalletClick() {
        if (sessionManagement.isLoggedIn()) {
            drawer.closeDrawer(GravityCompat.START);
            if (sessionManagement.userBlockStatus().equalsIgnoreCase("2")) {
                WalletFragment fm = new WalletFragment();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.contentPanel, fm);
                transaction.commit();
            } else {
                showBloackDialog();
            }
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void myRewardClick() {
        if (sessionManagement.isLoggedIn()) {
            drawer.closeDrawer(GravityCompat.START);
            RewardFragment fm = new RewardFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.contentPanel, fm);
            transaction.commit();
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void backStackListner(Fragment fr, ActionBarDrawerToggle toggle) {
        final String fm_name = fr.getClass().getSimpleName();
        if (fm_name.contentEquals("Home_fragment")) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.setDrawerIndicatorEnabled(true);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            toggle.syncState();

        } else if (fm_name.contentEquals("My_order_fragment") || fm_name.contentEquals("Thanks_fragment")) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            toggle.setDrawerIndicatorEnabled(false);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            toggle.syncState();

            toggle.setToolbarNavigationClickListener(view -> {
                HomeeeFragment fm = new HomeeeFragment(fragmentClickListner);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            });
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            toggle.syncState();
            toggle.setToolbarNavigationClickListener(view -> onBackPressed());
        }
    }

    private void notificationsClick() {
        loadFragment(new NotificationFragment());
    }

    private void showBloackDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setCancelable(true);
        alertDialog.setMessage("You are blocked from backend.\n Please Contact with customer care!");
        alertDialog.setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        new Thread(this::fetchBlockStatus).start();
        super.onStart();
    }

    private void getMapKey() {
        show();
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.MAPBOX_KEY, response -> {
            Gson mapGson = new Gson();
            MapboxModel mapModel = mapGson.fromJson(response, MapboxModel.class);
            String mapAccessToken = mapModel.getData().getMapApiKey();
            show();
            startActivityForResult(new Intent(MainActivity.this, MapboxActivity.class).putExtra("map_key", mapAccessToken), 22);
        }, error -> {
            show();
            error.printStackTrace();
        });
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
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
                error.printStackTrace();
            }
        });
        rq.add(request);
    }

    private void fetchBlockStatus() {
        if (!sessionManagement.userId().equalsIgnoreCase("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, USERBLOCKAPI, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString(statusKey);
                    if (status.equals("2")) {
                        sessionManagement.setUserBlockStatus("2");
                    } else {
                        sessionManagement.setUserBlockStatus("1");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("user_id", sessionManagement.userId());
                    return param;
                }
            };
            stringRequest.setRetryPolicy(new RetryPolicy() {
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
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }
    }

    private void getLocationRequest(boolean status) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(300000L);
        locationRequest.setFastestInterval(180000L);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        location = getLocation(status);
        if (location != null) {
            if (sessionManagement != null) {
                sessionManagement.setLocationPref(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
//                29.010260903193778, 77.06049536229332
//                sessionManagement.setLocationPref(String.valueOf(29.010260903193778), String.valueOf(77.06049536229332));
                getAddress();
            }
            if (status) {
                loadFragment(new HomeeeFragment(fragmentClickListner));
            }
        } else {
            setSupLocation(status);
        }
    }

    private void getAddress() {
        if (location != null && location.getLatitude()>0.0 && location.getLongitude()>0.0) {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
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
//                    sessionManagement.setLocationPref(String.valueOf(29.010260903193778), String.valueOf(77.06049536229332));
                    runOnUiThread(() -> {
                        if (returnedAddress.getAddressLine(0) != null) {
                            addres.setText(returnedAddress.getAddressLine(0));
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0) {
            Map<String, Integer> perms = new HashMap<>();
            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
            }
            if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                checkAndRequestPermissions(true);
            } else {
                showNeedDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showNeedDialog() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            showDialogOK("Location Services Permission required for this app",
                    (dialog, which) -> {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            checkAndRequestPermissions(true);
                        }
                    });
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    public Location getLocation(boolean status) {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                setSupLocation(status);
            } else if (isNetworkEnabled) {
                location = getLastLocation(LocationManager.NETWORK_PROVIDER);
            } else {
                location = getLastLocation(LocationManager.GPS_PROVIDER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    private Location getLastLocation(String networkProvider) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        locationManager.requestLocationUpdates(networkProvider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        location = locationManager.getLastKnownLocation(networkProvider);
        return location;
    }

    private void setSupLocation(boolean status) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location1 -> {
            if (location1 != null) {
                location = location1;
                getAddress();
            }
            if (status) {
                loadFragment(new HomeeeFragment(fragmentClickListner));
            }
        });
    }

    private void checkAndRequestPermissions(boolean status) {

        int locationPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int locationPermissionCoarse = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (locationPermissionCoarse != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Location permission for grocery door step delivery");
            alertDialog.setMessage("We need location for the following things and mentioned here.\n1. To show you your nearest grocery store at you location.\n2. To save your delivery address to provide your grocery at your door step or where you want.");
            alertDialog.setPositiveButton("Ok", (dialogInterface, i) -> {
                ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_LOCATION_PERMISSION);
                dialogInterface.dismiss();
            });
            alertDialog.setNegativeButton("dismiss", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            alertDialog.show();
        } else {
            getLocationRequest(status);

        }
    }



    private void checkUserNotify() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<NotifyModelUser> checkOtpStatus = apiInterface.getNotifyUser(sessionManagement.userId());

        checkOtpStatus.enqueue(new Callback<NotifyModelUser>() {
            @Override
            public void onResponse(@NonNull Call<NotifyModelUser> call, @NonNull retrofit2.Response<NotifyModelUser> response) {

                if (response.isSuccessful() && response.body() != null) {
                    NotifyModelUser modelUser = response.body();
                    if (modelUser.getStatus().equalsIgnoreCase("1")) {
                        sessionManagement.setEmailServer(modelUser.getData().getEmail());
                        sessionManagement.setUserSMSService(modelUser.getData().getSms());
                        sessionManagement.setUserInAppService(modelUser.getData().getApp());
                    } else {
                        sessionManagement.setEmailServer("0");
                        sessionManagement.setUserSMSService("0");
                        sessionManagement.setUserInAppService("0");
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<NotifyModelUser> call, @NonNull Throwable t) {

            }
        });

    }

    private void checkUserPayNotify() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<PaymentVia> checkOtpStatus = apiInterface.getPaymentVia();

        checkOtpStatus.enqueue(new Callback<PaymentVia>() {
            @Override
            public void onResponse(@NonNull Call<PaymentVia> call, @NonNull retrofit2.Response<PaymentVia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PaymentVia modelUser = response.body();
                    if (modelUser.getStatus().equalsIgnoreCase("1")) {
                        sessionManagement.setPaymentMethodOpt(modelUser.getData().getRazorpay(), modelUser.getData().getPaypal(), modelUser.getData().getPaystack());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PaymentVia> call, @NonNull Throwable t) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        return false;
    }

    private void loadFragment(Fragment fragment) {
//        getSupportFragmentManager().beginTransaction().addToBackStack(fragment.getClass().getName());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentPanel, fragment)
                .commitAllowingStateLoss();
    }

    private void initComponent() {
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(new HomeeeFragment(fragmentClickListner));
                    return true;
                /*case R.id.navigation_dashboard:
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        checkAndRequestPermissions(true);
                    }else {
                        loadFragment(new CategoryFragment(fragmentClickListner));
                    }
                    return true;*/
                case R.id.bisnis:
                    loadFragment(new BisnisFragment());
                    return true;
                /*case R.id.navigation_notifications12:
                    loadFragment(new NotificationFragment());
                    return true;*/
                case R.id.navigation_notifications123:
                    loadFragment(new CartFragment());
                    return true;
                case R.id.nav_my_profile:
                    if (sessionManagement.isLoggedIn()) {
                        EditProfileFragment fm = new EditProfileFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm).addToBackStack(null).commit();
                        return true;
                    } else {
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    }
                default:
                    return false;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fm = null;
        Bundle args = new Bundle();
        if (id == R.id.sign) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_my_profile) {
            fm = new EditProfileFragment();
        } else if (id == R.id.nav_aboutus) {
            startActivity(new Intent(getApplicationContext(), AboutUs.class));
        } else if (id == R.id.nav_policy) {
            fm = new TermsAndConditionFragment();
            args.putString("url", TERMS_URL);
            args.putString(titleKey, getResources().getString(R.string.nav_terms));
            fm.setArguments(args);
        } else if (id == R.id.nav_contact) {
            fm = new ContactUsFragment();
            args.putString("url", SUPPORT_URL);
            args.putString(titleKey, getResources().getString(R.string.nav_terms));
            fm.setArguments(args);

        } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_logout) {
            sessionManagement.logoutSession();
            finish();
        }
        if (fm != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm).addToBackStack(null).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getPackageName() + " APP"); //getPackageName()
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

        /*public void sideMenu() {
            if (sessionManagement.isLoggedIn()) {
                navMenu.findItem(R.id.nav_logout).setVisible(true);
                navMenu.findItem(R.id.nav_my_profile).setVisible(true);
                navMenu.findItem(R.id.sign).setVisible(false);
                navMenu.findItem(R.id.nav_powerd).setVisible(true);
                username.setText("Welcome! " + "" + sessionManagement.getUserDetails().get(BaseURL.KEY_NAME));
            } else {
                navMenu.findItem(R.id.login).setVisible(false);
                navMenu.findItem(R.id.nav_my_profile).setVisible(false);
                navMenu.findItem(R.id.nav_logout).setVisible(false);
                navMenu.findItem(R.id.sign).setVisible(true);
            }
        }*/

    @Override
    public void onLocationChanged(@NonNull Location locations) {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                if (!sessionManagement.getLatPref().equalsIgnoreCase("") && !sessionManagement.getLangPref().equalsIgnoreCase("")) {
                    onLocChanged1(locations);
                } else {
                    enterInFirst = true;
                    location = locations;
                    if (navigation.getSelectedItemId() == R.id.navigation_home) {
                        loadFragment(new HomeeeFragment(fragmentClickListner));
                    }
                    getAddress();
                }
            }
        }).start();

    }

    private void onLocChanged1(Location locations) {
        if (Double.parseDouble(sessionManagement.getLatPref())>0.0 && Double.parseDouble(sessionManagement.getLangPref())>0.0){
            DecimalFormat dFormat = new DecimalFormat(decimalRefKey);
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
                if (!enterInFirst) {
                    enterInFirst = true;
                    location = locations;
                    getAddress();
                    if (navigation.getSelectedItemId() == R.id.navigation_home) {
                        loadFragment(new HomeeeFragment(fragmentClickListner));
                    }
                }
            } else {
                enterInFirst = true;
                if (addres.getText().toString().equalsIgnoreCase("")) {
                    if (navigation.getSelectedItemId() == R.id.navigation_home) {
                        loadFragment(new HomeeeFragment(fragmentClickListner));
                    }
                    getAddress();
                }
            }
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(cardQtyKey)) {
            int badgeCount = pref.getInt(cardQtyKey, 0);
            if (badgeCount > 0) {
                navigation.getOrCreateBadge(R.id.navigation_notifications123).setNumber(badgeCount);
            } else {
                navigation.removeBadge(R.id.navigation_notifications123);
            }
        }
    }

    @Override
    protected void onDestroy() {
        pref.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 4 && data != null && data.getExtras() != null) {
            ArrayList<NewPendingDataModel> orderSubModels = (ArrayList<NewPendingDataModel>) data.getSerializableExtra("datalist");
            hitFragmentOrder(orderSubModels);
        } else if (requestCode == 22) {
            if (sessionManagement.getLatPref().length() > 1 && sessionManagement.getLangPref().length() > 1) {
                DecimalFormat dFormat = new DecimalFormat(decimalRefKey);
                LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
                double latitude = Double.parseDouble(dFormat.format(latLng.latitude));
                double longitude = Double.parseDouble(dFormat.format(latLng.longitude));
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                getAddress();
                if (navigation.getSelectedItemId() == R.id.navigation_home) {
                    loadFragment(new HomeeeFragment(fragmentClickListner));
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void hitFragmentOrder(ArrayList<NewPendingDataModel> orderSubModels) {
        if (orderSubModels != null) {
            dbcart.clearCart();
            for (int i = 0; i < orderSubModels.size(); i++) {
                NewPendingDataModel odModel = orderSubModels.get(i);
                if (odModel.getDescription() != null && !odModel.getDescription().equalsIgnoreCase("")) {
                    double price = Double.parseDouble(odModel.getPrice()) / Double.parseDouble(odModel.getQty());
                    HashMap<String, String> map = new HashMap<>();
                    map.put("varient_id", odModel.getVarientId());
                    map.put("product_name", odModel.getProductName());
                    map.put("category_id", odModel.getVarientId());
                    map.put(titleKey, odModel.getProductName());
                    map.put("price", String.valueOf(price));
                    map.put("mrp", odModel.getTotalMrp());
                    map.put("product_image", odModel.getVarientImage());
                    map.put(statusKey, "1");
                    map.put("in_stock", "");
                    map.put("unit_value", odModel.getQuantity() + "" + odModel.getUnit());
                    map.put("unit", "");
                    map.put("increament", "0");
                    map.put("rewards", "0");
                    map.put("stock", odModel.getStock());
                    map.put("product_description", odModel.getDescription());

                    if (!odModel.getQuantity().equalsIgnoreCase("0")) {
                        dbcart.setCart(map, Integer.parseInt(odModel.getQuantity()));
                    } else {
                        dbcart.removeItemFromCart(map.get("varient_id"));
                    }

                    hitFragmentOrderHelp();
                }
            }
            loadFragment(new CartFragment());
        }
    }

    private void hitFragmentOrderHelp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pref.edit().putInt(cardQtyKey, dbcart.getCartCount()).apply();
        }
    }


    private void getCurrency() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, CURRENCY_API, response -> {
            try {
                JSONObject currencyObject = new JSONObject(response);
                if (currencyObject.getString(statusKey).equalsIgnoreCase("1") && currencyObject.getString("message").equalsIgnoreCase("currency")) {
                    JSONObject dataObject = currencyObject.getJSONObject("data");
                    sessionManagement.setCurrency(dataObject.getString("currency_name"), dataObject.getString("currency_sign"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        stringRequest.setRetryPolicy(new RetryPolicy() {
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
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void checkOtpStatus() {

        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<ForgotEmailModel> checkOtpStatus = apiInterface.getOtpOnOffStatus();
        checkOtpStatus.enqueue(new Callback<ForgotEmailModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgotEmailModel> call, @NonNull retrofit2.Response<ForgotEmailModel> response) {
                if (response.isSuccessful()) {
                    ForgotEmailModel model = response.body();
                    if (model != null) {
                        if (model.getStatus().equalsIgnoreCase("0")) {
                            sessionManagement.setOtpStatus("0");
                        } else {
                            sessionManagement.setOtpStatus("1");
                        }
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ForgotEmailModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void checkMapSelection() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL_MAP)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<MapSelectionModel> checkOtpStatus = apiInterface.getMapSelectionStatus();
        checkOtpStatus.enqueue(new Callback<MapSelectionModel>() {
            @Override
            public void onResponse(@NonNull Call<MapSelectionModel> call, @NonNull retrofit2.Response<MapSelectionModel> response) {
                if (response.isSuccessful()) {
                    MapSelectionModel model = response.body();
                    if (model != null) {
                        if (model.getData().getMapbox().equalsIgnoreCase("1")) {
                            sessionManagement.setMapSelection("mapbox");
                        } else if (model.getData().getGoogleMap().equalsIgnoreCase("1")) {
                            sessionManagement.setMapSelection("googlemap");
                        }

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MapSelectionModel> call, @NonNull Throwable t) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
