package cn.vko.ring.common.base;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.umeng.message.PushAgent;

import butterknife.ButterKnife;
import cn.shikh.utils.SystemBarTintManager;
import cn.vko.ring.R;
import cn.vko.ring.common.umeng.EventCountAction;
import cn.vko.ring.common.volley.VolleyQueueController;
import cn.vko.ring.home.presenter.TimeCountPresenter;
import cn.vko.ring.im.session.activity.lupin.PromptSharedPreferences;
import cn.vko.ring.utils.ActivityUtilManager;

/**
 * cn.vko.circle.activity.BaseActivity
 * 
 * @author shikh <br/>
 *         create at 2015-7-7 下午3:47:16
 */
public abstract class BaseActivity extends FragmentActivity {

	private int guideResourceId = 0;// 引导页图片资源id
	private int guideResourceId1 = 1;// 引导页图片资源id
	private PromptSharedPreferences psp;

	public SystemBarTintManager tintManager;
//	public SwipeBackLayout mSlidingFinishLayout;
	private TimeCountPresenter mTimeCountPresenter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		mTimeCountPresenter = TimeCountPresenter.getInstance(this);
		getWindow().setFormat(PixelFormat.TRANSPARENT);
		onCreateBefore(savedInstanceState);
		setContentView(setContentViewResId());
		initStatusBar();
		ButterKnife.bind(this);
		PushAgent.getInstance(this).onAppStart();
		initView();
		initData();
		ActivityUtilManager.getInstance().addActivity(this);
		addGuideImage();// 添加引导页
		addGuideImage1();// 添加引导页
	}

	private void initSwipeBackLayout() {
		/*mSlidingFinishLayout = getSwipeBackLayout();
		mSlidingFinishLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		setSwipeBackEnable(isEnableSwipe());*/
	}
	protected boolean isEnableSwipe() {
		return true;
	}
	public void onCreateBefore(Bundle savedInstanceState) {
	};

	/**
	 * 
	 */
	public void initStatusBar() {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		// 创建状态栏的管理实例
		tintManager = new SystemBarTintManager(this);
		// 激活状态栏设置
		tintManager.setStatusBarTintEnabled(true);
		// 设置一个状态栏资源
		tintManager.setStatusBarTintResource(R.drawable.shape_change_color);
		// 激活导航栏设置
//		tintManager.setNavigationBarTintEnabled(false);
	}


	public abstract int setContentViewResId();

	public abstract void initView();

	public abstract void initData();

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		VolleyQueueController.getInstance().getRequestQueue(this).cancelAll(this);
		super.onStop();
//		mTimeCountPresenter.isBackground(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		EventCountAction.onActivityPauseCount(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ActivityUtilManager.getInstance().delActivity(this);
		ButterKnife.unbind(this);
		super.onDestroy();
	}

	public int getSlidingColor() {
		return ContextCompat.getColor(this,R.color.bg_main_view);
	}

	public static final ButterKnife.Action<View> ALPHA_FADE = new ButterKnife.Action<View>() {
		@Override
		public void apply(View view, int index) {
			AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
			alphaAnimation.setFillBefore(true);
			alphaAnimation.setDuration(500);
			alphaAnimation.setStartOffset(index * 100);
			view.startAnimation(alphaAnimation);
		}
	};

	/** 隐藏与现实软键盘 */
	public  void hideSoftInput(Context cx, View edit, boolean hide) {
		InputMethodManager imm = (InputMethodManager) cx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (hide) {
			imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
		} else {
			edit.setFocusable(true);
			edit.setFocusableInTouchMode(true);
			edit.requestFocus();
			imm.showSoftInput(edit, 0);
		}
	}

	public void hideSorftForce() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// onresume时，取消notification显示
//		EaseUI.getInstance().getNotifier().reset();
		EventCountAction.onActivityResumCount(this);
//		mTimeCountPresenter.isBackground(this);
	}


//	public void onRequestPermissionsResult(int permission,boolean granted){};
//显示引导图片
public void addGuideImage() {
	psp = new PromptSharedPreferences();
	View view = getWindow().getDecorView().findViewById(
			R.id.my_content_view);// 查找通过setContentView上的根布局
	if (view == null)
		return;
	if (psp.takeSharedPreferences(this)) {
		// 有过功能引导，就跳出
		return;
	}
	ViewParent viewParent = view.getParent();
	if (viewParent instanceof FrameLayout) {
		final FrameLayout frameLayout = (FrameLayout) viewParent;
		if (guideResourceId != 0) {
			// 设置了引导图片
			final ImageView guideImage = new ImageView(this);
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
			guideImage.setLayoutParams(params);
			guideImage.setScaleType(ImageView.ScaleType.FIT_XY);
			guideImage.setImageResource(guideResourceId);
			guideImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//删除引导图片
					frameLayout.removeView(guideImage);
					//保存记录
					psp.saveSharedPreferences(BaseActivity.this, "启动了");
				}
			});

			frameLayout.addView(guideImage);// 添加引导图片

		}
	}
}

	//显示引导图片
	public void addGuideImage1() {
		psp = new PromptSharedPreferences();
		View view = getWindow().getDecorView().findViewById(
				R.id.my_content_view);// 查找通过setContentView上的根布局
		if (view == null)
			return;
		if (psp.takeSharedPreferences(this)) {
			// 有过功能引导，就跳出
			return;
		}
		ViewParent viewParent = view.getParent();
		if (viewParent instanceof FrameLayout) {
			final FrameLayout frameLayout = (FrameLayout) viewParent;
			if (guideResourceId1 != 0) {
				// 设置了引导图片
				final ImageView guideImage = new ImageView(this);
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.FILL_PARENT);
				guideImage.setLayoutParams(params);
				guideImage.setScaleType(ImageView.ScaleType.FIT_XY);
				guideImage.setImageResource(guideResourceId1);
				guideImage.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//删除引导图片
						frameLayout.removeView(guideImage);
						//保存记录
						psp.saveSharedPreferences(BaseActivity.this, "启动了");
					}
				});

				frameLayout.addView(guideImage);// 添加引导图片

			}
		}
	}

	//获得图片id
	protected void setGuideResId(int resId) {
		this.guideResourceId = resId;
	}
	protected void setGuideResId1(int resId1) {
		this.guideResourceId1 = resId1;
	}


	/**
	 * 短时间显示Toast
	 * @param info 显示的内容
	 */
	public void showToast(String info) {
		Toast.makeText(this,info, Toast.LENGTH_SHORT).show();
	}
	/**
	 * 长时间显示Toast
	 * @param info 显示的内容
	 */
	public void showToastLong(String info) {
		Toast.makeText(this,info, Toast.LENGTH_LONG).show();
	}
	/**
	 * 短时间显示Toast
	 * @param info 显示的内容
	 */
	public void showToast(int resId){
		Toast.makeText(this,resId, Toast.LENGTH_SHORT).show();
	}
	/**
	 * 长时间显示Toast
	 * @param info 显示的内容
	 */
	public void showToastLong(int resId){
		Toast.makeText(this,resId, Toast.LENGTH_LONG).show();
	}

}
