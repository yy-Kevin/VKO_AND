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
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.umeng.EventCountAction;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.ContainsEmojiEditText;
import cn.vko.ring.common.widget.XGridView;
import cn.vko.ring.home.activity.AdActivity;
import cn.vko.ring.home.model.AD;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.home.presenter.AdPresenter;
import cn.vko.ring.im.config.preference.Preferences;
import cn.vko.ring.im.session.SessionHelper;
import cn.vko.ring.im.utils.DemoCache;
import cn.vko.ring.study.activity.CompCourseListActivity;
import cn.vko.ring.study.activity.CompCourseVideoAndTestActivity;
import cn.vko.ring.study.activity.SeacherCourseFromVoiceActivity;
import cn.vko.ring.study.activity.SyncCourseListActivity;
import cn.vko.ring.study.model.BaseSubjectInfoCourse;
import cn.vko.ring.study.model.ComprehensiveCourses;
import cn.vko.ring.study.model.KnowledgePointK1;
import cn.vko.ring.study.model.ParamData;
import cn.vko.ring.study.model.SubjectInfoCourse;
import cn.vko.ring.study.presenter.CusetemPresenter;
import cn.vko.ring.study.presenter.MySubjectPersoner;
import cn.vko.ring.study.presenter.RecommondKnowledgePresenter;
import cn.vko.ring.study.widget.FlowLayout;
import cn.vko.ring.utils.ACache;


/**
 * @ClassName: StudyFragment
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-10-29 下午2:23:27
 */
public class Study1Fragment extends BaseFragment implements
        MySubjectPersoner.ISelectSubjectlistener, RecommondKnowledgePresenter.IKnowlegeListener,UIDataListener<BaseSubjectInfoCourse> {

    public static String SUBJECT = "SUBJECT";

    @Bind(R.id.layout_subject)
    public LinearLayout layout_subject;
    @Bind(R.id.et_seacher)
    public ContainsEmojiEditText et_seacher;
    @Bind(R.id.flow_lay)
    public FlowLayout mFlowLay;
    @Bind(R.id.iv_seacher_photo)
    public ImageView iv_seacher_photo;
    @Bind(R.id.error_refresh)
    public TextView errorRefreshTv;
    @Bind(R.id.top_lay)
    public RelativeLayout topLayout;
    @Bind(R.id.iv_ad)
    public ImageView adIcon;
    @Bind(R.id.scroll_view)
    public ScrollView scrollView;

	/*@Bind(R.id.gv_subject)
	public XGridView mGridView;*/

    private VolleyUtils<BaseSubjectInfoCourse> volley;
    private List<SubjectInfoCourse> listSubject;
    private List<KnowledgePointK1> listRecommoned;

    private RecommondKnowledgePresenter recommondKnowledgePresenter;
    private MySubjectPersoner mySubjectPersoner;

    private BaseSubjectInfoCourse baseSubjectInfoCourse;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            getRecommondKnow();

        }
    };

    @Override
    public int setContentViewId() {
        return R.layout.fragment_study1;
    }
    /*
     * @Description: TODO
     */
    @Override
    public void initView(View root) {
//		EventBus.getDefault().register(this);
        volley = new VolleyUtils<BaseSubjectInfoCourse>(atx,BaseSubjectInfoCourse.class);
        volley.setUiDataListener(this);
        et_seacher.setInputType(InputType.TYPE_NULL);

    }

    @OnClick(R.id.iv_ad)
    public void onAdClicked(){
        StartActivityUtil.startActivity(getActivity(), AdActivity.class);
    }
    public void dealAd() {
        if(TextUtils.isEmpty(VkoConfigure
                .getConfigure(getActivity()).getString(AD.AD_START_TIME))||TextUtils.isEmpty(VkoConfigure.getConfigure(getActivity())
                .getString(AD.AD_END_TIME))){
            return;
        }
        long startTime = Long.parseLong(VkoConfigure
                .getConfigure(getActivity()).getString(AD.AD_START_TIME));
        long endTime = Long.parseLong(VkoConfigure.getConfigure(getActivity())
                .getString(AD.AD_END_TIME));
        long nowTime = System.currentTimeMillis();
        if(adIcon==null)return;
        if (nowTime > startTime && nowTime < endTime) {
            adIcon.setVisibility(View.VISIBLE);
            refreshAdState();
        } else {
            adIcon.setVisibility(View.GONE);
        }
    }
    public void refreshAdState() {
        if (!VkoConfigure.getConfigure(getActivity()).getBoolean(Constants.HAS_LOOK, false)) {
            adIcon.setImageResource(R.drawable.ad_point);
        }else{
            adIcon.setImageResource(R.drawable.ad_no_point);
        }
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        super.initData();
        UserInfo user = VkoContext.getInstance(getActivity()).getUser();
        if (user != null) {
            SUBJECT = "SUBJECT" + user.getUserId() + user.getGradeId();
        }
        baseSubjectInfoCourse = (BaseSubjectInfoCourse) ACache.get(getActivity()).getAsObject(SUBJECT);
        if (baseSubjectInfoCourse != null
                && baseSubjectInfoCourse.getData().size() > 0) {
            addSubjectView(baseSubjectInfoCourse.getData());
        }
        getSubjectData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                getAd();
            }
        }, 1000);

    }
    private void getAd() {
        // TODO Auto-generated method stub
        new AdPresenter(atx).getAds(callBack);
    }
    AdPresenter.ADCallBack callBack = new AdPresenter.ADCallBack() {

        @Override
        public void onAdResult(AD.ADactivity.ADitem item) {
            // TODO Auto-generated method stub
            if (item != null && !TextUtils.isEmpty(item.getActId())) {
                dealAd();
            }
        }
    };
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        onConfigRefresh();
    }

    public void onConfigRefresh() {
        if (recommondKnowledgePresenter != null) {
            recommondKnowledgePresenter.clear();
            recommondKnowledgePresenter.refreshData();
        }
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

    private void getRecommondKnow() {
        if(recommondKnowledgePresenter == null){
            recommondKnowledgePresenter = new RecommondKnowledgePresenter(mFlowLay, new ParamData.Builder(getActivity()).build(), getActivity());
//			recommondKnowledgePresenter = new RecommondKnowledgePresenter(mFlowLay, new ParamData.Builder(getActivity()).build(), getActivity());
            recommondKnowledgePresenter.setiKnowlistener(this);
        }
    }

    public void getSubjectData() {
        Uri.Builder builder = volley.getBuilder(ConstantUrl.VK_STUDY_SUBJECT);
        if (VkoContext.getInstance(atx).isLogin()) {
            builder.appendQueryParameter("token", VkoContext.getInstance(atx)
                    .getUser().getToken());
        }
        volley.sendGETRequest(true,builder);
    }


    /*
     * @Description: TODO
     */
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        EventCountAction.onFragRCount(this.getClass());
//		EventBus.getDefault().register(this);
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

        EventBus.getDefault().unregister(this);

        // wmv.postDelayed(new Runnable() {
        // @Override
        // public void run() {
        // // TODO Auto-generated method stub
        // wmv.stopAni();
        // }
        // }, 300);
    }

    @Override
    public void selectSubject(SubjectInfoCourse subjectInfo) {
        VkoConfigure.getConfigure(atx).put("subjectId", subjectInfo.getId());
        if (VkoContext.getInstance(atx).doLoginCheckToSkip(atx)) {
            return;
        }

        if (!TextUtils.isEmpty(subjectInfo.getId())&&subjectInfo.getId().equals("-1")){
            Toast.makeText(getActivity(),"跳到专题",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!subjectInfo.isGotoSync()) {
            // 去综合课程
            goToCompActivity(subjectInfo);
        } else {
            // 去同步课程
            goToCourseActivity(subjectInfo);
        }
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

    @OnClick(R.id.et_seacher)
    public void onSeacher(){
        Intent intent = new Intent(getActivity(), SeacherCourseFromVoiceActivity.class);
        atx.startActivity(intent);
    }

    @OnClick(R.id.iv_seacher_photo)
    public void seacher_photo() {
        // 拍照搜题
        // new MyPhotoSeacherPersoner(iv_seacher_photo, atx);
        Bundle bundle = new Bundle();
        bundle.putString("FROM", "SEARCH");
		/*StartActivityUtil.startActivity(atx, CameraActivity.class, bundle,
				Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
    }


    private void showSubjectView() {
        if (errorRefreshTv != null) {
            errorRefreshTv.setVisibility(View.INVISIBLE);
            layout_subject.setVisibility(View.VISIBLE);
        }
    }

    private void addSubjectView(List<SubjectInfoCourse> listSubject) {
		/*if (listSubject == null || listSubject.size() < 7) {


		} else {
			if (topLayout != null) {
				topLayout.setPadding(0, 0, 0, 0);
			}
		}*/
        if (topLayout != null) {
            topLayout.setPadding(0, 0, 0, (int) getResources()
                    .getDimension(R.dimen.dimen30));
        }
        errorRefreshTv.setVisibility(View.GONE);
        if(mySubjectPersoner == null){
            mySubjectPersoner = new MySubjectPersoner(layout_subject, getActivity());
            mySubjectPersoner.setIselectSubject(this);
        }
//		mySubjectPersoner.initGridView(listSubject,mGridView);
        mySubjectPersoner.initData(listSubject);
    }

    private void showError() {
        Toast.makeText(atx, "暂无数据,稍后重试",Toast.LENGTH_LONG).show();
        if (errorRefreshTv != null) {
            errorRefreshTv.setVisibility(View.VISIBLE);
            layout_subject.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.error_refresh)
    public void onErrorRefreshClick() {
        getSubjectData();
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
    @Override
    public void onClickKnowleg(KnowledgePointK1 k1) {
        if (k1 == null) {
            return;
        }
        // if
        // (!VkoContext.getInstance(getActivity()).doLoginCheckToSkip(getActivity()))
        // {
        ComprehensiveCourses compre = new ComprehensiveCourses();
        compre.setId(k1.getId());
        compre.setName(k1.getName());
        SubjectInfoCourse sf = new SubjectInfoCourse();
        sf.setId(k1.getSubjectId());
        compre.setSubjectInfo(sf);
        Intent intent = new Intent(atx, CompCourseVideoAndTestActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyComPressAndTestActivity", compre);
        intent.putExtras(bundle);
        atx.startActivity(intent);
        // }
    }

    @Subscribe
    public void onEventMainThread(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (msg.equals(Constants.SUBJECT_REFRESH)) {
                getSubjectData();
            }else
            if (msg.equals(Constants.RECOMMOND_REFRESH)) {
                onConfigRefresh();
            }else
            if(msg.equals("YXLOGINSUCCESS")){
                if(cuset != null){
                    SessionHelper.startP2PSession(getActivity(),"13886257916838578");
                    cuset = null;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDataChanged(BaseSubjectInfoCourse response) {
        if (response != null) {
            if (response.getData() != null && response.getData().size() > 0) {
                showSubjectView();
                listSubject = response.getData();
//				SubjectInfoCourse zhuanti = new SubjectInfoCourse();
//				zhuanti.setName("专题");
//				zhuanti.setVname("免费");
//				zhuanti.setId("-1");
//				listSubject.add(zhuanti);
                ACache.get(atx).put(SUBJECT, response);
                if(layout_subject == null) return;
                addSubjectView(listSubject);
            } else {
                showError();
            }
        } else {
            showError();
        }
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onErrorHappened(String errorCode, String errorMessage) {
        mHandler.sendEmptyMessage(0);
        if (layout_subject != null
                && layout_subject.getVisibility() == View.VISIBLE
                && layout_subject.getChildCount() > 0) {
            return;
        }
        showError();
    }
}
