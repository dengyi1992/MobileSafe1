package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.service.AddressService;
import com.dengyi.mobilesafe.service.CallSafeService;
import com.dengyi.mobilesafe.utils.ServiceStatusUtils;
import com.dengyi.mobilesafe.view.SettingClickView;
import com.dengyi.mobilesafe.view.SettingItemView;

/**
 * Created by deng on 2015/11/17.
 */
public class SettingActivity extends Activity {
    private SettingItemView sivUpdate;// 设置升级
    private SharedPreferences mPref;
    private SettingItemView sivAddress;
    private SettingClickView scvAddressStyle;
    final String[] items = new String[]{"半透明", "活力橙", "锐仕蓝", "苹果绿", "银灰色"};
    private SettingClickView scvAddressLocation;
    private SettingItemView sivCallSafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPref = getSharedPreferences("config", MODE_PRIVATE);
        initAutoUpdate();//自动更新
        initAddress();//是否显示归属地
        initAddressStyle();//提示框风格
        initAddressLocation();//提示框风格
        initCallSafe();//黑名单设置

    }




    private void initAutoUpdate() {
        /**
         * 自动更新设置
         */
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        sivUpdate.setTitle("自动更新设置");
        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        sivUpdate.setChecked(autoUpdate);
        if (autoUpdate) {
            sivUpdate.setDesc("自动更新已开启");
        } else {
            sivUpdate.setDesc("自动更新已关闭");
        }
        sivUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 判断当前的勾选状态
                if (sivUpdate.isChecked()) {
                    // 设置不勾选
                    sivUpdate.setChecked(false);
                    sivUpdate.setDesc("自动更新已关闭");
                    // 更新sp
                    mPref.edit().putBoolean("auto_update", false).commit();
                } else {
                    sivUpdate.setChecked(true);
                    sivUpdate.setDesc("自动更新已开启");
                    // 更新sp
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });

    }

    private void initAddress() {
        /**
         *归属地是否显示
         */

        sivAddress = (SettingItemView) findViewById(R.id.siv_address);
        sivAddress.setTitle("归属地是否显示");
        boolean showAddress = mPref.getBoolean("show_address", true);
        sivAddress.setChecked(ServiceStatusUtils.isServiceRunning(this, "com.dengyi.mobilesafe.service.AddressService"));
        //根据归属地服务是否运行来是否勾选
        if (ServiceStatusUtils.isServiceRunning(this, "com.dengyi.mobilesafe.service.AddressService")) {
            sivAddress.setDesc("显示归属地");
        } else {
            sivAddress.setDesc("不显示归属地");
        }
        sivAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 判断当前的勾选状态
                if (sivAddress.isChecked()) {
                    // 设置不勾选
                    sivAddress.setChecked(false);
                    sivAddress.setDesc("不显示归属地");
                    // 更新sp
                    mPref.edit().putBoolean("show_address", false).commit();
                    stopService(new Intent(SettingActivity.this, AddressService.class));
                    //停止服务
                } else {
                    sivAddress.setChecked(true);
                    sivAddress.setDesc("显示归属地");
                    // 更新sp
                    mPref.edit().putBoolean("show_address", true).commit();
                    startService(new Intent(SettingActivity.this, AddressService.class));
                }
            }
        });
    }

    /**
     * 修改提示框风格
     */
    private void initAddressStyle() {

        scvAddressStyle = (SettingClickView) findViewById(R.id.scv_style);
        scvAddressStyle.setTitle("归属地提示风格");
        int style = mPref.getInt("address_style", 0);
        scvAddressStyle.setDesc(items[style]);

        scvAddressStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChooseDialog();
            }
        });
    }

    /**
     * 弹出单选框
     */
    private void showSingleChooseDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("归属地提示风格");
        int style = mPref.getInt("address_style", 0);

        builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //保存选择的风格
                mPref.edit().putInt("address_style", which).commit();
                dialog.dismiss();
                scvAddressStyle.setDesc(items[which]);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 初始化修改归属框的位置
     *
     */
    private void initAddressLocation() {
        scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
        scvAddressLocation.setTitle("归属地提示位置");
        scvAddressLocation.setDesc("设置归属地提示框的位置");

        scvAddressLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, DragActivity.class));
            }
        });
    }

    /**
     * 黑名单开关
     */
    private void initCallSafe() {
        /**
         *归属地是否显示
         */

        sivCallSafe = (SettingItemView) findViewById(R.id.siv_callsafe);
        sivCallSafe.setTitle("黑名单是否开启");
        boolean openCallSafe = mPref.getBoolean("call_safe", true);
        sivCallSafe.setChecked(ServiceStatusUtils.isServiceRunning(this, "com.dengyi.mobilesafe.service.CallSafeService"));
        //根据归属地服务是否运行来是否勾选
        if (ServiceStatusUtils.isServiceRunning(this, "com.dengyi.mobilesafe.service.CallSafeService")) {
            sivCallSafe.setDesc("黑名单开启");
        } else {
            sivCallSafe.setDesc("黑名单关闭");
        }
        sivCallSafe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 判断当前的勾选状态
                if (sivCallSafe.isChecked()) {
                    // 设置不勾选
                    sivCallSafe.setChecked(false);
                    sivCallSafe.setDesc("黑名单关闭");
                    // 更新sp
                    mPref.edit().putBoolean("call_safe", false).commit();
                    stopService(new Intent(SettingActivity.this, CallSafeService.class));
                    //停止服务
                } else {
                    sivCallSafe.setChecked(true);
                    sivCallSafe.setDesc("黑名单开启");
                    // 更新sp
                    mPref.edit().putBoolean("call_safe", true).commit();
                    startService(new Intent(SettingActivity.this, CallSafeService.class));
                }
            }
        });
    }
}
