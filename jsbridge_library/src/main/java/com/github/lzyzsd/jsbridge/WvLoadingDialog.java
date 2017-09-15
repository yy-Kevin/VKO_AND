/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: WvLoadingDialog.java 
 * @Prject: JsBridge-master
 * @Package: com.vko.shikh.jsbridge 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-11 上午11:05:30 
 * @version: V1.0   
 */
package com.github.lzyzsd.jsbridge;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.lzyzsd.library.R;

/**
 * @ClassName: WvLoadingDialog
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-11 上午11:05:30
 */
public class WvLoadingDialog extends Dialog {
	private Context context;

	public WvLoadingDialog(Context context) {
		super(context, R.style.Dialog);
		this.context = context;
	}
	public WvLoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}
	public WvLoadingDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}
	/*
	 * @Description: TODO
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}
	/**
	 * @Title: init
	 * @Description: TODO
	 * @return: void
	 */
	private void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.loading, null);
		setContentView(view);
		getWindow().setGravity(Gravity.CENTER );
		
	}
}
