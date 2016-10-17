package com.hut.cwp.mcar.cwp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.hut.cwp.mcar.R;

public class MainActivity extends AppCompatActivity {

    ImageButton btn;

    LinearLayout layout;
    LinearLayout layout_a;

    LinearLayout.LayoutParams lp;

    private final static int MOVE_WIDTH = 20;

    int mTopMargin = 0;
    int mMaxTopMargin = 600;

    float startY;
    float currentY;
    float sY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPanel();
    }


    public void initPanel() {

        btn = (ImageButton) findViewById(R.id.btn);

        layout_a = (LinearLayout) findViewById(R.id.one);
        layout = (LinearLayout) findViewById(R.id.two_layout);


        lp = (LinearLayout.LayoutParams) layout.getLayoutParams();




        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("MOVE", "***************************");
                        startY = event.getRawY();
                        sY = startY;
                        Log.e("MOVE", "**startY**" + startY);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        currentY = event.getRawY();
                        Log.e("MOVE", "**currentY**" + currentY + "**startY**" + startY);
                        float dy = currentY - startY;

                        scroll(dy);

                        startY = currentY;

                        Log.e("MOVE", "**dy**" + dy + "**currentY**" + currentY + "**startY**" + startY);
                        break;
                    case MotionEvent.ACTION_UP:

                        if (Math.abs(currentY - sY) > 8) {

                            if (mTopMargin < mMaxTopMargin && mTopMargin > (mMaxTopMargin / 2)) {
                                //下滑

                                new AsynMove().execute(new Integer[]{MOVE_WIDTH});// 正数展开,向下

                            } else if ((mTopMargin > 0 && mTopMargin <= (mMaxTopMargin / 2))) {
                                //上滑

                                new AsynMove().execute(new Integer[]{-MOVE_WIDTH});// 正数展开,向下

                            }

                            return true;
                        }

                        break;
                }
                return false;

            }
        });

    }


    public void scroll(float positionOffset) {

        if (mTopMargin < mMaxTopMargin && positionOffset > 0) {
            Log.e("scroll", "Down" + mTopMargin);

            mTopMargin += (positionOffset);
            lp.topMargin = mTopMargin;
            layout.setLayoutParams(lp);
        } else if (mTopMargin > 0 && positionOffset < 0) {
            Log.e("scroll", "Up" + mTopMargin);

            mTopMargin += (positionOffset);
            lp.topMargin = mTopMargin;
            layout.setLayoutParams(lp);
        }

    }


    class AsynMove extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int times = 0;

            if (mTopMargin < mMaxTopMargin / 2) {

                if (mTopMargin % Math.abs(params[0]) == 0)// 整除
                    times = mTopMargin / Math.abs(params[0]);
                else
                    // 有余数
                    times = mTopMargin / Math.abs(params[0]) + 1;
                Log.e("doInBackground", "Up" + times);
            } else if (mTopMargin >= mMaxTopMargin / 2) {
                if ((mMaxTopMargin - mTopMargin) % Math.abs(params[0]) == 0)// 整除
                    times = mTopMargin / Math.abs(params[0]);
                else
                    // 有余数
                    times = (mMaxTopMargin - mTopMargin) / Math.abs(params[0]) + 1;
                Log.e("doInBackground", "Down" + times);
            }


            for (int i = 0; i < times; i++) {
                publishProgress(params);
                try {
                    Thread.sleep(Math.abs(params[0]));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... params) {

            mTopMargin += params[0];
            lp.topMargin = mTopMargin;
            layout.setLayoutParams(lp);
        }
    }

}
