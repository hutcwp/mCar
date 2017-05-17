package com.hut.cwp.mcar.activitys.other;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.app.MyApplication;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.databinding.ZActivityFeedbackBinding;
import com.hut.cwp.mcar.activitys.business.bean.FeedbackBean;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class FeedbackActivity extends BaseActivity {

    private boolean canBack = true;

    private ZActivityFeedbackBinding Binding;

    private int mLandState = MyApplication.getLandState();

    @Override
    protected int getLayoutId() {
        return R.layout.z_activity_feedback;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Binding = (ZActivityFeedbackBinding) getBinding();

        init();
    }

    @Override
    protected void loadData() {

        if (mLandState == MyApplication.NO_LAND) {
            Binding.tvPhoneNumber.setVisibility(View.VISIBLE);
        }
    }


    private void init() {

        Binding.btnBack.setOnClickListener(v -> {
            if (canBack) finish();
        });


        Binding.btCommit.setOnClickListener(v -> {

            String phone = getPhone();
            String content = Binding.tvContent.getText().toString();

            if (TextUtils.isEmpty(content)) {
                Toast.makeText(FeedbackActivity.this, "反馈或意见内容均不能为空！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(phone)) {
                Toast.makeText(FeedbackActivity.this, "填一个手机号吧，我们不想与您失联！", Toast.LENGTH_SHORT).show();
            } else {
                commitFeedback(phone, content);
            }
        });
    }

    /**
     * 向服务器 提交反馈
     * @param phone 手机号
     * @param content 内容
     */
    private void commitFeedback(String phone, String content) {

        canBack = false;
        Binding.rlLoading.setVisibility(View.VISIBLE);

        FeedbackBean feedbackBean = new FeedbackBean();
        feedbackBean.setPhone(phone);
        feedbackBean.setContent(content);

        feedbackBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(FeedbackActivity.this, "提交成功,感谢您的参与!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FeedbackActivity.this, FeedbackSuccessActivity.class));
                    finish();
                } else {
                    Toast.makeText(FeedbackActivity.this, "提交失败,请稍后再试!", Toast.LENGTH_SHORT).show();
                    canBack = true;
                    Binding.rlLoading.post(() -> Binding.rlLoading.setVisibility(View.GONE));
                }
            }
        });
    }

    /**
     * 获取电话号码
     *
     * @return phone 号码
     */
    private String getPhone() {

        String phone = null;
        //没有登陆
        if (mLandState == MyApplication.NO_LAND) {
            phone = Binding.tvPhoneNumber.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(FeedbackActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                phone = null;
            } else {
                if (phone.length() != 11) {
                    Toast.makeText(FeedbackActivity.this, "电话号码不合规范", Toast.LENGTH_SHORT).show();
                    phone = null;
                }
            }
        } else if (mLandState == MyApplication.HAD_LANDED) {
            phone = MyApplication.getUsername();
        }
        return phone;
    }

    @Override
    public void onBackPressed() {
        if (canBack) finish();
    }


}
