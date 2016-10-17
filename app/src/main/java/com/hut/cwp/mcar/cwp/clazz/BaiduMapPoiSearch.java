package com.hut.cwp.mcar.cwp.clazz;

import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.hut.cwp.mcar.cwp.activity.BNDemoMainActivity;

/**
 * Created by Adminis on 2016/10/14.
 */

public class BaiduMapPoiSearch {

    BaiduMap mBaiduMap;
    BNDemoMainActivity mContext;
    LatLng nodeLocation;
    PoiSearch mPoiSearch;
    OnGetPoiSearchResultListener poiListener;

    public BaiduMapPoiSearch(BaiduMap mBaiduMap, BNDemoMainActivity context) {

        mContext = context;
        this.mBaiduMap = mBaiduMap;

        init();

    }

    void init() {

        //初始化检索
        mPoiSearch = PoiSearch.newInstance();

        poiListener = new OnGetPoiSearchResultListener() {

            public void onGetPoiResult(PoiResult result) {

                if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果

                    Toast.makeText(mContext, "未找到结果", Toast.LENGTH_LONG).show();

                    return;
                }

                if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回

                    mBaiduMap.clear();
                    MyPoiOverlay poiOverlay = new MyPoiOverlay(mBaiduMap);
                    poiOverlay.setData(result);// 设置POI数据

                    mBaiduMap.setOnMarkerClickListener(poiOverlay);

                    poiOverlay.addToMap();// 将所有的overlay添加到地图上
                    poiOverlay.zoomToSpan();

                }
            }


            public void onGetPoiDetailResult(PoiDetailResult result) {

                if (result.error != SearchResult.ERRORNO.NO_ERROR) {

                    Toast.makeText(mContext, "抱歉，未找到结果",
                            Toast.LENGTH_SHORT).show();

                } else {
                    // 正常返回结果的时候，此处可以获得很多相关信息
                    // 获取pupouWindow的显示位置
                    nodeLocation = result.getLocation();

                }
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }

        };
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

    }

    public void boundSearch(BaiduMapLocal myLocate, String PoiKey) {

        PoiBoundSearchOption boundSearchOption = new PoiBoundSearchOption();

        LatLng southwest = new LatLng(myLocate.getmCurentLatitue() - 0.01, myLocate.getmCurrentLongLatitue() - 0.012);// 西南
        LatLng northeast = new LatLng(myLocate.getmCurentLatitue() + 0.01, myLocate.getmCurrentLongLatitue() - 0.012 + 0.012);// 东北

        LatLngBounds bounds = new LatLngBounds.Builder().include(southwest)
                .include(northeast).build();// 得到一个地理范围对象

        boundSearchOption.bound(bounds);// 设置poi检索范围
        boundSearchOption.keyword(PoiKey);// 检索关键字
        boundSearchOption.pageNum(0);

        mPoiSearch.searchInBound(boundSearchOption);// 发起poi范围检索请求
    }

    class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap arg0) {
            super(arg0);
        }

        //加油站的点击事件
        @Override
        public boolean onPoiClick(int arg0) {
            super.onPoiClick(arg0);

            PoiInfo poiInfo = getPoiResult().getAllPoi().get(arg0);
            // 检索poi详细信息
            mPoiSearch.searchPoiDetail(new PoiDetailSearchOption()
                    .poiUid(poiInfo.uid));

            Toast.makeText(mContext, "结果:" + poiInfo.address + "->" + poiInfo.name + "->" + poiInfo.phoneNum + "->"
                             + poiInfo.location.latitude + "->" + poiInfo.location.longitude,
                    Toast.LENGTH_SHORT).show();

            mContext.route_go(poiInfo.location);

            return true;
        }
    }


}
