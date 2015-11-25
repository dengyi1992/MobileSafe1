package com.dengyi.mobilesafe.db.Dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by deng on 2015/11/25.
 */
public class AntiVirusDao {
    private static final String PATH = "data/data/com.dengyi.mobilesafe/files/antivirus.db";
    public static String checkFileVirus(String md5){
        String desc=null;
        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
                SQLiteDatabase.OPEN_READONLY);
//        Cursor cursor = database.query("datable", new String[]{"desc"}, "md5", new String[]{md5}, null, null, null);
        Cursor cursor = database.rawQuery("select desc from datable where md5 = ?", new String[]{md5});
        //判断当前的游标是否可以移动
        if(cursor.moveToNext()){
            desc = cursor.getString(0);
        }
        cursor.close();
        return desc;



    }

    /**
     * 添加病毒md5
     * @param md5
     * @param desc
     */
    public static void addVirus(String md5,String desc){
        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
                SQLiteDatabase.OPEN_READWRITE);
        ContentValues values = new ContentValues();
        values.put("md5",md5);
        values.put("type",6);
        values.put("desc",desc);
        values.put("name","Android.Troj.AirAD.a");
        if (checkFileVirus(md5)==null){
            database.insert("datable", null, values);
        }else {
            System.out.println("病毒数据已经存在");
        }

        database.close();
    }
}
