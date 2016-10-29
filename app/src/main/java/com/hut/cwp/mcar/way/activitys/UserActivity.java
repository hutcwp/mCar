package com.hut.cwp.mcar.way.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.application.MyApplication;


public class UserActivity extends Activity {

    private RelativeLayout repassword;

    private ImageView btn_back;
    private TextView text_content,mTvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way_activity_user);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        init();

        repassword= (RelativeLayout) findViewById(R.id.bu_repassword);

        repassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this,RePasswordActivity.class);
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
