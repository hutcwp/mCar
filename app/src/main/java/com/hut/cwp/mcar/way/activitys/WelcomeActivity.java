package com.hut.cwp.mcar.way.activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.cwp.activity.BNDemoMainActivity;

/**
 * Created by Administrator on 2016/10/9 0009.
 */

public class WelcomeActivity extends Activity {

    private Button login;

    private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way_activity_welcome);

        register =  (Button) findViewById(R.id.bu_register);
        login =  (Button) findViewById(R.id.bu_login);

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,RegisterSMSActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void kankan(View v) {
        Intent intent = new Intent(WelcomeActivity.this,BNDemoMainActivity.class);
        startActivity(intent);
        finish();
    }
}
