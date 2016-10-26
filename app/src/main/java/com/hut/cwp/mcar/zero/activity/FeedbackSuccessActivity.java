package com.hut.cwp.mcar.zero.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hut.cwp.mcar.R;

public class FeedbackSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_feedback_success);
        getSupportActionBar().hide();
    }

    public void back1(View v) {
        finish();
    }

    public void again(View v) {
        startActivity(new Intent(this,FeedbackActivity.class));
        finish();
    }

    public void back2(View v) {
        finish();
    }
}
