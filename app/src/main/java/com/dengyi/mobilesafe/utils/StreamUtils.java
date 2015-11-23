package com.dengyi.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by deng on 2015/11/17.
 */
public class StreamUtils {
    public static String readFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        int len=0;
        byte[] buffer=new byte[1024];
        while ((len=inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,len);
        }
        String result=outputStream.toString();
        inputStream.close();
        outputStream.close();
        return result;
    }
}
