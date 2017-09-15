package cn.vko.ring.study.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.study.activity.SearchQuestionActivity;
import cn.vko.ring.study.model.PhotoAttr;
import cn.vko.ring.utils.ACache;
import cn.vko.ring.utils.ImageUtils;

public class MyPopWindowpersoner {
	public Activity context;
	public View view;
	private Handler handler;
	private ACache aCache;
	PhotoAttr pAttr;

	public MyPopWindowpersoner(Activity context, View view) {
		super();
		this.context = context;
		this.view = view;

		// showPopupWindow(view);
		handler = new Handler();
		aCache = ACache.get(context);
		pAttr = (PhotoAttr) aCache.getAsObject("PhotoAttr");
		initData();
	}

	private void initData() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				if (null != context.getWindow().getDecorView().getWindowToken()) {
					// showPopwindow();
					showPopupWindow(view);
					handler.removeCallbacks(this);
				} else {
					handler.postDelayed(this, 5);
				}
			}
		};
		handler.post(r);

	}

	@SuppressLint("NewApi")
	private void showPopupWindow(View view) {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.pop_seacher_photo, null);
		// 设置按钮的点击事件
		ImageView button = (ImageView) contentView
				.findViewById(R.id.iv_seacher_photo);

		if (pAttr != null && pAttr.getUrl() != null) {
			Log.e("MyPopWindowpersoner", pAttr.getUrl()+"");
			button.setImageBitmap(ImageUtils.loadPerviewImage( pAttr.getUrl(), 0, 0,button));
			LayoutParams p = button.getLayoutParams();
			p.width = 200;
			p.height = 200;
			button.setLayoutParams(p);			
		} else {
			return;
		}
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("ID", pAttr.getId());
				bundle.putInt("ITEM", pAttr.getItem());
				bundle.putString("FROM", pAttr.getFrom());
				bundle.putString("URL", pAttr.getUrl());
				StartActivityUtil.startActivity(context, SearchQuestionActivity.class,
						bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
			}
		});

		PopupWindow popupWindow = new PopupWindow(contentView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);
		popupWindow.setFocusable(false);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		/*
		 * popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
		 * R.drawable.seacher_backgroud));
		 */

		// 设置好参数之后再show

		popupWindow.showAsDropDown(view, -100, 20);

	}
}
