package com.hut.cwp.mcar.way.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.utils.ProxyLodingProgress;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class RegisterSMSActivity extends Activity {

    private ImageButton returnon;

    private ImageButton registergo;

    private Button putmassage;

    private EditText massageEdit;

    private EditText telEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way_activity_rigister_sms);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        BmobSMS.initialize(this, "2a2398211c95ae98184d1bc6570a1b40");

        returnon = (ImageButton) findViewById(R.id.bu_return2);
        registergo = (ImageButton) findViewById(R.id.bu_register_go);
        putmassage = (Button) findViewById(R.id.bu_massage);
        massageEdit = (EditText) findViewById(R.id.massage_rigister_Text);
        telEdit = (EditText) findViewById(R.id.tel_rigister_Text);


        registergo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String massage = massageEdit.getText().toString();
                final String tel = telEdit.getText().toString();

                if (massage.equals("") || tel.equals("") || tel.length() != 11) {
                    Toast.makeText(RegisterSMSActivity.this, "手机或验证码输入不合法", Toast.LENGTH_SHORT).show();
                } else {
                    //判断验证码是否正确、

                    ProxyLodingProgress.show(RegisterSMSActivity.this);

                    BmobSMS.verifySmsCode(RegisterSMSActivity.this, tel, massage, new VerifySMSCodeListener() {
                        @Override
                        public void done(BmobException ex) {
                            if (ex == null) {

                                Toast.makeText(RegisterSMSActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterSMSActivity.this, RegisterActivity.class);
                                intent.putExtra("account", tel);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterSMSActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                            }
                            ProxyLodingProgress.hide();
                        }

                    });

                }

            }


        });

        putmassage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String tel = telEdit.getText().toString();

                BmobSMS.requestSMSCode(RegisterSMSActivity.this, tel, "BmobSMS.initialize(this, \"ab032408ad55b67fa3e389c959b482cf\");", new RequestSMSCodeListener() {

                    @Override
                    public void done(Integer smsId, BmobException ex) {
                        // TODO Auto-generated method stub
                        if (ex == null) {//验证码发送成功
                            Toast.makeText(RegisterSMSActivity.this, "短信发送成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterSMSActivity.this, "短信发送失败，请检查输入信息或重试！", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(RegisterSMSActivity.this, "错误码 = "+ex.getErrorCode()+",errorMsg = "+ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        returnon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterSMSActivity.this, WelcomeActivity.class);
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
