package com.hut.cwp.mcar.zero.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Just on 2016/10/15.
 */

public class IllegalResultBean implements Parcelable {
    private String date;
    private String area;
    private String act;
    private String code;
    private String fen;
    private String money;
    private String handled;

    protected IllegalResultBean(Parcel in) {
        date = in.readString();
        area = in.readString();
        act = in.readString();
        code = in.readString();
        fen = in.readString();
        money = in.readString();
        handled = in.readString();
    }

    public static final Creator<IllegalResultBean> CREATOR = new Creator<IllegalResultBean>() {
        @Override
        public IllegalResultBean createFromParcel(Parcel in) {
            return new IllegalResultBean(in);
        }

        @Override
        public IllegalResultBean[] newArray(int size) {
            return new IllegalResultBean[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getHandled() {
        return handled;
    }

    public void setHandled(String handled) {
        this.handled = handled;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(area);
        dest.writeString(act);
        dest.writeString(code);
        dest.writeString(fen);
        dest.writeString(money);
        dest.writeString(handled);
    }
}
