package com.hut.cwp.mcar.activitys.map.clazz;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.hut.cwp.mcar.utils.Local;


/**
 * Created by Adminis on 2016/10/12.
 */

public class BaiduMapLocal {

    private Context context;
    private BaiduMap mBaiduMap;

    private BDLocationListener myListener = new MyLocationListener();

    /**
     * *  初始化定位的相关参数
     */
    private double mCurentLatitue;
    private double mCurrentLongLatitue;


    public BaiduMapLocal(Context context, BaiduMap baiduMap) {
        this.context = context;
        this.mBaiduMap = baiduMap;
    }


    /**
     * *  初始化定位的相关参数
     */

    public void initLocation() {

        LocationClient mLocationClient = new LocationClient(context.getApplicationContext());
        mLocationClient.registerLocationListener(myListener);    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);

        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    /**
     * 自定义的定位接口类，继承至BDLocationListener
     */
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //一直处于中心点的状态
            centerToMyLocation(location.getLatitude(), location.getLongitude());
            mCurentLatitue = location.getLatitude();
            mCurrentLongLatitue = location.getLongitude();

            //用于车辆违章查询的定位
            Local.setCity(location.getCity());
            Local.setProvince(location.getProvince());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    /**
     * 通过传入的经度与纬度，然后将地图画面位置居中
     *
     * @param mLatitue
     * @param mLongLatitue
     */

    private void centerToMyLocation(double mLatitue, double mLongLatitue) {

        LatLng latLng = new LatLng(mLatitue, mLongLatitue);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }

    public double getmCurentLatitue() {
        return mCurentLatitue;
    }

    public double getmCurrentLongLatitue() {
        return mCurrentLongLatitue;
    }
}
