package com.dengyi.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by deng on 2015/11/18.
 */
public class ToastUtils {
    public static void showToast(Context context,String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
