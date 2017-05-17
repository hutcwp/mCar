package com.hut.cwp.mcar.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.databinding.WayActivityResetPasswordBinding;
import com.hut.cwp.mcar.utils.ProxyLodingProgress;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class ResetPasswordActivity extends BaseActivity {


    private WayActivityResetPasswordBinding Binding;

    @Override
    protected int getLayoutId() {
        return R.layout.way_activity_reset_password;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Binding = (WayActivityResetPasswordBinding) getBinding();

        Binding.btnResetPassword.setOnClickListener(v -> resetPassword());


        Binding.btnBack.setOnClickListener(v -> {

            Intent intent = new Intent(ResetPasswordActivity.this, UserActivity.class);
            startActivity(intent);
            finish();

        });

    }

    /**
     * 重置密码
     *
     */
    private void resetPassword() {

        String oldPassword = Binding.edtOldPassword.getText().toString().trim();
        String newPassword = Binding.edtNewPassword.getText().toString().trim();
        String confirmPassword = Binding.edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(ResetPasswordActivity.this, R.string.not_null, Toast.LENGTH_SHORT).show();
        } else if (newPassword.length() < 6) {
            Toast.makeText(ResetPasswordActivity.this, R.string.password_length_less_than_six, Toast.LENGTH_SHORT).show();
        } else if (newPassword.equals(confirmPassword)) {

            ProxyLodingProgress.show(ResetPasswordActivity.this);

            BmobUser.updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //toast("密码修改成功，可以用新密码进行登录啦");
                        Toast.makeText(ResetPasswordActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(RePasswordActivity.this, BNDemoMainActivity.class);
//                                startActivity(intent);
                        finish();
                    } else {

                        Toast.makeText(ResetPasswordActivity.this, "密码修改失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    ProxyLodingProgress.hide();
                }

            });

        } else {
            Toast.makeText(ResetPasswordActivity.this, "两次输入新密码不符合", Toast.LENGTH_SHORT).show();
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
