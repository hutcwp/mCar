package com.hut.cwp.mcar.activitys.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.about.AboutActivity;
import com.hut.cwp.mcar.activitys.info.CarInfoActivity;
import com.hut.cwp.mcar.activitys.map.clazz.BaiduMapLocal;
import com.hut.cwp.mcar.activitys.map.clazz.BaiduMapNavi;
import com.hut.cwp.mcar.activitys.map.clazz.BaiduMapPoiSearch;
import com.hut.cwp.mcar.activitys.map.interfaces.MainPresent;
import com.hut.cwp.mcar.activitys.other.CheckActivity;
import com.hut.cwp.mcar.activitys.other.FeedbackActivity;
import com.hut.cwp.mcar.activitys.other.WebViewContentActivity;
import com.hut.cwp.mcar.activitys.safe.SafeActivity;
import com.hut.cwp.mcar.activitys.user.LoginActivity;
import com.hut.cwp.mcar.activitys.user.UserActivity;
import com.hut.cwp.mcar.app.Globel;
import com.hut.cwp.mcar.app.MyApplication;
import com.hut.cwp.mcar.utils.Local;
import com.hut.cwp.mcar.activitys.business.IllegalQueryActivity;
import com.hut.cwp.mcar.activitys.business.MoveCarActivity;
import com.hut.cwp.mcar.activitys.business.OilCityActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateStatus;

/**
 * Created by hutcwp on 2017/5/11.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */

public class BNMainPresentMain implements MainPresent {

    double endLatitue;
    double endLongLatitue;

    SuggestionSearch mSuggestionSearch;

    private BNMainActivity activity;
    private BaiduMapLocal baiduMapLocal;
    private BaiduMapNavi baiduMapNavi;
    private BaiduMapPoiSearch poiSearch;

    ArrayAdapter<String> adapter;

    private List<String> listData;

    //用于导航的功能
    static List<Activity> activityList = new LinkedList<>();
    List<SuggestionResult.SuggestionInfo> suggestionInfoList;



    public static final String ROUTE_PLAN_NODE = "routePlanNode";


    BNMainPresentMain(BNMainActivity activity) {

        this.activity = activity;

        baiduMapLocal = new BaiduMapLocal(activity, activity.mBaiduMap);
        baiduMapNavi = new BaiduMapNavi(activity);
        poiSearch = new BaiduMapPoiSearch(activity.mBaiduMap, activity);
        mSuggestionSearch = SuggestionSearch.newInstance();
        baiduMapLocal.initLocation();

        iniData();
        initListener(activity);

        // 打开log开关
        BNOuterLogUtil.setLogSwitcher(true);
        if (baiduMapNavi.initDirs()) {
            baiduMapNavi.initNavi();
        }
    }

    /**
     * 初始化数据
     */
    private void iniData() {

        suggestionInfoList = new ArrayList<>();

        listData = new ArrayList<>();
        listData.add("请输入关键字");

        Log.d("test", "null? listData" + (listData == null));
        adapter = new ArrayAdapter(activity, R.layout.cwp_item_main_autocomplete, listData);
    }

    /**
     * 初始化Listener
     *
     * @param activity BNMainActivity
     */
    private void initListener(final BNMainActivity activity) {
        /**
         * 搜索接口
         */
        mSuggestionSearch.setOnGetSuggestionResultListener(res -> {
            if (res == null || res.getAllSuggestions() == null) {
                if (res == null) {
                } else if (res.getAllSuggestions() == null) {

                }
            } else if (res.getAllSuggestions().get(0) != null) {
                //获取在线建议检索结果 , 默认截取第一个对象做为导航的参数值
                listData = new ArrayList<>();
                suggestionInfoList = new ArrayList<>();
                for (SuggestionResult.SuggestionInfo suggestionInfo : res.getAllSuggestions()) {
                    //suggestionInfo.key 经纬度
                    listData.add(suggestionInfo.key);
                    suggestionInfoList.add(suggestionInfo);

                }
                activity.Binding.lyMainMap.tvAutoComplete.setAdapter(
                        new ArrayAdapter(activity, R.layout.cwp_item_main_autocomplete, listData));
                activity.Binding.lyMainMap.tvAutoComplete.showDropDown();

            }
            activity.hideProgress();
        });
    }


    /**
     * 客户端调用方法
     * 在线建议查询
     * 实时导航
     */
    @Override
    public void searchDestination(String City, String Key) {

        City = Local.getCity();
        // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(Key)
                .city(City));
    }


    /**
     * 出发去某地
     *
     * @param target 目的地的LatLng,null代表冲当前地点出发
     */
    @Override
    public void goTo(LatLng target) {

        if (target != null) {
            endLatitue = target.latitude;
            endLongLatitue = target.longitude;
            routePlanToNavigation();
        } else {
            routePlanToNavigation();
        }
    }


    /**
     * 搜索附近兴趣点（加油站，停车场，修车店）
     *
     * @param poi 兴趣点
     */
    @Override
    public void searchPeriphery(String poi) {

        poiSearch.boundSearch(baiduMapLocal, poi);
    }

    /**
     * 通知移车
     */
    @Override
    public void moveCar() {

        activity.startActivity(new Intent(activity, MoveCarActivity.class));
    }

    /**
     * 今日油价
     */
    @Override
    public void gasPrice() {

        activity.startActivity(new Intent(activity, OilCityActivity.class));
    }

    /**
     * 违章记录
     */
    @Override
    public void ivIllegalRecord() {

        activity.startActivity(new Intent(activity, IllegalQueryActivity.class));
    }

    /**
     * 车辆体检
     */
    @Override
    public void checkCar() {

        activity.startActivity(new Intent(activity, CheckActivity.class));
    }


    /**
     * 保险
     */
    @Override
    public void insurance() {

        activity.startActivity(new Intent(activity, SafeActivity.class));
    }

    /**
     * 分享的功能
     */
    @Override
    public void Share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "要分享的内容!!!");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, "分享到"));

    }

    @Override
    public void about() {
        activity.startActivity(new Intent(activity, AboutActivity.class));
    }

    @Override
    public void feedback() {
        activity.startActivity(new Intent(activity, FeedbackActivity.class));
    }

    /**
     * 登出账号
     */
    @Override
    public void loginOut() {
        if (MyApplication.getLandState() == MyApplication.HAD_LANDED) {
            MyApplication.setLandState(MyApplication.NO_LAND);
            MyApplication.setUsername("");
            activity.closeMenu();
            Toast.makeText(activity, "已经退出当前账号", Toast.LENGTH_SHORT).show();
            Log.d("测试", "已经退出当前账号");
        } else if (MyApplication.getLandState() == MyApplication.NO_LAND) {
            Toast.makeText(activity, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检查更新
     */
    @Override
    public void update() {
        BmobUpdateAgent.forceUpdate(activity);//用于自动更新
        BmobUpdateAgent.setUpdateListener((updateStatus, updateInfo) -> {
            if (updateStatus == UpdateStatus.Yes) {//版本有更新
            } else if (updateStatus == UpdateStatus.No) {
                Toast.makeText(activity, "版本无更新", Toast.LENGTH_SHORT).show();
            } else if (updateStatus == UpdateStatus.EmptyField) {//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                Toast.makeText(activity, "请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。", Toast.LENGTH_SHORT).show();
            } else if (updateStatus == UpdateStatus.IGNORED) {
                Toast.makeText(activity, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
            } else if (updateStatus == UpdateStatus.ErrorSizeFormat) {
                Toast.makeText(activity, "请检查target_size填写的格式，请使用file.length()方法获取apk大小。", Toast.LENGTH_SHORT).show();
            } else if (updateStatus == UpdateStatus.TimeOut) {
                Toast.makeText(activity, "查询出错或查询超时", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 我的私家车
     */
    @Override
    public void myCar() {
        if (MyApplication.getLandState() == MyApplication.HAD_LANDED) {
            activity.startActivity(new Intent(activity, CarInfoActivity.class));
        } else if (MyApplication.getLandState() == MyApplication.NO_LAND) {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.putExtra("TAG", Globel.From);


            activity.startActivity(intent);
        }
    }


    /**
     * 用户信息
     */
    @Override
    public void userInfo() {
        if (MyApplication.getLandState() == MyApplication.HAD_LANDED) {
            activity.startActivity(new Intent(activity, UserActivity.class));
        } else if (MyApplication.getLandState() == MyApplication.NO_LAND) {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.putExtra("TAG", Globel.From);
            activity.startActivity(intent);
        }
    }

    /**
     * 具体实现方法
     * 导航以及路线规划
     */
    private void routePlanToNavigation() {

        BNRoutePlanNode sNode = new BNRoutePlanNode(baiduMapLocal.getmCurrentLongLatitue(), baiduMapLocal.getmCurentLatitue(), null, null, BNRoutePlanNode.CoordinateType.BD09LL);
        BNRoutePlanNode eNode = new BNRoutePlanNode(endLongLatitue, endLatitue, null, null, BNRoutePlanNode.CoordinateType.BD09LL);

        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);
        BaiduNaviManager.getInstance().launchNavigator(activity, list, 1, true, new DemoRoutePlanListener(sNode));

    }

    private class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /**
             * 设置途径点以及resetEndNode会回调该接口
             */
            for (Activity ac : activityList) {
                if (ac.getClass().getName().endsWith("BNGuideActivity")) {
                    return;
                }
            }
            Intent intent = new Intent(activity, BNGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, mBNRoutePlanNode);
            intent.putExtras(bundle);
            activity.startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(activity, "算路失败", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 文章查看
     * @param url
     */
    void startActivityWithUrl(String url) {

        Intent intent = new Intent(activity, WebViewContentActivity.class);
        intent.putExtra("URL", url);
        activity.startActivity(intent);
    }


}
