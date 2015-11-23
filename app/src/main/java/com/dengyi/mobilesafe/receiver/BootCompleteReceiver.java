package com.dengyi.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by deng on 2015/11/18.
 * 监听开机启动完成广播
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("protect", false)) {
            //只有在防盗保护开启的情况下才开启检测
            String sim = sharedPreferences.getString("sim", null);
            //获取绑定的sim卡信息
            if (TextUtils.isEmpty(sim)) {
                //获取当前手机的sim卡
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String currentSim = telephonyManager.getSimSerialNumber()+"1231";

                if (sim.equals(currentSim)) {
                    System.out.println("手机安全");
                    Toast.makeText(context, "手机安全", Toast.LENGTH_LONG).show();
                } else {
                    String phone=sharedPreferences.getString("phone", null);

                    //发送短信
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(phone,null,"sim卡已经发生变化",null,null);
                    System.out.println("sim卡已经变化，发送报警短信");
                    Toast.makeText(context, "sim卡已经变化，发送报警短信", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
