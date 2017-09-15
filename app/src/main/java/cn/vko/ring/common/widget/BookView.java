/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: BookView.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.common.widget 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-27 下午3:53:28 
 * @version: V1.0   
 */
package cn.vko.ring.common.widget;

import cn.vko.ring.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: BookView
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-27 下午3:53:28
 */
public class BookView extends LinearLayout  {
	private TextView tv;
	private boolean isChecked;
	private String text;

	/**
	 * @Description:TODO
	 */
	public BookView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public BookView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public BookView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		removeAllViews();
		tv = (TextView) View.inflate(context, R.layout.layout_book_view, null);
		addView(tv);
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isChecked=!isChecked;
				setChecked(isChecked);
			}
		});
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
		if (isChecked) {
			tv.setBackgroundResource(R.drawable.book_select_bg);
			tv.setTextColor(getResources().getColor(R.color.text_book_color));
		}else{
			tv.setTextColor(getResources().getColor(R.color.common_dark_text_color));
			tv.setBackgroundResource(R.drawable.book_unselect_bg);
		}
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
		if (!TextUtils.isEmpty(text)) {
			tv.setText(text);
		}
	}
	

}
