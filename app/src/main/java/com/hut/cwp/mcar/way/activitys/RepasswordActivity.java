package com.hut.cwp.mcar.way.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.application.MyApplication;


public class RepasswordActivity extends Activity {

    private RelativeLayout repassword;

    private ImageView btn_back;
    private TextView text_content,mTvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__user);

        init();

        repassword= (RelativeLayout) findViewById(R.id.bu_repassword);

        repassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepasswordActivity.this,RePasswordActivity2.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void init() {
        text_content = (TextView) findViewById(R.id.menu_title_text_content);
        btn_back = (ImageView) findViewById(R.id.menu_title_btn_back);
        mTvUsername= (TextView) findViewById(R.id.tv_re_password_username);
        mTvUsername.setText(MyApplication.getUsername());

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_content.setText("帐号");
    }
}
