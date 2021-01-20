package com.tecmanic.toketani.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceUtil {

    private SharedPreferences prefs = null;
    private Editor editor = null;

    public SharedPreferenceUtil(Context context) {
        this.prefs = context.getSharedPreferences("toketani", Context.MODE_PRIVATE);
        this.editor = this.prefs.edit();

    }


    public String getString(String key, String defaultvalue) {
        if (this.prefs == null) {
            return defaultvalue;
        }
        return this.prefs.getString(key, defaultvalue);
    }

    public void setString(String key, String value) {
        if (this.editor == null) {
            return;
        }
        this.editor.putString(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key, Boolean defaultvalue) {
        if (this.prefs == null) {
            return defaultvalue;
        }
        return this.prefs.getBoolean(key, defaultvalue);
    }

    public void setBoolean(String key, Boolean value) {
        if (this.editor == null) {
            return;
        }
        this.editor.putBoolean(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultvalue) {
        if (this.prefs == null) {
            return defaultvalue;
        }
        return this.prefs.getInt(key, defaultvalue);
    }

    public void setInt(String key, int value) {
        if (this.editor == null) {
            return;
        }
        this.editor.putInt(key, value);
        editor.apply();
    }
    public void clearAll() {
        if (this.editor == null) {
            return;
        }
        this.editor.clear().apply();

    }

    public void removeOneItem(String key) {
        if (this.editor == null) {
            return;
        }
        this.editor.remove(key);
    }

    public void save() {
        if (this.editor == null) {
            return;
        }
        this.editor.apply();
    }


}