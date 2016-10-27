package com.hut.cwp.mcar.zero.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.application.MyApplication;
import com.hut.cwp.mcar.zero.bean.FeedbackBean;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

//TODO 需要删除掉老司机的反馈界面
public class FeedbackActivity extends AppCompatActivity {
    private TextView mTvContent,mTvPhone;
    private Button mBtCommit,mBtBack;
    private RelativeLayout mRlLoading;
    private int mLandState=MyApplication.getLandState();
    private boolean canBack=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_feedback);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        init();
    }

    private void init() {
        mRlLoading= (RelativeLayout) findViewById(R.id.rl_feedback_loading);

        mBtBack= (Button) findViewById(R.id.bt_feedback_back);
        mTvContent= (TextView) findViewById(R.id.tv_feedback_content);
        mTvPhone= (TextView) findViewById(R.id.tv_feedback_phone);
        mBtCommit= (Button) findViewById(R.id.bt_feedback_commit);

        if (mLandState==MyApplication.NO_LAND) {
            mTvPhone.setVisibility(View.VISIBLE);
        }

        mBtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canBack) finish();
            }
        });
        mBtCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                String phone=null;

                //没有登陆
                if (mLandState==MyApplication.NO_LAND) {
                    phone= mTvPhone.getText().toString();
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(FeedbackActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                        phone = null;
                    }
                    else {
                        if (phone.length()!=11) {
                            Toast.makeText(FeedbackActivity.this, "电话号码不合规范", Toast.LENGTH_SHORT).show();
                            phone=null;
                        }
                    }
                } else if(mLandState==MyApplication.HAD_LANDED){
                    phone=MyApplication.getUsername();
                }
                final String content=mTvContent.getText().toString();
                if (TextUtils.isEmpty(content))
                    Toast.makeText(FeedbackActivity.this, "反馈或意见内容不能为空", Toast.LENGTH_SHORT).show();
                else {
                    if (phone!=null) {
                        final String finalPhone = phone;
                        canBack=false;
                        mRlLoading.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                FeedbackBean feedbackBean=new FeedbackBean();
                                feedbackBean.setPhone(finalPhone);
                                feedbackBean.setContent(content);
                                feedbackBean.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e==null){
                                            Toast.makeText(FeedbackActivity.this, "提交成功,感谢您的参与!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(FeedbackActivity.this,FeedbackSuccessActivity.class));
                                            finish();
                                        }else{
                                            Toast.makeText(FeedbackActivity.this, "提交失败,请稍后再试!", Toast.LENGTH_SHORT).show();
                                            canBack=true;
                                            mRlLoading.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mRlLoading.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }).start();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (canBack) finish();
    }
}
