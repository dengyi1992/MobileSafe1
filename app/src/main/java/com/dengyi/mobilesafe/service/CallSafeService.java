package com.dengyi.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.dengyi.mobilesafe.db.Dao.BlackNumberDao;

import java.lang.reflect.Method;

public class CallSafeService extends Service {

    private BlackNumberDao dao;
    private TelephonyManager tm;

    public CallSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dao = new BlackNumberDao(this);
        //获取系统的电话服务
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        MyPhoneStateListener listener = new MyPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        //初始化短信广播
        InnerReceiver innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class InnerReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");

            for (Object object : objects) {// 短信最多140字节,
                // 超出的话,会分为多条短信发送,所以是一个数组,因为我们的短信指令很短,所以for循环只执行一次
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = message.getOriginatingAddress();// 短信来源号码
                String messageBody = message.getMessageBody();// 短信内容
                //通过号码查询拦截模式
                String mode = dao.findNumber(originatingAddress);
                if (mode.equals("1")){
                    abortBroadcast();
                }else if (mode.equals("3")){
                    abortBroadcast();
                }
            }
        }
    }
    private class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            /**
             *
             * @see TelephonyManager#CALL_STATE_IDLE 空闲
             * @see TelephonyManager#CALL_STATE_RINGING 响铃
             * @see TelephonyManager#CALL_STATE_OFFHOOK 接通
             */
            switch (state){
//                case TelephonyManager.CALL_STATE_IDLE:
//                    break;
                case TelephonyManager.CALL_STATE_RINGING://电话铃响
                    String mode = dao.findNumber(incomingNumber);

                    if (mode.equals("1")||mode.equals("2")){
                        System.out.println("挂断黑名单电话号码");

                        Uri uri = Uri.parse("content://call_log/calls");

                        getContentResolver().registerContentObserver(uri, true, new MyContentObserver(new Handler(), incomingNumber));

                        //挂断电话
                        endCall();
                    }
                    break;
            }
        }
    }

    /**
     * 挂断电话
     */
    private void endCall() {

        try {
            //通过类加载器加载ServiceManager
            Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");
            //通过反射得到当前的方法
            Method method = clazz.getDeclaredMethod("getService", String.class);

            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);

            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);

            iTelephony.endCall();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyContentObserver extends ContentObserver {
        String incomingNumber;
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         * @param incomingNumber
         */
        public MyContentObserver(Handler handler, String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        //当数据改变的时候调用的方法
        @Override
        public void onChange(boolean selfChange) {

            getContentResolver().unregisterContentObserver(this);

            deleteCallLog(incomingNumber);

            super.onChange(selfChange);
        }
    }

    private void deleteCallLog(String incomingNumber) {
        Uri uri = Uri.parse("content://call_log/calls");

        getContentResolver().delete(uri,"number=?",new String[]{incomingNumber});
    }
}

