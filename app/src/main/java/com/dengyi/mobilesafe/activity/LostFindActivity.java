package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dengyi.mobilesafe.R;

/**
 * Created by deng on 2015/11/17.
 */
public class LostFindActivity extends Activity {
    private SharedPreferences mPrefs;
    private TextView tvNumber;
    private boolean protect;
    private TextView ifProtect;
    private ImageView ivLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs=getSharedPreferences("config",MODE_PRIVATE);
        boolean configed=mPrefs.getBoolean("configed",false);
        protect = mPrefs.getBoolean("protect", false);


        if (configed){
            //如果有设置则跳转到此页面
            setContentView(R.layout.activity_lostfind);
            tvNumber = (TextView) findViewById(R.id.tv_number);
            ifProtect= (TextView) findViewById(R.id.if_protect);
            ivLock=(ImageView)findViewById(R.id.iv_lock);
            String phone = mPrefs.getString("safe_phone", "");
            tvNumber.setText(phone);
            if (protect) {
                ivLock.setImageResource(R.drawable.lock);
                ifProtect.setText("防盗保护已经开启");
            } else {
                ivLock.setImageResource(R.drawable.unlock);
                ifProtect.setText("防盗保护没有开启");

                 }
        }else {
            //否则跳转到设置向导页面
            finish();
          startActivity(new Intent(this,Setup1Activity.class));
        }

    }
    public void reEnter(View view){
        startActivity(new Intent(this,Setup1Activity.class));
    }
}
