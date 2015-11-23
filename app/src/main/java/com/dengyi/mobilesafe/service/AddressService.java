package com.dengyi.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.db.Dao.AddressDao;

/**
 * Created by deng on 2015/11/19.
 */
public class AddressService extends Service {
    private TelephonyManager telephonyManager;
    private Mylistener listener;
    private OutCallReceiver receiver;
    private WindowManager windowManager;
    private View tvToast;
    public SharedPreferences mPrefs;
    private int winWidth;
    private int winHeight;
    private int startX;
    private int startY;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPrefs = getSharedPreferences("config", MODE_PRIVATE);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new Mylistener();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        receiver = new OutCallReceiver();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(receiver);//注销广播
    }

    class Mylistener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    System.out.println("电话铃响了");
                    String address = AddressDao.getAddress(incomingNumber);
                    Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG).show();
                    showToast(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //电话闲置的时候
                    if (windowManager!=null&&tvToast!=null){

                        windowManager.removeView(tvToast);
                    }
                        break;
                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    /**
     * Created by deng on 2015/11/19.\
     * 需要权限
     * <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
     */
    class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String number = getResultData();
            String address = AddressDao.getAddress(number);
//            Toast.makeText(context, address, Toast.LENGTH_LONG).show();
            showToast(address);
        }
    }

    /**
     * 自定义悬浮窗
     * WindowManager可以在第三方弹出自己的窗口
     */
    private void showToast(String text) {
        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        // 获取屏幕宽高
        winWidth = windowManager.getDefaultDisplay().getWidth();
        winHeight = windowManager.getDefaultDisplay().getHeight();
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        //TYPE不能满足要求
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.gravity= Gravity.LEFT+Gravity.TOP;//将中心位置改为左上方
        params.setTitle("Toast");
        int lastX = mPrefs.getInt("lastX", 0);
        int lastY = mPrefs.getInt("lastY", 0);
        //设置悬浮的位置
        params.x =lastX;
        params.y =lastY;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        tvToast = View.inflate(this, R.layout.toast_address, null);
        int[] backDraw=new int[]{R.drawable.call_locate_white,R.drawable.call_locate_orange,
                R.drawable.call_locate_blue,R.drawable.call_locate_green,R.drawable.call_locate_gray};
        int style = mPrefs.getInt("address_style", 0);
        tvToast.setBackgroundResource(backDraw[style]);
        TextView tvText = (TextView) tvToast.findViewById(R.id.tv_numbershow);
        tvText.setText(text);
        windowManager.addView(tvToast, params);

        tvToast.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("触摸到了");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        // 计算移动偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;

                        // 更新浮窗位置
                        params.x += dx;
                        params.y += dy;

                        // 防止坐标偏离屏幕
                        if (params.x < 0) {
                            params.x = 0;
                        }

                        if (params.y < 0) {
                            params.y = 0;
                        }

                        // 防止坐标偏离屏幕
                        if (params.x > winWidth - tvToast.getWidth()) {
                            params.x = winWidth - tvToast.getWidth();
                        }

                        if (params.y > winHeight - tvToast.getHeight()) {
                            params.y = winHeight - tvToast.getHeight();
                        }

                        // System.out.println("x:" + params.x + ";y:" + params.y);

                        windowManager.updateViewLayout(tvToast, params);

                        // 重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        // 记录坐标点
                        SharedPreferences.Editor edit = mPrefs.edit();
                        edit.putInt("lastX", params.x);
                        edit.putInt("lastY", params.y);
                        edit.commit();
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

    }
}
