package com.hut.cwp.mcar.activitys.user;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.WelcomeActivity;
import com.hut.cwp.mcar.app.App;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.utils.EncryptionUtil;
import com.hut.cwp.mcar.utils.ProxyLodingProgress;
import com.hut.cwp.mcar.activitys.map.BNMainActivity;
import com.hut.cwp.mcar.databinding.WayActivityRigisterBinding;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hutcwp on 2017/5/10.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */

public class RegisterActivity extends BaseActivity {


    private WayActivityRigisterBinding Binding;

    @Override
    protected int getLayoutId() {
        return R.layout.way_activity_rigister;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Binding = (WayActivityRigisterBinding) getBinding();

        Binding.btnComplete.setOnClickListener(v -> {

            String password = Binding.edtPwd.getText().toString();
            String password2 = Binding.edtPwdConfirm.getText().toString();
            if (password.equals("") || password2.equals("")) {
                Toast.makeText(RegisterActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "密码长度不能小于六个字符", Toast.LENGTH_SHORT).show();
            } else if (password.equals(password2)) {
                showProgress();
                String username = getIntent().getStringExtra("account");
                BmobUser bu = new BmobUser();
                username = "tetst";
                password = EncryptionUtil.encryByMD5(password);
                Log.d("test", "ped" + password);
                if (password.equals("")) {
                    Toast.makeText(RegisterActivity.this, "密码格式错误", Toast.LENGTH_LONG).show();
                } else {
                    bu.setUsername(username);
                    bu.setPassword(password);
                    //注意：不能用save方法进行注册
                    String finalUsername = username;
                    bu.signUp(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(RegisterActivity.this, "注册成功：" + s.toString() + "\n自动登录", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, BNMainActivity.class));
                                App.setLandState(App.HAD_LANDED);
                                App.setUsername(finalUsername);
                                finish();
                            } else {

                                Toast.makeText(RegisterActivity.this, "注册失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            ProxyLodingProgress.hide();
                        }
                    });
                }
            } else {

                Toast.makeText(RegisterActivity.this, "两次输入密码不符合", Toast.LENGTH_SHORT).show();
            }

        });

        Binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProxyLodingProgress.destroy();
    }
}