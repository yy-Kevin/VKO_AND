package com.netease.nim.uikit.common.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.netease.nim.uikit.common.viewpager.SlidingTabPagerAdapter;

public class TeemTabPagerAdapter extends SlidingTabPagerAdapter {

	@Override
	public int getCacheCount() {
		return TeemTab.values().length;
	}

	public TeemTabPagerAdapter(FragmentManager fm, Context context, ViewPager pager) {
		super(fm, TeemTab.values().length, context.getApplicationContext(), pager);
//		for (TeemTab tab : TeemTab.values()) {
//
//			try {
//
//				TabFragment fragment = null;
//
//				List<Fragment> fs = fm.getFragments();
//				if (fs != null) {
//					for (Fragment f : fs) {
//						if (f.getClass() == tab.clazz) {
//							fragment = (TabFragment) f;
//							break;
//						}
//					}
//				}
//
//				if (fragment == null) {
//					fragment = tab.clazz.newInstance();
//				}
//
//				fragment.setState(this);
//				fragment.attachTabData(tab);
//
//				fragments[tab.tabIndex] = fragment;
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
	}

	@Override
	public int getCount() {
		return TeemTab.values().length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		TeemTab tab = TeemTab.fromTabIndex(position);

		int resId = tab != null ? tab.resId : 0;

		return resId != 0 ? context.getText(resId) : "";
	}

}