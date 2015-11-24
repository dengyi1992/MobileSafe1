package com.dengyi.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.dengyi.mobilesafe.bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deng on 2015/11/24.
 */
public class TaskInfoParper {
    public static List<TaskInfo> getTaskInfo(Context context) {
        //获取进程管理器
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        ArrayList<TaskInfo> taskInfos = new ArrayList<>();
        //获取所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo :
                runningAppProcesses) {
            TaskInfo taskInfo = new TaskInfo();
            //进程名字（包名）
            String processName = runningAppProcessInfo.processName;
            taskInfo.setPackageName(processName);

            try {

                Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
                // Dirty弄脏
                // 获取到总共弄脏多少内存(当前应用程序占用多少内存)
                int totalPrivateDirty = memoryInfo[0].getTotalPrivateDirty() * 1024;

                taskInfo.setMemorySize(totalPrivateDirty);
                PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
                Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
                taskInfo.setIcon(icon);
                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                taskInfo.setAppName(appName);
                int flags = packageInfo.applicationInfo.flags;
                //ApplicationInfo.FLAG_SYSTEM 表示系统应用程序
                if((flags & ApplicationInfo.FLAG_SYSTEM) != 0 ){
                    //系统应用
                    taskInfo.setUserApk(false);
                }else{
//					/用户应用
                    taskInfo.setUserApk(true);

                }
                taskInfos.add(taskInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            activityManager.getProcessMemoryInfo(p);
        }
        return taskInfos;
    }
}
