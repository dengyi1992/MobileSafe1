package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.utils.SmsUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 高级工具
 * 利用抽取成接口可以使得工具类不需要改动
 * Created by deng on 2015/11/19.
 */
public class AToolsActivity extends Activity {

    private ProgressDialog pd;
//    @ViewInject(R.id.pb_backup)
//    private ProgressBar pb_backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
//        ViewUtils.inject(this);
    }

    /**
     * 归属地查询
     *
     */
    public void numberAddressQuery(View view) {
        startActivity(new Intent(this, AddressActivity.class));
    }

    /**
     * 短信备份
     *
     */
    public void backUpSms(View view) {
        //初始化一个进度条对话框
        pd = new ProgressDialog(AToolsActivity.this);
        pd.setTitle("提示");
        pd.setMessage("稍安勿燥，备份ing...再等会哦！亲");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.show();

        new Thread() {
            @Override
            public void run() {
                boolean result = SmsUtils.backUp(AToolsActivity.this, pd);
//                boolean result = SmsUtils.backUp(AToolsActivity.this, pb_backup);
                //子线程更新ui
                Looper.prepare();
                Toast.makeText(AToolsActivity.this, result ? "备份成功" : "备份失败", Toast.LENGTH_LONG).show();
                pd.dismiss();
                Looper.loop();
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }.start();

    }
    /**
     * 程序锁
     */
    public void appLock(View view){
        Intent intent = new Intent(AToolsActivity.this, AppLock.class);
        startActivity(intent);
    }
}
