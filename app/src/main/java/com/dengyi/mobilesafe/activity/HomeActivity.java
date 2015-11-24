package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.utils.Md5Utils;

/**
 * Created by deng on 2015/11/17.
 */
public class HomeActivity extends Activity {
    private GridView gvHome;
    private String[] mItems = new String[]{
            "手机防盗", "通讯卫士", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"
    };
    private int[] mPics = new int[]{
            R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
            R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
            R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings
    };
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        gvHome = (GridView) findViewById(R.id.gv_home);
        gvHome.setAdapter(new HomeAdapter());

        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //手机防盗
                        showPasswordDialog();
                        break;
                    case 1:
                        //通讯卫士
                        startActivity(new Intent(HomeActivity.this, CallSafeActivity.class));
                        break;
                    case 2:
                        //软件管理
                        startActivity(new Intent(HomeActivity.this, AppManagerActivity.class));
                        break;
                    case 3:
                        //进程管理
                        startActivity(new Intent(HomeActivity.this, TaskManagerActivity.class));
                        break;
                    case 7:
                        //跳转高级工具
                        startActivity(new Intent(HomeActivity.this, AToolsActivity.class));
                        break;
                    case 8:
                        //跳转设置中心
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 显示密码弹窗
     */
    private void showPasswordDialog() {
        //判断是否有设置密码
        String savedPassword = mPref.getString("password", null);
        if (TextUtils.isEmpty(savedPassword)) {
            //如果没有设置过，弹出设置密码窗口
            showPasswordSetDialog();
        } else {
            showPasswordInputDialog();
        }

    }

    private void showPasswordInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_input_password, null);
        // dialog.setView(view);//将自定义的布局文件设置给dialog
        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
        dialog.setView(view, 0, 0, 0, 0);//设置边距为零
        Button btnOk = (Button) view.findViewById(R.id.bt_ok);
        Button btnCancel = (Button) view.findViewById(R.id.bt_cancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password =Md5Utils.encode(etPassword.getText().toString()) ;

                if (!TextUtils.isEmpty(password)) {
                    String savedpassword = mPref.getString("password", null);
                    if (password.equals(savedpassword)){
                        Toast.makeText(HomeActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        //跳转到手机防盗页
                        startActivity(new Intent(HomeActivity.this,LostFindActivity.class));

                    }else {
                        Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                }


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showPasswordSetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_set_password, null);
        // dialog.setView(view);//将自定义的布局文件设置给dialog
        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
        final EditText etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confrim);
        dialog.setView(view, 0, 0, 0, 0);//设置边距为零
        Button btnOk = (Button) view.findViewById(R.id.bt_ok);
        Button btnCancel = (Button) view.findViewById(R.id.bt_cancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirm)) {
                    if (password.equals(passwordConfirm)) {
                        Toast.makeText(HomeActivity.this, "设置成功", Toast.LENGTH_LONG).show();
                        mPref.edit().putString("password", Md5Utils.encode(password)).commit();

                        dialog.dismiss();
                        //跳转到手机防盗页
                        startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
                    } else {
                        Toast.makeText(HomeActivity.this, "两次密码不一致", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(HomeActivity.this, R.layout.homelist_item, null);
            ImageView ivItem = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
            tvItem.setText(mItems[position]);
            ivItem.setImageResource(mPics[position]);
            return view;
        }
    }
}
