package cn.vko.ring.classgroup.view;

import java.util.List;

import cn.vko.ring.home.model.Pager;
import cn.vko.ring.im.session.extension.CourseMsg;

/**
 * desc:
 * Created by jiarh on 16/5/12 11:46.
 */
public  interface CourseListView {

    void getShowData(Pager pager,List<CourseMsg> datas);
    void onClickItem(CourseMsg t,int position);
}
