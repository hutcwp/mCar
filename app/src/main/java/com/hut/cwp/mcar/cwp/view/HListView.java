package com.hut.cwp.mcar.cwp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by Adminis on 2016/10/8.
 */

public class HListView extends LinearLayout {

    float startX;

    float currentX;

    float sX;

    int duration = 0;

    public HListView(Context context) {
        super(context);
        init();
    }

    public HListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init() {

        Log.e("TestForHListView", "init");
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        startX = event.getRawX();
                        sX = startX;

                        Log.e("TestForHListView", "ACTION_DOWN");
                        break;

                    case MotionEvent.ACTION_MOVE:

                        currentX = event.getRawX();

                        float dx = currentX - startX;

                        duration -= dx;
                        scroll(duration);

                        startX = currentX;

                        Log.e("TestForHListView", "ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:

                        if (Math.abs(currentX - sX) > 8) {

                            if (duration > 150) {
                                int s = getScreenWidth() / 4 + 80;
                                scroll(s);
                                duration = s;
                            } else {
                                scroll(0);
                                duration = 0;
                            }

                            return true;
                        }
                        break;
                }
                return false;

            }
        });

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            // lp.weight = 0;
            lp.width = getScreenWidth() / (childCount - 1) + 15;
            view.setLayoutParams(lp);
        }

    }

    /*
   ** 获得屏幕宽度
    */
    public int getScreenWidth() {

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public void scroll(float positionOffset) {

        this.scrollTo((int) positionOffset, 0);

        this.invalidate();
        Log.e("TestForHListView", "positionOffset" + positionOffset);
    }

}
