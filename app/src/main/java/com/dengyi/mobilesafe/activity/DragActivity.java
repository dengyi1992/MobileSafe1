package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dengyi.mobilesafe.R;

/**
 * Created by deng on 2015/11/19.
 */
public class DragActivity extends Activity {
    private TextView tvTop;
    private TextView tvBottom;
    private ImageView ivDrag;
    private int startX;
    private int startY;
    private SharedPreferences mPrefs;
    long[] mHits = new long[2];// 数组长度表示要点击的次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        mPrefs = getSharedPreferences("config", MODE_PRIVATE);
        tvTop = (TextView) findViewById(R.id.tv_top);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);
        ivDrag = (ImageView) findViewById(R.id.iv_drag);
        int lastX = mPrefs.getInt("lastX", 0);
        int lastY = mPrefs.getInt("lastY", 0);
//        ivDrag.layout(lastX, lastY, lastX + ivDrag.getWidth(), lastY + ivDrag.getHeight());
        final int winWidth = getWindowManager().getDefaultDisplay().getWidth();
        final int winHeight = getWindowManager().getDefaultDisplay().getHeight();
        if (lastY > winHeight / 2) {
            tvTop.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.INVISIBLE);
        } else {
            tvTop.setVisibility(View.INVISIBLE);
            tvBottom.setVisibility(View.VISIBLE);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag
                .getLayoutParams();// 获取布局对象
        layoutParams.leftMargin = lastX;// 设置左边距
        layoutParams.topMargin = lastY;// 设置top边距

        ivDrag.setLayoutParams(layoutParams);// 重新设置位置
        //双击居中
        ivDrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();// 开机后开始计算的时间
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    // 把图片居中
                    ivDrag.layout(winWidth / 2 - ivDrag.getWidth() / 2,
                            ivDrag.getTop(), winWidth / 2 + ivDrag.getWidth()
                                    / 2, ivDrag.getBottom());
                }
            }
        });


        // 设置触摸监听
        ivDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();
                        //获取偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;

                        int l = ivDrag.getLeft() + dx;
                        int t = ivDrag.getTop() + dy;
                        int r = ivDrag.getRight() + dx;
                        int b = ivDrag.getBottom() + dy;
                        //判断是否有超出屏幕边界
                        if (l < 0 || r > winWidth || t < 0 || b > winHeight - 20) {
                            break;
                        }
                        if (t > winHeight / 2) {
                            tvTop.setVisibility(View.VISIBLE);
                            tvBottom.setVisibility(View.INVISIBLE);
                        } else {
                            tvTop.setVisibility(View.INVISIBLE);
                            tvBottom.setVisibility(View.VISIBLE);
                        }
                        //更新界面
                        ivDrag.layout(l, t, r, b);


                        //重新设置起点
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        // 记录坐标点
                        SharedPreferences.Editor edit = mPrefs.edit();
                        edit.putInt("lastX", ivDrag.getLeft());
                        edit.putInt("lastY", ivDrag.getTop());
                        edit.commit();
                        break;

                    default:
                        break;
                }
                //如果返回true拦截传递，事件要双击实现就要返回false
                return false;
            }
        });
    }
}
