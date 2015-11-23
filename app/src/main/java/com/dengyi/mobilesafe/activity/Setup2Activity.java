package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.utils.ToastUtils;
import com.dengyi.mobilesafe.view.SettingItemView;

/**
 * Created by deng on 2015/11/17.
 */
public class Setup2Activity extends SetupBaseActivity {
    private SettingItemView sivBindSim;
    private GestureDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        sivBindSim = (SettingItemView) findViewById(R.id.siv_bindSim);

        String sim = mPrefs.getString("sim", null);
        if (!TextUtils.isEmpty(sim)) {
            sivBindSim.setChecked(true);
            sivBindSim.setDesc("sim卡已经绑定");
        } else {
            sivBindSim.setChecked(false);
            sivBindSim.setDesc("sim卡没有绑定");
        }
        sivBindSim.setTitle("点击绑定sim卡");
        sivBindSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivBindSim.isChecked()) {
                    sivBindSim.setChecked(false);
                    sivBindSim.setDesc("sim卡没有绑定");
                    //删除已经绑定的sim卡数据
                    mPrefs.edit().remove("sim").commit();
                } else {
                    sivBindSim.setChecked(true);
                    sivBindSim.setDesc("sim卡已经绑定");

                    //保存sim卡的信息,获取序列号
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = telephonyManager.getSimSerialNumber();
                    System.out.println(simSerialNumber);
                    //将sim卡序列号保存
                    mPrefs.edit().putString("sim", simSerialNumber).commit();
                }
            }
        });


    }


    public void showprevious() {
        finish();
        overridePendingTransition(R.animator.previoustransin, R.animator.previoustransout);

    }

    public void shownext() {
        //没有绑定sim卡就不许
        String sim=mPrefs.getString("sim",null);
        if (TextUtils.isEmpty(sim)){
            ToastUtils.showToast(this,"必须绑定sim卡");
            return;
        }
        startActivity(new Intent(this, Setup3Activity.class));
        overridePendingTransition(R.animator.transout, R.animator.transin);
    }
}
