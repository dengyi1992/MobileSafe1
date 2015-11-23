package com.dengyi.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by deng on 2015/11/20.
 */
public class BlackNumberDBHelper extends SQLiteOpenHelper {
    public BlackNumberDBHelper(Context context) {
        super(context, "safe.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表 blacknumber 主键_id自增长 ,number黑名单号码，mode拦截模式：1电话拦截 2短信拦截  3全部拦截
        db.execSQL("create table blacknumber (_id integer primary key autoincrement,number varchar(20),mode varchar(2))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
