package com.hut.cwp.mcar.cwp.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.cwp.view.CircleProgressBar;

public class CheckActivity extends Activity {

    private CircleProgressBar circleProgressBar;

    private Runnable mProgressRunable;

    private boolean isRunning;

    private ImageView img_check, img_check_result;


    private int totalProgress = 100;
    private int currentProgress;


    private RelativeLayout layout_result, layout_check;

    private LinearLayout layout_touch_result, layout_touch_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cwp_activity_check);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        initFinvViewById();

        initLisetner();


    }

    private void initFinvViewById() {

        circleProgressBar = (CircleProgressBar) findViewById(R.id.circleProgressbar);

        mProgressRunable = new ProgressRunable();

        img_check = (ImageView) findViewById(R.id.img_check);
        img_check_result = (ImageView) findViewById(R.id.img_check_result);


        layout_result = (RelativeLayout) findViewById(R.id.layout_result);
        layout_check = (RelativeLayout) findViewById(R.id.layout_check);

        layout_touch_result = (LinearLayout) findViewById(R.id.layout_touch_result);
        layout_touch_check = (LinearLayout) findViewById(R.id.layout_touch_check);


    }

    private void initLisetner() {

        layout_touch_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_check.setImageResource(R.drawable.cwp_acticity_check);
                img_check_result.setImageResource(R.drawable.cwp_activity_check_result_press);
                layout_result.setVisibility(View.VISIBLE);
                layout_check.setVisibility(View.GONE);
            }
        });

        layout_touch_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_check.setImageResource(R.drawable.cwp_activity_check_press);
                img_check_result.setImageResource(R.drawable.cwp_activity_check_result);
                layout_check.setVisibility(View.VISIBLE);
                layout_result.setVisibility(View.GONE);
            }
        });

        circleProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isRunning) {
                    currentProgress = 0;
                    new Thread(mProgressRunable).start();
                }
            }
        });

    }


    class ProgressRunable implements Runnable {
        @Override
        public void run() {

            isRunning = true;

            while (currentProgress < totalProgress) {

                currentProgress += 1;
                circleProgressBar.setProgress(currentProgress);

                try {

                    Thread.sleep(100);
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            isRunning = false;
        }
    }


}
