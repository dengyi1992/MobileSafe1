package com.dengyi.mobilesafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.fragment.LockFragment;
import com.dengyi.mobilesafe.fragment.UnLockFragment;

/**
 * Created by deng on 2015/11/25.
 */
public class AppLock extends FragmentActivity implements View.OnClickListener {

    private TextView tvUnlock;
    private TextView tvLock;
    private FrameLayout flContent;
    private FragmentManager fragmentManager;
    private UnLockFragment unLockFragment;
    private LockFragment lockFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();

    }
    private void initUI() {
        setContentView(R.layout.activity_app_lock);

        flContent = (FrameLayout) findViewById(R.id.fl_content);
        tvUnlock = (TextView) findViewById(R.id.tv_unlock);
        tvLock = (TextView) findViewById(R.id.tv_lock);

        tvUnlock.setOnClickListener(this);
        tvLock.setOnClickListener(this);

        //的大fragment管理者
        fragmentManager = getSupportFragmentManager();
        //开启事物
        FragmentTransaction mTransaction = fragmentManager.beginTransaction();
        unLockFragment = new UnLockFragment();
        lockFragment = new LockFragment();

        mTransaction.replace(R.id.fl_content, unLockFragment).commit();
    }
    private void initData() {

    }


    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (v.getId()){
            case R.id.tv_unlock:
                tvUnlock.setBackgroundResource(R.drawable.tab_left_pressed);
                tvLock.setBackgroundResource(R.drawable.tab_right_default);
                transaction.replace(R.id.fl_content, unLockFragment);
                break;
            case R.id.tv_lock:
                tvUnlock.setBackgroundResource(R.drawable.tab_left_default);
                tvLock.setBackgroundResource(R.drawable.tab_right_pressed);
                transaction.replace(R.id.fl_content, lockFragment);
                break;
            default:
                break;

        }
        transaction.commit();
    }
}
