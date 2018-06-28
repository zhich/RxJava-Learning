package com.zch.rxjava.getstarted;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zch.libbase.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by zch on 2018/5/2.
 */

public class MainActivity extends BaseActivity {

    @BindView(R2.id.recyclerview)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        ItemAdapter itemAdapter = new ItemAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(itemAdapter);
    }
}