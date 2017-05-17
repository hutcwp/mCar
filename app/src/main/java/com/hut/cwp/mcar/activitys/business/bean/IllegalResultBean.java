package com.hut.cwp.mcar.activitys.business.bean;

import java.util.List;

/**
 * Created by hutcep .
 */

public class IllegalResultBean   {

    /**
     * total_money : 200
     * count : 1
     * total_score : 3
     * historys : [{"officer":"","city_name":"测试成功","city_id":189,"occur_area":"测试成功","occur_date":"2014-08-27 09:51:00","fen":3,"status":"N","info":"测试成功","id":3758969,"money":200,"car_id":2930454,"province_id":14,"code":"13010"}]
     * status : 2001
     */

    private int total_money;
    private int count;
    private int total_score;
    private int status;
    private List<HistorysBean> historys;

    public int getTotal_money() {
        return total_money;
    }

    public void setTotal_money(int total_money) {
        this.total_money = total_money;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<HistorysBean> getHistorys() {
        return historys;
    }

    public void setHistorys(List<HistorysBean> historys) {
        this.historys = historys;
    }


}
