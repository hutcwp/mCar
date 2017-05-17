package com.hut.cwp.mcar.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.WelcomeActivity;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.databinding.WayActivityRigisterSmsBinding;
import com.hut.cwp.mcar.utils.ProxyLodingProgress;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class RegisterSMSActivity extends BaseActivity {


    private WayActivityRigisterSmsBinding Binding;

    @Override
    protected int getLayoutId() {
        return R.layout.way_activity_rigister_sms;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Binding = (WayActivityRigisterSmsBinding) getBinding();

        //初始化Bomb信息模块
        BmobSMS.initialize(this, "a248d73a620fa5f2458692545f8bc07a");


        Binding.btnRegister.setOnClickListener(v -> register());

        Binding.btnSend.setOnClickListener(v -> sendMessage());

        Binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterSMSActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * 发送短信验证码
     */
    private void sendMessage() {
        //用户手机号
        String tel = Binding.edtTelNumber.getText().toString();

        BmobSMS.requestSMSCode(RegisterSMSActivity.this, tel, "您的验证码是`%smscode%`，有效期为`%ttl%`分钟。您正在使用`%appname%`的验证码。【比目科技】", new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                // TODO Auto-generated method stub
                if (ex == null) {//验证码发送成功
                    Toast.makeText(RegisterSMSActivity.this, "短信发送成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterSMSActivity.this, "短信发送失败，请检查输入信息或重试！", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterSMSActivity.this, "错误码 = " + ex.getErrorCode() + ",errorMsg = " + ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /***
     * 注册
     */
    private void register() {
        String massage = Binding.edtCode.getText().toString();
        final String tel = Binding.edtTelNumber.getText().toString();

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

    @Override
    protected void loadData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProxyLodingProgress.destroy();
    }
}
