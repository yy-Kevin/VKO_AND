/**   
 * Copyright © 2016 cn.vko.com. All rights reserved.
 * 
 * @Title: CommonDialogBase.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.common.widget.dialog 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2016-1-7 下午4:01:44 
 * @version: V1.0   
 */
package cn.vko.ring.common.widget.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseDialog;

/** 
 * @ClassName: CommonDialogBase 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2016-1-7 下午4:01:44  
 */
public abstract class CommonDialogBase extends BaseDialog {

	View rootView;
	public CommonDialogBase(Context context, int theme) {
		super(context, theme);
		
	}
	public CommonDialogBase(Context context, int theme,Object t) {
		super(context, theme);
		this.context = context;
		getCommonBuilder(t);
		dealViews(rootView);
		dealListener();
	
	}
	public abstract Object getCommonBuilder(Object t);
	public abstract void dealViews(View rootView);
	public abstract void dealListener();
	@Override
	public void setUpViews(View root) {
		this.rootView=root;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public boolean getCancelableOnclick() {
		// TODO Auto-generated method stub
		return true;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public int getGravity() {
		// TODO Auto-generated method stub
		
		return Gravity.CENTER;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public int getRootId() {
		// TODO Auto-generated method stub
		return R.layout.dialog_common_view;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public int getAnimatStly() {
		// TODO Auto-generated method stub
		return 0;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public LayoutParams getILayoutParams() {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		return lp;
	}
	/*  
	 * 
	 * @Description: TODO
	 *  
	 */
	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub
		
	}
}
