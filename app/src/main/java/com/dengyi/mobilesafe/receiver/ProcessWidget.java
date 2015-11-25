package com.dengyi.mobilesafe.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dengyi.mobilesafe.service.KillProcessWidgetService;

/**
 * Created by deng on 2015/11/25.
 * 当前广播生命周期只有10s
 * 不能做耗时操作
 */
public class ProcessWidget extends AppWidgetProvider {
//    private static final String a = "MoSecurity.ProcessWidget";
//
//    public void onUpdate(Context paramContext, AppWidgetManager paramAppWidgetManager, int[] paramArrayOfInt)
//    {
//        Log.d("MoSecurity.ProcessWidget", "onUpdate");
//        paramAppWidgetManager = new Intent(paramContext, ProcessService.class);
//        Bundle paramArrayOfInt = new Bundle();
//        paramArrayOfInt.putString("process_service_type", "update");
//        paramAppWidgetManager.putExtras(paramArrayOfInt);
//        paramContext.startService(paramAppWidgetManager);
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        System.out.println("onReceive");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        System.out.println("onUpdate");
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        System.out.println("onEnabled");
        //开启服务，当开启时
        Intent intent = new Intent(context, KillProcessWidgetService.class);
        context.startService(intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        System.out.println("onDeleted");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context, KillProcessWidgetService.class);
        context.stopService(intent);
        System.out.println("onDeleted");
    }
}