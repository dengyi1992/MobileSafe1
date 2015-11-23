package com.dengyi.mobilesafe.utils;

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
}
