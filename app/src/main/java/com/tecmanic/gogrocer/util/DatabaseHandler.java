package com.tecmanic.gogrocer.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String CART_TABLE = "cart";
    public static final String VARIENT_ID = "varient_id";
    public static final String COLUMN_QTY = "qty";
    public static final String COLUMN_IMAGE = "product_image";
    public static final String COLUMN_NAME = "product_name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_REWARDS = "rewards";
    public static final String COLUMN_INCREAMENT = "increament";
    public static final String COLUMN_UNIT_VALUE = "unit_value";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "product_description";
    private static final String DBNAME = "demhynnjf";
    private static final int DBVERSION = 3;
    private SQLiteDatabase db;
    private String texConstKey = " TEXT NOT NULL, ";
    private String doubConstKey = " DOUBLE NOT NULL, ";
    private String selectContKey = "Select *  from ";
    private String whereConstKey = " where ";

    public DatabaseHandler(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        String exe = "CREATE TABLE IF NOT EXISTS " + CART_TABLE
                + "(" + VARIENT_ID + " integer primary key, "
                + COLUMN_QTY + " DOUBLE NOT NULL,"
                + COLUMN_IMAGE + texConstKey
                + COLUMN_NAME + texConstKey
                + COLUMN_PRICE + doubConstKey
                + COLUMN_UNIT_VALUE + doubConstKey
                + COLUMN_UNIT + doubConstKey
                + COLUMN_REWARDS + doubConstKey
                + COLUMN_INCREAMENT + doubConstKey
                + COLUMN_STOCK + doubConstKey
                + COLUMN_TITLE + texConstKey
                + COLUMN_DESCRIPTION + " TEXT NOT NULL "
                + ")";

        db.execSQL(exe);

    }

    public boolean setCart(Map<String, String> map, Integer qty) {
        db = getWritableDatabase();
        if (isInCart(map.get(VARIENT_ID))) {
            db.execSQL("update " + CART_TABLE + " set " + COLUMN_QTY + " = '" + qty + "' where " + VARIENT_ID + "=" + map.get(VARIENT_ID));
            db.close();
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put(VARIENT_ID, map.get(VARIENT_ID));
            values.put(COLUMN_QTY, qty);
            values.put(COLUMN_IMAGE, map.get(COLUMN_IMAGE));
            values.put(COLUMN_INCREAMENT, map.get(COLUMN_INCREAMENT));
            values.put(COLUMN_NAME, map.get(COLUMN_NAME));
            values.put(COLUMN_PRICE, map.get(COLUMN_PRICE));
            values.put(COLUMN_REWARDS, map.get(COLUMN_REWARDS));
            values.put(COLUMN_UNIT_VALUE, map.get(COLUMN_UNIT_VALUE));
            values.put(COLUMN_UNIT, map.get(COLUMN_UNIT));
            values.put(COLUMN_STOCK, map.get(COLUMN_STOCK));
            values.put(COLUMN_TITLE, map.get(COLUMN_TITLE));
            values.put(COLUMN_DESCRIPTION, map.get(COLUMN_DESCRIPTION));
            db.insert(CART_TABLE, null, values);
            db.close();
            return true;
        }
    }

    public boolean isInCart(String id) {
        db = getReadableDatabase();
        String qry = selectContKey + CART_TABLE + whereConstKey + VARIENT_ID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        return cursor.getCount() > 0;
    }

    public String getCartItemQty(String id) {

        db = getReadableDatabase();
        String qry = selectContKey + CART_TABLE + whereConstKey + VARIENT_ID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));

    }

    public String getInCartItemQty(String id) {
        if (isInCart(id)) {
            db = getReadableDatabase();
            String qry = selectContKey + CART_TABLE + whereConstKey + VARIENT_ID + " = " + id;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));
        } else {
            return "0.0";
        }
    }


    public String getInCartItemQtys(String id) {
        if (isInCart(id)) {
            db = getReadableDatabase();
            String qry = selectContKey + CART_TABLE + whereConstKey + VARIENT_ID + " = " + id;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));
        } else {
            return "0";
        }
    }

    public int getCartCount() {
        int count = 0;
        boolean inExp = false;
        db = getReadableDatabase();
        String qry = selectContKey + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        try {
            count = cursor.getCount();
        } catch (Exception e) {
            inExp = true;
            e.printStackTrace();
        } finally {
            cursor.close();
            if (inExp) {
                db = getReadableDatabase();
                cursor = db.rawQuery(qry, null);
                count = cursor.getCount();
                cursor.close();
            }

        }
        return count;
    }

    public String getTotalAmount() {
        db = getReadableDatabase();
        String qry = "Select SUM(" + COLUMN_QTY + " * " + COLUMN_PRICE + ") as total_amount  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String total = cursor.getString(cursor.getColumnIndex("total_amount"));
        if (total != null) {

            return total;
        } else {
            return "0";
        }
    }


    public List<HashMap<String, String>> getCartAll() {
        List<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = selectContKey + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(VARIENT_ID, cursor.getString(cursor.getColumnIndex(VARIENT_ID)));
            map.put(COLUMN_QTY, cursor.getString(cursor.getColumnIndex(COLUMN_QTY)));
            map.put(COLUMN_IMAGE, cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
            map.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            map.put(COLUMN_PRICE, cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)));
            map.put(COLUMN_REWARDS, cursor.getString(cursor.getColumnIndex(COLUMN_REWARDS)));
            map.put(COLUMN_UNIT_VALUE, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_VALUE)));
            map.put(COLUMN_UNIT, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT)));
            map.put(COLUMN_INCREAMENT, cursor.getString(cursor.getColumnIndex(COLUMN_INCREAMENT)));
            map.put(COLUMN_STOCK, cursor.getString(cursor.getColumnIndex(COLUMN_STOCK)));
            map.put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            map.put(COLUMN_DESCRIPTION, cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }

    public String getColumnRewards() {
        db = getReadableDatabase();
        String qry = "SELECT rewards FROM " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String reward = cursor.getString(cursor.getColumnIndex(COLUMN_REWARDS));
        if (reward != null) {

            return reward;
        } else {
            return "0";
        }
    }

    public void clearCart() {
        db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE);
    }

    public void removeItemFromCart(String id) {
        db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE + whereConstKey + VARIENT_ID + " = " + id);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+CART_TABLE);
        onCreate(db);
    }

}
