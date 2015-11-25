package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;


import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.bean.AppInfo;
import com.dengyi.mobilesafe.db.Dao.AntiVirusDao;
import com.dengyi.mobilesafe.engine.AppInfos;
import com.dengyi.mobilesafe.utils.Md5Utils;

import java.util.List;

/**
 * Created by deng on 2015/11/25.
 */
public class AntiVirusActivity extends Activity {
    protected static final int BEGING = 1;
    private Message message;
    protected static final int SCANING = 2;
    protected static final int FINISH = 3;
    private TextView tvInitVirus;
    private ProgressBar pbScanning;
    private ImageView ivScanning;
    private LinearLayout llContent;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();

    }


    private void initUI() {
        setContentView(R.layout.activity_anti_virus);
        ivScanning = (ImageView) findViewById(R.id.iv_scanning);
        tvInitVirus = (TextView) findViewById(R.id.tv_init_virus);
        pbScanning = (ProgressBar) findViewById(R.id.pb_scanning);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        scrollView = (ScrollView) findViewById(R.id.sv_view);
        /**
         * Constructor to use when building a RotateAnimation from code
         *
         * @param fromDegrees Rotation offset to apply at the start of the
         *        animation.
         *
         * @param toDegrees Rotation offset to apply at the end of the animation.
         *
         * @param pivotXType Specifies how pivotXValue should be interpreted. One of
         *        Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
         *        Animation.RELATIVE_TO_PARENT.
         * @param pivotXValue The X coordinate of the point about which the object
         *        is being rotated, specified as an absolute number where 0 is the
         *        left edge. This value can either be an absolute number if
         *        pivotXType is ABSOLUTE, or a percentage (where 1.0 is 100%)
         *        otherwise.
         * @param pivotYType Specifies how pivotYValue should be interpreted. One of
         *        Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
         *        Animation.RELATIVE_TO_PARENT.
         * @param pivotYValue The Y coordinate of the point about which the object
         *        is being rotated, specified as an absolute number where 0 is the
         *        top edge. This value can either be an absolute number if
         *        pivotYType is ABSOLUTE, or a percentage (where 1.0 is 100%)
         *        otherwise.
         *                    初始化旋转动画
         */
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        //设置动画无限循环
        rotateAnimation.setRepeatCount(-1);
        ivScanning.startAnimation(rotateAnimation);
    }

    private void initData() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case BEGING:
                        tvInitVirus.setText("初始化8核心引擎");
                        break;
                    case SCANING:
                        //扫描
                        TextView child = new TextView(AntiVirusActivity.this);
                        ScanInfo scanInfo = (ScanInfo) msg.obj;
                        String descS = scanInfo.desc ? "扫描危险" : "扫描安全";
                      if (scanInfo.desc) {
                         child.setTextColor(Color.RED);
                      }else {
                          child.setTextColor(Color.GREEN);
                      }
                        child.setText(scanInfo.appName + ":"+descS);
                        llContent.addView(child);
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                //设置一直往下滑动
                                scrollView.fullScroll(scrollView.FOCUS_DOWN);
                            }
                        });

                        break;
                    case FINISH:
                        ivScanning.clearAnimation();
                        ivScanning.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {

                PackageManager packageManager = getPackageManager();
                List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
                //总共的app数
                int totoalApp = installedPackages.size();
                int progress=0;
                pbScanning.setMax(totoalApp);
                for (PackageInfo packageInfo :
                        installedPackages) {
                    progress++;
                    ScanInfo scanInfo = new ScanInfo();
                    //获取到当前手机上面的app名字
                    String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                    scanInfo.appName = appName;
                    String packgeName = packageInfo.applicationInfo.packageName;
                    scanInfo.packageName = packgeName;
                    //获取应用的目录，来查询md5
                    String sourceDir = packageInfo.applicationInfo.sourceDir;
                    String md5 = Md5Utils.getFileMd5(sourceDir);
                    String desc = AntiVirusDao.checkFileVirus(md5);
//                    System.out.println("=========================分割线===========================");
//                    System.out.println(appName);
//                    System.out.println(md5);
//                    Animation
//                    664761b09daf5f0a2303587b8ce0fe38
                    //判断是md5是否在数据库中,有的话返回描述
                    if (desc != null) {
                        scanInfo.desc = true;
                    } else {
                        scanInfo.desc = false;

                    }
                    message = Message.obtain();
                    message.what = SCANING;
                    message.obj = scanInfo;
                    handler.sendMessage(message);
                    pbScanning.setProgress(progress);
                    SystemClock.sleep(50);
                }
                message = Message.obtain();
                message.what = FINISH;
                handler.sendMessage(message);

            }
        }.start();


    }

    static class ScanInfo {
        boolean desc;
        String appName;
        String packageName;
    }

}
