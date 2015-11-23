package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dengyi.mobilesafe.R;

/**
 * Created by deng on 2015/11/17.
 */
public class Setup4Activity extends SetupBaseActivity {

    private CheckBox cbProtect;
    private boolean protect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        cbProtect = (CheckBox) findViewById(R.id.cb_protect);
        protect = mPrefs.getBoolean("protect", false);
        if (protect) {
            cbProtect.setText("防盗保护已经开启");
            cbProtect.setChecked(true);
        } else {
            cbProtect.setText("防盗保护没有开启");
            cbProtect.setChecked(false);
        }
        //当checkbox发生变化时回掉此方法
        cbProtect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbProtect.setText("防盗保护已经开启");
                    mPrefs.edit().putBoolean("protect", true).commit();

                } else {
                    cbProtect.setText("防盗保护没有开启");
                    mPrefs.edit().putBoolean("protect", false).commit();
                }
            }
        });

    }
////    设置完成跳转到LostFindActivity
//    public void next(View view){
//        finish();
//        mPrefs=getSharedPreferences("config",MODE_PRIVATE);
//        mPrefs.edit().putBoolean("configed",true).commit();
//        startActivity(new Intent(this, LostFindActivity.class));
//    }
//
//
//
//    public void previous(View view){
//
//    }

    @Override
    public void showprevious() {
        finish();
        overridePendingTransition(R.animator.previoustransin, R.animator.previoustransout);
    }

    @Override
    public void shownext() {
        finish();
//        mPrefs是父类中继承过来的
        mPrefs = getSharedPreferences("config", MODE_PRIVATE);
        mPrefs.edit().putBoolean("configed", true).commit();
        startActivity(new Intent(this, LostFindActivity.class));
    }
}
