package com.hut.cwp.mcar.zero.bean;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Just on 2016/10/9.
 */

public class ProvinceBean {
    private String province;
    private List<CityBean> cities=new ArrayList<>();

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<CityBean> getCities() {
        return cities;
    }

    public void setCities(JSONArray jsonArrayCities) {
        for (int i=0,l=jsonArrayCities.length();i<l;i++) {
            try {
                JSONObject jsonObjectCity=jsonArrayCities.getJSONObject(i);
                CityBean cityBean=new Gson().fromJson(String.valueOf(jsonObjectCity),CityBean.class);
                cities.add(cityBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
