package com.hut.cwp.mcar.way.activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.application.MyApplication;
import com.hut.cwp.mcar.cwp.activity.BNDemoMainActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends Activity {


    private EditText accountEdit;

    private EditText passwordEdit;

    private ImageButton login;

    private ImageButton returnon;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way_layout_login);


        accountEdit = (EditText) findViewById(R.id.login_account);
        passwordEdit = (EditText) findViewById(R.id.login_password);
        login = (ImageButton) findViewById(R.id.bu_login_in);
        returnon =  (ImageButton) findViewById(R.id.bu_return1);


        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String account2 = accountEdit.getText().toString().trim();
                String password2 = passwordEdit.getText().toString().trim();
                if (TextUtils.isEmpty(account2)||TextUtils.isEmpty(password2)) {
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    BmobUser bu2 = new BmobUser();
                    bu2.setUsername(account2);
                    bu2.setPassword(password2);

                    bu2.login(new SaveListener<BmobUser>() {

                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {

                            if(e==null){
                                try {
                                    if (!"FROM_BNDemoMainActivity".equals(getIntent().getStringExtra("TAG"))) {
                                        startActivity(new Intent(LoginActivity.this,BNDemoMainActivity.class));
                                    }
                                } catch (NullPointerException e1) {
                                    e1.printStackTrace();
                                }
                                MyApplication.setLandState(MyApplication.HAD_LANDED);
                                MyApplication.setUsername(account2);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

        returnon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (!"FROM_BNDemoMainActivity".equals(getIntent().getStringExtra("TAG"))) {
                        Intent intent = new Intent(LoginActivity.this,WelcomeActivity.class);
                        startActivity(intent);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }

}





