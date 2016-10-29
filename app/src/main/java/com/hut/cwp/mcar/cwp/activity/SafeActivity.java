package com.hut.cwp.mcar.cwp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hut.cwp.mcar.R;

public class SafeActivity extends Activity {

    private ImageView btn_back;
    private TextView text_content;
    private Intent intent;


    private View item_one, item_two, item_three, item_four, item_five;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cwp_activity_safe);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        init();


    }

    void init() {

        text_content = (TextView) findViewById(R.id.menu_title_text_content);
        btn_back = (ImageView) findViewById(R.id.menu_title_btn_back);

        item_one = findViewById(R.id.item_one);
        item_two = findViewById(R.id.item_two);
        item_three = findViewById(R.id.item_three);
        item_four = findViewById(R.id.item_four);
        item_five = findViewById(R.id.item_five);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_content.setText("车辆保险");

        item_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //太平洋保险
                url = "http://www.ecpic.com.cn/cxzq/2014gb/sy/";
                startActivityWithUrl(url);

            }
        });

        item_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //中国人民保险
                url = "http://www.epicc.com.cn/";
                startActivityWithUrl(url);
            }
        });

        item_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //亚太财产
                url = "http://www.apiins.com/maechannel/carInsurance.do";
                startActivityWithUrl(url);

            }
        });

        item_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //阳光
                url = "http://www.sinosig.com/";
                startActivityWithUrl(url);

            }
        });

        item_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //中华车险
                url = "http://e.cic.cn/";
                startActivityWithUrl(url);

            }
        });

    }


    private void startActivityWithUrl(String url) {

        intent = new Intent(SafeActivity.this, WebViewContentActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }

}
