package com.dengyi.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dengyi.mobilesafe.db.Dao.AddressDao;

/**
 * Created by deng on 2015/11/19.\
 * 需要权限
 * <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
 */
public class OutCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String number = getResultData();
        String address = AddressDao.getAddress(number);
        Toast.makeText(context, address, Toast.LENGTH_LONG).show();
    }
}
