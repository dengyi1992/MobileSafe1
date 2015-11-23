package com.dengyi.mobilesafe.bean;

/**
 * Created by deng on 2015/11/20.
 */

public class BlackNumberInfo {
    /**
     * 黑名单电话号码
     */
    private String number;


    /**
     * 黑名单模式
     * 1.全部拦截

     * 2.电话拦截
     * 3.短信拦截
     */
    private String mode;
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
