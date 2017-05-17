package com.hut.cwp.mcar.activitys.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.databinding.ZActivityIllegalResultBinding;
import com.hut.cwp.mcar.activitys.business.adapter.IllegalResultAdapter;
import com.hut.cwp.mcar.activitys.business.bean.HistorysBean;
import com.hut.cwp.mcar.activitys.business.bean.IllegalResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hutcwp on 2017/5/14.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */
public class IllegalResultActivity extends BaseActivity {

    private RecyclerView mRvResultList;

    private List<HistorysBean> mResult = new ArrayList<>();

    private ZActivityIllegalResultBinding Binding;

    @Override
    protected int getLayoutId() {
        return R.layout.z_activity_illegal_result;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Binding = (ZActivityIllegalResultBinding) getBinding();
        Binding.title.btnBack.setOnClickListener(v -> finish());
        Binding.title.tvTitle.setText("查询结果");

        mRvResultList = (RecyclerView) findViewById(R.id.rv_il_result_list);

    }

    @Override
    protected void loadData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String info = bundle.getString("info");

        IllegalResultBean illegalResult = new Gson().fromJson(info, IllegalResultBean.class);
        List<HistorysBean> records = illegalResult.getHistorys();

        mResult.addAll(records);
        mRvResultList.setLayoutManager(new LinearLayoutManager(mRvResultList.getContext()));
        mRvResultList.setAdapter(new IllegalResultAdapter(mResult));

    }


}
