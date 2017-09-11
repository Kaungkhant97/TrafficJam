package com.unihackchallenge.mmtrafficreport;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SPrefHelper {

  public static String getString(Context context, String key, String defaultValue) {
    SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
    return sPref.getString(key, defaultValue);
  }

  public static Boolean getBoolean(Context context, String key, Boolean defaultValue) {
    SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
    return sPref.getBoolean(key, defaultValue);
  }

  public static Integer getInteger(Context context, String key, Integer defaultValue) {
    SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
    return sPref.getInt(key, defaultValue);
  }

  public static void putString(Context context, String key, String value) {
    SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
    sPref.edit().putString(key, value).apply();
  }

  public static void putInteger(Context context, String key, Integer value) {
    SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
    sPref.edit().putInt(key, value).apply();
  }

  public static void putBoolean(Context context, String key, Boolean value) {
    SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
    sPref.edit().putBoolean(key, value).apply();
  }


}