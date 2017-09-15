package cn.vko.ring.im.session.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.common.viewpager.PagerSlidingTabStrip;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.classgroup.adapter.CourseTabPagerAdapter;
import cn.vko.ring.classgroup.model.TaskItemData;
import cn.vko.ring.classgroup.model.UpDataResult;
import cn.vko.ring.classgroup.presenter.UpdataToServerPresenterImp;
import cn.vko.ring.classgroup.view.UpDataView;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.im.session.extension.CourseDataParam;
import cn.vko.ring.im.session.extension.CourseMsg;


public class SendCourseActivity extends BaseActivity implements ViewPager.OnPageChangeListener, UpDataView {

    public static final String SELECT_COURSE = "select_course";
    public static final String SESSSION_ID="session_id";

    TextView textView;
    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.main_tab_pager)
    ViewPager mainTabPager;
    @Bind(R.id.btn_send)
    TextView btnSend;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.imagebtn)
    ImageView imagebtn;

    private int scrollState;
    private String groupId;

    private CourseTabPagerAdapter adapter;
    private List<CourseMsg> selectCourses;
    private UpdataToServerPresenterImp updataToServer;

    public static void startActivityForResult(Activity activity, int reqCode,String sessionId) {
        Intent intent = new Intent();
        intent.setClass(activity, SendCourseActivity.class);
        intent.putExtra(SESSSION_ID,sessionId);
        activity.startActivityForResult(intent, reqCode);
    }

    private void selectFile(List<CourseMsg> courseMsgs) {
        Intent intent = new Intent();
        CourseDataParam param = new CourseDataParam();
        param.setData(courseMsgs);
        intent.putExtra(SELECT_COURSE, param);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.btn_send)
    public void onSendClicked() {
        upDataToServer(selectCourses);

    }

    private void upDataToServer(List<CourseMsg> selectCourses) {
        if (updataToServer==null){
            updataToServer = new UpdataToServerPresenterImp(this,this);

        }
        if (!TextUtils.isEmpty(groupId))
        updataToServer.upData(selectCourses,groupId);
    }

    @Override
    public int setContentViewResId() {
        return R.layout.activity_send_course;
    }

    @Override
    public void initView() {
        getIntentData();
        setupPager();
        setupTabs();
        btnSend.setVisibility(View.GONE);
        tvTitle.setText("发送课程");

    }

    private void getIntentData() {
        Intent intent = getIntent();
        groupId = intent.getStringExtra(SESSSION_ID);

    }

    @OnClick(R.id.iv_back)
    public void goBack(){
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void setCallBack(CourseMsg msg) {
        if (selectCourses == null) {
            selectCourses = new ArrayList<>();
        }
        if (selectCourses.size() > 0)
            for (CourseMsg cc : selectCourses) {
                //移除
                if (cc.getCourseId().equals(msg.getCourseId()) && !msg.isChecked()) {
                    selectCourses.remove(cc);
                    break;
                }


            }
        //添加
        if (msg.isChecked()) {
            selectCourses.add(msg);
        }

        if (selectCourses.size()>0){
            btnSend.setVisibility(View.VISIBLE);
            btnSend.setText(getString(R.string.send_course, selectCourses.size()));
        }else {
            btnSend.setVisibility(View.GONE);
        }

    }

    /**
     * 设置viewPager
     */
    private void setupPager() {
        // CACHE COUNT
        adapter = new CourseTabPagerAdapter(getSupportFragmentManager(), this, mainTabPager);
        mainTabPager.setOffscreenPageLimit(adapter.getCacheCount());
        // page swtich animation
//        mainTabPager.setPageTransformer(true, new FadeInOutPageTransformer());
        // ADAPTER
        mainTabPager.setAdapter(adapter);
        // TAKE OVER CHANGE
        mainTabPager.setOnPageChangeListener(this);
    }

    /**
     * 设置tab条目
     */
    private void setupTabs() {
        tabs.setOnCustomTabListener(new PagerSlidingTabStrip.OnCustomTabListener() {
            @Override
            public int getTabLayoutResId(int position) {
                return R.layout.tab_layout_main;
            }

            @Override
            public boolean screenAdaptation() {
                return true;
            }
        });
        tabs.setViewPager(mainTabPager);
        tabs.setOnTabClickListener(adapter);
        tabs.setOnTabDoubleTapListener(adapter);
    }


    @Override
    public void initData() {

    }




    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // TO TABS
        tabs.onPageScrolled(position, positionOffset, positionOffsetPixels);
        // TO ADAPTER
        adapter.onPageScrolled(position);
    }

    @Override
    public void onPageSelected(int position) {
        // TO TABS
        tabs.onPageSelected(position);

        selectPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        tabs.onPageScrollStateChanged(state);

        scrollState = state;

        selectPage(mainTabPager.getCurrentItem());
    }

    private void selectPage(int page) {
        // TO PAGE
        if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
            adapter.onPageSelected(mainTabPager.getCurrentItem());
        }
    }


    @Override
    public void over(UpDataResult result) {
        if (null!=result&&result.getData()!=null){
            List<CourseMsg> courseMsgs = new ArrayList<>();
            for (CourseMsg msg :selectCourses){
                courseMsgs.add(msg);

            }
            selectCourses.clear();
           for (TaskItemData.TaskItem entity : result.getData()){
               CourseMsg msg  = new CourseMsg();
               msg.setType(entity.getType());
               msg.setTitle(entity.getTitle());
               msg.setTeacherName(entity.getUserName());
               msg.setTeacherId(entity.getTeacherId());
               msg.setObjId(entity.getObjId());
               msg.setTaskId(entity.getId());
               msg.setUrl(entity.getUrl());
               msg.setObjInfo(entity.getDescription());
               msg.setStartTime(entity.getStartTime());
               msg.setEndTime(entity.getEndTime());
               T: for (CourseMsg m : courseMsgs){
                   if (m.getCourseId().equals(msg.getObjId())){

                       msg.setCourseType(m.getCourseType());
                       break  T;
                   }
               }
               selectCourses.add(msg);
           }
            selectFile(selectCourses);
        }
    }
}
