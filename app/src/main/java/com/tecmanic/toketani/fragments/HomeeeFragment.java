package com.tecmanic.toketani.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecmanic.toketani.Categorygridquantity;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.activity.AddressLocationActivity;
import com.tecmanic.toketani.activity.CategoryPage;
import com.tecmanic.toketani.activity.DealActivity;
import com.tecmanic.toketani.activity.MapboxActivity;
import com.tecmanic.toketani.activity.ProductDetails;
import com.tecmanic.toketani.activity.ViewAllTopDeals;
import com.tecmanic.toketani.adapters.BannerAdapter;
import com.tecmanic.toketani.adapters.BannerAdapter2;
import com.tecmanic.toketani.adapters.HomeAdapter;
import com.tecmanic.toketani.adapters.MainScreenAdapter;
import com.tecmanic.toketani.adapters.NewCategoryGridAdapter;
import com.tecmanic.toketani.adapters.Popular_adapter;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.constans.RecyclerTouchListener;
import com.tecmanic.toketani.modelclass.MainScreenList;
import com.tecmanic.toketani.modelclass.MapboxModel;
import com.tecmanic.toketani.modelclass.NewCartModel;
import com.tecmanic.toketani.modelclass.NewCategoryDataModel;
import com.tecmanic.toketani.modelclass.NotificationBannerStatus;
import com.tecmanic.toketani.modelclass.Popular_model;
import com.tecmanic.toketani.modelclass.homemodel.Banner1;
import com.tecmanic.toketani.modelclass.homemodel.Banner2;
import com.tecmanic.toketani.modelclass.homemodel.MainHomeModel;
import com.tecmanic.toketani.modelclass.homemodel.NewTopCategory;
import com.tecmanic.toketani.network.ApiInterface;
import com.tecmanic.toketani.util.AppController;
import com.tecmanic.toketani.util.CustomVolleyJsonRequest;
import com.tecmanic.toketani.util.FragmentClickListner;
import com.tecmanic.toketani.util.ProdcutDetailsVerifier;
import com.tecmanic.toketani.util.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.VolleyLog.TAG;
import static com.tecmanic.toketani.config.BaseURL.ADDRESS;
import static com.tecmanic.toketani.config.BaseURL.CITY;
import static com.tecmanic.toketani.config.BaseURL.LAT;
import static com.tecmanic.toketani.config.BaseURL.LONG;
import static com.tecmanic.toketani.config.BaseURL.MY_PREPRENCE;

public class HomeeeFragment extends Fragment implements View.OnClickListener {
    private final List<NewTopCategory> categoryModelsList = new ArrayList<>();
    private final ArrayList<String> imageString1 = new ArrayList<>();
    private final List<MainScreenList> screenLists = new ArrayList<>();
    private final List<NewCartModel> topSelling = new ArrayList<>();
    private final List<NewCartModel> whatsNew = new ArrayList<>();
    private final List<NewCartModel> recentSelling = new ArrayList<>();
    private final List<NewCartModel> dealOftheday = new ArrayList<>();
    private final String actionKey = "action_name";
    private final String noorderkey = "No Order found in your location!";
    private final String whatsNewKey = "WHAT'S NEW";
    private final String statusKey = "status";
    private final String bannerStringKey = "banner_name";
    private final String bannerIdKey = "banner_id";
    private final String bannerImageKey = "banner_image";
    private final List<Banner1> banner_1 = new ArrayList<>();
    private final List<Banner2> banner_2 = new ArrayList<>();
    public TextView txt;
    public LinearLayout cancl;
    ViewPager viewPager;
    TabLayout tabLayout;
    Float translationY = 100f;
    FloatingActionButton fabMain;
    FloatingActionButton fabOne;
    FloatingActionButton fabTwo;
    FloatingActionButton fabThree;
    FloatingActionButton fabfour;
    LinearLayout parentLay;
    LinearLayout progressBar;
    CardView searchLayout;
    NestedScrollView scrollView;
    RecyclerView rvItems;
    SliderLayout bannerSlider;
    SliderLayout featuredslider;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    Boolean isMenuOpen = false;
    TextView viewallTopdeals;
    String latitude;
    String longitude;
    String address;
    String city;
    SharedPreferences sharedPreferences;
    //    ArrayList<String> imageString = new ArrayList<>();
    RecyclerView recyclerImages;
    LinearLayout changeLocLay;
    RecyclerView recyclerImages1;
    int pageNumber = 0;
    BottomSheetBehavior behavior;
    private BannerAdapter bannerAdapter;
    private BannerAdapter bannerAdapter1;
    private BannerAdapter2 bannerAdapter2;
    private HomeAdapter adapter;
    private SessionManagement sessionManagement;
    private FragmentClickListner fragmentClickListner;
    private ViewPager2 viewPager2;
    private ContentLoadingProgressBar progressDialog;
    private MainScreenAdapter screenAdapter;
    private Context contexts;
    private boolean bannerStatus = false;
    private String bannerText = "";
    private RelativeLayout notificationLay;
    private LinearLayout banner_layount;
    private TextView notificaitonStatus;
    private Animation a;
    private boolean enterVal = false;
    private LinearLayout llPop, lltop;
    private RecyclerView rv_populrcate;
    private RecyclerView recycler_product;
    private NewCategoryGridAdapter productadapter;
    private LinearLayout bottom_sheet;
    private Popular_adapter popularAdapter;
    private final List<Popular_model> popularList = new ArrayList<>();
    private final List<NewCategoryDataModel> newCategoryDataModel = new ArrayList<>();
    private ContentLoadingProgressBar product_loader;

    public HomeeeFragment() {
    }

    public HomeeeFragment(FragmentClickListner fragmentClickListner) {
        this.fragmentClickListner = fragmentClickListner;
    }

    private void getNotificationStatus() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<NotificationBannerStatus> checkOtpStatus = apiInterface.getNotificationBannerStatus();
        checkOtpStatus.enqueue(new Callback<NotificationBannerStatus>() {
            @Override
            public void onResponse(@NonNull Call<NotificationBannerStatus> call, @NonNull retrofit2.Response<NotificationBannerStatus> response) {
                if (response.isSuccessful()) {
                    NotificationBannerStatus model = response.body();
                    if (model != null) {
                        bannerStatus = model.getData().getStatus().equalsIgnoreCase("1");
                        bannerText = model.getData().getNotice();
                        if (bannerStatus) {
                            notificationLay.setVisibility(View.VISIBLE);
                            notificaitonStatus.setText(bannerText);
                            notificaitonStatus.setSelected(true);
                        } else {
                            notificationLay.setVisibility(View.GONE);
                            notificaitonStatus.setText("");
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationBannerStatus> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);
        requireActivity().setTitle(getResources().getString(R.string.app_name));
        contexts = container.getContext();
        sharedPreferences = requireContext().getSharedPreferences(MY_PREPRENCE, MODE_PRIVATE);
        sessionManagement = new SessionManagement(container.getContext());
        progressDialog = view.findViewById(R.id.progressB);
        latitude = sharedPreferences.getString(LAT, null);
        longitude = sharedPreferences.getString(LONG, null);
        address = sharedPreferences.getString(ADDRESS, null);
        city = sharedPreferences.getString(CITY, null);
        rvItems = view.findViewById(R.id.rv_home);
        progressBar = view.findViewById(R.id.progress_bar);
        product_loader = view.findViewById(R.id.product_loader);
        progressBar.setOnClickListener(views -> Log.i("TAG", "not work"));
        /*changeLocLay = view.findViewById(R.id.change_loc_lay);*/
        notificationLay = view.findViewById(R.id.notification_lay);
        notificaitonStatus = view.findViewById(R.id.notificaiton_status);
        banner_layount = view.findViewById(R.id.banner_layount);
        notificaitonStatus.setSelected(true);
        /*TextView changeLoc = view.findViewById(R.id.change_loc);*/
        tabLayout = view.findViewById(R.id.tablayout);
        viewallTopdeals = view.findViewById(R.id.viewall_topdeals);
        viewPager = view.findViewById(R.id.pager_product);
        viewPager2 = view.findViewById(R.id.viewpa_2);
        recyclerImages1 = view.findViewById(R.id.recycler_image_slider1);
        recyclerImages = view.findViewById(R.id.recycler_image_slider);
        recyclerImages.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        bannerAdapter = new BannerAdapter(getActivity(), banner_1);
        recyclerImages.setAdapter(bannerAdapter);
        recyclerImages1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        bannerAdapter2 = new BannerAdapter2(getActivity(), banner_2);
        recyclerImages1.setAdapter(bannerAdapter2);
        bannerSlider = view.findViewById(R.id.relative_banner);
        featuredslider = view.findViewById(R.id.featured_img_slider);
        rvItems = view.findViewById(R.id.rv_home);
        fabMain = view.findViewById(R.id.fabMain);
        fabOne = view.findViewById(R.id.fabOne);
        fabTwo = view.findViewById(R.id.fabTwo);
        fabThree = view.findViewById(R.id.fabThree);
        fabfour = view.findViewById(R.id.fabfour);
        parentLay = view.findViewById(R.id.parent_lay);
        llPop = view.findViewById(R.id.llPop);
        rv_populrcate = view.findViewById(R.id.rv_populrcate);
        txt = view.findViewById(R.id.txt);
        cancl = view.findViewById(R.id.cancl);
        bottom_sheet = view.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottom_sheet);
//        new Handler().postDelayed(() -> {
//
//        },500L);

        a = AnimationUtils.loadAnimation(contexts, R.anim.slide_out_right_to_left);
        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = () -> {
            Log.i("TAGPAGE", "" + pageNumber);
            if (pageNumber >= bannerAdapter2.getItemCount() - 1) {
                enterVal = true;
                pageNumber = 0;
                Activity activity = (Activity) contexts;
                activity.runOnUiThread(() -> {
                    banner_layount.setVisibility(View.INVISIBLE);
                    recyclerImages1.smoothScrollToPosition(0);
                    recyclerImages1.postDelayed(() -> {
                        a.reset();
                        banner_layount.clearAnimation();
                        banner_layount.setVisibility(View.INVISIBLE);
                        banner_layount.startAnimation(a);
                    }, 90);
                });
            } else {
                if (!enterVal) {
                    pageNumber++;
                    recyclerImages1.smoothScrollToPosition(pageNumber);
                }
            }

        };
        worker.scheduleAtFixedRate(runnable, 5000, 3000L, TimeUnit.MILLISECONDS);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                banner_layount.setVisibility(View.VISIBLE);
                enterVal = false;
//                worker.scheduleAtFixedRate(runnable, 5000, 3000L,TimeUnit.MILLISECONDS);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        screenAdapter = new MainScreenAdapter(container.getContext(), screenLists, new ProdcutDetailsVerifier() {
            @Override
            public void onProductClick(int position, String ViewType) {
                if (screenLists.get(viewPager2.getCurrentItem()).getViewType().equalsIgnoreCase("TOP SELLING")) {
                    Intent intent = new Intent(requireActivity(), ProductDetails.class);
                    intent.putExtra("sId", topSelling.get(position).getProductId());
                    intent.putExtra("sVariant_id", topSelling.get(position).getVarientId());
                    intent.putExtra("sName", topSelling.get(position).getProductName());
                    intent.putExtra("descrip", topSelling.get(position).getDescription());
                    intent.putExtra("price", topSelling.get(position).getPrice());
                    intent.putExtra("mrp", topSelling.get(position).getMrp());
                    intent.putExtra("unit", topSelling.get(position).getUnit());
                    intent.putExtra("stock", topSelling.get(position).getStock());
                    intent.putExtra("qty", topSelling.get(position).getQuantity());
                    intent.putExtra("image", topSelling.get(position).getProductImage());

                    startActivityForResult(intent, 21);
                } else if (screenLists.get(viewPager2.getCurrentItem()).getViewType().equalsIgnoreCase("RECENT SELLING")) {
                    Intent intent = new Intent(requireActivity(), ProductDetails.class);
                    intent.putExtra("sId", recentSelling.get(position).getProductId());
                    intent.putExtra("sVariant_id", recentSelling.get(position).getVarientId());
                    intent.putExtra("sName", recentSelling.get(position).getProductName());
                    intent.putExtra("descrip", recentSelling.get(position).getDescription());
                    intent.putExtra("price", recentSelling.get(position).getPrice());
                    intent.putExtra("mrp", recentSelling.get(position).getMrp());
                    intent.putExtra("unit", recentSelling.get(position).getUnit());
                    intent.putExtra("stock", recentSelling.get(position).getStock());
                    intent.putExtra("qty", recentSelling.get(position).getQuantity());
                    intent.putExtra("image", recentSelling.get(position).getProductImage());

                    startActivityForResult(intent, 21);
                } else if (screenLists.get(viewPager2.getCurrentItem()).getViewType().equalsIgnoreCase("DEALS OF THE DAY")) {
                    Intent intent = new Intent(requireActivity(), ProductDetails.class);
                    intent.putExtra("sId", dealOftheday.get(position).getProductId());
                    intent.putExtra("sVariant_id", dealOftheday.get(position).getVarientId());
                    intent.putExtra("sName", dealOftheday.get(position).getProductName());
                    intent.putExtra("descrip", dealOftheday.get(position).getDescription());
                    intent.putExtra("price", dealOftheday.get(position).getPrice());
                    intent.putExtra("mrp", dealOftheday.get(position).getMrp());
                    intent.putExtra("unit", dealOftheday.get(position).getUnit());
                    intent.putExtra("stock", dealOftheday.get(position).getStock());
                    intent.putExtra("qty", dealOftheday.get(position).getQuantity());
                    intent.putExtra("image", dealOftheday.get(position).getProductImage());

                    startActivityForResult(intent, 21);
                } else if (screenLists.get(viewPager2.getCurrentItem()).getViewType().equalsIgnoreCase("WHAT'S NEW")) {
                    Intent intent = new Intent(requireActivity(), ProductDetails.class);
                    intent.putExtra("sId", whatsNew.get(position).getProductId());
                    intent.putExtra("sVariant_id", whatsNew.get(position).getVarientId());
                    intent.putExtra("sName", whatsNew.get(position).getProductName());
                    intent.putExtra("descrip", whatsNew.get(position).getDescription());
                    intent.putExtra("price", whatsNew.get(position).getPrice());
                    intent.putExtra("mrp", whatsNew.get(position).getMrp());
                    intent.putExtra("unit", whatsNew.get(position).getUnit());
                    intent.putExtra("stock", whatsNew.get(position).getStock());
                    intent.putExtra("qty", whatsNew.get(position).getQuantity());
                    intent.putExtra("image", whatsNew.get(position).getProductImage());
                    startActivityForResult(intent, 21);
                }
            }
        });
        viewPager2.setAdapter(screenAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvItems.setLayoutManager(gridLayoutManager);
        rvItems.setItemAnimator(new DefaultItemAnimator());
        rvItems.setNestedScrollingEnabled(false);
        adapter = new HomeAdapter(categoryModelsList);
        rvItems.setAdapter(adapter);
        rvItems.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvItems, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String getid = categoryModelsList.get(position).getCat_id();
                Intent intent = new Intent(getActivity(), CategoryPage.class);
                intent.putExtra("cat_id", getid);
                intent.putExtra("title", categoryModelsList.get(position).getTitle());
                intent.putExtra("image", categoryModelsList.get(position).getImage());
                startActivityForResult(intent, 24);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        /*changeLoc.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(contexts, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(contexts, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                fragmentClickListner.onLocationPermission(true);
            } else {
                if (sessionManagement.getMapSelection().equalsIgnoreCase("mapbox")) {
                    getMapKey();
                } else {
                    startActivityForResult(new Intent(v.getContext(), AddressLocationActivity.class), 22);
                }
            }
        });*/
        product_loader.hide();
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 3);
        rv_populrcate.setLayoutManager(gridLayoutManager1);
        popularAdapter = new Popular_adapter(popularList);
        rv_populrcate.setAdapter(popularAdapter);
        rv_populrcate.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_populrcate, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                product_loader.show();
                newCategoryDataModel.clear();
                if (productadapter!=null){
                    productadapter.notifyDataSetChanged();
                }
                product(popularList.get(position).getCat_id());
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        cancl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        Categorygridquantity categorygridquantity = new Categorygridquantity() {
            @Override
            public void onClick(View vieww, int position, String ccId, String iddd) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                txt.setText(iddd);

                //   Varient_product(ccId, recyler_popup, iddd);
            }

            @Override
            public void onCartItemAddOrMinus() {

            }

            @Override
            public void onProductDetials(int position) {
                Intent intent = new Intent(requireActivity(), ProductDetails.class);
                intent.putExtra("sId", newCategoryDataModel.get(position).getProductId());
                intent.putExtra("sVariant_id", newCategoryDataModel.get(position).getVarientId());
                intent.putExtra("sName", newCategoryDataModel.get(position).getProductName());
                intent.putExtra("descrip", newCategoryDataModel.get(position).getDescription());
                intent.putExtra("price", newCategoryDataModel.get(position).getPrice());
                intent.putExtra("mrp", newCategoryDataModel.get(position).getMrp());
                intent.putExtra("unit", newCategoryDataModel.get(position).getUnit());
                intent.putExtra("stock", newCategoryDataModel.get(position).getStock());
                intent.putExtra("qty", newCategoryDataModel.get(position).getQuantity());
                intent.putExtra("image", newCategoryDataModel.get(position).getProductImage());
                startActivityForResult(intent, 21);
            }
        };
        recycler_product = view.findViewById(R.id.recycler_product);
        recycler_product.setLayoutManager(new GridLayoutManager(getContext(), 1));
        productadapter = new NewCategoryGridAdapter(newCategoryDataModel, getContext(), categorygridquantity);
        recycler_product.setAdapter(productadapter);


//        bannerAdapter1 = new BannerAdapter(getActivity(), imageString1);
//        recyclerImages1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        recyclerImages1.setAdapter(bannerAdapter1);
        searchLayout = view.findViewById(R.id.ll3);
        scrollView = view.findViewById(R.id.scroll_view);
        scrollView.setSmoothScrollingEnabled(true);
        if (isOnline()) {
//            makeGetSliderRequest();
//            secondBanner();
//            topSelling();
            getHomePageRequest();
            popularCategoryRequest();
        }

        getNotificationStatus();

        searchLayout.setOnClickListener(v -> {

            SearchFragment searchFragment = new SearchFragment();
            FragmentManager manager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.contentPanel, searchFragment);
            fragmentTransaction.commit();

        });


        fabOne.setAlpha(0f);
        fabTwo.setAlpha(0f);
        fabThree.setAlpha(0f);
        fabfour.setAlpha(0f);

        fabOne.setTranslationY(translationY);
        fabTwo.setTranslationY(translationY);
        fabThree.setTranslationY(translationY);
        fabfour.setTranslationY(translationY);

        fabMain.setOnClickListener(this);
        fabOne.setOnClickListener(this);
        fabTwo.setOnClickListener(this);
        fabThree.setOnClickListener(this);
        fabfour.setOnClickListener(this);

        closeMenu(false);

        setTabs();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                viewPager2.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                try {
                    if (ViewPager2.SCROLL_STATE_IDLE == state) {
                        screenAdapter.notifyItemChanged(viewPager2.getCurrentItem());
                    } else {
                        super.onPageScrollStateChanged(state);
                    }
                } catch (IllegalStateException e) {
                    super.onPageScrollStateChanged(state);
                }
            }
        });

        viewallTopdeals.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ViewAllTopDeals.class);
            if (tabLayout.getTabAt(viewPager2.getCurrentItem()).getText().toString().equalsIgnoreCase("TOP SELLING")) {
                flowIntentTopDeal(intent);
            } else if (tabLayout.getTabAt(viewPager2.getCurrentItem()).getText().toString().equalsIgnoreCase("RECENT SELLING")) {
                flowIntentRecentDeal(intent);
            } else if (tabLayout.getTabAt(viewPager2.getCurrentItem()).getText().toString().equalsIgnoreCase("DEALS OF THE DAY")) {
                flowIntentDealDay(v);
            } else if (tabLayout.getTabAt(viewPager2.getCurrentItem()).getText().toString().equalsIgnoreCase(whatsNewKey)) {
                flowIntentWhatsNew(intent);
            }
        });
        return view;
    }

    public void getHomePageRequest() {
        screenLists.clear();
        topSelling.clear();
        recentSelling.clear();
        dealOftheday.clear();
        whatsNew.clear();
        progressDialog.show();
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<MainHomeModel> mainHomeModel = apiInterface.getMainHomeModel(sessionManagement.getLatPref(), sessionManagement.getLangPref());
//        Call<MainHomeModel> mainHomeModel = apiInterface.getMainHomeModel("18.4265948", "79.8621061");
        mainHomeModel.enqueue(new Callback<MainHomeModel>() {
            @Override
            public void onResponse(@NonNull Call<MainHomeModel> call, @NonNull retrofit2.Response<MainHomeModel> response) {
                progressDialog.hide();
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equalsIgnoreCase("1")) {
                    Log.i("HomeD", response.body().toString());
                    banner_1.clear();
                    banner_1.addAll(response.body().getBanner1());
//                    ArrayList<String> demo = new ArrayList<>();
//                    demo.replaceAll();
                    bannerAdapter.notifyDataSetChanged();
                    banner_2.clear();
                    banner_2.addAll(response.body().getBanner2());
                    bannerAdapter2.notifyDataSetChanged();
                    topSelling.clear();
                    topSelling.addAll(response.body().getTop_selling());
                    recentSelling.clear();
                    recentSelling.addAll(response.body().getRecentselling());
                    dealOftheday.clear();
                    dealOftheday.addAll(response.body().getDeal_products());
                    whatsNew.clear();
                    whatsNew.addAll(response.body().getWhats_new());
                    if (topSelling.size() > 0) {
                        screenLists.add(new MainScreenList("TOP SELLING", topSelling, recentSelling, dealOftheday, whatsNew));
                    }
                    if (recentSelling.size() > 0) {
                        screenLists.add(new MainScreenList("RECENT SELLING", topSelling, recentSelling, dealOftheday, whatsNew));
                    }
                    if (dealOftheday.size() > 0) {
                        screenLists.add(new MainScreenList("DEALS OF THE DAY", topSelling, recentSelling, dealOftheday, whatsNew));
                    }
                    if (whatsNew.size() > 0) {
                        screenLists.add(new MainScreenList("WHAT'S NEW", topSelling, recentSelling, dealOftheday, whatsNew));
                    }

                    if (screenLists.size() > 0) {
                        /*changeLocLay.setVisibility(View.GONE);*/
//                    viewpager_layout.setVisibility(View.VISIBLE);
                        viewPager2.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.VISIBLE);
                        viewallTopdeals.setVisibility(View.VISIBLE);
                    } else {
                        /*changeLocLay.setVisibility(View.VISIBLE);*/
//                    viewpager_layout.setVisibility(View.GONE);
                        viewPager2.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.GONE);
                        viewallTopdeals.setVisibility(View.GONE);
                    }
                    setTabs();
                    screenAdapter.notifyDataSetChanged();
                    categoryModelsList.clear();
                    categoryModelsList.addAll(response.body().getTop_category());
                    if (categoryModelsList.size() > 0) {
//                        lltop.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    } else {
//                        lltop.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainHomeModel> call, @NonNull Throwable t) {
                if (screenLists.size() > 0) {
                    /*changeLocLay.setVisibility(View.GONE);*/
//                    viewpager_layout.setVisibility(View.VISIBLE);
                    viewPager2.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    viewallTopdeals.setVisibility(View.VISIBLE);
                } else {
                    /*changeLocLay.setVisibility(View.VISIBLE);*/
//                    viewpager_layout.setVisibility(View.GONE);
                    viewPager2.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    viewallTopdeals.setVisibility(View.GONE);
                }
                setTabs();
                screenAdapter.notifyDataSetChanged();
                if (categoryModelsList.size() > 0) {
//                    lltop.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else {
//                    lltop.setVisibility(View.GONE);
                }
                progressDialog.hide();
            }
        });

    }

    private void flowIntentTopDeal(Intent intent) {
        if (!topSelling.isEmpty()) {
            intent.putExtra(actionKey, "Top_Deals_Fragment");
            startActivityForResult(intent, 56);
        } else {
            Toast.makeText(contexts, noorderkey, Toast.LENGTH_SHORT).show();
        }
    }

    private void flowIntentWhatsNew(Intent intent) {
        if (!whatsNew.isEmpty()) {
            intent.putExtra(actionKey, "Whats_New_Fragment");
            startActivityForResult(intent, 56);
        } else {
            Toast.makeText(contexts, noorderkey, Toast.LENGTH_SHORT).show();
        }
    }

    private void flowIntentRecentDeal(Intent intent) {
        if (!recentSelling.isEmpty()) {
            intent.putExtra(actionKey, "Recent_Details_Fragment");
            startActivityForResult(intent, 56);
        } else {
            Toast.makeText(contexts, noorderkey, Toast.LENGTH_SHORT).show();
        }
    }

    private void flowIntentDealDay(View v) {
        if (!dealOftheday.isEmpty()) {
            Intent intent1 = new Intent(v.getContext(), DealActivity.class);
            intent1.putExtra(actionKey, "Deals_Fragment");
            startActivityForResult(intent1, 56);
        } else {
            Toast.makeText(contexts, noorderkey, Toast.LENGTH_SHORT).show();
        }
    }

    private void show() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void getMapKey() {
        show();
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.MAPBOX_KEY, response -> {
            Gson mapGson = new Gson();
            MapboxModel mapModel = mapGson.fromJson(response, MapboxModel.class);
            String mapAccessToken = mapModel.getData().getMapApiKey();
            show();
            startActivityForResult(new Intent(requireActivity(), MapboxActivity.class).putExtra("map_key", mapAccessToken), 22);
        }, error -> {
            show();
            error.printStackTrace();
        });

        RequestQueue rq = Volley.newRequestQueue(requireActivity());
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

    private void setTabs() {
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == 0) {
                if (!screenLists.isEmpty()) {
                    tab.setText(screenLists.get(0).getViewType());
                }
            } else if (position == 1) {
                if (!screenLists.isEmpty()) {
                    tab.setText(screenLists.get(1).getViewType());
                }

            } else if (position == 2) {
                if (!screenLists.isEmpty()) {
                    tab.setText(screenLists.get(2).getViewType());
                }
            } else if (position == 3) {
                tab.setText(whatsNewKey);
            }

        });
        tabLayoutMediator.attach();
    }

//    private void topSelling() {
//        progressDialog.show();
//        screenLists.clear();
//        topSelling.clear();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOME_TOP_SELLING, response -> {
//            Log.d("HomeTopSelling", response);
//
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String status = jsonObject.getString(statusKey);
//                if (status.equals("1")) {
//                    screenLists.add(new MainScreenList("TOP SELLING", topSelling, recentSelling, dealOftheday, whatsNew));
//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<List<NewCartModel>>() {
//                    }.getType();
//                    List<NewCartModel> listorl = gson.fromJson(jsonObject.getString("data"), listType);
//                    topSelling.addAll(listorl);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//                recentDeal();
//            }
//
//        }, error -> recentDeal()) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("lat", sessionManagement.getLatPref());
//                params.put("lng", sessionManagement.getLangPref());
//                params.put("city", sessionManagement.getLocationCity());
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(contexts);
//        requestQueue.getCache().clear();
//        stringRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 60000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 0;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//                error.printStackTrace();
//            }
//        });
//        requestQueue.add(stringRequest);
//    }
//
//    private void whatsNew() {
//        whatsNew.clear();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, WHATSNEW, response -> {
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String status = jsonObject.getString(statusKey);
//                if (status.equals("1")) {
//                    screenLists.add(new MainScreenList(whatsNewKey, topSelling, recentSelling, dealOftheday, whatsNew));
//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<List<NewCartModel>>() {
//                    }.getType();
//                    List<NewCartModel> listorl = gson.fromJson(jsonObject.getString("data"), listType);
//                    whatsNew.addAll(listorl);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//
//                if (!screenLists.isEmpty()) {
//                    changeLocLay.setVisibility(View.GONE);
//                    viewPager2.setVisibility(View.VISIBLE);
//                    tabLayout.setVisibility(View.VISIBLE);
//                    viewallTopdeals.setVisibility(View.VISIBLE);
//                } else {
//                    changeLocLay.setVisibility(View.VISIBLE);
//                    viewPager2.setVisibility(View.GONE);
//                    tabLayout.setVisibility(View.GONE);
//                    viewallTopdeals.setVisibility(View.GONE);
//                }
//                setTabs();
//                progressDialog.hide();
//                screenAdapter.notifyDataSetChanged();
//                makeGetCategoryRequest();
//
//            }
//
//        }, error -> {
//            if (!screenLists.isEmpty()) {
//                changeLocLay.setVisibility(View.GONE);
//                viewPager2.setVisibility(View.VISIBLE);
//                tabLayout.setVisibility(View.VISIBLE);
//                viewallTopdeals.setVisibility(View.VISIBLE);
//            } else {
//                changeLocLay.setVisibility(View.VISIBLE);
//                viewPager2.setVisibility(View.GONE);
//                tabLayout.setVisibility(View.GONE);
//                viewallTopdeals.setVisibility(View.GONE);
//            }
//            setTabs();
//            progressDialog.hide();
//            screenAdapter.notifyDataSetChanged();
//            makeGetCategoryRequest();
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("lat", sessionManagement.getLatPref());
//                params.put("lng", sessionManagement.getLangPref());
//                params.put("city", sessionManagement.getLocationCity());
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(contexts);
//        requestQueue.getCache().clear();
//        stringRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 60000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 0;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//                error.printStackTrace();
//            }
//        });
//        requestQueue.add(stringRequest);
//    }
//
//
//    private void dealOfTheDay() {
//        dealOftheday.clear();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOME_DEAL, response -> {
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String status = jsonObject.getString(statusKey);
//                if (status.equals("1")) {
//                    screenLists.add(new MainScreenList("DEALS OF THE DAY", topSelling, recentSelling, dealOftheday, whatsNew));
//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<List<NewCartModel>>() {
//                    }.getType();
//                    List<NewCartModel> listorl = gson.fromJson(jsonObject.getString("data"), listType);
//                    dealOftheday.addAll(listorl);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//                whatsNew();
//            }
//
//        }, error -> whatsNew()) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("lat", sessionManagement.getLatPref());
//                params.put("lng", sessionManagement.getLangPref());
//                params.put("city", sessionManagement.getLocationCity());
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(contexts);
//        requestQueue.getCache().clear();
//        stringRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 60000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 0;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//                error.printStackTrace();
//            }
//        });
//        requestQueue.add(stringRequest);
//    }
//
//    private void recentDeal() {
//        recentSelling.clear();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOME_RECENT, response -> {
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String status = jsonObject.getString(statusKey);
//                if (status.equals("1")) {
//                    screenLists.add(new MainScreenList("RECENT SELLING", topSelling, recentSelling, dealOftheday, whatsNew));
//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<List<NewCartModel>>() {
//                    }.getType();
//                    List<NewCartModel> listorl = gson.fromJson(jsonObject.getString("data"), listType);
//                    recentSelling.addAll(listorl);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//                dealOfTheDay();
//            }
//
//        }, error -> dealOfTheDay()) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("lat", sessionManagement.getLatPref());
//                params.put("lng", sessionManagement.getLangPref());
//                params.put("city", sessionManagement.getLocationCity());
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(contexts);
//        requestQueue.getCache().clear();
//        stringRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 60000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 0;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//                error.printStackTrace();
//            }
//        });
//        requestQueue.add(stringRequest);
//    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fabMain.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        fabOne.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabOne.setVisibility(View.VISIBLE);
        fabTwo.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.setVisibility(View.VISIBLE);
        fabThree.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.setVisibility(View.VISIBLE);
        fabfour.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabfour.setVisibility(View.VISIBLE);
    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabOne.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabOne.setVisibility(View.GONE);
        fabTwo.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.setVisibility(View.GONE);
        fabThree.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.setVisibility(View.GONE);
        fabfour.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabfour.setVisibility(View.GONE);


    }

    private void closeMenu(boolean value) {
        isMenuOpen = value;

        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabOne.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabOne.setVisibility(View.GONE);
        fabTwo.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.setVisibility(View.GONE);
        fabThree.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.setVisibility(View.GONE);
        fabfour.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabfour.setVisibility(View.GONE);


    }

    private void handleFabOne() {
        Log.i(TAG, "handleFabOne: ");
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabMain:
                if (isMenuOpen) {
                    fabOne.setVisibility(View.GONE);
                    fabTwo.setVisibility(View.GONE);
                    fabThree.setVisibility(View.GONE);
                    fabfour.setVisibility(View.GONE);
                    closeMenu();
                } else {
                    fabOne.setVisibility(View.VISIBLE);
                    fabTwo.setVisibility(View.VISIBLE);
                    fabThree.setVisibility(View.VISIBLE);
                    fabfour.setVisibility(View.VISIBLE);
                    openMenu();
                }
                break;
            case R.id.fabOne:
                Intent sendIntent1 = new Intent();
                sendIntent1.setAction(Intent.ACTION_SEND);
                sendIntent1.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + " APP");
                sendIntent1.setType("text/plain");
                startActivity(sendIntent1);
                handleFabOne();
                if (isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
                break;
            case R.id.fabTwo:
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
                break;
            case R.id.fabThree:
                String smsNumber = "919889887711";
                openWhatsApp(smsNumber);
                break;
            case R.id.fabfour:
                if (isPermissionGranted()) {
                    callAction();
                }
                break;
            default:
                break;
        }
    }

    private void openWhatsApp(String numberwhats) {
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(numberwhats) + "@s.whatsapp.net");//phone number without "+" prefix

            startActivity(sendIntent);
        } else {
            Toast.makeText(requireContext(), " Whatsapp is not installed in your device.", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = requireContext().getPackageManager();
        boolean appInstalled = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                callAction();
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void callAction() {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + "919889887711"));
        startActivity(callIntent);

    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            return true;
        }
    }

//    private void makeGetSliderRequest() {
//        imageString.clear();
//        Map<String, String> params = new HashMap<>();
//        params.put("parent", "");
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET, BANNER, params,
//                response -> {
//                    try {
//                        ArrayList<Map<String, String>> listarray = new ArrayList<>();
//                        JSONArray jsonArray = response.getJSONArray("data");
//                        if (jsonArray.length() <= 0) {
//                            recyclerImages.setVisibility(View.GONE);
//                        } else {
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                Map<String, String> urlMaps = new HashMap<>();
//                                urlMaps.put(bannerStringKey, jsonObject.getString(bannerStringKey));
//                                urlMaps.put(bannerIdKey, jsonObject.getString(bannerIdKey));
//                                urlMaps.put(bannerImageKey, BANN_IMG_URL + jsonObject.getString(bannerImageKey));
//                                imageString.add(BANN_IMG_URL + jsonObject.getString(bannerImageKey));
//                                listarray.add(urlMaps);
//                                CustomSlider textSliderView = new CustomSlider(getActivity());
//                                textSliderView.description(urlMaps.get("")).image(urlMaps.get(bannerImageKey)).setScaleType(BaseSliderView.ScaleType.Fit);
//                                textSliderView.bundle(new Bundle());
//                                textSliderView.getBundle().putString("extra", urlMaps.get(bannerStringKey));
//                                textSliderView.getBundle().putString("extra", urlMaps.get(bannerIdKey));
//                            }
//                            bannerAdapter.notifyDataSetChanged();
//
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }, error -> {
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
//        requestQueue.getCache().clear();
//        jsonObjReq.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 90000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 0;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//                error.printStackTrace();
//            }
//        });
//        requestQueue.add(jsonObjReq);
//
//
//    }
//
//    private void secondBanner() {
//        imageString1.clear();
//        Map<String, String> params = new HashMap<>();
//        params.put("parent", "");
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET, SECONDARY_BANNER, params,
//                response -> {
//                    try {
//                        ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
//                        JSONArray jsonArray = response.getJSONArray("data");
//                        if (jsonArray.length() <= 0) {
//                            recyclerImages1.setVisibility(View.GONE);
//                        } else {
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                HashMap<String, String> urlMaps = new HashMap<>();
//                                urlMaps.put(bannerStringKey, jsonObject.getString(bannerStringKey));
//                                urlMaps.put(bannerIdKey, jsonObject.getString("sec_banner_id"));
//                                urlMaps.put(bannerImageKey, BANNER_IMG_URL + jsonObject.getString(bannerImageKey));
//                                imageString1.add(BANNER_IMG_URL + jsonObject.getString(bannerImageKey));
//                                listarray.add(urlMaps);
//                            }
//
//                            bannerAdapter1.notifyDataSetChanged();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }, Throwable::printStackTrace);
//        jsonObjReq.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 90000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 0;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
//        requestQueue.getCache().clear();
//        requestQueue.add(jsonObjReq);
//
//
//    }
//
//    private void makeGetCategoryRequest() {
//        Map<String, String> params = new HashMap<>();
//        params.put("lat", sessionManagement.getLatPref());
//        params.put("lng", sessionManagement.getLangPref());
//        params.put("city", sessionManagement.getLocationCity());
//        categoryModelsList.clear();
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
//                BaseURL.TOPSIX, params, response -> {
//            try {
//                if (response.length() > 0) {
//                    String status = response.getString(statusKey);
//                    if (status.contains("1")) {
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<NewTopCategory>>() {
//                        }.getType();
//                        List<NewTopCategory> categoryModels = gson.fromJson(response.getString("data"), listType);
//                        categoryModelsList.addAll(categoryModels);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }, Throwable::printStackTrace);
//        jsonObjReq.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 90000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 0;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//                error.printStackTrace();
//            }
//        });
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 24) {
            if (screenAdapter != null) {
                screenAdapter.notifyDataSetChanged();
            }
            if (productadapter != null) {
                productadapter.notifyDataSetChanged();
            }
            if (data != null && data.getBooleanExtra("open", false) && fragmentClickListner != null) {
                fragmentClickListner.onFragmentClick(data.getBooleanExtra("open", false));
            }
        } else if (requestCode == 56) {
            if (data != null && data.getBooleanExtra("carttogo", false)) {
                if (fragmentClickListner != null) {
                    fragmentClickListner.onFragmentClick(data.getBooleanExtra("carttogo", false));
                }
            } else {
                getHomePageRequest();
                popularCategoryRequest();
            }
        } else if (requestCode == 22 && fragmentClickListner != null) {
            fragmentClickListner.onChangeHome(true);
        } else if (requestCode == 21) {
            if (screenAdapter != null) {
                screenAdapter.notifyDataSetChanged();
            }
            if (productadapter != null) {
                productadapter.notifyDataSetChanged();
            }
        }
    }

    private void popularCategoryRequest() {
//        progressDialog.show();
        popularList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseURL.POPULARCARTHOME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("popularcateHome", response);

                // progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("1")) {
                        llPop.setVisibility(View.VISIBLE);
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Popular_model>>() {
                        }.getType();
                        List<Popular_model> popModel = gson.fromJson(jsonObject.getString("data"), listType);
                        popularList.addAll(popModel);
                    } else {
                        llPop.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                } finally {
                    if (popularList.size() > 0) {
                        product(popularList.get(0).getCat_id());
                    }
                    if (popularAdapter != null) {
                        popularAdapter.notifyDataSetChanged();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (popularAdapter != null) {
                    popularAdapter.notifyDataSetChanged();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("lat", sessionManagement.getLatPref());
                param.put("lng", sessionManagement.getLangPref());
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void product(String getid) {
        newCategoryDataModel.clear();
        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", getid);
        params.put("lat", sessionManagement.getLatPref());
        params.put("lng", sessionManagement.getLangPref());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.CAT_PRODUCT, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApi", response.toString());

                // progressDialog.dismiss();


                try {
                    String status = response.getString("status");

//                    String message = response.getString("message");

                    if (status.contains("1")) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewCategoryDataModel>>() {
                        }.getType();
                        List<NewCategoryDataModel> listorl = gson.fromJson(response.getString("data"), listType);
                        newCategoryDataModel.addAll(listorl);

                    }
                    // progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    // progressDialog.dismiss();
                } finally {
                    //progressDialog.dismiss();
                    if (productadapter != null) {
                        productadapter.notifyDataSetChanged();
                    }
                    product_loader.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
                //   progressDialog.dismiss();
                product_loader.hide();
                VolleyLog.d("", "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                }
            }
        });

        jsonObjReq.setRetryPolicy(new RetryPolicy() {
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}