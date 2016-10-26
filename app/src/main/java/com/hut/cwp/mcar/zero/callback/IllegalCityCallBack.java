package com.hut.cwp.mcar.zero.callback;

import com.hut.cwp.mcar.zero.bean.ProvinceBean;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Just on 2016/10/12.
 */

public abstract class IllegalCityCallBack extends Callback<List<ProvinceBean>> {
    @Override
    public List<ProvinceBean> parseNetworkResponse(Response response, int id) throws Exception {
        String str=response.body().string();
        JSONObject jsonObject=new JSONObject(str);
        String error_code=jsonObject.getString("error_code");
        List<ProvinceBean> provinces=new ArrayList<ProvinceBean>();
        if (error_code.equals("0")) {
            JSONObject resultJsonObject = jsonObject.getJSONObject("result");
            Iterator citiesIterator = resultJsonObject.keys();
            while (citiesIterator.hasNext()) {
                String provinceStr = (String) citiesIterator.next();//得到的是省级（直辖市）简写
                JSONObject provinceJsonObject=null;
                try {
                    provinceJsonObject = resultJsonObject.getJSONObject(provinceStr);
                } catch (JSONException e) {
                    continue;
                }
                if (provinceJsonObject!=null) {
                    ProvinceBean provinceBean = new ProvinceBean();
                    provinceBean.setProvince(provinceJsonObject.getString("province"));
                    JSONArray citiesJsonArray = provinceJsonObject.getJSONArray("citys");
                    provinceBean.setCities(citiesJsonArray);
                    provinces.add(provinceBean);
                }
            }
        }
        return provinces;
    }
}
