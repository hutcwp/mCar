package com.hut.cwp.mcar.cwp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hut.cwp.mcar.R;

/**
 * Created by Adminis on 2016/10/16.
 */

public class MyPanel extends LinearLayout {

    ImageButton btn;
    TextView text;

    LinearLayout layout;
    LinearLayout layout_a;

    LayoutInflater inflater;

    ViewGroup.LayoutParams lp;

    Context mContext;

    private final static int MOVE_WIDTH = 20;

    int mTopMargin = 0;
    int mMaxTopMargin = 600;

    float startY;
    float currentY;
    float sY;

    public MyPanel(Context context) {
        super(context);
    }

    public MyPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyPanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        init();
    }


    void init() {
        btn = (ImageButton) findViewById(R.id.btn);
//        text = (TextView) findViewById(R.id.text);

        layout_a = (LinearLayout) findViewById(R.id.one);
        layout = (LinearLayout) findViewById(R.id.two_layout);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        layout.setLayoutParams(llp);
//        inflater = LayoutInflater.from(mContext);
//        View view =inflater.inflate(R.layout.activity_main,null);
//
//        layout = (LinearLayout) view.findViewById(R.id.two_layout);
//
//


//        text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "OnClick", Toast.LENGTH_SHORT).show();
//            }
//        });

//        btn.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.e("MOVE", "***************************");
//                        startY = event.getRawY();
//                        sY = startY;
//                        Log.e("MOVE", "**startY**" + startY);
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
//
//                        currentY = event.getRawY();
//                        Log.e("MOVE", "**currentY**" + currentY + "**startY**" + startY);
//                        float dy = currentY - startY;
//
//                        scroll(dy);
//
//                        startY = currentY;
//
//                        Log.e("MOVE", "**dy**" + dy + "**currentY**" + currentY + "**startY**" + startY);
//                        break;
//                    case MotionEvent.ACTION_UP:
//
//                        if (Math.abs(currentY - sY) > 8) {
//
//                            if (mTopMargin < mMaxTopMargin && mTopMargin > (mMaxTopMargin / 2)) {
//                                //下滑
//
//                                new AsynMove().execute(new Integer[]{MOVE_WIDTH});// 正数展开,向下
//
//                            } else if ((mTopMargin > 0 && mTopMargin <= (mMaxTopMargin / 2))) {
//                                //上滑
//
//                                new AsynMove().execute(new Integer[]{-MOVE_WIDTH});// 正数展开,向下
//
//                            }
//
//                            return true;
//                        }
//
//                        break;
//                }
//                return false;
//
//            }
//        });

    }

//    public void scroll(float positionOffset) {
//
//        if (mTopMargin < mMaxTopMargin && positionOffset > 0) {
//            Log.e("scroll", "Down" + mTopMargin);
//
//            mTopMargin += (positionOffset);
//            lp.topMargin = mTopMargin;
//            layout.setLayoutParams(lp);
//        } else if (mTopMargin > 0 && positionOffset < 0) {
//            Log.e("scroll", "Up" + mTopMargin);
//
//            mTopMargin += (positionOffset);
//            lp.topMargin = mTopMargin;
//            layout.setLayoutParams(lp);
//        }
//
//        Log.e("TestForHListView", "positionOffset" + positionOffset);
//    }


    /*
       ** 获得屏幕宽度
        */
//    public int getScreenWidth() {
//
//        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics metrics = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(metrics);
//        return metrics.widthPixels;
//    }


//    class AsynMove extends AsyncTask<Integer, Integer, Void> {
//
//        @Override
//        protected Void doInBackground(Integer... params) {
//            int times = 0;
//
//            if (mTopMargin < mMaxTopMargin / 2) {
//
//                if (mTopMargin % Math.abs(params[0]) == 0)// 整除
//                    times = mTopMargin / Math.abs(params[0]);
//                else
//                    // 有余数
//                    times = mTopMargin / Math.abs(params[0]) + 1;
//                Log.e("doInBackground", "Up" + times);
//            } else if (mTopMargin >= mMaxTopMargin / 2) {
//                if ((mMaxTopMargin - mTopMargin) % Math.abs(params[0]) == 0)// 整除
//                    times = mTopMargin / Math.abs(params[0]);
//                else
//                    // 有余数
//                    times = (mMaxTopMargin - mTopMargin) / Math.abs(params[0]) + 1;
//                Log.e("doInBackground", "Down" + times);
//            }
//
//
//            for (int i = 0; i < times; i++) {
//                publishProgress(params);
//                try {
//                    Thread.sleep(Math.abs(params[0]));
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
////            if (mTopMargin < mMaxTopMargin / 2) {
////
////                mTopMargin = 0;
////                lp.topMargin = mTopMargin;
////                layout.setLayoutParams(lp);
////            } else if (mTopMargin >= mMaxTopMargin / 2) {
////
////                mTopMargin = mMaxTopMargin;
////                lp.topMargin = mTopMargin;
////                layout.setLayoutParams(lp);
////            }
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... params) {
//
////            mTopMargin += params[0];
////            lp.topMargin = mTopMargin;
////            layout.setLayoutParams(lp);
//        }
//    }
}
