package com.zch.rxjava;

import android.content.Intent;
import android.view.View;

import com.zch.libbase.base.BaseActivity;

import butterknife.OnClick;

public class StartActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_start;
    }

    @Override
    protected void init() {
        startActivity(new Intent(this, com.zch.rxjava.getstarted.MainActivity.class));
        finish();
    }

    @OnClick({R.id.get_started})
    public void onClickEvent(View v) {
        int id = v.getId();
        if (id == R.id.get_started) {
            startActivity(new Intent(this, com.zch.rxjava.getstarted.MainActivity.class));
        }
    }

}
