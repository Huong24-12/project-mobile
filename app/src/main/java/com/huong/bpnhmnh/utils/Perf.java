package com.huong.bpnhmnh.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class Perf {
    private static SharedPreferences preferences;
    public static void setLong(String name, long gt, Context activity) {
        preferences = activity.getSharedPreferences("HIHI", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(name, gt);
        editor.apply();

    }


    public static void setInt(String name, int gt, Context activity) {
        preferences = activity.getSharedPreferences("HIHI", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(name, gt);
        editor.apply();

    }

    public static int getInt(String name, Context activity, int defaultValue) {
        preferences = activity.getSharedPreferences("HIHI", MODE_PRIVATE);
        return preferences.getInt(name, defaultValue);
    }

    public static long getLong(String name, Context activity, long defaultValue) {
        preferences = activity.getSharedPreferences("HIHI", MODE_PRIVATE);
        return preferences.getLong(name, defaultValue);
    }

    public static void setString(String name, String gt, Context activity) {
        preferences = activity.getSharedPreferences("HIHI", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(name, gt);
        editor.apply();
    }
    public static String getString(String name, String er, Context activity) {
        preferences = activity.getSharedPreferences("HIHI", MODE_PRIVATE);
        return preferences.getString(name, er);
    }

    public static void setBoolean(String name, boolean gt, Context activity) {
        preferences = activity.getSharedPreferences("HIHI", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(name, gt);
        editor.apply();

    }

    public static boolean getBoolean(String name, Context activity) {
        preferences = activity.getSharedPreferences("HIHI", MODE_PRIVATE);
        return preferences.getBoolean(name, false);
    }
}
