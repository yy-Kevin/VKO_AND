/*
 *SystemActivity.java [V 1.0.0]
 *classes : cn.vko.ring.mine.views.SystemActivity
 *宣义阳 Create at 2015-7-24 上午10:58:16
 */
package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.CleanCacheUtil;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.widget.SwitchButton;
import cn.vko.ring.common.widget.dialog.CommonDialog;
import cn.vko.ring.common.widget.dialog.CommonDialog.Builder.OnButtonClicked;
import cn.vko.ring.home.activity.LoginActivity;
import cn.vko.ring.home.activity.Main2Activity;
import cn.vko.ring.home.activity.MainActivity;
import cn.vko.ring.home.listener.IDeviceTokenBindListener;
import cn.vko.ring.home.presenter.PushMessagePresenter;
import cn.vko.ring.im.login.LogoutHelper;
import cn.vko.ring.utils.ACache;
import cn.vko.ring.utils.FileUtil;
import cn.vko.ring.utils.WebViewUtil;

public class SystemActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_cache)
	public TextView tvCache;
	@Bind(R.id.ll_one)
	public SwitchButton ll_one;
	@Bind(R.id.ll_two)
	public SwitchButton ll_two;
	@Bind(R.id.ll_three)
	public SwitchButton ll_three;
	@Bind(R.id.but_exit)
	public Button btnLogout;
	private boolean oneFlag;
	private boolean twoFlag;
	private boolean threeFlag;
	PushMessagePresenter p;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_system_setup;
	}

	@Override
	public void initView() {
		// rgButton.setOnCheckedChangeListener(this);
		ll_one.setOnCheckedChangeListener(this);
		ll_two.setOnCheckedChangeListener(this);
		ll_three.setOnCheckedChangeListener(this);
		// p = new PushMessagePresenter(this);


		if (!VkoContext.getInstance(this).isLogin()) {
			btnLogout.setBackgroundResource(R.drawable.shape_gray_btn);
		}


	}

	// 计算缓存大小
	private void getVkoCache() {
		long size = 0;
		try {
			size = CleanCacheUtil.getFolderSize(new File(this.getFilesDir()
					.getAbsolutePath() + "/"));
			size += CleanCacheUtil.getFolderSize(new File(this.getCacheDir()
					.getAbsolutePath() + "/"));
			size += CleanCacheUtil.getFolderSize(new File("/data/data/"
					+ this.getPackageName() + "/databases/"));
			// size += DataCleanManger.getFolderSize(new
			// File("/data/data/cn.vko.ring/shared_prefs/"));
			// size += DataCleanManger.getFolderSize(new
			// File("/data/data/cn.vko.ring/databases/"));
			// data/data/cn.vko.ring/shared_prefs
			// /data/data/<package_name>/databases
			size += CleanCacheUtil.getFolderSize(FileUtil.getAudioDir());
			size += CleanCacheUtil.getFolderSize(FileUtil.getCacheDir());
			size += CleanCacheUtil.getFolderSize(FileUtil.getCrashDir());
			size += CleanCacheUtil.getFolderSize(FileUtil.getCameraDir());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (size == 0) {
			tvCache.setText("0KB");
		} else {
			tvCache.setText(String.format("%.2f", size * 1.0 / (1024 * 1024))
					+ "M");
		}
	}

	@Override
	public void initData() {
		getVkoCache();
		tvTitle.setText(R.string.i_system_design);
		p = new PushMessagePresenter(this);

		oneFlag = VkoConfigure.getConfigure(this).getBoolean(
				Constants.STATEWATCH, false);
		ll_one.setChecked(oneFlag);

		twoFlag = VkoConfigure.getConfigure(this).getBoolean(
				Constants.STATEDOWN, false);
		ll_two.setChecked(twoFlag);

		threeFlag = VkoConfigure.getConfigure(this).getBoolean(
				Constants.STATENOTIFACATION, true);

		ll_three.setChecked(threeFlag);
	}

	// 推送开关
	private void swichChange(boolean flag) {
		if(p == null) return;
		if (flag) {
			 p.openPush();
		} else {
			 p.closePush();
		}
	}

	// 退出程序
	@OnClick(R.id.but_exit)
	public void butExitClick() {
		if (VkoContext.getInstance(this).isLogin()) {
			showExitDialog();
		}
	}

	private void showExitDialog() {
		new CommonDialog.Builder(this)
		.setTitleText("提示")
		.setContentText("亲，确定要退出吗？")
		.setCancleText("取消")
		.setSureText("退出")
		.setOnButtonClicked(new OnButtonClicked() {
			@Override
			public void onSureButtonClick(CommonDialog commonDialog) {
//				unBindDeviceToken(commonDialog);
				outApp(commonDialog);
			}
			@Override
			public void onCancleButtonClick(CommonDialog commonDialog) {
				commonDialog.dismiss();
			}
		}).build().show();
	}

	private void unBindDeviceToken(final CommonDialog sweetAlertDialog) {
		if (p != null) {
			p.setType(1);
			p.setDeviceTokenBindListener(new IDeviceTokenBindListener() {
				@Override
				public void unBind(boolean isSuccess) {
					if (isSuccess) {
						outApp(sweetAlertDialog);
					} else {
						Toast.makeText(SystemActivity.this, "稍后在试",Toast.LENGTH_LONG).show();

					}
				}
			});
			p.bindDeviceToken("");
		}
	}
	/**
	 * 
	 * @Title: outApp
	 * @Description: TODO
	 * @param sweetAlertDialog
	 * @return: void
	 */
	private void outApp(CommonDialog sweetAlertDialog) {
		sweetAlertDialog.dismiss();
		swichChange(false);
		exitApp();
	}

	/**
	 * 退出账号
	 */
	protected void exitApp() {
		clearCache();
		// pushToggle(false);
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra("exit", 0);
		startActivity(intent);
		ACache.get(this,"user").clear();
		VkoContext.getInstance(this).setUser(null);
		MainActivity.mHandler.sendEmptyMessage(100);
		//清除webView cookiet s
		WebViewUtil.clearCache(this);
		LogoutHelper.logout();//退出云信
		finish();
	}

	@OnClick(R.id.iv_back)
	public void imgFinish() {
		this.finish();
	}

	// 关于微课
	@OnClick(R.id.tv_about)
	public void aboutVko() {
		StartActivityUtil.startActivity(this, AboutVkoActivity.class,
				Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	// 清除缓存
	@OnClick(R.id.rl_clear_cache)
	public void clearCacheClick() {
		showDlalogclearCache();
	
	}

	private void showDlalogclearCache() {
		new CommonDialog.Builder(this)
		.setContentText("确认清除缓存？")
		.setCancleText("取消")
		.setSureText("确认")
		.setOnButtonClicked(new OnButtonClicked() {
			@Override
			public void onSureButtonClick(CommonDialog commonDialog) {
				clearCache();
				commonDialog.dismiss();
			}
			@Override
			public void onCancleButtonClick(CommonDialog commonDialog) {
				commonDialog.dismiss();
			}
		}).build().show();

	}

	protected void clearCache() {
		ACache.get(this).clear();
		String[] files = new String[] {
				FileUtil.getCacheDir().getAbsolutePath(),
				FileUtil.getAudioDir().getAbsolutePath(),
				FileUtil.getWebViewDir().getAbsolutePath(),
				FileUtil.getCameraDir().getAbsolutePath(),
				FileUtil.getCrashDir().getAbsolutePath() };
		CleanCacheUtil.cleanApplicationData(this, files);
		Toast.makeText(getApplicationContext(), "清理完成", Toast.LENGTH_SHORT)
				.show();
		tvCache.setText("0KB");
	}

	private void pushToggle(boolean open) {
		if (p == null) {
			p = new PushMessagePresenter(this);
		}
		if (open) {
			p.openPush();
			return;
		}
		p.closePush();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// EventCountAction.onActivityResumCount(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// EventCountAction.onActivityPauseCount(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
		switch (compoundButton.getId()) {
			case R.id.ll_one:
				oneFlag = b;
				VkoConfigure.getConfigure(this).put(Constants.STATEWATCH, oneFlag);
//			EventBus.getDefault().post(Constants.REFRESH_RESTORE);
//				ll_one.setChecked(oneFlag);
				break;
			case R.id.ll_two:
				twoFlag = b;
				VkoConfigure.getConfigure(this).put(Constants.STATEDOWN, twoFlag);
//				ll_two.setChecked(twoFlag);
				break;
			case R.id.ll_three:
				threeFlag = b;
				VkoConfigure.getConfigure(this).put(Constants.STATENOTIFACATION,
						threeFlag);
//				ll_three.setChecked(threeFlag);

				swichChange(threeFlag);
				pushToggle(threeFlag);
				break;
		}
	}
}
