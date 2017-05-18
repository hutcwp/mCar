package com.hut.cwp.mcar.activitys.info;

import android.util.Log;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.info.bean.CarInfo;
import com.hut.cwp.mcar.activitys.info.adapter.MyCarInfoAdapter;
import com.hut.cwp.mcar.activitys.info.intefaces.InfoPresent;
import com.hut.cwp.mcar.app.App;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hutcwp on 2017/5/10.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */

public class CarInfoPresent implements InfoPresent {


    boolean deleteAble = false;
    boolean addAble = false;

    private MyCarInfoAdapter adapter;

    List<CarInfo> myCarInfoList = new ArrayList<>();

    private CarInfoActivity activity;

    CarInfoPresent(CarInfoActivity activity) {

        this.activity = activity;

    }


    /**
     * 车辆添加的方法
     */
    public void addCar(String licensePlate, String engine, String vin) {

        if (addAble) {
            activity.showProgress();
            //加入信息
            CarInfo carInfo = new CarInfo();
            carInfo.setUsername(App.getUsername());
            carInfo.setLicensePlate(licensePlate);
            carInfo.setEngine(engine);
            carInfo.setVin(vin);

            carInfo.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        Toast.makeText(activity, "添加成功", Toast.LENGTH_SHORT).show();
                        myCarInfoList.add(carInfo);
                        adapter.notifyDataSetChanged();
                        activity.setCarCount(myCarInfoList.size());
                    } else {
                        Log.d("test", "e: " + e.getMessage());
                        Toast.makeText(activity, "添加失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    activity.hideProgress();
                }
            });
        }
        addAble = false;
    }

    @Override
    public void deleteCar(CarInfo carInfo) {

        if (deleteAble) {
            activity.showProgress();
            carInfo.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG).show();
                        myCarInfoList.remove(carInfo);
                        adapter.notifyDataSetChanged();
                        activity.setCarCount(myCarInfoList.size());
                    } else {
                        Toast.makeText(activity, "删除失败", Toast.LENGTH_LONG).show();
                    }
                    activity.hideProgress();
                }
            });
        }
        deleteAble = false;
    }

    @Override
    public void queryCar() {

        BmobQuery<CarInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("username", App.getUsername());
//        Log.d("tag", "usename:" + App.getUsername());
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<CarInfo>() {
            @Override
            public void done(List<CarInfo> object, BmobException e) {
                if (object.size() >= 0) {
                    Toast.makeText(activity, "查询成功：共" + object.size() + "条数据。", Toast.LENGTH_SHORT).show();
                    myCarInfoList.addAll(object);
                    initAdapter(R.layout.way_item_mycar);
                    activity.setCarCount(myCarInfoList.size());
                } else {
                    Toast.makeText(activity, activity.getString(R.string.query_failure) + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("bmob", activity.getString(R.string.failure) + e.getMessage() + "," + e.getErrorCode());
                }
                activity.Binding.swipeRefresh.setRefreshing(false);
            }
        });
    }


    /**
     * 刷新数据
     */
    public void refreshData() {

        initData();
    }

    /**
     * 初始化Adapter
     *
     * @param item_res_id 资源ID
     */
    void initAdapter(int item_res_id) {

        adapter = new MyCarInfoAdapter(activity, item_res_id, myCarInfoList);
        activity.Binding.listview.setAdapter(adapter);
    }

    /**
     * 初始化列表数据
     */
    void initData() {
        myCarInfoList = new ArrayList<>();
        queryCar();
    }


}
