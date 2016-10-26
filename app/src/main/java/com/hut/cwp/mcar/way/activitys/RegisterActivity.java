package com.hut.cwp.mcar.way.activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hut.cwp.mcar.R;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class RegisterActivity extends Activity {

    private ImageButton returnon;

    private ImageButton registergo;

    private Button putmassage;

    private EditText massageEdit;

    private EditText telEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way_layout_rigister);

        BmobSMS.initialize(this, "2a2398211c95ae98184d1bc6570a1b40");

        returnon = (ImageButton) findViewById(R.id.bu_return2);
        registergo =  (ImageButton) findViewById(R.id.bu_register_go);
        putmassage= (Button) findViewById(R.id.bu_massage);
        massageEdit = (EditText) findViewById(R.id.massage_rigister_Text);
        telEdit = (EditText) findViewById(R.id.tel_rigister_Text);


        registergo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String massage = massageEdit.getText().toString();
                final String tel = telEdit.getText().toString();


                if (massage.equals("") || tel.equals("") || tel.length()!=11) {
                    Toast.makeText(RegisterActivity.this, "手机或验证码输入不合法", Toast.LENGTH_SHORT).show();
                }
                else{
                    //判断验证码是否正确、
                    BmobSMS.verifySmsCode(RegisterActivity.this, tel, massage, new VerifySMSCodeListener() {
                        @Override
                        public void done(BmobException ex) {
                            if (ex == null) {

                                Toast.makeText(RegisterActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
                                intent.putExtra("account",tel);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });


                }


            }


        });

        putmassage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String tel = telEdit.getText().toString();

                BmobSMS.requestSMSCode(RegisterActivity.this, tel, "BmobSMS.initialize(this, \"ab032408ad55b67fa3e389c959b482cf\");",new RequestSMSCodeListener() {

                    @Override
                    public void done(Integer smsId,BmobException ex) {
                        // TODO Auto-generated method stub
                        if(ex==null){//验证码发送成功
                            Toast.makeText(RegisterActivity.this, "短信发送成功，短信id："+smsId, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "errorCode = "+ex.getErrorCode()+",errorMsg = "+ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //发送验证码
                //SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //String sendTime = format.format(new Date());
                /*BmobSMS.requestSMS(RegisterActivity.this, tel, "BmobSMS.initialize(this, \"ab032408ad55b67fa3e389c959b482cf\");",null,new RequestSMSCodeListener() {

                    @Override
                    public void done(Integer smsId, BmobException ex) {
                        // TODO Auto-generated method stub
                        if(ex==null){//
                            //Log.i("bmob","短信发送成功，短信id："+smsId);
                            Toast.makeText(RegisterActivity.this, "短信发送成功，短信id："+smsId, Toast.LENGTH_SHORT).show();
                            //用于查询本次短信发送详情
                            /*putmassage.setClickable(false);
                            putmassage.setBackgroundColor(Color.GRAY);

                            new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    //putmassage.setBackgroundResource(R.drawable.button_shape02);
                                    putmassage.setText(millisUntilFinished / 1000 + "秒");
                                }

                                @Override
                                public void onFinish() {
                                    putmassage.setClickable(true);
                                    //putmassage.setBackgroundResource(R.drawable.button_shape);
                                    putmassage.setText("重新发送");
                                }
                            }.start();*/
                        /*}else{
                            Log.i("bmob","errorCode = "+ex.getErrorCode()+",errorMsg = "+ex.getLocalizedMessage());
                            Toast.makeText(RegisterActivity.this, "errorCode = "+ex.getErrorCode()+",errorMsg = "+ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

            }
        });

        returnon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
