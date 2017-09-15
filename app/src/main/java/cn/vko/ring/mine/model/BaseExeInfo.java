package cn.vko.ring.mine.model;

import java.io.Serializable;

/**
 * Created by A on 2016/12/13.
 */
public class BaseExeInfo implements Serializable {

    private int code;
    private String msg;
    private long stime;
    private String data;
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
    public long getStime() {
        return stime;
    }
    public void setStime(long stime) {
        this.stime = stime;
    }
    public String isData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }


}
