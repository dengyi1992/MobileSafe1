package com.dengyi.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by deng on 2015/11/25.
 */
public class KillProcessService extends Service{

    private LockScreenReceiver lockScreenReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //锁屏广播
        lockScreenReceiver = new LockScreenReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(lockScreenReceiver,filter);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                //业务逻辑
                System.out.println("===============我被调用了========================");
            }
        };
        /**
         * 进行调度
         * 第一参数表示用哪个类进行调度
         *
         */
        timer.schedule(task,10000,10000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(lockScreenReceiver);
        lockScreenReceiver=null;
    }

    private class LockScreenReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取到进程管理器
            ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            //获取所有在运行的进程
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo:
                    runningAppProcesses
                 ) {
                activityManager.killBackgroundProcesses(runningAppProcessInfo.processName);

            }
        }
    }
}
