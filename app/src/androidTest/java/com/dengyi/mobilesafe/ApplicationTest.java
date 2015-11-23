package com.dengyi.mobilesafe;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.dengyi.mobilesafe.bean.BlackNumberInfo;
import com.dengyi.mobilesafe.db.Dao.BlackNumberDao;

import java.util.List;
import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public Context mContext;
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        this.mContext=getContext();
        super.setUp();
    }

    public void testAdd(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        Random random = new Random();
        for (int i=0;i<200;i++){
            //设置13300000000为long形的
            Long number= 13300000000l+i;
            dao.add(number+"",String.valueOf(random.nextInt(3)+""));
        }
    }
    public void testDelete(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        boolean delete = dao.delete("13300000000");
        assertEquals(true,delete);
    }
    public void testFind(){
        BlackNumberDao dao=new BlackNumberDao(mContext);
        String mode = dao.findNumber("13300000001");
        System.out.println(mode);
    }
    public void testFindAll(){
        BlackNumberDao dao=new BlackNumberDao(mContext);
        List<BlackNumberInfo> blackNumberInfos = dao.findAll();
        for (BlackNumberInfo blackNumberInfo:blackNumberInfos
             ) {
            System.out.println(blackNumberInfo.getNumber()+":"+blackNumberInfo.getMode());
        }
    }
}