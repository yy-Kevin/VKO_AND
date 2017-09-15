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
package cn.vko.ring.circle.fragment;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.ViewUtils;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.umeng.EventCountAction;
import cn.vko.ring.common.widget.NoScrollViewPager;
import cn.vko.ring.common.widget.SimpleViewPagerIndicator;
import cn.vko.ring.common.widget.pop.CircleAddPop;
import cn.vko.ring.common.widget.pop.CircleFollowPop;
import cn.vko.ring.home.adapter.TabFragmentPagerAdapter;

/**
 * @ClassName: StudyFragment
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-10-29 下午2:23:27
 */
public class CircleBarFragment extends BaseFragment {

	private final static String FOLLOW_KEY = "follow";
	private String[] titles = new String[] { "广场", "圈子", "发现" };
	@Bind(R.id.iv_back)
	public ImageView ivBack;
	@Bind(R.id.imagebtn)
	public ImageView ivAdd;
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.id_stickynavlayout_viewpager)
	public NoScrollViewPager mViewPager;
	@Bind(R.id.layout_indicator)
	public SimpleViewPagerIndicator mIndicator;

	private TabFragmentPagerAdapter mAdapter;
	private List<Fragment> fragments;

	private CircleFollowPop mPOP;
	private CircleAddPop pop;
	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_circle_bar;
	}

	@Override
	public void initView(View root) {
		// TODO Auto-generated method stub
		ivBack.setVisibility(View.GONE);
		ivAdd.setVisibility(View.VISIBLE);
		ivAdd.setImageResource(R.drawable.icon_add_head);
		tvTitle.setText("圈子");
		mIndicator.setTitles(titles, mViewPager);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		if(VkoConfigure.getConfigure(getActivity()).getBoolean(FOLLOW_KEY, false)){
			initFragment();
		}else {
			//请求网络数据是否关注过如果还没有则进入推荐关注页
			showFollowPOP();
		}
	}

	private void showFollowPOP() {
		// TODO Auto-generated method stub
		mPOP = new CircleFollowPop(atx);
		mPOP.update(0, 0,ViewUtils.getScreenWidth(atx), ViewUtils.getRealHeight(atx));
		mPOP.showAtLocation(mViewPager, Gravity.CENTER, 0, 0);
		initFragment();
	}

	private void initFragment() {
		// TODO Auto-generated method stub
		fragments = new ArrayList<Fragment>();
		fragments.add(new SquareFragment());
		fragments.add(new GroupFragment());
		fragments.add(new CircleFoundFragment());
		mViewPager.setClipChildren(false);
		mViewPager.setOffscreenPageLimit(3);

		mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(),
				fragments);
		mViewPager.setAdapter(mAdapter);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		EventCountAction.onFragRCount(this.getClass());
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		EventCountAction.onFragPCount(this.getClass());
	}
	
	@OnClick(R.id.imagebtn)
	void sayAdd(View view){
		if (pop == null) {
			pop = new CircleAddPop(getActivity());
			pop.update(0, 0, ViewUtils.getScreenWidth(getActivity())*2/5,
					LayoutParams.WRAP_CONTENT);
		}
		pop.showAsDropDown(view);
	}

}
