package com.hut.cwp.mcar.zero.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.utils.Local;
import com.hut.cwp.mcar.zero.callback.OilCityCallback;
import com.zhy.http.okhttp.OkHttpUtils;

/*
还需要根据老司机提供的方法来确定位置信息
 */
public class OilCityActivity extends Activity {

    private TextView mTv97;
    private TextView mTv93;
    private TextView mTv90;
    private TextView mTv0;
    private TextView mReTime;
    private TextView mPlace;
    private TextView mFailed;
    private ProgressBar mPb;
    private RelativeLayout mRlOilCity;
    private Button mBack;
    private static String URL_PART = "http://api.avatardata.cn/OilPrice/LookUp?key=3e4fe1f9e2ff4cbc84339018aebe1061&prov=";  //阿凡达平台（免费）  需要直接键入省级区域名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_oil_city);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        init();

        mRlOilCity.setVisibility(View.INVISIBLE);
        mPb.setVisibility(View.VISIBLE);

        //在此之前，需要获取位置信息，同时更改用于测试的URL
        final String currentProvince= Local.getProvince();
        if (!TextUtils.isEmpty(currentProvince)&&!currentProvince.equals("NULL")) {
            String url=URL_PART+currentProvince;
            OkHttpUtils.get().url(url).build().execute(new OilCityCallback() {

                @Override
                public void onError(okhttp3.Call call, Exception e, int id) {
                    Toast.makeText(OilCityActivity.this,"获取失败-1"+e.toString(),Toast.LENGTH_SHORT).show();
                    mRlOilCity.setVisibility(View.VISIBLE);
                    mFailed.setVisibility(View.VISIBLE);
                    mPb.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(Bundle response, int id) {
                    mRlOilCity.setVisibility(View.VISIBLE);
                    if("0".equals(response.getString("error_code"))) {
                        mTv97.setText(response.getString("p97"));
                        mTv93.setText(response.getString("p93"));
                        mTv90.setText(response.getString("p90"));
                        mTv0.setText(response.getString("p0"));
                        mReTime.setText("刷新时间"+response.getString("re_time"));
                        mPlace.setText(currentProvince);
                    } else {
                        Toast.makeText(OilCityActivity.this,"获取失败-2",Toast.LENGTH_SHORT).show();
                        mFailed.setVisibility(View.VISIBLE);
                    }
                    mPb.setVisibility(View.GONE);
                }
            });
        } else {
            mRlOilCity.setVisibility(View.VISIBLE);
            mFailed.setVisibility(View.VISIBLE);
            mPb.setVisibility(View.GONE);
        }

    }

    private void init() {
        mBack= (Button) findViewById(R.id.bt_oil_city_back);
        mPb= (ProgressBar) findViewById(R.id.pb);
        mTv97= (TextView) findViewById(R.id.tv_oil_city_price97);
        mTv93= (TextView) findViewById(R.id.tv_oil_city_price93);
        mTv90= (TextView) findViewById(R.id.tv_oil_city_price90);
        mTv0= (TextView) findViewById(R.id.tv_oil_city_price0);
        mReTime= (TextView) findViewById(R.id.tv_oil_city_re_time);
        mPlace= (TextView) findViewById(R.id.tv_oil_city_place);
        mFailed= (TextView) findViewById(R.id.tv_oil_city_failed);
        mRlOilCity= (RelativeLayout) findViewById(R.id.rl_oil_city);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
