package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.bean.TaskInfo;
import com.dengyi.mobilesafe.engine.TaskInfoParper;
import com.dengyi.mobilesafe.utils.SharedPreferencesUtils;
import com.dengyi.mobilesafe.utils.SystemInfoUtils;
import com.dengyi.mobilesafe.utils.ToastUtils;
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
    private TaskManagerAdapter adapter;
    private long availMen;
    private int processCount;

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
        processCount = SystemInfoUtils.getProcessCount(this);
        tv_task_process_count.setText("进程:" + processCount + "个");
        //获取到内存的基本信息
//        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
//        activityManager.getMemoryInfo(memoryInfo);
//
//        long availMem = memoryInfo.availMem;
//        long totalMem = memoryInfo.totalMem;//高版本可用，低版本不可用
        availMen = SystemInfoUtils.getAvailMen(this);
        tv_task_memory.setText("剩余/总内存" + Formatter.formatFileSize(TaskManagerActivity.this, availMen)
                + "/" + Formatter.formatFileSize(TaskManagerActivity.this, SystemInfoUtils.getTotalMen(this)));
        lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 得到当前点击listview的对象
                Object object = lv_task.getItemAtPosition(position);


                if (object != null && object instanceof TaskInfo) {

                    TaskInfo taskInfo = (TaskInfo) object;

                    ViewHolder holder = (ViewHolder) view.getTag();
                    //如果是自己就跳过

                    if (taskInfo.getPackageName().equals(getPackageName())) {
                        return;
                    }

                    // 判断当前的item是否被勾选上
                    /**
                     * 如果被勾选上了。那么就改成没有勾选。 如果没有勾选。就改成已经勾选
                     */
                    if (taskInfo.isChecked()) {
                        taskInfo.setChecked(false);
                        holder.cb_app_status.setChecked(false);
                    } else {
                        taskInfo.setChecked(true);
                        holder.cb_app_status.setChecked(true);
                    }

                }

            }

        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter = new TaskManagerAdapter();
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
            /**
             * 判断当前用户是否需要展示系统进程
             * 如果需要就全部展示
             * 如果不需要就展示用户进程
             */
//            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            boolean result = SharedPreferencesUtils.getBoolean(TaskManagerActivity.this,"is_show_system", true);
            if(result){
                return userTaskInfos.size() + 1 + systemAppInfos.size() + 1;
            }else{
                return userTaskInfos.size() + 1;
            }
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
            return taskInfo;
        }

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

            /**
             * 设置缓存
             */
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

            if (taskInfo.isChecked()) {
                holder.cb_app_status.setChecked(true);
            } else {
                holder.cb_app_status.setChecked(false);
            }
            //判断当前展示的item是否是自己的程序。如果是。就把程序给隐藏
            if (taskInfo.getPackageName().equals(getPackageName())) {
                //隐藏
                holder.cb_app_status.setVisibility(View.INVISIBLE);
            } else {
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

    /**
     * 全选
     * 执行完最好是刷新下ui
     * 会出现不同步现象
     *
     * @param view
     */
    public void selectAll(View view) {
        for (TaskInfo taskInfo : userTaskInfos
                ) {

            //判断当前是否是自己的程序，如果是自己
            if (taskInfo.getPackageName().equals(getPackageName())) {
                taskInfo.setChecked(false);
                continue;
            }
            taskInfo.setChecked(true);
        }
        for (TaskInfo taskInfo : systemAppInfos
                ) {
            taskInfo.setChecked(true);
        }
        //刷新页面
        adapter.notifyDataSetChanged();
    }

    /**
     * 反选
     *
     * @param view
     */
    public void selectOpposite(View view) {
        for (TaskInfo taskInfo : userTaskInfos
                ) {
            //判断当前是否是自己的程序，如果是自己
            if (taskInfo.getPackageName().equals(getPackageName())) {
                taskInfo.setChecked(false);
                continue;
            }
            if (taskInfo.isChecked()) {
                taskInfo.setChecked(false);
            } else {
                taskInfo.setChecked(true);
            }
        }
        for (TaskInfo taskInfo : systemAppInfos
                ) {
            if (taskInfo.isChecked()) {
                taskInfo.setChecked(false);
            } else {
                taskInfo.setChecked(true);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 清理进程
     *
     * @param view
     */
    public void killProcess(View view) {
        //得先获取进程管理器
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //清理进程的集合防止ConcurrentModificationException错误，迭代时候，改变大小引起的错误
        List<TaskInfo> killList = new ArrayList<>();
        //记录清理的进程数
        int totalCount = 0;
        //记录清理的进程大小
        int clearMemo = 0;
        for (TaskInfo taskInfo :
                userTaskInfos) {
            if (taskInfo.isChecked()) {

                //杀死进程 参数是包名
//                activityManager.killBackgroundProcesses(taskInfo.getPackageName());
//                userTaskInfos.remove(taskInfo);
                killList.add(taskInfo);
                totalCount++;
                clearMemo += taskInfo.getMemorySize();
            }
        }
        for (TaskInfo taskInfo :
                systemAppInfos) {
            if (taskInfo.isChecked()) {

                //杀死进程 参数是包名
//                activityManager.killBackgroundProcesses(taskInfo.getPackageName());
//                systemAppInfos.remove(taskInfo);
                killList.add(taskInfo);
                totalCount++;
                clearMemo += taskInfo.getMemorySize();
            }
        }
        /**
         * 先添加集合后移除列表
         */
        for (TaskInfo taskInfo : killList
                ) {
            if (taskInfo.isUserApk()) {
                userTaskInfos.remove(taskInfo);
            } else {
                systemAppInfos.remove(taskInfo);
            }
            activityManager.killBackgroundProcesses(taskInfo.getPackageName());
        }
        adapter.notifyDataSetChanged();
        ToastUtils.showToast(TaskManagerActivity.this, "一共清理了" + totalCount + "个进程，释放了" + Formatter.formatFileSize(TaskManagerActivity.this,
                clearMemo) + "内存");
        tv_task_process_count.setText("进程:" + (processCount - totalCount) + "个");
        tv_task_memory.setText("剩余/总内存" + Formatter.formatFileSize(TaskManagerActivity.this, availMen + clearMemo)
                + "/" + Formatter.formatFileSize(TaskManagerActivity.this, SystemInfoUtils.getTotalMen(this)));
    }

    /**
     * 跳转设置页面
     *
     * @param view
     */
    public void taskSetting(View view) {
        Intent intent = new Intent(TaskManagerActivity.this, TaskSettingActivity.class);
        startActivity(intent);
    }

    //设置后返回刷新
    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();

        }
    }
}
