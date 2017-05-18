package com.hut.cwp.mcar.activitys.user;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.WelcomeActivity;
import com.hut.cwp.mcar.app.Globel;
import com.hut.cwp.mcar.app.App;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.utils.EncryptionUtil;
import com.hut.cwp.mcar.utils.ProxyLodingProgress;
import com.hut.cwp.mcar.activitys.map.BNMainActivity;
import com.hut.cwp.mcar.databinding.WayActivityLoginBinding;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hutcwp on 2017/5/10.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 * 登录Activity
 */

public class LoginActivity extends BaseActivity {


    private WayActivityLoginBinding Binding;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected int getLayoutId() {
        return R.layout.way_activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Binding = (WayActivityLoginBinding) getBinding();


        Binding.btnLogin.setOnClickListener(v -> login());

        Binding.btnBack.setOnClickListener(v -> {
            try {
                if (!Globel.From.equals(getIntent().getStringExtra("TAG"))) {
                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            finish();
        });
    }

    @Override
    protected void loadData() {

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = pref.getString("username", "");

        if (!TextUtils.isEmpty(username)) {
            String password = pref.getString("password", "");
            Binding.edtAccount.setText(username);
            Binding.edtPassword.setText(password);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProxyLodingProgress.destroy();
    }

    private void login() {

        final String account2 = Binding.edtAccount.getText().toString().trim();
        final String password2 = Binding.edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(account2) || TextUtils.isEmpty(password2)) {
            Toast.makeText(LoginActivity.this, R.string.accout_and_passwod_can_not_null, Toast.LENGTH_SHORT).show();
        } else {
            BmobUser bu2 = new BmobUser();
            bu2.setUsername(account2);
            bu2.setPassword(EncryptionUtil.encryByMD5(password2));

            ProxyLodingProgress.show(LoginActivity.this);

            bu2.login(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if (e == null) {
                        try {
                            if (!"FROM_BNDemoMainActivity".equals(getIntent().getStringExtra("TAG"))) {
                                startActivity(new Intent(LoginActivity.this, BNMainActivity.class));
                            }
                        } catch (NullPointerException e1) {
                            e1.printStackTrace();
                        }
                        editor = pref.edit();
                        editor.putString("username", account2);
                        editor.putString("password", password2);
                        editor.apply();
                        App.setLandState(App.HAD_LANDED);
                        App.setUsername(account2);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.login_failure) + e, Toast.LENGTH_SHORT).show();
                    }
                    ProxyLodingProgress.hide();
                }
            });

        }

    }
}





