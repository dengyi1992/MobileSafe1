package com.dengyi.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by deng on 2015/11/24.
 */

public class TaskInfo {
    private Drawable icon;
    private String packageName;
    private String appName;
    private long memorySize;
    private boolean userApk;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }

    public boolean isUserApk() {
        return userApk;
    }

    public void setUserApk(boolean userApk) {
        this.userApk = userApk;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", memorySize=" + memorySize +
                ", userApk=" + userApk +
                '}';
    }
}
