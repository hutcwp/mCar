package com.hut.cwp.mcar.activitys.user;

import android.content.Intent;
import android.os.Bundle;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.app.MyApplication;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.databinding.WayActivityUserBinding;


public class UserActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.way_activity_user;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        WayActivityUserBinding binding1 = (WayActivityUserBinding) getBinding();

        //获取当前用户名
        binding1.tvUsername.setText(MyApplication.getUsername());
        //设置标题
        binding1.title.tvTitle.setText(R.string.account);
        //设置返回
        binding1.title.btnBack.setOnClickListener(v -> finish());
        binding1.lyResetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void loadData() {

    }



}
