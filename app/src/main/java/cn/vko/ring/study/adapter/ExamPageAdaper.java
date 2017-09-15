/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ExamPageAdaper.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.adapter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-7-29 下午3:02:28 
 * @version: V1.0   
 */
package cn.vko.ring.study.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * @ClassName: ExamPageAdaper
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-7-29 下午3:02:28
 */
public class ExamPageAdaper extends PagerAdapter {
	List<WebView> wvList = new ArrayList<WebView>();
	Context mContext;

	public ExamPageAdaper(List<WebView> wvList, Context mContext) {
		super();
		this.wvList = wvList;
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return wvList.size();
	}
	/*
	 * @Description: TODO
	 * @param container
	 * @param position
	 * @return
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(wvList.get(position));
		return wvList.get(position);
	}
	/*
	 * @Description: TODO
	 * @param container
	 * @param position
	 * @param object
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(wvList.get(position));
	}
	/*
	 * @Description: TODO
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	
	public View getItemView(int position){
		return wvList.get(position);
	}
}
