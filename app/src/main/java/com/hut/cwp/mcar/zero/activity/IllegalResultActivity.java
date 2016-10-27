package com.hut.cwp.mcar.zero.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.zero.adapter.IllegalResultAdapter;
import com.hut.cwp.mcar.zero.bean.IllegalResultBean;

import java.util.ArrayList;
import java.util.List;

public class IllegalResultActivity extends AppCompatActivity {

    private RecyclerView mRvResultList;
//    private List<IllegalResultBean> mResult;
    private List<IllegalResultBean> mResult=new ArrayList<>();
    private IllegalResultAdapter mAdapter;

    private TextView mTvLsnum;
    private ImageView btn_back;
    private TextView text_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_illegal_result);

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        IllegalResultBean bean1=new IllegalResultBean();
        bean1.setDate("2016-04-27 14:44:00");
        bean1.setArea("市立医院路口");
        bean1.setAct("驾驶机动车违反道路交通信号灯通行的");
        bean1.setCode("");
        bean1.setFen("6");
        bean1.setMoney("150");
        bean1.setHandled("1");

        IllegalResultBean bean2=new IllegalResultBean();
        bean2.setDate("2016-04-27 14:44:00");
        bean2.setArea("市立医院路口");
        bean2.setAct("驾驶机动车违反道路交通信号灯通行的");
        bean2.setCode("");
        bean2.setFen("6");
        bean2.setMoney("150");
        bean2.setHandled("");

        IllegalResultBean bean3=new IllegalResultBean();
        bean3.setDate("2016-04-27 14:44:00");
        bean3.setArea("市立医院路口");
        bean3.setAct("驾驶机动车违反道路交通信号灯通行的");
        bean3.setCode("");
        bean3.setFen("6");
        bean3.setMoney("150");
        bean3.setHandled("1");

        mResult.add(bean1);
        mResult.add(bean2);
        mResult.add(bean3);

//        Intent intent = getIntent();
//        mResult = intent.getParcelableArrayListExtra("result");
//        mTvLsnum = (TextView) findViewById(R.id.tv_il_result_lsnum);
//        mTvLsnum.setText(intent.getStringExtra("lsnum"));
        mRvResultList = (RecyclerView) findViewById(R.id.rv_il_result_list);
        mRvResultList.setLayoutManager(new LinearLayoutManager(mRvResultList.getContext()));
        mAdapter = new IllegalResultAdapter(mResult);
        mRvResultList.setAdapter(mAdapter);

        text_content = (TextView) findViewById(R.id.menu_title_text_content);
        btn_back = (ImageView) findViewById(R.id.menu_title_btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_content.setText("查询结果");
    }


}
