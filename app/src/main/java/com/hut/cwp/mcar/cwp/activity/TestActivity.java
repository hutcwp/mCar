package com.hut.cwp.mcar.cwp.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.cwp.view.Panel;

/**
 * Created by Adminis on 2016/10/9.
 */

public class TestActivity extends Activity {

    public Panel panel;
    public LinearLayout container;
    public TextView topView, bottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_test);

        topView = (TextView) findViewById(R.id.topView);
        bottomView = (TextView) findViewById(R.id.bottomview);
        bottomView.setText("测试组件，红字白底");
        bottomView.setTextColor(Color.RED);
        bottomView.setBackgroundColor(Color.WHITE);

        container = (LinearLayout) findViewById(R.id.container);
        panel = new Panel(this, topView, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        container.addView(panel);//加入Panel控件

        //新建测试组件
        TextView tvTest = new TextView(this);
        tvTest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tvTest.setText("测试组件，红字白底");
        tvTest.setTextColor(Color.RED);
        tvTest.setBackgroundColor(Color.BLUE);

        //加入到Panel里面
        panel.fillPanelContainer(tvTest);
        panel.setPanelClosedEvent(panelClosedEvent);
        panel.setPanelOpenedEvent(panelOpenedEvent);


    }

    Panel.PanelClosedEvent panelClosedEvent = new Panel.PanelClosedEvent() {

        @Override
        public void onPanelClosed(View panel) {
            Log.e("panelClosedEvent", "panelClosedEvent");
        }

    };

    Panel.PanelOpenedEvent panelOpenedEvent = new Panel.PanelOpenedEvent() {

        @Override
        public void onPanelOpened(View panel) {
            Log.e("panelOpenedEvent", "panelOpenedEvent");
        }

    };


}
