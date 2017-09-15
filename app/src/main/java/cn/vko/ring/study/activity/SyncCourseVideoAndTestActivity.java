package cn.vko.ring.study.activity;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.widget.NoScrollViewPager;
import cn.vko.ring.common.widget.SimpleViewPagerIndicator;
import cn.vko.ring.home.adapter.TabFragmentPagerAdapter;
import cn.vko.ring.study.fragment.CourseTestFragment;
import cn.vko.ring.study.fragment.SyncCourseVideoFragment;
import cn.vko.ring.study.model.KnowledgeSection;
import cn.vko.ring.study.model.ParamData;

/**
 * 同步课的视频和练习
 * Created by shikh on 2016/5/10.
 */
public class SyncCourseVideoAndTestActivity extends BaseActivity {
    private String[] titles = new String[] {"视频课程","练习测评"};


    @Bind(R.id.tv_title)
    public TextView tv_title;

    @Bind(R.id.layout_indicator)
    public SimpleViewPagerIndicator mIndicator;

    @Bind(R.id.id_stickynavlayout_viewpager)
    public NoScrollViewPager mViewPager;

    private TabFragmentPagerAdapter mAdapter;
    private List<Fragment> fragments;

    private SyncCourseVideoFragment videoFragment;
    private CourseTestFragment testFragment;

    public KnowledgeSection knowsection;
    private int type = 0;

    @Override
    public int setContentViewResId() {
        return R.layout.activity_courese_test_video_list;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mIndicator.setBoolean(true);
        mIndicator.setTitles(titles,mViewPager);

        fragments = new ArrayList<Fragment>();
        videoFragment=new SyncCourseVideoFragment();
        testFragment=new CourseTestFragment();
        fragments.add(videoFragment);
        fragments.add(testFragment);
        mViewPager.setClipChildren(false);
        mViewPager.setOffscreenPageLimit(2);
        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragments);//new TabFragmentPagerAdapter(getFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        type = getIntent().getExtras().getInt("TYPE");
        knowsection = (KnowledgeSection) getIntent().getExtras()
                .getSerializable("KnowledgeSection");
        tv_title.setText(knowsection.getChapterName()+"");
        if (knowsection!=null) {
            testFragment.setParamData(new ParamData.Builder(this)
                    .setSectionId(knowsection.getChapterId())
                    .setBookId(knowsection.getBookId()).setSubjectId(knowsection.getSubjectInfo()== null?null:knowsection.getSubjectInfo().getId()).build());
            videoFragment.setArguments(getIntent().getExtras());
        }
    }
    @Subscribe
    public void onEventMainThread(String event) {
        if (event.equals(Constants.REFRESH_SYNCTEST_SYNC_DATA_VIDEO)) {
            if(videoFragment!=null){
                videoFragment.getKnowDataTask(knowsection);
            }
        }else if(event.equals(Constants.LOCKCHAPTER)){
            type = 1;
            if(videoFragment != null){
                videoFragment.setType(type);
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.iv_back)
    public void goBack() {
        finish();
    }
}
