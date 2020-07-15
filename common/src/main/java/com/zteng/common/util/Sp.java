package com.zteng.common.util;

import android.content.Context;
import android.content.SharedPreferences;


public class Sp {

   private final static String SP_NAME = "settings";

    private static SharedPreferences getSharedPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return preferences;
    }

    /**
     * 统一管理所有存储boolean
     */
    public static void setBoolean(Context context, String key, boolean value){
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }
    public static boolean getBoolean(Context context, String key, boolean defValue){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean(key, defValue);
    }
    /**
     * 统一管理所有存储String
     */
    public static void setString(Context context, String key, String value){
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, value);
        edit.commit();
    }
    public static String getString(Context context, String key, String defValue){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(key, defValue);
    }
    /**
     * 统一管理所有存储int
     */
    public static void setInt(Context context, String key,int value){
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }
    public static int getInt(Context context, String key, int defValue){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getInt(key, defValue);
    }

}
