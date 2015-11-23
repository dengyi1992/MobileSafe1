package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.utils.ToastUtils;
import com.dengyi.mobilesafe.view.SettingItemView;

/**
 * Created by deng on 2015/11/17.
 */
public class Setup3Activity extends SetupBaseActivity {

    private EditText etPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        etPhone = (EditText) findViewById(R.id.et_phone);

        String phone = mPrefs.getString("safe_phone", "");
        etPhone.setText(phone);
    }


    @Override
    public void showprevious() {


        finish();
        overridePendingTransition(R.animator.previoustransin, R.animator.previoustransout);
    }

    @Override
    public void shownext() {
        String phone = etPhone.getText().toString().trim();// 注意过滤空格
        if(TextUtils.isEmpty(etPhone.getText().toString())){
            ToastUtils.showToast(this,"安全号码不能为空");
            return;
        }
        mPrefs.edit().putString("safe_phone", phone).commit();// 保存安全号码
        startActivity(new Intent(this, Setup4Activity.class));
        overridePendingTransition(R.animator.transout, R.animator.transin);
    }

    public void selectcontact(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {//确保返回时是有值得，如果没有选好，不操作
            String phone = data.getStringExtra("phone");
            phone = phone.replaceAll("-", "").replaceAll(" ", "");// 替换-和空格

            etPhone.setText(phone);// 把电话号码设置给输入框
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}