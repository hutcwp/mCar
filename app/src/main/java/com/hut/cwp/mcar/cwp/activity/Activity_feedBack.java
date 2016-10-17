package com.hut.cwp.mcar.cwp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hut.cwp.mcar.R;

public class Activity_feedBack extends Activity {

    Button btn_push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        btn_push = (Button) findViewById(R.id.push);

        btn_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setContentView(R.layout.activity_feedback_result);
            }
        });


    }
}
