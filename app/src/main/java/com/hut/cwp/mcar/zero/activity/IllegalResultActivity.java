package com.hut.cwp.mcar.zero.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.zero.adapter.IllegalResultAdapter;
import com.hut.cwp.mcar.zero.bean.IllegalResultBean;

import java.util.List;

public class IllegalResultActivity extends AppCompatActivity {
    private RecyclerView mRvResultList;
    private List<IllegalResultBean> mResult;
    private IllegalResultAdapter mAdapter;
    private TextView mTvLsnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_illegal_result);

        getSupportActionBar().hide();

        init();
    }

    private void init() {
        Intent intent=getIntent();
        mResult=intent.getParcelableArrayListExtra("result");
        mTvLsnum= (TextView) findViewById(R.id.tv_il_result_lsnum);
        mTvLsnum.setText(intent.getStringExtra("lsnum"));
        mRvResultList= (RecyclerView) findViewById(R.id.rv_il_result_list);
        mRvResultList.setLayoutManager(new LinearLayoutManager(mRvResultList.getContext()));
        mAdapter=new IllegalResultAdapter(mResult);
        mRvResultList.setAdapter(mAdapter);
    }

    public void back(View v) {
        finish();
    }
}
