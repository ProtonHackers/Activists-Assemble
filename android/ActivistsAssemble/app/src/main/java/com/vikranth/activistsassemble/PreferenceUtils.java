package com.vikranth.activistsassemble;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public final class PreferenceUtils {
    public static final String PF_PLAN_KEY = "plan";
    public static final String PF_TW_KEY = "twitter";
    public static final String PF_DATES_KEY = "dates";
    public static final String PF_TIMES_KEY = "times";
    public static final String PF_FAV_KEY = "fav";

    private PreferenceUtils() {
    }

    public static void putFirebaseTime(Context context, String timeKey, String id, ArrayList<String> stringList) {
        putStringList(context, timeKey + id, stringList);
//        Log.d("Time Key", timeKey + id);
    }

    public static String getString(Context context, String key) {
//        Log.d("getString", key);
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");
    }

    public static int getInt(Context context, String key) {
//        Log.d("getInt", key);
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
    }

    public static int getNotificationTime(Context context, String key) {
//        Log.d("getInt", key);
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 2);
    }

    public static void putString(Context context, String key, String value) {
        Log.d("putString", key + "::" + value);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
    }

    public static void putInt(Context context, String key, int value) {
        Log.d("putInt", key + "::" + value);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key) {
//        Log.d("getBoolean", key);
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false);
    }

    public static void putBoolean(Context context, String key, boolean value) {
//        Log.d("putBoolean", key + ":::" + value);
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(key, value)
                .apply();
    }

    public static ArrayList<String> getListString(Context context, String key) {
//        Log.d("getListString", key);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getString(key, "").equalsIgnoreCase("")) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    public static ArrayList<String> getTimeList(Context context, String key, String id) {
//        Log.d("getTimeList", key + "::" + id);
        return getListString(context, key + id);
    }

    public static void putStringList(Context context, String key, ArrayList<String> stringList) {
//        Log.d("putStringList", key + ":::" + stringList.toString());
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key,
                        TextUtils.join("‚‗‚", stringList.toArray(new String[stringList.size()])))
                .apply();
    }
}
