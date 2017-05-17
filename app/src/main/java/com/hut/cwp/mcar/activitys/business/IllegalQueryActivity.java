package com.hut.cwp.mcar.activitys.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.cheshouye.api.client.WeizhangClient;
import com.cheshouye.api.client.WeizhangIntentService;
import com.cheshouye.api.client.json.CityInfoJson;
import com.cheshouye.api.client.json.ProvinceInfoJson;
import com.cheshouye.api.client.json.WeizhangResponseJson;
import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.business.adapter.CarInfoAdapter;
import com.hut.cwp.mcar.activitys.business.bean.CarInfoBean;
import com.hut.cwp.mcar.activitys.info.bean.CarInfo;
import com.hut.cwp.mcar.app.MyApplication;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.databinding.ZActivityIllegalQueryBinding;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by hutcwp on 2017/5/14.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */

public class IllegalQueryActivity extends BaseActivity {


    ZActivityIllegalQueryBinding Binding;

    private List<CarInfoBean> mCarInfoBeanList = new ArrayList<>();

    CarInfoBean selectedCar = null;

    CarInfoAdapter mCarsInfoAdapter;

    boolean isStart = false;

    List<ProvinceInfoJson> pList = new ArrayList<>();

    List<CityInfoJson> cList = new ArrayList<>();

    List<String> data = new ArrayList<>();
    List<String> data2 = new ArrayList<>();

    int oldSelected = -1;

    int currentProvinceCode = 12; //默认值为12
    int currentCityCode = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.z_activity_illegal_query;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Binding = (ZActivityIllegalQueryBinding) getBinding();

        Binding.btnBack.setOnClickListener(v -> finish());

        Binding.btnSelectProvince.setOnClickListener(view -> {
            pList = WeizhangClient.getAllProvince();
            data = new ArrayList<>();
            if (pList != null) {
                for (ProvinceInfoJson province : pList) {
                    data.add(province.getProvinceName());
                }
            } else {
                Log.d("test", "pList == null");
            }
            Binding.lvCarIndo.setAdapter(new ArrayAdapter<>(IllegalQueryActivity.this, R.layout.item_select_area, data));
            Binding.lvCarIndo.setOnItemClickListener((adapterView, view12, position, l) -> setProvinceListener(position));
        });

        Binding.btnSelectCity.setOnClickListener(view -> {

            cList = WeizhangClient.getCitys(currentProvinceCode);
            if (cList != null) {
                data2 = new ArrayList<>();
                for (CityInfoJson city : cList) {
                    data2.add(city.getCity_name());
                }
            }
            Binding.lvCarIndo.setAdapter(new ArrayAdapter<String>(IllegalQueryActivity.this, R.layout.item_select_area, data2));
            Binding.lvCarIndo.setOnItemClickListener((adapterView, view1, position, l) -> setCityListener(position));
        });

        Binding.btnQuery.setOnClickListener(view -> {

            if (!isStart) {
                try {
                    search();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Binding.lvCarIndo.setOnItemClickListener((adapterView, view, position, l) -> {
            //  Toast.makeText(IllegalQueryActivity.this, "click", Toast.LENGTH_SHORT).show();
            setCarInfoListener(view, position);
        });

    }

    /**
     * 查询违章记录
     */
    public void search() {
        // 声明一个子线程
        new Thread() {
            @Override
            public void run() {
                // 这里写入子线程需要做的工作
                com.cheshouye.api.client.json.CarInfo car = new com.cheshouye.api.client.json.CarInfo();

                car.setChepai_no("粤B12345");
                car.setChejia_no("123456");
                car.setEngine_no("156489789");
                car.setRegister_no("156456");//证书编号
                car.setCity_id(currentCityCode);
//                car.setChepai_no(selectedCar.getChepaino());
//                car.setChejia_no(selectedCar.getChejiano());
//                car.setEngine_no(selectedCar.getEngineno());
//                car.setRegister_no("156456");//证书编号
//                car.setCity_id(109);//城市编号


                WeizhangResponseJson info = WeizhangClient.getWeizhang(car);

                Log.d("info", info.toJson());
                int status = info.getStatus();
                // if (status == 0) {
                //0表示正常
                Bundle bundle = new Bundle();
                bundle.putString("info", info.toJson());
                Intent intent = new Intent(IllegalQueryActivity.this, IllegalResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                // }
                isStart = false;

            }
        }.start();

    }


    @Override
    protected void loadData() {

        Intent weizhangIntent = new Intent(this, WeizhangIntentService.class);
        weizhangIntent.putExtra("appId", 2552);//您的appId
        weizhangIntent.putExtra("appKey", "6f1e136dc94fef01444e0da938cd295d");//您的appKey
        startService(weizhangIntent);


        pList = WeizhangClient.getAllProvince();

        cList = WeizhangClient.getCitys(12);

        if (MyApplication.getLandState() == MyApplication.HAD_LANDED) {
            BmobQuery<CarInfo> carInfoBmobQuery = new BmobQuery<>();
            carInfoBmobQuery.addWhereEqualTo("username", MyApplication.getUsername());
            carInfoBmobQuery.findObjects(new FindListener<CarInfo>() {
                @Override
                public void done(List<CarInfo> list, final BmobException e) {
                    if (e == null && list.size() > 0) {
                        for (CarInfo carInfo : list) {
                            CarInfoBean tempBean = new CarInfoBean();
                            tempBean.setChepaino(carInfo.getLicensePlate());
                            tempBean.setEngineno(carInfo.getEngine());
                            tempBean.setChejiano(carInfo.getVin());
                            mCarInfoBeanList.add(tempBean);
                        }
                    }

                    mCarsInfoAdapter = new CarInfoAdapter(IllegalQueryActivity.this, mCarInfoBeanList);
                    Binding.lvCarIndo.setAdapter(mCarsInfoAdapter);
                    //如果Adapter的datas为0，则将它设置为不可以点击
                    if (mCarInfoBeanList.size() == 0) {
                        selectedCar = null;
                        Binding.btnQuery.setClickable(false);
                        Binding.tvIlQyQuery.setTextColor(ContextCompat.getColor(IllegalQueryActivity.this, R.color.gray));
                        Binding.btnQuery.setBackgroundResource(R.drawable.z_il_qy_unquery);
                    }
                }
            });

        }

    }


    /**
     * 设置车辆信息的点击事件
     *
     * @param view     布局
     * @param position 布局的位置
     */
    void setCarInfoListener(View view, int position) {
        selectedCar = mCarInfoBeanList.get(position);
        Binding.btnQuery.setClickable(true);
        Binding.tvIlQyQuery.setTextColor(ContextCompat.getColor(IllegalQueryActivity.this, R.color.csy_green));
        Binding.btnQuery.setBackgroundResource(R.drawable.z_il_qy_query);
        view.findViewById(R.id.rb_il_qy_car_info_radio_box).setBackgroundResource(R.drawable.z_il_qy_radio_box_selected);
        if (oldSelected != position) {
            if (oldSelected != -1) {

                mCarInfoBeanList.get(oldSelected).setChecked(false);
            }

            mCarInfoBeanList.get(position).setChecked(true);
            oldSelected = position;
        }
        mCarsInfoAdapter.update();

    }

    /**
     * 设置省份的点击事件
     *
     * @param position 布局的位置
     */
    void setProvinceListener(int position) {
        Binding.btnSelectProvince.setText(data.get(position));
        currentProvinceCode = pList.get(position).getProvinceId();

        Binding.lvCarIndo.setAdapter(new ArrayAdapter<>(IllegalQueryActivity.this, R.layout.item_select_area, data2));
        Binding.lvCarIndo.setOnItemClickListener((adapterView, view1, pos, l) -> setCityListener(pos));

    }

    /**
     * 设置城市的点击事件
     *
     * @param position 布局的位置
     */
    void setCityListener(int position) {
        Binding.btnSelectCity.setText(data2.get(position));
        currentCityCode = cList.get(position).getCity_id();

        mCarsInfoAdapter = new CarInfoAdapter(IllegalQueryActivity.this, mCarInfoBeanList);
        Binding.lvCarIndo.setAdapter(mCarsInfoAdapter);
        Binding.lvCarIndo.setOnItemClickListener((adapterView, view, pos, l) -> setCarInfoListener(view,pos));
    }

}
