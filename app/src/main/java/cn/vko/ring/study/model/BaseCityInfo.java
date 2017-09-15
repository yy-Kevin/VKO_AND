package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.mine.model.CityInfo;

/**
 * Created by A on 2017/7/13.
 */
public class BaseCityInfo  implements Serializable {
    private int code;
    private String msg;
    private String stime;
    private LocationCityInfo data;
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
    public String getStime() {
        return stime;
    }
    public void setStime(String stime) {
        this.stime = stime;
    }
    public LocationCityInfo getData() {
        return data;
    }
    public void setData(LocationCityInfo data) {
        this.data = data;
    }


}
