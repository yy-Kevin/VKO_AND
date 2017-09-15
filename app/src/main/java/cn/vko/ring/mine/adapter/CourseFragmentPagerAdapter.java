/*
 *CourseFragmentPagerAdapter.java [V 1.0.0]
 *classes : cn.vko.ring.mine.adapter.CourseFragmentPagerAdapter
 *宣义阳 Create at 2015-8-3 下午6:34:45
 */
package cn.vko.ring.mine.adapter;

import java.util.List;

import cn.vko.ring.common.base.BaseFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * cn.vko.ring.mine.adapter.CourseFragmentPagerAdapter
 * @author 宣义阳 
 * create at 2015-8-3 下午6:34:45
 */
public class CourseFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<BaseFragment> list;
	/**
	 * @param fm
	 */
	public CourseFragmentPagerAdapter(FragmentManager fm,List<BaseFragment> list) {
		super(fm);
		this.list = list;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return list.size();
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		//container.addView(list.get(position));
		Fragment f = (Fragment) super.instantiateItem(container, position);
		return f;
	}
	
//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		// TODO Auto-generated method stub
//		container.removeView(((View)(object)));
//	}

}
