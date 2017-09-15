package cn.vko.ring.im.session.extension;

import java.io.Serializable;
import java.util.List;

/**
 * desc:
 * Created by jiarh on 16/5/3 14:39.
 */
public class TestDataParam implements Serializable {
    private List<CourseMsg> data;

    public List<CourseMsg> getData() {
        return data;
    }

    public void setData(List<CourseMsg> data) {
        this.data = data;
    }
}
