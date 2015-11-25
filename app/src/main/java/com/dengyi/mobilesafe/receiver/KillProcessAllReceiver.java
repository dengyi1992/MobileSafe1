package com.dengyi.mobilesafe.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dengyi.mobilesafe.utils.ToastUtils;

import java.util.List;

/**
 * Created by deng on 2015/11/25.
 */
public class KillProcessAllReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //得到正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo :
                runningAppProcesses ) {
            activityManager.killBackgroundProcesses(runningAppProcessInfo.processName);
        }
        ToastUtils.showToast(context,"清理完毕");
    }
}
