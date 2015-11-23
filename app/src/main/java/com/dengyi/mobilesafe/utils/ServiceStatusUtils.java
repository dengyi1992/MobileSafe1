package com.dengyi.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by deng on 2015/11/19.
 * 检测服务是否在运行
 */
public class ServiceStatusUtils {
    public static boolean isServiceRunning(Context context, String servicename ){
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos= activityManager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runningServiceInfo:runningServiceInfos
             ) {
            String className=runningServiceInfo.service.getClassName();
            if (servicename.equals(className))
            {
                return true;
            }
            System.out.println(className);
        }
        return false;
    }

}
