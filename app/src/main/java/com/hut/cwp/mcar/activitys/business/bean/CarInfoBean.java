package com.hut.cwp.mcar.activitys.business.bean;
/**
 * Created by hutcwp on 2017/5/14.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */

public class CarInfoBean {
    private String chepaino;//车牌号
    private String chejiano;//车架号
    private String engineno;//发动机号
    private boolean checked = false; //是否选中

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getChepaino() {
        return chepaino;
    }

    public void setChepaino(String chepaino) {
        this.chepaino = chepaino;
    }

    public String getChejiano() {
        return chejiano;
    }

    public void setChejiano(String chejiano) {
        this.chejiano = chejiano;
    }

    public String getEngineno() {
        return engineno;
    }

    public void setEngineno(String engineno) {
        this.engineno = engineno;
    }
}
