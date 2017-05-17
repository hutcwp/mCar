package com.hut.cwp.mcar.activitys.other;

import android.content.Intent;
import android.os.Bundle;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.BaseActivity;

public class FeedbackSuccessActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.z_activity_feedback_success;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        findViewById(R.id.btn_back).setOnClickListener(view -> back());
        findViewById(R.id.btn_return).setOnClickListener(view -> back());
        findViewById(R.id.btn_feedback_again).setOnClickListener(view -> feedbakAgain());
    }

    @Override
    protected void loadData() {

    }

    /**
     * 再次留言
     */
    public void feedbakAgain() {
        startActivity(new Intent(this, FeedbackActivity.class));
        finish();
    }


    public void back() {
        finish();
    }
}
