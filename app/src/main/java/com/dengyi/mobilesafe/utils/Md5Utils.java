package com.dengyi.mobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * MD5加密
 * Created by deng on 2015/11/17.
 */
public class Md5Utils {
    public static String encode(String ps){
        try {
            MessageDigest instance=MessageDigest.getInstance("MD5");
            byte[] digest=instance.digest(ps.getBytes());
            StringBuffer sb=new StringBuffer();
            for (byte b:digest){
                int i=b&0xff;
                String hexString=Integer.toHexString(i);
                if (hexString.length()<2){
                    hexString="0"+hexString;//如果1位，则补0
                }
                sb.append(hexString);
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件md5值（病毒特征码）
     * @param sourceDir
     * @return
     */
    public static String getFileMd5(String sourceDir) {
        File file = new File(sourceDir);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len=-1;
            //数字摘要
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            while ((len=fileInputStream.read(buffer))!=-1){
                messageDigest.update(buffer,0,len);
            }
            byte[] digest = messageDigest.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b:digest){
                int i=b&0xff;
                String hexString=Integer.toHexString(i);
                if (hexString.length()<2){
                    hexString="0"+hexString;//如果1位，则补0
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
