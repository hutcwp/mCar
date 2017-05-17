package com.hut.cwp.mcar.activitys.business.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Just on 2016/10/25.
 */

public class FeedbackBean extends BmobObject {
    private String phone;
    private String content;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
