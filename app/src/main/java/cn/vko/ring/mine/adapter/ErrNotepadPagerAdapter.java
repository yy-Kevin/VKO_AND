/*******************************************************************************
 * Copyright (c) 2015 by Luxury Corporation all right reserved.
 * 2015-8-20 
 * 
 *******************************************************************************/ 
package cn.vko.ring.mine.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 
 * 编写日期:	2015-8-20
 * 作者:	宋宁宁
 * 
 * 历史记录
 *    修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class ErrNotepadPagerAdapter extends PagerAdapter {

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

}
