package com.hut.cwp.mcar.way.activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hut.cwp.mcar.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity2 extends Activity {

    private ImageButton returnon;

    private ImageButton registerfinish;

    private EditText accountEdit;

    private EditText passwordEdit;

    private EditText password2Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way_layout_rigister2);

        returnon = (ImageButton) findViewById(R.id.bu_return3);
        registerfinish =  (ImageButton) findViewById(R.id.bu_register_finish);
        accountEdit = (EditText) findViewById(R.id.account_rigister_Text);
        passwordEdit = (EditText) findViewById(R.id.password_rigister_Text);
        password2Edit = (EditText) findViewById(R.id.password2_rigister_Text);

        registerfinish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String password2 = password2Edit.getText().toString();

                if (account.equals("") ||  password.equals("") || password2.equals("")) {
                    Toast.makeText(RegisterActivity2.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
                //Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                //startActivity(intent);
                //finish();

                else if (password.equals(password2)) {
                    //BmobUser user = new BmobUser();

                    BmobUser bu = new BmobUser();
                    bu.setUsername(account);
                    bu.setPassword(password);
                    //bu.setEmail("sendi@163.com");
//注意：不能用save方法进行注册
                    bu.signUp(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser s, BmobException e) {
                            if(e==null){
                                //toast("注册成功:" +s.toString());
                                Toast.makeText(RegisterActivity2.this, "添加数据成功："+s.toString(), Toast.LENGTH_SHORT).show();
                            }else{
                                //loge(e);
                                Toast.makeText(RegisterActivity2.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    /*User user = new User();
                    user.setAccount(account);
                    user.setPassword(password);
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if(e==null){
                                //toast("添加数据成功，返回objectId为："+objectId);
                                Toast.makeText(RegisterActivity2.this, "添加数据成功，返回objectId为："+objectId, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity2.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                //toast("创建数据失败：" + e.getMessage());
                                Toast.makeText(RegisterActivity2.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/

                }
                else  {
                    Toast.makeText(RegisterActivity2.this, "两次输入密码不符合", Toast.LENGTH_SHORT).show();
                }


            }


        });

        returnon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity2.this,RegisterActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }


}