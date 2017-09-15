package cn.vko.ring.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class TabFragmentPagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> fragments;

	public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment fragment = fragments.get(position % fragments.size());
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.fragments == null ? 0 : fragments.size();
	}

	// 初始化每个页卡选项
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		//得到缓存的fragment
		Fragment fragment = (Fragment) super.instantiateItem(container,position);
		return fragment;
	
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return PagerAdapter.POSITION_NONE;
	}

}
