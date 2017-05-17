package com.hut.cwp.mcar.activitys.about;


import android.os.Bundle;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.databinding.CwpActivityAboutBinding;

public class AboutActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.cwp_activity_about;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        CwpActivityAboutBinding binding = (CwpActivityAboutBinding) getBinding();

        binding.lyTitle.btnBack.setOnClickListener(v-> finish());
        binding.lyTitle.tvTitle.setText(R.string.about);
    }

    @Override
    protected void loadData() {

    }


}
