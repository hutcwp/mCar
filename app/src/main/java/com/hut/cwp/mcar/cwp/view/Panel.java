package com.hut.cwp.mcar.cwp.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Adminis on 2016/10/8.
 */

public class Panel extends LinearLayout implements GestureDetector.OnGestureListener {

    public interface PanelClosedEvent {
        void onPanelClosed(View panel);
    }

    public interface PanelOpenedEvent {
        void onPanelOpened(View panel);
    }

    private final static int HANDLE_WIDTH = 120;
    private final static int MOVE_WIDTH = 20;

    private Button btnHandler;
    private LinearLayout panelContainer;

    private int mTopMargin = 400;

    private Context mContext;
    private GestureDetector mGestureDetector;

    private boolean mIsScrolling = false;
    private float mScrollY;

    private PanelClosedEvent panelClosedEvent = null;
    private PanelOpenedEvent panelOpenedEvent = null;

    public Panel(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;
        this.setOrientation(LinearLayout.VERTICAL);
        //定义手势识别
        mGestureDetector = new GestureDetector(mContext, this);
        mGestureDetector.setIsLongpressEnabled(false);

//        LayoutParams lp = new LayoutParams(width, height);
//        lp.topMargin = -lp.height + HANDLE_WIDTH;
//        Log.i("Test", "height:" + height + "*topMargim" + lp.topMargin);
//        mTopMargin = Math.abs(lp.topMargin);
//        this.setLayoutParams(lp);

        //设置Handler的属性
        btnHandler = new Button(context);
        btnHandler.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HANDLE_WIDTH));
        //btnHandler.setOnClickListener(handlerClickEvent);
        btnHandler.setOnTouchListener(handlerTouchEvent);
        this.addView(btnHandler);

        //设置Container的属性
        panelContainer = new LinearLayout(context);
        panelContainer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        this.addView(panelContainer);
    }

    public Panel(Context context, AttributeSet attrs, int defStyleAttr, Button btnHandler) {
        super(context, attrs, defStyleAttr);

    }

    public Panel(Context context, View otherView, int width, int height) {
        super(context);
        this.mContext = context;
        this.setOrientation(LinearLayout.VERTICAL);
        //定义手势识别
        mGestureDetector = new GestureDetector(mContext, this);
        mGestureDetector.setIsLongpressEnabled(false);

        //改变Panel附近组件的属性
//        LayoutParams otherLP = (LayoutParams) otherView.getLayoutParams();
//        otherLP.weight = 1;
//        otherView.setLayoutParams(otherLP);

        //设置Panel本身的属性
        LayoutParams lp = new LayoutParams(width, height);
        lp.topMargin = -lp.height + HANDLE_WIDTH;
        Log.i("Test", "height:" + height + "*topMargim" + lp.topMargin);
//        mTopMargin = Math.abs(lp.topMargin);
        this.setLayoutParams(lp);


        //设置Handler的属性
        btnHandler = new Button(context);
        btnHandler.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HANDLE_WIDTH));
        //btnHandler.setOnClickListener(handlerClickEvent);
        btnHandler.setOnTouchListener(handlerTouchEvent);
        this.addView(btnHandler);

        //设置Container的属性
        panelContainer = new LinearLayout(context);
        panelContainer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        this.addView(panelContainer);

    }

    private View.OnTouchListener handlerTouchEvent = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP && //onScroll时的ACTION_UP
                    mIsScrolling == true) {
                LayoutParams lp = (LayoutParams) Panel.this.getLayoutParams();
                if (lp.topMargin >= (mTopMargin / 2)) {//往左超过一半
                    new AsynMove().execute(new Integer[]{-MOVE_WIDTH});// 正数展开
                } else if (lp.topMargin < (mTopMargin / 2)) {//往右拖拉
                    new AsynMove().execute(new Integer[]{MOVE_WIDTH});// 负数收缩
                }
            }
            return mGestureDetector.onTouchEvent(event);
        }
    };

    /**
     * 定义收缩时的回调函数
     *
     * @param event
     */
    public void setPanelClosedEvent(PanelClosedEvent event) {
        this.panelClosedEvent = event;
    }

    /**
     * 定义展开时的回调函数
     *
     * @param event
     */
    public void setPanelOpenedEvent(PanelOpenedEvent event) {
        this.panelOpenedEvent = event;
    }

    /**
     * 把View放在Panel的Container
     *
     * @param v
     */
    public void fillPanelContainer(View v) {

        panelContainer.addView(v);
    }

    /**
     * 异步移动Panel
     *
     * @author hellogv
     */
    class AsynMove extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int times;
            if (mTopMargin % Math.abs(params[0]) == 0)// 整除
                times = mTopMargin / Math.abs(params[0]);
            else
                // 有余数
                times = mTopMargin / Math.abs(params[0]) + 1;

            Log.i("Test", "****params****" + params[0]);

            Log.i("Test", times + "********" + mTopMargin);

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
            LayoutParams lp = (LayoutParams) Panel.this.getLayoutParams();
            if (params[0] < 0)
//                lp.topMargin = Math.max(lp.topMargin + params[0], (-mTopMargin));
                lp.topMargin = lp.topMargin + params[0];
            else
                lp.topMargin = lp.topMargin + params[0];
//                lp.topMargin = Math.min(lp.topMargin + params[0], 0);

            if (lp.topMargin == 0 && panelOpenedEvent != null) {//展开之后
                panelOpenedEvent.onPanelOpened(Panel.this);//调用OPEN回调函数
            } else if (lp.topMargin == -(mTopMargin) && panelClosedEvent != null) {//收缩之后
                panelClosedEvent.onPanelClosed(Panel.this);//调用CLOSE回调函数
            }
            Panel.this.setLayoutParams(lp);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mScrollY = 0;
        mIsScrolling = false;
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//        LayoutParams lp = (LayoutParams) Panel.this.getLayoutParams();
//        if (lp.topMargin < mTopMargin)// CLOSE的状态
//        {
//            new AsynMove().execute(new Integer[]{MOVE_WIDTH});// 正数展开,向下
//        } else if (lp.topMargin >= mTopMargin)// OPEN的状态
//        {
//            new AsynMove().execute(new Integer[]{-MOVE_WIDTH});// 负数收缩，向上
//        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        mIsScrolling = true;
        mScrollY += distanceY;
        //特别注意mScroll的正负数

        //        Log.e("mScrollY", mScrollY + "");
        LayoutParams lp = (LayoutParams) Panel.this.getLayoutParams();

        if (lp.topMargin > -300 && mScrollY > 0) {//往上拖拉
        //            Log.e("mScrollY", "*****111111*****");
            lp.topMargin = Math.min((lp.topMargin + (int) -mScrollY), mTopMargin);
            Panel.this.setLayoutParams(lp);

        } else if (lp.topMargin < mTopMargin && mScrollY < 0) {//往下拖拉
        //            Log.e("mScrollY", "*****222222*****");
            lp.topMargin = Math.max((lp.topMargin + (int) -(mScrollY)), 0);
            Panel.this.setLayoutParams(lp);

        }
        Log.e("mScrollY", "*****lp.topMargin == mTopMargin*****" + (lp.topMargin == mTopMargin));
        Log.e("mScrollY", "*****lp.topMargin == 0*****" + (lp.topMargin == 0));
        //有错误
        if (lp.topMargin == mTopMargin && panelOpenedEvent != null) {//展开之后
            Log.e("mScrollY", "*****open*****");
            panelOpenedEvent.onPanelOpened(Panel.this);//调用OPEN回调函数
        } else if (lp.topMargin == 0 && panelClosedEvent != null) {//收缩之后
            Log.e("mScrollY", "*****close*****");
            panelClosedEvent.onPanelClosed(Panel.this);//调用CLOSE回调函数
        }
        Log.e("onScroll", lp.rightMargin + "");
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

}