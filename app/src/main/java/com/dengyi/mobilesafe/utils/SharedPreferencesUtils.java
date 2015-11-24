package com.dengyi.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by deng on 2015/11/24.
 */
public class SharedPreferencesUtils {
    public static final String SP_NAME="config";
    public static void saveBoolean(Context context,String key,boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key,value).commit();
    }
    public static boolean getBoolean(Context context,String key,boolean defValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,defValue);

    }
}
