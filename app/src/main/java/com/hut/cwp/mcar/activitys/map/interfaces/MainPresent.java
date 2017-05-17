package com.hut.cwp.mcar.activitys.map.interfaces;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by hutcwp on 2017/5/11.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */

public interface MainPresent {

    //搜索目的地
    void searchDestination(String City, String Key);

    //出发去某地
    void goTo(LatLng target);

    //搜索周边服务
    void searchPeriphery(String poi);

    //通知移车
    void moveCar();

    //今日油价
    void gasPrice();

    //违章记录
    void ivIllegalRecord();

    //车辆体检
    void checkCar();

    //保险
    void insurance();

    //关于
    void about();
    //反馈
    void feedback();
    //我的私家车
    void myCar();
    //用户信息
    void userInfo();
    //登出
    void loginOut();
    //更新
    void update();
    //分享
    void Share();

}
