package cn.vko.ring.study.model;

import java.io.Serializable;

/**
 * Created by A on 2017/7/13.
 */
public class LocationCityInfo implements Serializable {
    private String sourceId;

    public String getSourceId() {
        return sourceId;
    }
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
