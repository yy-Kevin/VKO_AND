package cn.vko.ring.home.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.netease.nimlib.sdk.NimIntent;
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
import cn.vko.ring.common.widget.dialog.VbDialog;
import cn.vko.ring.home.adapter.TabFragmentPagerAdapter;
import cn.vko.ring.home.listener.IMenuItemClickListener;
import cn.vko.ring.home.model.TabMenuInfo;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.home.presenter.PushMessagePresenter;
import cn.vko.ring.home.presenter.VersionUpdatePresenter;
import cn.vko.ring.home.widget.ChooseGradeDialog;
import cn.vko.ring.home.widget.TabMenuView;
import cn.vko.ring.im.login.LogoutHelper;
import cn.vko.ring.im.main.fragment.HomeFragment;
import cn.vko.ring.im.main.model.Extras;
import cn.vko.ring.im.session.SessionHelper;
import cn.vko.ring.mine.fragment.MeFragment;
import cn.vko.ring.study.fragment.StudyFragment;


/**
 * @author shikh 主界面
 */
public class Main2Activity extends BaseActivity {
	@Bind(R.id.bottom_menu)
	public TabMenuView tabView;
	/**
	 * 存放底部菜单对应的四个模块
	 */
	private List<BaseFragment> pagerList = new ArrayList<BaseFragment>();
	private boolean isChecked;
//	private TabFragmentPagerAdapter mAdapter;
	private MeFragment mineFragment;
	private StudyFragment studyFragment;
	private ClassGroupragment classGroupragment;
	private BaseFragment mCurrentFragment;
	public static Handler mHandler;
	private ChooseGradeDialog selectGradeDialog;

	private PushMessagePresenter messagePresenter;
	private static final String TAG = "MainActivity";
	private static final String EXTRA_APP_QUIT = "APP_QUIT";
	private static final int REQUEST_CODE_NORMAL = 1;
	private static final int REQUEST_CODE_ADVANCED = 2;
	private int index = 0;
	private UserInfo user;
	private TabMenuInfo study,mine,classGroup;
	HomeFragment homeFragment;

	@Override
	public int setContentViewResId() {
		return R.layout.activity_main_modify;
	}

	@Override
	protected boolean isEnableSwipe() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//		super.onSaveInstanceState(outState, outPersistentState);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		onParseIntent();
	}
	@Override
	public void initView() {
		EventBus.getDefault().register(this);
		tintManager.setStatusBarTintResource(R.color.study_top_bg);
		user = VkoContext.getInstance(this).getUser();
		initBottomMenu();
		initFragment();
		initHandler();
		if (VkoContext.getInstance(Main2Activity.this).isLogin() && VkoContext.getInstance(this).getGradeId().equals("-1")) {
			showDialog(false);
		} else {
			giveVbByFirst();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	public static void start(Context context, Intent extras) {

		Intent intent = new Intent();
		intent.setClass(context, Main2Activity.class);
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
		}  else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P)) {
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
		if (study==null)
		 study = new TabMenuInfo(R.id.main_menu_item_study,
				R.drawable.bar_study_dark, R.drawable.bar_study,
				R.string.study_text, R.color.text_default_color,
				R.color.bg_main_blue_color);
		if (classGroup==null)
		 classGroup = new TabMenuInfo(R.id.main_menu_item_message,
				R.drawable.bar_msg_press, R.drawable.bar_msg_normal,
				R.string.class_text, R.color.text_default_color,
				R.color.bg_main_blue_color);
		if (mine==null)
		 mine = new TabMenuInfo(R.id.main_menu_item_mine,
				R.drawable.bar_mine_dark, R.drawable.bar_mine,
				R.string.mine_text, R.color.text_default_color,
				R.color.bg_main_blue_color);
		tabView.addMenuItem(study, 0);
		user = VkoContext.getInstance(this).getUser();
		if(user != null && user.getIsSchoolUser() == 1){
			tabView.addMenuItem(classGroup, 1);
			tabView.addMenuItem(mine, 2);
		}else{
			tabView.addMenuItem(mine, 1);
		}
		tabView.setOnMenuClickListener(new IMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position) {
//				mViewPager.setCurrentItem(position, false);
				onChangePage(position);
				if (position == 0) {
					tintManager.setStatusBarTintResource(R.color.study_top_bg);
				} else {
					tintManager.setStatusBarTintResource(R.color.blue);
				}
			}
		});
		// 默认选中课程页
		tabView.setSelectIndex(index);

	}

	private void initFragment() {
		// TODO Auto-generated method stub
		studyFragment = new StudyFragment();
		pagerList.add(studyFragment);
		if(user != null && user.getIsSchoolUser() == 1){
			classGroupragment = new ClassGroupragment();
			pagerList.add(classGroupragment);
		}
		mineFragment = new MeFragment();
		pagerList.add(mineFragment);
		addContent(studyFragment);
//		mCurrentFragment = studyFragment;

	}

	/**
	 * @param page
	 */
	@SuppressLint("NewApi")
	protected void onChangePage(int page) {
		// TODO Auto-generated method stub
		index = page;
		tabView.setSelectIndex(page);
		BaseFragment mFragment = pagerList.get(page);
		switchContent(mCurrentFragment,mFragment);
//
	}

	@Override
	public void initData() {
		new VersionUpdatePresenter(this,true);
		openPush();
	}

	@Subscribe
	public void onEventMainThread(String msg) {
		if (!TextUtils.isEmpty(msg)) {
			// 用户登录时刷新数据
			if (msg.equals(Constants.LOGIN_REFRESH)) {
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

	private void refreshData() {
		// TODO Auto-generated method stub
		for (int i = 0; i < pagerList.size(); i++) {
			try {
				BaseFragment fragment = (BaseFragment) pagerList.get(i);
				if (i == index) {
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
			selectGradeDialog = new ChooseGradeDialog(Main2Activity.this);
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
		if(user != null && user.getIsSchoolUser() == 1 && pagerList.size() != 3){//添加班群
			classGroupragment = new ClassGroupragment();
			pagerList.add(1,classGroupragment);

			tabView.reset();
			tabView.addMenuItem(study,0);
			tabView.addMenuItem(classGroup, 1);
			tabView.addMenuItem(mine, 2);
			if(index == 1){
				index = 2;
			}
		}else if((user == null  || user.getIsSchoolUser() == 0) && pagerList.size() == 3){//删除班群
			pagerList.remove(classGroupragment);
			tabView.reset();
			tabView.addMenuItem(study,0);
			tabView.addMenuItem(mine, 1);
			if(index != 0){
				index -=1;
			}
		}
		// 默认选中课程页
		tabView.setSelectIndex(index);
	}

	// 注销
	private void onLogout() {
		// 清理缓存&注销监听
		LogoutHelper.logout();

		// 启动登录
//        cn.vko.ring.im.login.LoginActivity.start(this);
//        finish();
	}

	public void switchContent(BaseFragment from, BaseFragment to) {
		if (mCurrentFragment != to) {
			mCurrentFragment = to;
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
					android.R.anim.fade_in, android.R.anim.fade_out);
			if (!to.isAdded()) {    // 先判断是否被add过
				transaction.hide(from).add(R.id.content_frame, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
//				if(!to.mHasLoadOnce){
//					to.initData();
//				}
			}
//			transaction.replace(R.id.content_frame,to).commit();
		}
	}
	public void addContent(Fragment from){
		if(from == null) return;
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
				android.R.anim.fade_in, android.R.anim.fade_out);
		transaction.add(R.id.content_frame, from).commit();
	}
	}
