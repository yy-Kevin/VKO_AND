package cn.vko.ring.study.model;

import cn.vko.ring.common.base.BaseResponseInfo;

/**
 * Created by shikh on 2016/5/27.
 */
public class StringRequestInfo extends BaseResponseInfo {
    private  String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
