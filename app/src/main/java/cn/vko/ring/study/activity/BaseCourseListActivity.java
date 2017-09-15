package cn.vko.ring.study.activity;

import android.view.View;
import android.widget.AdapterView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.OnClick;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.study.model.SubjectInfoCourse;

/**
 * 同步综合课列表基类
 * Created by shikh on 2016/5/10.
 */
public abstract class BaseCourseListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private XListView mListView;
//    public BaseListAdapter mAdapter;
    public SubjectInfoCourse subjectInfo;
    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mListView = setXListView();
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        getCourseListTask();
    }

    public abstract void getCourseListTask();

    public abstract XListView setXListView();

    @Subscribe
    public void onEventMainThread(String event) {
        if (event.equals(Constants.REFRESH_SYNCTEST_SYNC_DATA_VIDEO)) {
            subjectInfo.setRefreshHomeSubject(true);
            getCourseListTask();
        }
        if (event.equals(Constants.REFRESH_COURSE_STAR_DATA)) {
            subjectInfo.setRefreshHomeSubject(false);
            getCourseListTask();
        } else if (event.equals(Constants.OPEN_MEMBER)) {
            getCourseListTask();
//        } else if (event.equals(Constants.LOCKCHAPTER)) {//解锁章节
//            mKnowledgeSyncTreePresenter.lockChapter();
        }
    }


    @OnClick(R.id.iv_back)
    public void goback() {
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}


