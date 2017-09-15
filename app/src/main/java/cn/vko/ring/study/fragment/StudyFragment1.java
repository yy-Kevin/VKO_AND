/**
 * Copyright © 2015 cn.vko.com. All rights reserved.
 *
 * @Title: StudyFragment.java
 * @Prject: SvkoCircle
 * @Package: cn.vko.ring.mine.fragment
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-10-29 下午2:23:27
 * @version: V1.0
 */
package cn.vko.ring.study.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.auth.LoginInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.umeng.EventCountAction;
import cn.vko.ring.common.widget.NoScrollViewPager;
import cn.vko.ring.common.widget.SimpleViewPagerIndicator;
import cn.vko.ring.im.config.preference.Preferences;
import cn.vko.ring.im.session.SessionHelper;
import cn.vko.ring.im.utils.DemoCache;
import cn.vko.ring.mine.adapter.CourseFragmentPagerAdapter;
import cn.vko.ring.mine.fragment.ComprehensiveFragment;
import cn.vko.ring.mine.fragment.LocalCourseFragment;
import cn.vko.ring.mine.fragment.SynchrCourseFragment;
import cn.vko.ring.study.activity.CompCourseListActivity;
import cn.vko.ring.study.activity.SyncCourseListActivity;
import cn.vko.ring.study.model.BaseSubjectInfoCourse;
import cn.vko.ring.study.model.KnowledgePointK1;
import cn.vko.ring.study.model.SubjectInfoCourse;
import cn.vko.ring.study.presenter.CusetemPresenter;
import cn.vko.ring.study.presenter.MySubjectPersoner;
import cn.vko.ring.study.presenter.RecommondKnowledgePresenter;


/**
 * @ClassName: StudyFragment
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-10-29 下午2:23:27
 */
public class StudyFragment1 extends BaseFragment implements
        MySubjectPersoner.ISelectSubjectlistener, RecommondKnowledgePresenter.IKnowlegeListener,UIDataListener<BaseSubjectInfoCourse> {

    public static String SUBJECT = "SUBJECT";

    private String[] titles = new String[] {"培优直播","基础课程","特色课程"};
    //	private String[] titles = new String[] {"同步课程","综合课程"};
    @Bind(R.id.tv_title)
    public TextView tvTitle;
    @Bind(R.id.iv_back)
    public ImageView ivBack;
    @Bind(R.id.my_viewpager1)
    public NoScrollViewPager mViewPager;
    @Bind(R.id.layout_indicator1)
    public SimpleViewPagerIndicator mIndicator;

    private FragmentStatePagerAdapter mAdapter;
    private List<BaseFragment> fragments;



    @Override
    public int setContentViewId() {
        return R.layout.fragment1_study;
    }
    /*
     * @Description: TODO
     */
    @Override
    public void initView(View root) {
        mIndicator.setTitles(titles,mViewPager);
        tvTitle.setText("学习");
        ivBack.setVisibility(View.GONE);
    }



    @Override
    public void initData() {
        fragments = new ArrayList<BaseFragment>();
        fragments.add(new PeiyouFragment());
        fragments.add(new Study1Fragment());
        fragments.add(new SpecialFragment());
        mViewPager.setClipChildren(false);
        mViewPager.setOffscreenPageLimit(2);
//        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()){
//
//            @Override
//            public int getCount() {
//                return fragments.size();
//            }
//
//            @Override
//            public Fragment getItem(int position) {
//                return fragments.get(position);
//            }
//        };
        mAdapter= new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        };
        mViewPager.setAdapter(mAdapter);

    }



    /*
     *
     * @Description: TODO
     */
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

    }



    /*
     * @Description: TODO
     */
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        EventCountAction.onFragRCount(this.getClass());
        // wmv.resetAni();
    }

    /*
     * @Description: TODO
     */
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        EventCountAction.onFragPCount(this.getClass());
        // wmv.postDelayed(new Runnable() {
        // @Override
        // public void run() {
        // // TODO Auto-generated method stub
        // wmv.stopAni();
        // }
        // }, 300);
    }



    // 去综合课程
    private void goToCompActivity(SubjectInfoCourse subjectInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyComprehensiveupgradeActivity", subjectInfo);
        StartActivityUtil.startActivity(atx, CompCourseListActivity.class,
                bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    // 去同步课程
    private void goToCourseActivity(SubjectInfoCourse subjectInfo) {
        Bundle b = new Bundle();
        b.putSerializable("MyCourseSetionActivity", subjectInfo);
        StartActivityUtil.startActivity(atx, SyncCourseListActivity.class, b, Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }






    @Subscribe
    public void onEventMainThread(String msg) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClickKnowleg(KnowledgePointK1 k1) {

    }

    @Override
    public void selectSubject(SubjectInfoCourse info) {

    }

    @Override
    public void onDataChanged(BaseSubjectInfoCourse data) {

    }

    @Override
    public void onErrorHappened(String errorCode, String errorMessage) {

    }
    CusetemPresenter cuset;
    @OnClick(R.id.custom)
    public void onCusetemService() {
//		EaseLoginPresenter presenter = EaseLoginPresenter.getInstance();
//		presenter.createAccount(getActivity());
        if(VkoContext.getInstance(atx).isLogin()){
            if(VkoContext.getInstance(atx).getUser().getImToken() != null){
                if(DemoCache.getAccount() == null){
                    if(getLoginInfo() != null){
                        SessionHelper.startP2PSession(getActivity(),"13886257916838578");
                    }else{
                        cuset = new CusetemPresenter(getActivity());
                    }

                }else{
                    SessionHelper.startP2PSession(getActivity(),"13886257916838578");
                }

            }else{
                cuset = new CusetemPresenter(getActivity());
            }
        }else{
            Toast.makeText(atx,"登录后才能使用",Toast.LENGTH_SHORT).show();
        }
    }
    public LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }
}
