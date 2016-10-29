package com.hut.cwp.mcar.cwp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.utils.ProxyLodingProgress;
import com.hut.cwp.mcar.cwp.view.CircleProgressBar;

public class CheckActivity extends Activity {

    private CircleProgressBar circleProgressBar;

    private int totalProgress = 100;
    private int currentProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cwp_activity_check);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        circleProgressBar = (CircleProgressBar) findViewById(R.id.circleProgressbar);


        ProxyLodingProgress.show(CheckActivity.this);


        new Thread(new ProgressRunable()).start();


    }

    class ProgressRunable implements Runnable {
        @Override
        public void run() {
            while (currentProgress < totalProgress) {
                currentProgress += 1;
                circleProgressBar.setProgress(currentProgress);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
