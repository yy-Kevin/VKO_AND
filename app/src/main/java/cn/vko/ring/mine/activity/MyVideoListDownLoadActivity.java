package cn.vko.ring.mine.activity;


import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.widget.NoScrollViewPager;
import cn.vko.ring.common.widget.SimpleViewPagerIndicator;
import cn.vko.ring.home.adapter.TabFragmentPagerAdapter;
import cn.vko.ring.mine.fragment.MyDownLoadFragment;
import cn.vko.ring.mine.fragment.MyFinishedDownLoadFragment;
import cn.vko.ring.mine.listener.IRefreshDataListener;


public class MyVideoListDownLoadActivity extends BaseActivity implements IRefreshDataListener {
	private String[] titles = new String[] {"下载中","已下载"};
	@Bind(R.id.iv_back)
	public ImageView iv_back;
	@Bind(R.id.tv_title)
	public TextView tv_title;
	@Bind(R.id.id_stickynavlayout_viewpager)
	public NoScrollViewPager mViewPager;
	@Bind(R.id.layout_indicator)
	public SimpleViewPagerIndicator mIndicator;
	
	private TabFragmentPagerAdapter mAdapter;
	private List<Fragment> fragments;
	private MyDownLoadFragment myDownloadfragment;
	private MyFinishedDownLoadFragment myFinishedDownLoadFragment;
	@Override
	public int setContentViewResId() {

		return R.layout.activity_video_down;
	}

	@OnClick(R.id.iv_back)
	public void lvLeftClick() {
		this.finish();
	}

	@Override
	public void initView() {
		mIndicator.setTitles(titles,mViewPager);
		tv_title.setText("我的下载");
	}

	@Override
	public void initData() {
		fragments = new ArrayList<Fragment>();
		myDownloadfragment=new MyDownLoadFragment();
		myFinishedDownLoadFragment=new MyFinishedDownLoadFragment();
		myDownloadfragment.setIslistener(this);
		
		fragments.add(myDownloadfragment);
		fragments.add(myFinishedDownLoadFragment);
		
		mViewPager.setClipChildren(false);
		mViewPager.setOffscreenPageLimit(2);
		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragments);//new TabFragmentPagerAdapter(getFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapter);
	}

	@Override
	public void refreshData() {
		// TODO Auto-generated method stub
		myFinishedDownLoadFragment.setRefreshData();
		myDownloadfragment.refreshData();
	}

}


