package com.hut.cwp.mcar.way.activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.application.MyApplication;
import com.hut.cwp.mcar.base.utils.ProxyLodingProgress;
import com.hut.cwp.mcar.cwp.activity.BNDemoMainActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends Activity {

    private ImageButton returnon;

    private ImageButton registerfinish;

    private EditText passwordEdit;

    private EditText password2Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way_activity_rigister);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        returnon = (ImageButton) findViewById(R.id.bu_return3);
        registerfinish = (ImageButton) findViewById(R.id.bu_register_finish);
        passwordEdit = (EditText) findViewById(R.id.password_rigister_Text);
        password2Edit = (EditText) findViewById(R.id.password2_rigister_Text);

        registerfinish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String password = passwordEdit.getText().toString();
                String password2 = password2Edit.getText().toString();

                if (password.equals("") || password2.equals("")) {

                    Toast.makeText(RegisterActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {

                    Toast.makeText(RegisterActivity.this, "密码长度不能小于六个字符", Toast.LENGTH_SHORT).show();

                } else if (password.equals(password2)) {

                    ProxyLodingProgress.show(RegisterActivity.this);

                    final String username = getIntent().getStringExtra("account");

                    BmobUser bu = new BmobUser();

                    bu.setUsername(username);
                    bu.setPassword(password);

                    //注意：不能用save方法进行注册
                    bu.signUp(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser s, BmobException e) {
                            if (e == null) {

                                Toast.makeText(RegisterActivity.this, "注册成功：" + s.toString() + "\n自动登录", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, BNDemoMainActivity.class));
                                MyApplication.setLandState(MyApplication.HAD_LANDED);
                                MyApplication.setUsername(username);
                                finish();
                            } else {

                                Toast.makeText(RegisterActivity.this, "注册失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            ProxyLodingProgress.hide();
                        }
                    });
                } else {

                    Toast.makeText(RegisterActivity.this, "两次输入密码不符合", Toast.LENGTH_SHORT).show();
                }


            }


        });

        returnon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ProxyLodingProgress.destroy();
    }
}