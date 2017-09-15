package cn.vko.ring.mine.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 对所有页面使用的viewPager的抽取
 * @author xuanyiyang
 *
 * @param <T>
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {

	public List<T> list = new ArrayList<T>();
	public BasePagerAdapter(List<T> list){
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public abstract Object instantiateItem(ViewGroup container, int position) ;
		
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}

}
