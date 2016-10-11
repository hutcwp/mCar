package com.hut.cwp.mcar.zero.other;

import android.os.Bundle;

import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.Response;

/**
 * Created by Just on 2016/10/11.
 */

public abstract class OilCityCallback extends Callback<Bundle> {
    @Override
    public Bundle parseNetworkResponse(Response response, int id) throws Exception {
        JSONObject backJsonObject=new JSONObject(response.body().string());
        Bundle bundle=new Bundle();
        String error_code=backJsonObject.getString("error_code");
        bundle.putString("error_code",error_code);
        if(error_code.equals("0")) {
            //??  为什么是jsonArray（数组）
            JSONArray jsonArray=backJsonObject.getJSONObject("result").getJSONArray("list");
            for (int i=0,l=jsonArray.length();i<l;i++) {
                JSONObject temp=jsonArray.getJSONObject(i);
                bundle.putString("p0",temp.getString("p0").substring(0,4));
                bundle.putString("p90",temp.getString("p90").substring(0,4));
                bundle.putString("p93",temp.getString("p93").substring(0,4));
                bundle.putString("p97",temp.getString("p97").substring(0,4));
                String time= Calendar.getInstance().getTime().toString();
                bundle.putString("re_time",time.substring(11,16));
            }
        }
        return bundle;
    }
}
