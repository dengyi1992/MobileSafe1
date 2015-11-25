package com.dengyi.mobilesafe.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.receiver.ProcessWidget;
import com.dengyi.mobilesafe.utils.SystemInfoUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by deng on 2015/11/25.
 * 一键清理桌面控件
 */
public class KillProcessWidgetService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //每隔5秒更新widget

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                System.out.println("================widgetServiceIsRunning===================");
                //把当前布局文件添加
                //创建了一个远程view

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
                /**
                 * note:这里面没有findviewbyid
                 *
                 */
                views.setTextViewText(R.id.process_count,"进程数为："+ SystemInfoUtils.getProcessCount(getApplicationContext())+"个");
                views.setTextViewText(R.id.process_memory,"可用内存："+
                        Formatter.formatFileSize(getApplicationContext(),SystemInfoUtils.getAvailMen(getApplicationContext())));
                Intent intent = new Intent();
                //发送隐式意图
                intent.setAction("com.dengyi.mobilesafe");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                views.setOnClickPendingIntent(R.id.btn_clear,pendingIntent);

                //第一个参数是上下文
                //第二个表示当前由哪个广播处理
                ComponentName provider = new ComponentName(getApplicationContext(), ProcessWidget.class);
                appWidgetManager.updateAppWidget(provider,views);
            }
        };
        //从0开始，每隔5秒执行一次
        timer.schedule(timerTask,0,5000);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
