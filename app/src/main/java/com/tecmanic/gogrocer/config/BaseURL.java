package com.tecmanic.gogrocer.config;


public class BaseURL {
    public static final String MY_PREPRENCE = "my_preprence";
    public static final String PREFS_NAME = "GroceryLoginPrefs";
    public static final String PREFS_NAME2 = "GroceryLoginPrefs2";
    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_NAME = "user_fullname";
    public static final String KEY_EMAIL = "user_email";
    public static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";
    public static final String WALLET_TOTAL_AMOUNT = "WALLET_TOTAL_AMOUNT";
    public static final String COUPON_TOTAL_AMOUNT = "COUPON_TOTAL_AMOUNT";
    public static final String KEY_ID = "user_id";
    public static final String CART_ID_FINAL = "cart_id_final";
    public static final String KEY_MOBILE = "user_phone";
    public static final String KEY_IMAGE = "user_image";
    public static final String KEY_WALLET_AMMOUNT = "wallet_ammount";
    public static final String KEY_REWARDS_POINTS = "rewards_points";
    public static final String KEY_PAYMENT_METHOD = "payment_method";
    public static final String KEY_PINCODE = "pincode";
    public static final String KEY_SOCITY_ID = "Socity_id";
    public static final String KEY_REWARDS = "rewards";
    public static final String KEY_SOCITY_NAME = "socity_name";
    public static final String KEY_HOUSE = "house_no";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String USER_SKIP = "user_skip";
    public static final String USER_CURRENCY_CNTRY = "user_currency_country";
    public static final String USER_CURRENCY = "user_currency";
    public static final String USER_LAT = "user_lat";
    public static final String USER_LANG = "user_lang";
    public static final String USER_CITY = "user_city";
    public static final String USER_STOREID = "user_storeid";
    public static final String PAYMENT_PAYSTACK = "payment_paystack";
    public static final String PAYMENT_PAYPAL = "payment_paypal";
    public static final String PAYMENT_RAZORPZY = "payment_razorpay";
    public static final String APP_OTP_STATUS = "user_otp_search";
    public static final String COUNTRY_CODE = "country_code";
    public static final String ADDRESS = "address";
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;


    //adreeessss
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String COUNTRY = "country";
    public static final String LAT = "lat";
    public static final String LONG = "long";
    public static final String KEY_STORE_COUNT = "STORE_COUNT";

    //Store Selection
    public static final String KEY_NOTIFICATION_COUNT = "NOTIFICATION_COUNT";
    //Firebase
    public static final String SHARED_PREF = "ah_firebase";
    public static final String TOPIC_GLOBAL = "global";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String KEY_PASSWORD = "password";
    public static final String USER_STATUS = "user_status";
    public static final String USER_OTP = "user_otp";
    public static final String MAP_SELECTION = "map_selection";
    public static final String USER_EMAIL_SERVICE = "user_email_service";
    public static final String USER_SMS_SERVICE = "user_sms_service";
    public static final String USER_INAPP_SERVICE = "user_inapp_service";
    //City and Store Id
    public static final String CITY_ID = "CITY_ID";
    public static final String STORE_ID = "STORE_ID";
    public static final String APP_NAME = "GoGrocer";
//    public static final String BASE_URL = "https://gogrocer.tecmanic.com/api/";
    public static final String BASE_URL = "https://gogrocer.tecmanic.com/api/";
    public static final String BASE_URL_MAP = BASE_URL;
    public static final String IMG_URL = "https://gogrocer.tecmanic.com/";
    public static final String BANN_IMG_URL = IMG_URL;
    public static final String BANNER_IMG_URL = IMG_URL;

    public static final String SIGN_UP = BASE_URL + "signup";
    public static final String SIGN_UP_OTP = BASE_URL + "verify_phone";

    public static final String LOGIN = BASE_URL + "login";
    public static final String USERBLOCKAPI = BASE_URL + "user_block_check";
    public static final String FORGET_PASSWORD = BASE_URL + "forget_password";
    public static final String VERIFY_OTP = BASE_URL + "verify_otp";
    public static final String CHANGE_PASS = BASE_URL + "change_password";

    public static final String HOME_TOP_SELLING = BASE_URL + "top_selling";
    public static final String HOME_RECENT = BASE_URL + "recentselling";
    public static final String HOME_DEAL = BASE_URL + "dealproduct";
    public static final String REDEEM_REWARDS = BASE_URL + "redeem_rewards";
    public static final String BANNER = BASE_URL + "banner";
    public static final String SECONDARY_BANNER = BASE_URL + "secondary_banner";

    public static final String CATEGORIES = BASE_URL + "catee";
    public static final String PRODUCT_VARIENT = BASE_URL + "varient";
    public static final String SEARCH = BASE_URL + "search";

    public static final String CITY_LIST_URL = BASE_URL + "city";
    public static final String SOCIETY_LIST_URL = BASE_URL + "society";
    public static final String ADD_ADDRESS = BASE_URL + "add_address";
    public static final String SHOW_ADDRESS = BASE_URL + "show_address";
    public static final String SELECT_ADDRESS_URL = BASE_URL + "select_address";
    public static final String EDIT_ADDRESS = BASE_URL + "edit_address";
    public static final String DELETE_ORDER_URL = BASE_URL + "cancelling_reasons";
    public static final String DELETE_ORDER = BASE_URL + "delete_order";
    public static final String DELIVERY_INFO = BASE_URL + "delivery_info";

    public static final String CALENDER_URL = BASE_URL + "timeslot";

    public static final String WALLET_REFRESH = BASE_URL + "walletamount?user_id=";
    public static final String RECHAREGEWALLET = BASE_URL + "recharge_wallet";
    public static final String MYPROFILE = BASE_URL + "myprofile";
    public static final String ORDER_DONE_URL = BASE_URL + "completed_orders";
    public static final String PENDING_ORDER_URL = BASE_URL + "ongoing_orders";

    public static final String ABOUT_URL = BASE_URL + "appaboutus";
    public static final String TOPSIX = BASE_URL + "topsix";
    public static final String TERMS_URL = BASE_URL + "appterms";

    public static final String DELETE_ALL_NOTIFICATION = BASE_URL + "delete_all_notification";
    public static final String SUPPORT_URL = BASE_URL + "appterms";

    public static final String EDIT_PROFILE_URL = BASE_URL + "profile_edit";
    public static final String CAT_PRODUCT = BASE_URL + "cat_product";
    public static final String ORDER_CONTINUE = BASE_URL + "make_an_order";
    public static final String MIN_MAX_ORDER = BASE_URL + "minmax";
    public static final String REWARDLINES = BASE_URL + "rewardlines";

    public static final String ADD_ORDER_URL = BASE_URL + "checkout";
    public static final String COUPON_CODE = BASE_URL + "couponlist";
    public static final String APPLY_COUPON = BASE_URL + "apply_coupon";
    public static final String WHATSNEW = BASE_URL + "whatsnew";

    public static final String NOTICE_U_RL = BASE_URL + "notificationlist";
    public static final String UPDATENOTIFYBY = BASE_URL + "updatenotifyby";
    public static final String CURRENCY_API = BASE_URL + "currency";

    public static final String GOOGLEMAP_KEY = BASE_URL + "google_map";
    public static final String MAPBOX_KEY = BASE_URL + "mapbox";
    public static final String POPULARCARTHOME = BASE_URL + "homecat";

}
