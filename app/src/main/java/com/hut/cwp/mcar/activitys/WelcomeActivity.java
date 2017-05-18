package com.hut.cwp.mcar.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.activitys.map.BNMainActivity;
import com.hut.cwp.mcar.databinding.WayActivityWelcomeBinding;
import com.hut.cwp.mcar.activitys.user.LoginActivity;
import com.hut.cwp.mcar.activitys.user.RegisterActivity;

/**
 * 欢迎界面,app入口
 * Created by Administrator on 2016/10/9 0009.
 */

public class WelcomeActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.way_activity_welcome;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        WayActivityWelcomeBinding binding1 = (WayActivityWelcomeBinding) getBinding();


        binding1.btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
//            finish();
        });

        binding1.btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
//            finish();
        });

    }

    @Override
    protected void loadData() {

    }


    /**
     * 随便看看，不需要登录
     *
     * @param v 布局
     */
    public void broswer(View v) {
        Intent intent = new Intent(WelcomeActivity.this, BNMainActivity.class);
        startActivity(intent);
        //finish();
    }

}
