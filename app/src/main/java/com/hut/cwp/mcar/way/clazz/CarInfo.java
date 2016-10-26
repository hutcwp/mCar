package com.hut.cwp.mcar.way.clazz;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class CarInfo extends BmobObject {

    private String licensePlate;

    private String username;

    private String engine;

    private String vin;



    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
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



    public String getVin() {

        return vin;
    }

    public void setVin(String vin) {

        this.vin = vin;
    }




}
