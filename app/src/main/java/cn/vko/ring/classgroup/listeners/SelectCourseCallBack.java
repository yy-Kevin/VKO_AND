package cn.vko.ring.classgroup.listeners;

import cn.vko.ring.im.session.extension.CourseMsg;

/**
 * desc:
 * Created by jiarh on 16/5/12 14:52.
 */
public interface SelectCourseCallBack {
    //这里获取到的是点击的课程
    void getSelected(CourseMsg course);
}
