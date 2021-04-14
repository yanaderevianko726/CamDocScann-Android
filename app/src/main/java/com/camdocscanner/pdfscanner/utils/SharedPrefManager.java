package com.camdocscanner.pdfscanner.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefManager {

    private static SharedPreferences sharedPref;

    private static String package_name = "";
    private static final String admob_enable = package_name + "admob_enable";

    public SharedPrefManager (Context context) {
        package_name = context.getPackageName();
        sharedPref = context.getSharedPreferences(package_name, MODE_PRIVATE);
    }

    public static void setSharedPrefAdEnable(){
        sharedPref.edit().putString(admob_enable, variables.ads_banner_status_check).apply();
    }

    public static void unSetSharedPrefAdEnable(){
        sharedPref.edit().putString(admob_enable, variables.ads_banner_status_check_un).apply();
    }

    public static String getSharedPrefAdEnable(){
        return sharedPref.getString(admob_enable, "checked");
    }
}
