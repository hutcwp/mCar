package com.hut.cwp.mcar.cwp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hut.cwp.mcar.R;

public class Activity_feedBack extends Activity {

    private ImageView btn_back;
    private TextView text_content;

    private Button btn_push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        init();


        btn_push = (Button) findViewById(R.id.push);

        btn_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setContentView(R.layout.activity_feedback_result);
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:+297890152@qq.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "这是单方发送的邮件主题");
                intent.putExtra(Intent.EXTRA_TEXT, "这是单方发送的邮件内容");
                startActivity(intent);

            }
        });


    }

    void init() {

        text_content = (TextView) findViewById(R.id.menu_title_text_content);
        btn_back = (ImageView) findViewById(R.id.menu_title_btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_content.setText("反馈");
    }

}
