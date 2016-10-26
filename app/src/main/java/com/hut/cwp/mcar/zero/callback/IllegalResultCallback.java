package com.hut.cwp.mcar.zero.callback;

import com.google.gson.Gson;
import com.hut.cwp.mcar.zero.bean.IllegalResultBean;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Just on 2016/10/15.
 */

public abstract class IllegalResultCallback extends Callback<List<IllegalResultBean>> {

    @Override
    public List<IllegalResultBean> parseNetworkResponse(Response response, int id) throws Exception {
        List<IllegalResultBean> list=new ArrayList<>();
        String str=response.body().string();
        JSONObject jsonObject=new JSONObject(str);
        String error_code=jsonObject.getString("error_code");
        if (error_code.equals("0")) {
            JSONObject resultJsonObject = jsonObject.getJSONObject("result");
            Iterator citiesIterator = resultJsonObject.keys();
            while (citiesIterator.hasNext()) {
                String keyStr = (String) citiesIterator.next();
                if (keyStr.equals("lists")) {
                    JSONArray resultList=resultJsonObject.getJSONArray("lists");
                    for (int i=0,l=resultList.length();i<l;i++) {
                        try {
                            JSONObject jsonObjectResult=resultList.getJSONObject(i);
                            IllegalResultBean illegalResultBean=new Gson().fromJson(String.valueOf(jsonObjectResult),IllegalResultBean.class);
                            list.add(illegalResultBean);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }
        return list;
    }
}
