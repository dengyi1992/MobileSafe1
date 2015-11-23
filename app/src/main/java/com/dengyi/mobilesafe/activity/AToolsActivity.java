package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dengyi.mobilesafe.R;

/**
 * Created by deng on 2015/11/19.
 */
public class AToolsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public void numberAddressQuery(View view) {
        startActivity(new Intent(this, AddressActivity.class));
    }
}
