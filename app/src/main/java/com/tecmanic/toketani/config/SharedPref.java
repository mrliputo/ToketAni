package com.tecmanic.toketani.config;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    static SharedPreferences prefs;

    public static void putBoolean(Context ctx, String key, boolean val) {
        prefs = ctx.getSharedPreferences(BaseURL.APP_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(key, val).commit();

    }

    public static boolean getBoolean(Context ctx, String key) {
        prefs = ctx.getSharedPreferences(BaseURL.APP_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public static void putInt(Context ctx, String key, int score) {
        prefs = ctx.getSharedPreferences(BaseURL.APP_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(key, score).apply();

    }

    public static int getInt(Context ctx, String key) {
        prefs = ctx.getSharedPreferences(BaseURL.APP_NAME, Context.MODE_PRIVATE);

        return prefs.getInt(key, 0);
    }

    public static void putString(Context ctx, String key, String score) {
        prefs = ctx.getSharedPreferences(BaseURL.APP_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(key, score).apply();

    }

    public static String getString(Context ctx, String key) {
        prefs = ctx.getSharedPreferences(BaseURL.APP_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }
}
