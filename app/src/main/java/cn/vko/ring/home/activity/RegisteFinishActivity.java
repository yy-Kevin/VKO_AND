/*
 *RegisteActivityDown.java [V 1.0.0]
 *classes : cn.vko.ring.mine.views.RegisteActivityDown
 *宣义阳 Create at 2015-7-27 上午9:25:11
 */
package cn.vko.ring.home.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.home.model.UserInfo;

/**
 * cn.vko.ring.mine.views.RegisteActivityDown
 * 
 * @author 宣义阳 create at 2015-7-27 上午9:25:11
 */
public class RegisteFinishActivity extends BaseActivity {

	private FinishActivity finishActivity;
	@Bind(R.id.tv_title)
	public TextView tvTitle;

	@Override
	public int setContentViewResId() {

		return R.layout.activity_regist_finish;
	}

	@Override
	public void initView() {
		if (finishActivity == null) {
			finishActivity = new FinishActivity();
		}
		registerReceiver(finishActivity, new IntentFilter("finishActivity"));
	}

	@Override
	public void initData() {
		tvTitle.setText("注册成功");
	}

	@OnClick(R.id.tv_experience)
	public void experienceClick() {
		UserInfo user = VkoContext.getInstance(this).getUser();
		if (!TextUtils.isEmpty(user.getSchoolId())
				&& Integer.parseInt(user.getGradeId()) >= 0) {
			EventBus.getDefault().post(Constants.LOGIN_REFRESH);
//			EaseHelper.getInstance().logout(true, null);
			// ViewUtils.startActivity(this, MainActivity.class);
		} else {
			StartActivityUtil.startActivity(this, RegisteTwoActivity.class);
		}
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (finishActivity != null) {
			unregisterReceiver(finishActivity);
		}
	}

	private class FinishActivity extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (!isFinishing()) {
				finish();
			}
		}
	}

	@OnClick(R.id.iv_back)
	public void setBack() {
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// EventCountAction.onActivityResumCount(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// EventCountAction.onActivityPauseCount(this);
	}
}
