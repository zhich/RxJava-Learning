package com.zch.libbase.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by zch on 2018/5/2.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initBeforeCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        int layoutResId = getLayoutResource();
        if (layoutResId != 0) {
            setContentView(layoutResId);
        }
        ButterKnife.bind(this);
        init();
    }

    private void initBeforeCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 设置手机屏幕的旋转不触发
    }

    protected abstract int getLayoutResource();

    protected abstract void init();
}
