package cn.vko.ring.common.base;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

	public boolean mHasLoadOnce = false;
	public Activity atx;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		atx = getActivity();
		View view = View.inflate(atx, setContentViewId(), null);
		ButterKnife.bind(this, view);
		initView(view);
		return view;
	}
	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (getUserVisibleHint() && isVisible()) {
			initData();
		}
	}

	public abstract int setContentViewId();

	public abstract void initView(View root);

	public void initData() {
		mHasLoadOnce = true;
	};

	@SuppressLint("NewApi")
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {

		// TODO Auto-generated method stub
		if (isVisible()) {
			if (isVisibleToUser && !mHasLoadOnce) {
				initData();
			}
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
}
