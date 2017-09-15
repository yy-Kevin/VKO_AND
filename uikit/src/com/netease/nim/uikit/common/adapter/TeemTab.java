package com.netease.nim.uikit.common.adapter;


import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.fragment.CourseFragment;
import com.netease.nim.uikit.common.fragment.TestFragment;
import com.netease.nim.uikit.common.reminder.ReminderId;
import com.netease.nim.uikit.session.fragment.TeamMessageFragment;


public enum TeemTab {
    CLASS_TEEM(0, ReminderId.SESSION, TeamMessageFragment.class, R.string.teem_class, R.layout.nim_team_message_activity),
    COURSE(1, ReminderId.CONTACT, CourseFragment.class, R.string.teem_course, R.layout.fragment_course),
   TEST(2,ReminderId.INVALID, TestFragment.class, R.string.teem_test, R.layout.fragment_test);

    public final int tabIndex;

    public final int reminderId;

    public final Class clazz;

    public final int resId;

    public final int fragmentId;

    public final int layoutId;

    TeemTab(int index, int reminderId, Class clazz, int resId, int layoutId) {
        this.tabIndex = index;
        this.reminderId = reminderId;
        this.clazz = clazz;
        this.resId = resId;
        this.fragmentId = index;
        this.layoutId = layoutId;
    }

    public static final TeemTab fromReminderId(int reminderId) {
        for (TeemTab value : TeemTab.values()) {
            if (value.reminderId == reminderId) {
                return value;
            }
        }

        return null;
    }

    public static final TeemTab fromTabIndex(int tabIndex) {
        for (TeemTab value : TeemTab.values()) {
            if (value.tabIndex == tabIndex) {
                return value;
            }
        }

        return null;
    }
}
