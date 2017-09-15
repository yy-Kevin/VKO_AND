/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: RefreshView.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.study.weight 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-25 下午2:58:41 
 * @version: V1.0   
 */
package cn.vko.ring.study.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.R;

/**
 * @ClassName: RefreshView
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-25 下午2:58:41
 */
public class RefreshView extends View {
	private Bitmap b;
	private Paint mPaint;
	private int bgColor = getResources().getColor(R.color.recom_tvbg_color);
	private int w, h;
	private int Space = 5;
	ObjectAnimator oo;

	public RefreshView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public RefreshView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		Space = (int) (ViewUtils.getScreenDensity(context)*Space);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		mPaint.setColor(bgColor);
		mPaint.setStyle(Paint.Style.FILL);
		b = BitmapFactory.decodeResource(getResources(), R.drawable.refresh);
		w = b.getWidth();
		h = b.getHeight();
	}
	/*
	 * @Description: TODO
	 */
	@Override
protected void onDraw(Canvas canvas) {
	// TODO Auto-generated method stub
	super.onDraw(canvas);
	float radius=Math.max(w/2, h/2)+Space/2;
	canvas.drawCircle(w/2+Space/2, h/2+Space/2, radius, mPaint);
	canvas.drawBitmap(b, Space/2, Space/2, mPaint);
	
}
	/*  
	 * 
	 * @Description: TODO
	 *  
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(w+Space, h+Space);
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void startAni(){
		 oo= ObjectAnimator.ofFloat(this, "rotation", 0,360);
		oo.setDuration(500);
		oo.setRepeatCount(-1);
		oo.setRepeatMode(ValueAnimator.RESTART);
		oo.start();
		
	}
	
	public void stopAni(){
		if (oo!=null&&oo.isRunning()) {
			oo.cancel();
			oo=null;
		}
	}
}
