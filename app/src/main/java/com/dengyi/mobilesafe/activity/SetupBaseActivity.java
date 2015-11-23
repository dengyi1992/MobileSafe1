package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.dengyi.mobilesafe.R;

/**
 * Created by deng on 2015/11/18.
 * 设置引导页的基类
 * 不需要在清单文件中显示
 */
public abstract class SetupBaseActivity extends Activity {
    private GestureDetector mDetector;
    public SharedPreferences mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getSharedPreferences("config", MODE_PRIVATE);
        mDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    /**
                     *
                     * @param e1 滑动起点
                     * @param e2 滑动终点
                     * @param velocityX 水平速度
                     * @param velocityY 竖直速度
                     * @return
                     * 当距离达到移动距离，或者速度也达到就触发事件
                     */
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {
                        if (e2.getRawY() - e1.getRawY() < 100 | e1.getRawY() - e2.getRawY() < 100 | Math.abs(velocityX) > 100) {
                            //右划，上一页
                            if (e2.getRawX() - e1.getRawX() > 200) {
                                showprevious();
                                return true;
                            }
                            if (e1.getRawX() - e2.getRawX() > 200) {
                                shownext();
                                return true;
                            }
                        }

                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
    }

    //按钮下一页
    public void next(View view) {
        shownext();
    }

    public void previous(View view) {
        showprevious();
    }

    public abstract void showprevious();

    public abstract void shownext();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);//委托给收拾识别器
        return super.onTouchEvent(event);
    }
}
