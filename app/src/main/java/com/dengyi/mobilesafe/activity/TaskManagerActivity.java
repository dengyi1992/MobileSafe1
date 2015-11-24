package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.bean.TaskInfo;
import com.dengyi.mobilesafe.engine.TaskInfoParper;
import com.dengyi.mobilesafe.utils.SystemInfoUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deng on 2015/11/24.
 */
public class TaskManagerActivity extends Activity {
    @ViewInject(R.id.tv_task_process_count)
    private TextView tv_task_process_count;
    @ViewInject(R.id.tv_task_memory)
    private TextView tv_task_memory;
    @ViewInject(R.id.lv_task)
    private ListView lv_task;
    private List<TaskInfo> taskInfos;
    private List<TaskInfo> userTaskInfos;
    private List<TaskInfo> systemAppInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
    }

    /**
     * 初始化ui
     * ActivityManager 活动管理器
     * PackageManager  包管理器
     */

    private void initUI() {
        setContentView(R.layout.activity_task_manager);
        ViewUtils.inject(this);
        //得到进程管理者
//        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        //获取当前手机的所有运行的进程
//        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
//        int size = runningAppProcesses.size();

        tv_task_process_count.setText("进程:" + SystemInfoUtils.getProcessCount(this) + "个");
        //获取到内存的基本信息
//        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
//        activityManager.getMemoryInfo(memoryInfo);
//
//        long availMem = memoryInfo.availMem;
//        long totalMem = memoryInfo.totalMem;//高版本可用，低版本不可用
        tv_task_memory.setText("剩余/总内存" + Formatter.formatFileSize(TaskManagerActivity.this, SystemInfoUtils.getAvailMen(this))
                + "/" + Formatter.formatFileSize(TaskManagerActivity.this, SystemInfoUtils.getTotalMen(this)));


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            TaskManagerAdapter adapter = new TaskManagerAdapter();
            lv_task.setAdapter(adapter);
        }
    };

    /**
     * 初始化数据
     */
    private void initData() {
        new Thread() {
            @Override
            public void run() {

                taskInfos = TaskInfoParper.getTaskInfo(TaskManagerActivity.this);
                userTaskInfos = new ArrayList<TaskInfo>();

                systemAppInfos = new ArrayList<TaskInfo>();
                for (TaskInfo taskInfo : taskInfos) {

                    if (taskInfo.isUserApk()) {
                        userTaskInfos.add(taskInfo);
                    } else {
                        systemAppInfos.add(taskInfo);
                    }

                }

                handler.sendEmptyMessage(0);
//单独使用这个方法也可以实现
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        TaskManagerAdapter adapter = new TaskManagerAdapter();
//                lv_task.setAdapter(adapter);
//                    }
//                });
            }
        }.start();
    }

    private class TaskManagerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return taskInfos.size();
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            } else if (position == userTaskInfos.size() + 1) {
                return null;
            }

            TaskInfo taskInfo;

            if (position < (userTaskInfos.size() + 1)) {
                // 用户程序
                taskInfo = userTaskInfos.get(position - 1);// 多了一个textview的标签 ，
                // 位置需要-1
            } else {
                // 系统程序
                int location = position - 1 - userTaskInfos.size() - 1;
                taskInfo = systemAppInfos.get(location);
            }
            return taskInfo;        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                // 第0个位置显示的应该是 用户程序的个数的标签。
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                tv.setText("用户程序：" + userTaskInfos.size() + "个");
                return tv;
            } else if (position == (userTaskInfos.size() + 1)) {
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                tv.setText("系统程序：" + systemAppInfos.size() + "个");
                return tv;
            }
            ViewHolder holder;
            View view;
            if (convertView != null && convertView instanceof LinearLayout) {
                view = convertView;

                holder = (ViewHolder) view.getTag();

            } else {
                view = View.inflate(TaskManagerActivity.this,
                        R.layout.item_task_manager, null);

                holder = new ViewHolder();

                holder.iv_app_icon = (ImageView) view
                        .findViewById(R.id.iv_app_icon);

                holder.tv_app_name = (TextView) view
                        .findViewById(R.id.tv_task_name);

                holder.tv_app_memory_size = (TextView) view
                        .findViewById(R.id.tv_app_memory_size);

                holder.cb_app_status = (CheckBox) view
                        .findViewById(R.id.cb_app_status);

                view.setTag(holder);
            }

            TaskInfo taskInfo;

            if (position < (userTaskInfos.size() + 1)) {
                // 用户程序
                taskInfo = userTaskInfos.get(position - 1);// 多了一个textview的标签 ，
                // 位置需要-1
            } else {
                // 系统程序
                int location = position - 1 - userTaskInfos.size() - 1;
                taskInfo = systemAppInfos.get(location);
            }
            // 这个是设置图片控件的大小
            // holder.iv_app_icon.setBackgroundDrawable(d)
            // 设置图片本身的大小
            holder.iv_app_icon.setImageDrawable(taskInfo.getIcon());

            holder.tv_app_name.setText(taskInfo.getAppName());

            holder.tv_app_memory_size.setText("内存占用:"
                    + Formatter.formatFileSize(TaskManagerActivity.this,
                    taskInfo.getMemorySize()));

//            if (taskInfo.isChecked()) {
//                holder.cb_app_status.setChecked(true);
//            } else {
//                holder.cb_app_status.setChecked(false);
//            }
            //判断当前展示的item是否是自己的程序。如果是。就把程序给隐藏
            if(taskInfo.getPackageName().equals(getPackageName())){
                //隐藏
                holder.cb_app_status.setVisibility(View.INVISIBLE);
            }else{
                //显示
                holder.cb_app_status.setVisibility(View.VISIBLE);
            }

            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_app_icon;
        TextView tv_app_name;
        TextView tv_app_memory_size;
        CheckBox cb_app_status;
    }
}
