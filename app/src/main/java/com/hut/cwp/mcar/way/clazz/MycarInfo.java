package com.hut.cwp.mcar.way.clazz;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/10/23 0023.
 */

public class MycarInfo extends BmobObject{

    private String licensePlate;

    private String frame;

    private String engine;


    public MycarInfo (String licensePlate,String engine,String frame){
        this.licensePlate =licensePlate;
        this.frame=frame;
        this.engine=engine;
    }

    public String getEngine() {

        return engine;
    }

    public String getFrame() {

        return frame;
    }

    public String getLicensePlate() {

        return licensePlate;
    }




}
