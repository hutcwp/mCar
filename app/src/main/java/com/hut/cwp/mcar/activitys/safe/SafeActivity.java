package com.hut.cwp.mcar.activitys.safe;

import android.content.Intent;
import android.os.Bundle;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.other.WebViewContentActivity;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.databinding.CwpActivitySafeBinding;

public class SafeActivity extends BaseActivity {


    private String url;

    private CwpActivitySafeBinding Binding;

    @Override
    protected int getLayoutId() {
        return R.layout.cwp_activity_safe;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Binding = (CwpActivitySafeBinding) getBinding();

        Binding.lyTitle.tvTitle.setText(R.string.car_insurance);
        Binding.lyTitle.btnBack.setOnClickListener(v -> finish() );
        Binding.itemOne.setOnClickListener(view -> {
            //太平洋保险
            url = "http://www.ecpic.com.cn/cxzq/2014gb/sy/";
            startActivityWithUrl(url);

        });

        Binding.itemTwo.setOnClickListener(view -> {
            //中国人民保险
            url = "http://www.epicc.com.cn/";
            startActivityWithUrl(url);
        });

        Binding.itemThree.setOnClickListener(view -> {
            //亚太财产
            url = "http://www.apiins.com/maechannel/carInsurance.do";
            startActivityWithUrl(url);

        });

        Binding.itemFour.setOnClickListener(view -> {
            //阳光
            url = "http://www.sinosig.com/";
            startActivityWithUrl(url);

        });

        Binding.itemFive.setOnClickListener(view -> {
            //中华车险
            url = "http://e.cic.cn/";
            startActivityWithUrl(url);

        });


    }

    @Override
    protected void loadData() {

    }


    /**
     * 打开webView进行浏览
     * @param url 地址
     */
    private void startActivityWithUrl(String url) {

        Intent intent = new Intent(SafeActivity.this, WebViewContentActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }

}
