package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dengyi.mobilesafe.R;

/**
 * 进程管理的设置页面
 * Created by deng on 2015/11/24.
 */
public class TaskSettingActivity extends Activity{
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
                if (isChecked){
                    SharedPreferences.Editor edit =  sp.edit();
                    edit.putBoolean("is_show_system",true);
                    edit.commit();
                }else {
                    SharedPreferences.Editor edit =  sp.edit();
                    edit.putBoolean("is_show_system",false);
                    edit.commit();
                }
            }
        });
    }
}
