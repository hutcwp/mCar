package com.hut.cwp.mcar.activitys.info.intefaces;

import com.hut.cwp.mcar.activitys.info.bean.CarInfo;

/**
 * Created by hutcwp on 2017/5/10.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */

public interface InfoPresent {


    void queryCar();

    void addCar(String licensePlate, String engine, String vin);

    void deleteCar(CarInfo carInfo);

}
