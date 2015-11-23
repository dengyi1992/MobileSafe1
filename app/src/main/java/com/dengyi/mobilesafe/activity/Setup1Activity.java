package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;

import com.dengyi.mobilesafe.R;

/**
 * Created by deng on 2015/11/17.
 * 第一设置向导页
 */
public class Setup1Activity extends SetupBaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }
//    public void next(View view){
//        startActivity(new Intent(this,Setup2Activity.class));
//
//        //两个界面之间切换的动画
//
//        overridePendingTransition(R.animator.transout,R.animator.transin);
//    }

    @Override
    public void showprevious() {

    }

    @Override
    public void shownext() {
        startActivity(new Intent(this,Setup2Activity.class));

        //两个界面之间切换的动画

        overridePendingTransition(R.animator.transout,R.animator.transin);
    }
}
