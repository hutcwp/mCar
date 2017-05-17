package com.hut.cwp.mcar.activitys.business;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.databinding.ZActivityOilCityBinding;
import com.hut.cwp.mcar.utils.Local;
import com.hut.cwp.mcar.activitys.business.callback.OilCityCallback;
import com.zhy.http.okhttp.OkHttpUtils;


public class OilCityActivity extends BaseActivity {

    private ZActivityOilCityBinding Binding;

    private static final String URL_HOST = "http://route.showapi.com/";
    private static String Province = Local.getProvince();
    private static String API_KEY = "5a313da3410b442abbfe9c9045991348";
    private static String API_ID = "38144";

    private static String URL_PATH =
            URL_HOST + "138-46?" +
                    "showapi_appid=" + API_ID + "&prov=" + Province + "&showapi_sign=" + API_KEY;

    //加载数据失败时显示的页面
    private LinearLayout mLlFailedLoading;
    private RelativeLayout mRlOilCity;

    @Override
    protected int getLayoutId() {
        return R.layout.z_activity_oil_city;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Binding = (ZActivityOilCityBinding) getBinding();

        init();

        loading();
    }

    @Override
    protected void loadData() {

    }


    /**
     * 加载数据
     */
    private void loading() {

        mLlFailedLoading.setVisibility(View.GONE);
        mRlOilCity.setVisibility(View.INVISIBLE);
        showProgress();

        if (!TextUtils.isEmpty(Province) && !Province.equals("NULL")) {
            String url = URL_PATH;
            OkHttpUtils.get().url(url).build().execute(new OilCityCallback() {
                @Override
                public void onError(okhttp3.Call call, Exception e, int id) {
                    Toast.makeText(OilCityActivity.this, "获取失败-1" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    mRlOilCity.setVisibility(View.GONE);
                    mLlFailedLoading.setVisibility(View.VISIBLE);
                    hideProgress();
                }

                @Override
                public void onResponse(Bundle bundle, int id) {
                    if ("0".equals(bundle.getString("showapi_res_code"))) {
                        mRlOilCity.setVisibility(View.VISIBLE);
                        Binding.tvOilCityPrice97.setText(bundle.getString("p97"));
                        Binding.tvOilCityPrice93.setText(bundle.getString("p93"));
                        Binding.tvOilCityPrice90.setText(bundle.getString("p90"));
                        Binding.tvOilCityPrice0.setText(bundle.getString("p0"));
                        String updateTime = getString(R.string.update_time) + bundle.getString("ct");
                        Binding.tvOilCityReTime.setText(updateTime);
                        Binding.tvOilCityPlace.setText(Province);
                    } else {
                        Toast.makeText(OilCityActivity.this, "获取失败-2", Toast.LENGTH_SHORT).show();
                        mRlOilCity.setVisibility(View.GONE);
                        mLlFailedLoading.setVisibility(View.VISIBLE);
                    }
                    hideProgress();
                }

            });
        } else {
            mRlOilCity.setVisibility(View.GONE);
            mLlFailedLoading.setVisibility(View.VISIBLE);
            hideProgress();
        }
    }

    /**
     * 初始化
     */
    private void init() {

        mRlOilCity = (RelativeLayout) findViewById(R.id.rl_oil_city);

        Binding.btnBack.setOnClickListener(v -> finish());

        mLlFailedLoading = (LinearLayout) findViewById(R.id.layout_failed_loading);
        Button mLayoutBtBack = (Button) findViewById(R.id.bt_failed_loading_back);
        Button mLayoutBtRetry = (Button) findViewById(R.id.bt_layout_failed_loading_retry);

        mLayoutBtBack.setOnClickListener(v -> finish());
        mLayoutBtRetry.setOnClickListener(v -> loading());
    }

}
