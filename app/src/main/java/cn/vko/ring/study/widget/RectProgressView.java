package cn.vko.ring.study.widget;

import cn.vko.ring.R;
import cn.vko.ring.utils.StringUtils;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class RectProgressView extends View {
	/**
	 * mPaint 鐢荤瑪瀵硅薄
	 */
	private Paint mPaint;

	/**
	 * mInitColor 杩涘害鏉″垵濮嬮鑹�	 */
	private int mInitColor;

	/**
	 * mProgressColor 杩涘害鏉¤繘搴﹂鑹�	 */
	private int mProgressColor;

	/**
	 * mBorderWidth 杈规瀹藉害
	 */
	private float mBorderWidth;

	/**
	 * mBorderColor 杈规棰滆壊
	 */
	private int mBorderColor;

	/**
	 * mTextColor 鏂囨湰棰滆壊
	 */
	private int mTextColor;

	/**
	 * mTextSize 鏂囨湰澶у皬
	 */
	private float mTextSize;

	/**
	 * mText 鏂囨湰鍐呭
	 */
	private String mText;

	/**
	 * mProgress 杩涘害
	 */
	private int mProgress;

	/**
	 * mMax 鏈�ぇ鍊�	 */
	private int mMax;

	/**
	 * mRadius 鍦嗚澶у皬
	 */
	private float mRadius;

	public RectProgressView(Context context) {
		this(context, null);
	}

	public RectProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RectProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initArray(context, attrs);
	}

	private void initArray(Context context, AttributeSet attrs) {
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.RectProgressView);
		this.mInitColor = array.getColor(
				R.styleable.RectProgressView_initColor, getContext()
						.getResources()
						.getColor(R.color.default_rect_initColor));
		this.mProgressColor = array.getColor(
				R.styleable.RectProgressView_progressColor,
				getContext().getResources().getColor(
						R.color.default_rect_progressColor));
		this.mBorderColor = array.getColor(
				R.styleable.RectProgressView_borderColor,
				getContext().getResources().getColor(
						R.color.default_rect_borderColor));
		this.mBorderWidth = array.getDimension(
				R.styleable.RectProgressView_borderWidth,
				getContext().getResources().getDimension(
						R.dimen.default_rect_borderWidth));
		this.mTextColor = array.getColor(
				R.styleable.RectProgressView_textColor, getContext()
						.getResources()
						.getColor(R.color.default_rect_textColor));
		this.mTextSize = array.getDimension(
				R.styleable.RectProgressView_textSize,
				getContext().getResources().getDimension(
						R.dimen.default_rect_textSize));
		this.mText = array.getString(R.styleable.RectProgressView_text);
		this.mProgress = array.getInteger(
				R.styleable.RectProgressView_progress,
				getContext().getResources().getInteger(
						R.integer.default_rect_progress));
		this.mMax = array.getInteger(
				R.styleable.RectProgressView_max,
				getContext().getResources().getInteger(
						R.integer.default_rect_max));
		this.mRadius = array.getDimension(
				R.styleable.RectProgressView_radiuscri,
				getContext().getResources().getDimension(
						R.dimen.default_rect_radius));
		array.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		initPaint();
		int width = getWidth();
		int height = getHeight();
		//缁樺埗杈规鐭╁舰
		if (this.mBorderWidth > 0.0f) {
			this.mPaint.setColor(this.mBorderColor);
			RectF borderRectf = new RectF(0, 0, width, height);
			canvas.drawRoundRect(borderRectf, this.mRadius, this.mRadius,
					this.mPaint);
		}
		
		//缁樺埗鍒濆鐭╁舰
		this.mPaint.setColor(this.mInitColor);
		RectF initRectf = new RectF(this.mBorderWidth, this.mBorderWidth, width
				- this.mBorderWidth, height - this.mBorderWidth);
		canvas.drawRoundRect(initRectf, this.mRadius, this.mRadius, this.mPaint);

		//缁樺埗杩涘害鐭╁舰
		float current = (float) this.mProgress / (float) this.mMax;
		if (current > 0.0f) {
			this.mPaint.setColor(this.mProgressColor);
			RectF progressRectf = new RectF(this.mBorderWidth,
					this.mBorderWidth, (width - this.mBorderWidth) * current,
					height - this.mBorderWidth);
			if (this.mProgress == this.mMax) {
				canvas.drawRoundRect(progressRectf, this.mRadius, this.mRadius,
						this.mPaint);
			} else {
				float[] radii = { this.mRadius, this.mRadius, 0, 0, 0, 0,
						this.mRadius, this.mRadius };
				Path path = new Path();
				path.addRoundRect(progressRectf, radii, Path.Direction.CW);
				canvas.drawPath(path, this.mPaint);
			}
		}

		//缁樺埗鏂囨湰
		if (!StringUtils.isEmpty(this.mText)) {
			this.mPaint.setColor(this.mTextColor);
			this.mPaint.setTextSize(this.mTextSize);
			FontMetricsInt fontMetrics = this.mPaint.getFontMetricsInt();
			float baseline = initRectf.top
					+ (initRectf.bottom - initRectf.top - fontMetrics.bottom + fontMetrics.top)
					/ 2 - fontMetrics.top;
			canvas.drawText(this.mText, 10, baseline, this.mPaint);
		}
	}

	/**
	 * 
	 * @Title: initPaint 
	 * @Description: 鍒濆鍖栫敾绗斿璞�	 * @param     璁惧畾鏂囦欢 
	 * @return void    杩斿洖绫诲瀷 
	 * @throws
	 */
	private void initPaint() {
		this.mPaint = new Paint();
		this.mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		this.mPaint.setStyle(Style.FILL);
	}

	public int getmInitColor() {
		return mInitColor;
	}

	public void setmInitColor(int mInitColor) {
		this.mInitColor = mInitColor;
	}

	public int getmProgressColor() {
		return mProgressColor;
	}

	public void setmProgressColor(int mProgressColor) {
		this.mProgressColor = mProgressColor;
	}

	public float getmBorderWidth() {
		return mBorderWidth;
	}

	public void setmBorderWidth(float mBorderWidth) {
		this.mBorderWidth = mBorderWidth;
	}

	public int getmBorderColor() {
		return mBorderColor;
	}

	public void setmBorderColor(int mBorderColor) {
		this.mBorderColor = mBorderColor;
	}

	public int getmTextColor() {
		return mTextColor;
	}

	public void setmTextColor(int mTextColor) {
		this.mTextColor = mTextColor;
	}

	public float getmTextSize() {
		return mTextSize;
	}

	public void setmTextSize(float mTextSize) {
		this.mTextSize = mTextSize;
	}

	public String getmText() {
		return mText;
	}

	public void setmText(String mText) {
		this.mText = mText;
	}

	public synchronized int getmProgress() {
		return mProgress;
	}

	public synchronized void setmProgress(int mProgress) {
		if (mProgress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		}
		if (mProgress > this.mMax) {
			mProgress = this.mMax;
		}
		if (mProgress <= this.mMax) {
			this.mProgress = mProgress;
			postInvalidate();
		}
	}

	public synchronized int getmMax() {
		return mMax;
	}

	public synchronized void setmMax(int mMax) {
		if (mMax < 0) {
			throw new IllegalArgumentException("max not less than 0");
		}
		this.mMax = mMax;
	}

	public float getmRadius() {
		return mRadius;
	}

	public void setmRadius(float mRadius) {
		this.mRadius = mRadius;
	}
}

