package com.hut.cwp.mcar.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.hut.cwp.mcar.utils.CrashHandler;

import cn.bmob.v3.Bmob;

/**
 * Created by Adminis on 2016/9/21.
 */

public class App extends Application {

    public static int HAD_LANDED = 1;
    public static int NO_LAND = 0;

    private static String mUsername = "";

    private static int mLandState = HAD_LANDED;//TODO 还需要设置登录状态


    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        Bmob.initialize(getApplicationContext(), "a248d73a620fa5f2458692545f8bc07a");//Bomb's AppKey

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

    }

    public static void setLandState(int state) {
        mLandState = state;
        if (state == NO_LAND) {
            mUsername = "";
        }
    }

    public static int getLandState() {
        return mLandState;
    }

    public static String getUsername() {
        return mUsername;
    }

    public static void setUsername(String username) {
        mUsername = username;
    }
}
