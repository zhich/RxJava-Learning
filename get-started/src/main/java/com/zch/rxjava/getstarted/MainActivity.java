package com.zch.rxjava.getstarted;

import android.content.Intent;
import android.view.View;

import com.zch.libbase.base.BaseActivity;
import com.zch.rxjava.getstarted.nesting_network_request.UserActivity;
import com.zch.rxjava.getstarted.unconditional_network_request_polling.GetTranslationActivity;

import butterknife.OnClick;

/**
 * Created by zch on 2018/5/2.
 */

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

    }

    @OnClick({R2.id.get_started, R2.id.create_operators, R2.id.convert_operators,
            R2.id.combined_merge_operators,
            R2.id.unconditional_network_request_polling, R2.id.nesting_network_request})
    public void onClickEvent(View v) {
        int id = v.getId();
        if (id == R.id.get_started) {
            startActivity(new Intent(this, GetStartedActivity.class));
        } else if (id == R.id.create_operators) {
            startActivity(new Intent(this, CreateOperatorsActivity.class));
        } else if (id == R.id.convert_operators) {
            startActivity(new Intent(this, ConvertOperatorsActivity.class));
        } else if (id == R.id.combined_merge_operators) {
            startActivity(new Intent(this, CombinedMergeOperatorsActivity.class));
        } else if (id == R.id.unconditional_network_request_polling) {
            startActivity(new Intent(this, GetTranslationActivity.class));
        } else if (id == R.id.nesting_network_request) {
            startActivity(new Intent(this, UserActivity.class));
        }
    }
}