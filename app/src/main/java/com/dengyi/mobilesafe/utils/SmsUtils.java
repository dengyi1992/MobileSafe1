package com.dengyi.mobilesafe.utils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;
import android.widget.ProgressBar;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by deng on 2015/11/23.
 * 短信备份工具类
 */
public class SmsUtils {

    private static FileOutputStream fileOutputStream;
    private static File smsBackUpFile;

    public static boolean backUp(Context context,ProgressDialog pd) {
        /**
         * 目的：备份短信
         * 1.判断是否有sd卡
         * 2.权限--- 内容观察者
         * 3.将短信写到sd卡数据库
         *
         */
        //判断当前sd卡状态值
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                System.out.println("存储卡可用");


//                smsBackUpFile = new File(Environment.getExternalStorageDirectory(),"sms.xml");
                smsBackUpFile = new File("/mnt/sdcard/backUp.xml");
                fileOutputStream = new FileOutputStream(smsBackUpFile);
                //序列化器
                //在android中关于xml解析都是pull解析
                XmlSerializer serializer = Xml.newSerializer();
                //短信序列化
                serializer.setOutput(fileOutputStream, "utf-8");

                ContentResolver resolver = context.getContentResolver();
                //获取短信的路径
                Uri uri = Uri.parse("content://sms/");
                // type=1 收受短信，type=2 发送短信 curosr：游标
                Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
                int count = cursor.getCount();
                pd.setMax(count);
                int process = 0;
                //设置pd的最大的值
                /**
                 * tartDocumen 第二个参数表示是否是独立
                 */
                serializer.startDocument("utf-8", true);
                serializer.startTag(null, "smss");//开始节点
                serializer.attribute(null,"size", String.valueOf(count));
                //设置节点上的属性

                while (cursor.moveToNext()) {
                    serializer.startTag(null, "sms");
                    String address = cursor.getString(0);
                    String date = cursor.getString(1);
                    String type = cursor.getString(2);
                    String body = Crypto.encrypt("123", cursor.getString(3));
                    /**
                     * "123"是加密种子
                     * 密钥
                     */


                    serializer.startTag(null, "address");
                    System.out.println("短信地址：" + address);
                    serializer.text(address);
                    serializer.endTag(null, "address");

                    serializer.startTag(null, "date");
                    System.out.println("短信时间：" + date);
                    serializer.text(date);
                    serializer.endTag(null, "date");

                    serializer.startTag(null, "body");
                    System.out.println("短信内容：" + body);
                    serializer.text(body);
                    serializer.endTag(null, "body");

                    serializer.startTag(null, "type");
                    System.out.println("短信类型：" + type);
                    serializer.text(type);
                    serializer.endTag(null, "type");

                    serializer.endTag(null, "sms");
                    process++;
                    pd.setProgress(process);
                }


                cursor.close();

                serializer.endTag(null, "smss");

                serializer.endDocument();

                fileOutputStream.close();

                return true;


            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }
}
