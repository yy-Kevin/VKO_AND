package cn.vko.ring.home.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.classgroup.fragment.ClassGroupragment;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.widget.NoScrollViewPager;
import cn.vko.ring.common.widget.dialog.VbDialog;
import cn.vko.ring.home.adapter.TabFragmentPagerAdapter;
import cn.vko.ring.home.listener.IMenuItemClickListener;
import cn.vko.ring.home.model.AD;
import cn.vko.ring.home.model.TabMenuInfo;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.home.presenter.AdPresenter;
import cn.vko.ring.home.presenter.PushMessagePresenter;
import cn.vko.ring.home.presenter.VersionUpdatePresenter;
import cn.vko.ring.home.widget.ChooseGradeDialog;
import cn.vko.ring.home.widget.TabMenuView;

import cn.vko.ring.im.config.preference.Preferences;
import cn.vko.ring.im.login.LogoutHelper;
import cn.vko.ring.im.main.fragment.HomeFragment;
import cn.vko.ring.im.main.model.Extras;
import cn.vko.ring.im.session.SessionHelper;
import cn.vko.ring.im.utils.DemoCache;
import cn.vko.ring.mine.fragment.MeFragment;
import cn.vko.ring.study.fragment.Study1Fragment;
import cn.vko.ring.study.fragment.StudyFragment;
import cn.vko.ring.study.fragment.StudyFragment1;
import cn.vko.ring.study.presenter.CusetemPresenter;


/**
 * @author shikh 主界面
 */
public class MainActivity extends BaseActivity {
    @Bind(R.id.layout_parent)
    public LinearLayout layoutParent;
    @Bind(R.id.vp_main)
    public NoScrollViewPager mViewPager;
    @Bind(R.id.bottom_menu)
    public TabMenuView tabView;
    /**
     * 存放底部菜单对应的四个模块
     */
    private List<Fragment> pagerList = new ArrayList<Fragment>();
    private boolean isChecked;
    private TabFragmentPagerAdapter mAdapter;
    private MeFragment mineFragment;
    private StudyFragment studyFragment;
    private ClassGroupragment classGroupragment;
    public static Handler mHandler;
    private ChooseGradeDialog selectGradeDialog;

    private PushMessagePresenter messagePresenter;
    private static final String TAG = "MainActivity";
    private static final String EXTRA_APP_QUIT = "APP_QUIT";
    private static final int REQUEST_CODE_NORMAL = 1;
    private static final int REQUEST_CODE_ADVANCED = 2;
    private int index = 0;
    private UserInfo user;
    private TabMenuInfo study, mine, classGroup;

    private long exitTime = 0;

    @Override
    public int setContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isEnableSwipe() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        onParseIntent();
    }

    @Override
    public void initView() {
//        //bugly
//        CrashReport.initCrashReport(getApplicationContext(), "注册时申请的APPID", false);
        EventBus.getDefault().register(this);
        mViewPager.setNoScroll(true);
        tintManager.setStatusBarTintResource(R.drawable.shape_change_color);
        user = VkoContext.getInstance(this).getUser();
        initBottomMenu();
        initFragment();
        initHandler();
        if (VkoContext.getInstance(MainActivity.this).isLogin() && VkoContext.getInstance(this).getGradeId().equals("-1")) {
            showDialog(false);
        } else {
            giveVbByFirst();
        }
        //登录环信
//		initEase();
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    private void onParseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            IMMessage message = (IMMessage) getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            switch (message.getSessionType()) {
                case P2P:
                    SessionHelper.startP2PSession(this, message.getSessionId());
                    break;
                case Team:
                    SessionHelper.startTeamSession(this, message.getSessionId());
                    break;
                default:
                    break;
            }
        } else if (intent.hasExtra(EXTRA_APP_QUIT)) {
            onLogout();
            return;
        } else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P)) {
            Intent data = intent.getParcelableExtra(Extras.EXTRA_DATA);
            String account = data.getStringExtra(Extras.EXTRA_ACCOUNT);
            if (!TextUtils.isEmpty(account)) {
                SessionHelper.startP2PSession(this, account);
            }
        }
    }

    private void giveVbByFirst() {
        if (VkoContext.getInstance(this).isLogin()
                && VkoContext.getInstance(this).getUser().getFirstLoginVb() > 0) {
            new VbDialog(this).showDialog(VkoContext.getInstance(this).getUser().getFirstLoginVb());
            UserInfo user = VkoContext.getInstance(this).getUser();
            user.setFirstLoginVb(0);
            VkoContext.getInstance(this).setUser(user);
        }
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 100) {
                    finish();
                }
            }
        };
    }

    private void initBottomMenu() {
//        Log.e("++++++++++++++》》》","=====走initBottomMenu了");
        if (study == null)
            study = new TabMenuInfo(R.id.main_menu_item_study,
                    R.drawable.bar_study_blue, R.drawable.bar_study_null,
                    R.string.study_text, R.color.text_default_color,
                    R.color.bg_main_blue_color);
        if (classGroup == null)
            classGroup = new TabMenuInfo(R.id.main_menu_item_message,
                    R.drawable.bar_msg_blue, R.drawable.bar_msg_null,
                    R.string.class_text, R.color.text_default_color,
                    R.color.bg_main_blue_color);
        if (mine == null)
            mine = new TabMenuInfo(R.id.main_menu_item_mine,
                    R.drawable.bar_mine_blue, R.drawable.bar_mine_null,
                    R.string.mine_text, R.color.text_default_color,
                    R.color.bg_main_blue_color);
        user = VkoContext.getInstance(this).getUser();
//        if (user != null && user.getIsSchoolUser() == 1) {
        if (user != null) {
            tabView.addMenuItem(classGroup, 0);
            tabView.addMenuItem(study, 1);
            tabView.addMenuItem(mine, 2);
        } else {
            tabView.addMenuItem(study, 0);
            tabView.addMenuItem(mine, 1);
        }
        tabView.setOnMenuClickListener(new IMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position) {
                mViewPager.setCurrentItem(position, false);
            }
        });
        // 默认选中课程页
        tabView.setSelectIndex(index);
        mViewPager.setCurrentItem(index);

    }

    private void initFragment() {
        // TODO Auto-generated method stub
//        Log.e("++++++++++++++》》》","=====走initFragment了");
//		if(user != null && user.getIsSchoolUser() == 1){
        if(user != null){
            classGroupragment = new ClassGroupragment();
            pagerList.add(classGroupragment);
//			classGroupragment = new ClassGroupragment();
//			pagerList.add(classGroupragment);
        }
//        studyFragment = new StudyFragment1();
        studyFragment = new StudyFragment();
        pagerList.add(studyFragment);
        mineFragment = new MeFragment();
        pagerList.add(mineFragment);
        mViewPager.setClipChildren(false);

        mViewPager.setOffscreenPageLimit(pagerList.size());
        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), pagerList);
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onChangePage(position);
                if (position == 0) {
                    tintManager.setStatusBarTintResource(R.drawable.shape_change_color);
                } else {
                    tintManager.setStatusBarTintResource(R.drawable.shape_change_color);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//		mViewPager.setCurrentItem(index, false);
    }

    /**
     * @param page
     */
    @SuppressLint("NewApi")
    protected void onChangePage(int page) {
        // TODO Auto-generated method stub
        index = page;
        tabView.setSelectIndex(page);
    }

    @Override
    public void initData() {
        new VersionUpdatePresenter(this, true);
        openPush();
    }

    @Subscribe
    public void onEventMainThread(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            // 用户登录时刷新数据
            if (msg.equals(Constants.LOGIN_REFRESH)) {
                checkedImToken();
                /**
                 * 登陆成功后再开一下通知，及时绑定devicetoken
                 */
                notifyBindDeviceToken();
                changeView();
                if (VkoContext.getInstance(this).getGradeId().equals("-1")) {
                    showDialog(true);
                    return;
                }
                refreshData();
            } else if (msg.equals(Constants.OPEN_MEMBER)) {//开通会员
                mineFragment.updateVipInfo();
            }
            if (msg.equals(Constants.ACTION_DEAL_AD)) {
                //点了广告
//				studyFragment.refreshAdState();
            }
        }
    }

    private void checkedImToken() {
        if (VkoContext.getInstance(this).getUser().getImToken() != null) {
            if (DemoCache.getAccount() == null) {
                if (getLoginInfo() == null) {
                    new CusetemPresenter(this);
                }
            }
        } else {
            new CusetemPresenter(this);
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

    private void refreshData() {
        // TODO Auto-generated method stub
        for (int i = 0; i < pagerList.size(); i++) {
            try {
                BaseFragment fragment = (BaseFragment) pagerList.get(i);
                if (i == mViewPager.getCurrentItem()) {
                    fragment.initData();
                } else {
                    fragment.mHasLoadOnce = false;
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


        }
        giveVbByFirst();
    }

    private void showDialog(boolean isRefresh) {
        if (selectGradeDialog == null) {
            selectGradeDialog = new ChooseGradeDialog(MainActivity.this);
            selectGradeDialog.setCanceledOnTouchOutside(false);
            if (isRefresh) {
                selectGradeDialog.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        refreshData();
                    }
                });
            } else {
                giveVbByFirst();
            }
        }
        selectGradeDialog.show();
    }

    private void notifyBindDeviceToken() {
        if (messagePresenter != null) {
            messagePresenter.updateBindDeviceTokenStatus();
        } else {
            openPush();
        }
    }

    private void openPush() {
        messagePresenter = new PushMessagePresenter(this);
        messagePresenter.openPush();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void changeView() {
        user = VkoContext.getInstance(this).getUser();
//        Log.e("++++++++++++++》》》","=====走changeView了");
//		if(user != null && user.getIsSchoolUser() == 1 && pagerList.size() != 3){//添加班群
        if (user != null  && pagerList.size() != 3) {//添加班群
            pagerList.clear();
            classGroupragment = new ClassGroupragment();
            pagerList.add(classGroupragment);
            studyFragment = new StudyFragment();
            pagerList.add(studyFragment);
            mineFragment = new MeFragment();
            pagerList.add(mineFragment);
            mAdapter.notifyDataSetChanged();

            tabView.reset();
            tabView.addMenuItem(classGroup, 0);
            tabView.addMenuItem(study, 1);
            tabView.addMenuItem(mine, 2);
//            if (index == 1) {
//                index = 2;
//            }
        }
//		else if((user == null  || user.getIsSchoolUser() == 0) && pagerList.size() == 3){//删除班群
//			pagerList.remove(classGroupragment);
//			mAdapter.notifyDataSetChanged();
//			tabView.reset();
//			tabView.addMenuItem(study,0);
//			tabView.addMenuItem(mine, 1);
//			if(index != 0){
//				index =0;
//			}
//		}
//		else if ((user == null  || user.getIsSchoolUser() == 0) && pagerList.size() == 3){
//			pagerList.clear();
//			studyFragment = new StudyFragment1();
//			pagerList.add(studyFragment);
//			classGroupragment = new ClassGroupragment();
//			pagerList.add(classGroupragment);
//			mineFragment = new MeFragment();
//			pagerList.add(mineFragment);
//			mAdapter.notifyDataSetChanged();
//
//			tabView.reset();
//			tabView.addMenuItem(study,0);
//			tabView.addMenuItem(classGroup, 1);
//			tabView.addMenuItem(mine, 2);
//			if(index == 1){
//				index = 2;
//			}
//		}
        // 默认选中课程页
        tabView.setSelectIndex(index);
        mViewPager.setCurrentItem(index);
    }

    // 注销
    private void onLogout() {
        // 清理缓存&注销监听
        LogoutHelper.logout();

        // 启动登录
//        cn.vko.ring.im.login.LoginActivity.start(this);
//        finish();
    }


    // 返回键 监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出微课圈", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);


    }


}
