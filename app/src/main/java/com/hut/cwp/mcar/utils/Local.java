package com.hut.cwp.mcar.utils;

/**
 * Created by Adminis on 2016/10/13.
 */

public class Local {

    private static String Province = "NULL";
    private static String City = "NULL";

    public static String getProvince() {
        return Province;
    }

    public static void setProvince(String province) {
        Province = province.replace("省","");
    }

    public static String getCity() {
        return City;
    }

    public static void setCity(String city) {
        City = city.replace("市","");
    }


}
