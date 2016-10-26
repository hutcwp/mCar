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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class RePasswordActivity2 extends Activity {

    private EditText oldpasswordEdit;

    private EditText newpasswordEdit;

    private EditText newpassword2Edit;

    private ImageButton repassword;

    private ImageButton returnon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way_layout_repassword2);

        oldpasswordEdit = (EditText) findViewById(R.id.oldpassword_repassword_Text);
        newpasswordEdit = (EditText) findViewById(R.id.newpassword_repassword_Text);
        newpassword2Edit = (EditText) findViewById(R.id.newpassword2_repassword_Text);
        repassword = (ImageButton) findViewById(R.id.bu_repassword_finish);
        returnon =  (ImageButton) findViewById(R.id.bu_return10);

        repassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String oldpassword = oldpasswordEdit.getText().toString().trim();
                String newpassword = newpasswordEdit.getText().toString().trim();
                String newpassword2 = newpassword2Edit.getText().toString().trim();

                if (TextUtils.isEmpty(oldpassword)||TextUtils.isEmpty(newpassword)||TextUtils.isEmpty(newpassword2)) {
                    Toast.makeText(RePasswordActivity2.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
                else if(newpassword.length()<6){
                    Toast.makeText(RePasswordActivity2.this, "密码长度不能小于六个字符", Toast.LENGTH_SHORT).show();
                }
                else if (newpassword.equals(newpassword2)){

                    BmobUser.updateCurrentUserPassword(oldpassword, newpassword, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //toast("密码修改成功，可以用新密码进行登录啦");
                                Toast.makeText(RePasswordActivity2.this, "密码修改成功", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(RePasswordActivity2.this, BNDemoMainActivity.class);
//                                startActivity(intent);
                                finish();
                            } else {
                                //toast("失败:" + e.getMessage());
                                Toast.makeText(RePasswordActivity2.this, "密码修改失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                }
                else{
                    Toast.makeText(RePasswordActivity2.this, "两次输入新密码不符合", Toast.LENGTH_SHORT).show();
                }

            }
        });


        returnon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RePasswordActivity2.this,RepasswordActivity.class);
                startActivity(intent);
                finish();

            }
        });




    }
}
