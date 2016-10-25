package com.hut.cwp.mcar.cwp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hut.cwp.mcar.R;

public class Activity_feedBack extends Activity {

    private ImageView btn_back;
    private TextView text_content;

    private Button btn_push;

    private EditText user_mail_id;
    private EditText user_suggest_content;

    String string_user_mail;
    String string_user_suggest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        init();
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


    }

    void init() {

        btn_push = (Button) findViewById(R.id.push);

        user_mail_id = (EditText) findViewById(R.id.user_mail_id);
        user_suggest_content = (EditText) findViewById(R.id.user_suggest_content);


        text_content = (TextView) findViewById(R.id.menu_title_text_content);
        btn_back = (ImageView) findViewById(R.id.menu_title_btn_back);


        text_content.setText("反馈");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                string_user_mail = user_mail_id.getText().toString().trim();
//                string_user_suggest = user_suggest_content.getText().toString().trim();
//
//                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                intent.setData(Uri.parse("mailto:hutcwp@163.com"));
//                intent.putExtra(Intent.EXTRA_SUBJECT, "用户反馈");
//                intent.putExtra(Intent.EXTRA_TEXT, "反馈意见来自于：" + string_user_mail +
//                        "  内容为：" + string_user_suggest);
//                startActivity(intent);

                setContentView(R.layout.activity_feedback_result);
            }
        });

    }

}
