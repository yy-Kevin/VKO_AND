package cn.vko.ring.home.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.home.model.UserInfo;

/**
 * 欢迎页
 * 
 * @author shikh
 * 
 */
public class SplashActivity extends BaseActivity {
	private String url;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				startApp();
				finish();
				break;
			}
		}
	};

	private void startApp() {
		// TODO Auto-generated method stub
		int oldVer = VkoConfigure.getConfigure(this).getInt("VERSION", 0);
		int curVer = getVersionCode();
		if (oldVer < curVer) {//版本更新 本进入引导页 因需求更改直接进入主界面
			VkoConfigure.getConfigure(this).put("VERSION", curVer);
//			goGuideActivity();
			UserInfo user = VkoContext.getInstance(this).getUser();
			if (user == null) {
				// 添加访客身份 即使user==null,也可以进入主界面
//				StartActivityUtil.startActivity(this, MainActivity.class);
				StartActivityUtil.startActivity(this, LoginActivity.class);
			}else {
				StartActivityUtil.startActivity(this, MainActivity.class);
			}
		} else {
			// 如果用户信息不用空 表示用户已经登录
			UserInfo user = VkoContext.getInstance(this).getUser();
			if (user == null) {
				// 添加访客身份 即使user==null,也可以进入主界面
//				StartActivityUtil.startActivity(this, MainActivity.class);
				StartActivityUtil.startActivity(this, LoginActivity.class);
			} else {
				StartActivityUtil.startActivity(this, MainActivity.class);
				/*if (!TextUtils.isEmpty(user.getSchoolId())
						&& Integer.parseInt(user.getGradeId()) >= 0) {
					// EMGroupManager.getInstance().loadAllGroups();
					// EMChatManager.getInstance().loadAllConversations();
					ViewUtils.startActivity(this, MainActivity.class);
				} else {
					ViewUtils.startActivity(this, RegisteTwoActivity.class);
				}*/
			}
		}
		finish();
	}

	@Override
	public int setContentViewResId() {
		return R.layout.activity_splash;
	}

	@Override
	public void initView() {
		// getUpdateTask();
		tintManager.setStatusBarTintResource(R.color.transparent);
	}

	@Override
	public void initData() {
		// getUpdateTask();
		handler.sendEmptyMessageDelayed(0, 2000);
	}

	public int getVersionCode() {
		PackageManager pm = this.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(this.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
