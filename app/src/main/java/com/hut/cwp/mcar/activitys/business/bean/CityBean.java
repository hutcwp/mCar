package com.hut.cwp.mcar.activitys.business.bean;

/**
 * Created by Just on 2016/10/9.
 */

public class CityBean {
    private String city_name;
    private String city_code;
    private String engine;//是否需要发动机号0不需要,1需要
    private String engineno;//需要几位发动机号0全部,1-9需要发动机号后N位
    private String classa;//是否需要车架号0不需要,1需要
    private String classno;//需要几位车架号0全部,1-9需要车架号后N位

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getEngineno() {
        return engineno;
    }

    public void setEngineno(String engineno) {
        this.engineno = engineno;
    }

    public String getClassa() {
        return classa;
    }

    public void setClassa(String classa) {
        this.classa = classa;
    }

    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }
}
