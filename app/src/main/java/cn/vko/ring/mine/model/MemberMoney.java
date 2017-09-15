package cn.vko.ring.mine.model;

import java.io.Serializable;

/**
 * Description:
 * Created by $Agree on 2016/11/3.17:01
 */
public class MemberMoney implements Serializable {

    private int code;
    private String msg;
    private long stime;
    private Data data;

    public long getStime() {
        return stime;
    }
    public void setStime(long stime) {
        this.stime = stime;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }
}
