package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.service.KillProcessService;
import com.dengyi.mobilesafe.utils.ServiceStatusUtils;

/**
 * 进程管理的设置页面
 * Created by deng on 2015/11/24.
 */
public class TaskSettingActivity extends Activity{

    private CheckBox cb_setting_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

    }

    private void initUI() {
        setContentView(R.layout.activity_task_setting);
        CheckBox showSysApp = (CheckBox) findViewById(R.id.cb_showsysapp);

        final SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean is_show_system = sp.getBoolean("is_show_system", true);
        showSysApp.setChecked(is_show_system);
        showSysApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    SharedPreferences.Editor edit =  sp.edit();
                    edit.putBoolean("is_show_system",isChecked);
                    edit.commit();

            }
        });
        //定时清理进程
        cb_setting_time = (CheckBox) findViewById(R.id.cb_setting_time);
        cb_setting_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            Intent intent = new Intent(TaskSettingActivity.this, KillProcessService.class);

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(intent);
                } else {
                    stopService(intent);
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ServiceStatusUtils.isServiceRunning(TaskSettingActivity.this, "com.dengyi.mobilesafe.service.KillProcessService")){
            cb_setting_time.setChecked(true);
        }else {
            cb_setting_time.setChecked(false);
        }
    }
}
