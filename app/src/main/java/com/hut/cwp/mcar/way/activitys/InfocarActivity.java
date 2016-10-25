package com.hut.cwp.mcar.way.activitys;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hut.cwp.mcar.R;

public class InfocarActivity extends Activity {

    private ImageView addCar;
    private ImageView btn_back;
    private TextView text_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);
        init();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        addCar = (ImageView) findViewById(R.id.add_intcar);

        addCar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(InfocarActivity.this, InfocarActivity2.class);
//                //Intent intent = new Intent(RepasswordActivity.this,MainActivity.class);
//                startActivity(intent);
//                //finish();

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
        text_content.setText("我的私家车");
    }
}
