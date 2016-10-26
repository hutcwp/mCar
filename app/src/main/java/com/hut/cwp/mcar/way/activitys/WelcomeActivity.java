package com.hut.cwp.mcar.way.activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.cwp.activity.BNDemoMainActivity;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2016/10/9 0009.
 */

public class WelcomeActivity extends Activity {

    private Button login;

    private Button register;

    private ImageButton kankan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way_layout_welcome);

        Bmob.initialize(this, "ab032408ad55b67fa3e389c959b482cf");



        register =  (Button) findViewById(R.id.bu_register);
        login =  (Button) findViewById(R.id.bu_login);
        kankan = (ImageButton)findViewById(R.id.bu_kankan);

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeActivity.this,RegisterActivity.class);
                startActivity(intent);
                //finish();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                //finish();

            }
        });

        kankan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeActivity.this,BNDemoMainActivity.class);
                startActivity(intent);
                //finish();

            }
        });


    }
}
