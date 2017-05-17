package com.hut.cwp.mcar.activitys.business.callback;

import android.os.Bundle;

import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.Response;

/**
 * okhttp3的回调接口
 */

public abstract class OilCityCallback extends Callback<Bundle> {

    /**
     * {
     *"showapi_res_code": 0, 返回0表示正确，其他的代表异常情况
     *"showapi_res_error": "",
     *"showapi_res_body": {
     *"ret_code": 0,
     *"list": [
     *{
     *"prov": "北京",
     *"p90": "5.84(京89号)",
     *"p0": "5.87",
     *"p95": "6.64",
     *"p97": "6.64(京95号)",
     *"p89": "6.07",
     *"p92": "6.23",
     *"ct": "2017-05-14 07:02:28.983",
     *"p93": "6.23(京92号)"
     *}]
     *}
     *}
     */
    @Override
    public Bundle parseNetworkResponse(Response response, int id) throws Exception {

        String result = response.body().string();
        JSONObject backJsonObject = new JSONObject(result);

        Bundle bundle = new Bundle();
        String showapi_res_code = backJsonObject.getString("showapi_res_code");
        bundle.putString("showapi_res_code", showapi_res_code);
        if (showapi_res_code.equals("0")) {
            JSONArray jsonArray = backJsonObject.getJSONObject("showapi_res_body").getJSONArray("list");
            for (int i = 0, l = jsonArray.length(); i < l; i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                bundle.putString("p0", temp.getString("p0").substring(0, 4));
                bundle.putString("p90", temp.getString("p90").substring(0, 4));
                bundle.putString("p93", temp.getString("p93"));
                bundle.putString("p97", temp.getString("p97").substring(0, 4));
                String time = Calendar.getInstance().getTime().toString();
                bundle.putString("ct", time.substring(11, 16));
            }
        }

        return bundle;
    }
}
