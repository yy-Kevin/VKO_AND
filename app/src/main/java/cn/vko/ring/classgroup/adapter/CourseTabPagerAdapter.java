package cn.vko.ring.classgroup.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.netease.nim.uikit.common.viewpager.SlidingTabPagerAdapter;

import java.util.List;

import cn.vko.ring.classgroup.model.CourseTab;
import cn.vko.ring.im.main.fragment.MainTabFragment;


public class CourseTabPagerAdapter extends SlidingTabPagerAdapter {

	@Override
	public int getCacheCount() {
		return CourseTab.values().length;
	}

	public CourseTabPagerAdapter(FragmentManager fm, Context context, ViewPager pager) {
		super(fm, CourseTab.values().length, context.getApplicationContext(), pager);
		for (CourseTab tab : CourseTab.values()) {

			try {

				MainTabFragment fragment = null;

				List<Fragment> fs = fm.getFragments();
				if (fs != null) {
					for (Fragment f : fs) {
						if (f.getClass() == tab.clazz) {
							fragment = (MainTabFragment) f;
							break;
						}
					}
				}

				if (fragment == null) {
					fragment = tab.clazz.newInstance();
				}

				fragment.setState(this);
				fragment.attachTabData(tab);

				fragments[tab.tabIndex] = fragment;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getCount() {
		return CourseTab.values().length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		CourseTab tab = CourseTab.fromTabIndex(position);

		int resId = tab != null ? tab.resId : 0;

		return resId != 0 ? context.getText(resId) : "";
	}

}