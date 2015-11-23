package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.db.Dao.AddressDao;

/**
 * Created by deng on 2015/11/19.
 */
public class AddressActivity extends Activity {
    private EditText etPhoneAddress;
    private TextView tvAddresss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        etPhoneAddress = (EditText) findViewById(R.id.et_phone_address);
        tvAddresss = (TextView) findViewById(R.id.tv_address);

        etPhoneAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address = AddressDao.getAddress(etPhoneAddress.getText().toString());
                tvAddresss.setText(address);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void queryAddress(View view) {
//        获取归属地并且显示
        if (!TextUtils.isEmpty(etPhoneAddress.getText().toString())){
            String address = AddressDao.getAddress(etPhoneAddress.getText().toString());
           tvAddresss.setText(address);
        }else {
            Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);
           etPhoneAddress.startAnimation(shake);
            vibrate();
        }
    }
    /**
     * 手机震动
     *
     *
     */
    private void vibrate(){
        Vibrator vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        vibrator.vibrate(1000);
        vibrator.vibrate(new long[]{200,400,200,400},-1);
        //震动500毫秒，暂停1秒，。。。-1表示不循环
        //0表示从头循环
    }
}
