package com.hut.cwp.mcar.way.clazz;

import android.widget.EditText;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class CarInfo extends BmobObject {

    private String licensePlate;

    private String usernameObject;

    private String engine;

    private String frame;



    public String getUsernameObject() {

        return usernameObject;
    }

    public void setUsernameObject(String usernameObject) {

        this.usernameObject = usernameObject;
    }

    public String getLicensePlate() {

        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {

        this.licensePlate = licensePlate;
    }


    public String getEngine() {

        return engine;
    }

    public void setEngine(String engine) {

        this.engine = engine;
    }



    public String getFrame() {

        return frame;
    }

    public void setFrame(String frame) {

        this.frame = frame;
    }




}
