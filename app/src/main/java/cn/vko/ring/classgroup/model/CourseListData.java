package cn.vko.ring.classgroup.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;
import cn.vko.ring.home.model.Pager;
import cn.vko.ring.im.session.extension.CourseMsg;

/**
 * desc:
 * Created by jiarh on 16/5/12 11:22.
 */
public class CourseListData extends BaseResponseInfo {

    private CourseData data;

    public  CourseData getData() {
        return data;
    }

    public void setData(CourseData data) {
        this.data = data;
    }

    public static class CourseData implements Serializable {
        private Pager pager;
        private List<CourseMsg> courseList;

        public List<CourseMsg> getCourseList() {
            return courseList;
        }

        public void setCourseList(List<CourseMsg> courseList) {
            this.courseList = courseList;
        }

        public Pager getPager() {
            return pager;
        }

        public void setPager(Pager pager) {
            this.pager = pager;
        }
    }
}

