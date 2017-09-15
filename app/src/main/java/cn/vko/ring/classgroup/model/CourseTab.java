package cn.vko.ring.classgroup.model;

import cn.vko.ring.R;
import cn.vko.ring.classgroup.fragment.GroupCourseFragment;
import cn.vko.ring.classgroup.fragment.PerCourseFragment;
import cn.vko.ring.im.main.fragment.MainTabFragment;
import cn.vko.ring.im.main.model.TAB;

/**
 * desc:
 * Created by jiarh on 16/5/12 10:06.
 */
public enum  CourseTab implements TAB{

    PER_COURSE(0, CourseTabId.PER_COURSE, PerCourseFragment.class, R.string.per_course, R.layout.coursel_list_lay),
    GROUP_COURSE(1, CourseTabId.GROUP_COURSE, GroupCourseFragment.class, R.string.group_course, R.layout.coursel_group_list_lay);

    public final int tabIndex;

    public final int reminderId;

    public final Class<? extends MainTabFragment> clazz;

    public final int resId;

    public final int fragmentId;

    public final int layoutId;

    CourseTab(int index, int reminderId, Class<? extends MainTabFragment> clazz, int resId, int layoutId) {
        this.tabIndex = index;
        this.reminderId = reminderId;
        this.clazz = clazz;
        this.resId = resId;
        this.fragmentId = index;
        this.layoutId = layoutId;
    }

    public static final CourseTab fromReminderId(int reminderId) {
        for (CourseTab value : CourseTab.values()) {
            if (value.reminderId == reminderId) {
                return value;
            }
        }

        return null;
    }

    public static final CourseTab fromTabIndex(int tabIndex) {
        for (CourseTab value : CourseTab.values()) {
            if (value.tabIndex == tabIndex) {
                return value;
            }
        }

        return null;
    }

    @Override
    public int getLayoutId() {
        return layoutId;
    }
}
