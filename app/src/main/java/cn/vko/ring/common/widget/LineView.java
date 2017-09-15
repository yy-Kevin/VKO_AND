/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: LineView.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.found.widgets 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-18 下午1:51:30 
 * @version: V1.0   
 */
package cn.vko.ring.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.R;

/** 
 * @ClassName: LineView 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-18 下午1:51:30  
 */
public class LineView extends View{

	private Paint mPaint;
	private int lineWith = 1;
	private int color = getResources().getColor(R.color.common_line_color);
	private Type type;

	private int SPACE = 15;
	public LineView(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}

	/** 
	 * 
	 * @Description:TODO 
	 */
	public LineView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}
	
	public void setType(Type type) {
		this.type = type;
		this.setVisibility(View.VISIBLE);
	}

	public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(color);
		mPaint.setStrokeWidth(lineWith);
		SPACE = (int) ViewUtils.getScreenDensity(context)*SPACE;
		
	}
	/*  
	 * 
	 * @Description: TODO
	 *  
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		switch (type) {
			case FILL:
				canvas.drawLine(0, getHeight()/2, getWidth(), getHeight()/2, mPaint);
				break;
			case LEFT_SPACE:
				canvas.drawLine(SPACE, getHeight()/2, getWidth(), getHeight()/2, mPaint);
				break;
			case RIGHT_SPACE:
				canvas.drawLine(0, getHeight()/2, getWidth()-SPACE, getHeight()/2, mPaint);
				break;
			case BOTH_SPACE:
				canvas.drawLine(SPACE, getHeight()/2, getWidth()-SPACE, getHeight()/2, mPaint);
				break;
			default:
				canvas.drawLine(0, getHeight()/2, getWidth(), getHeight()/2, mPaint);
				break;
		}
		postInvalidate();
	}
	public enum Type{
		FILL,LEFT_SPACE,RIGHT_SPACE,BOTH_SPACE
	}
}
