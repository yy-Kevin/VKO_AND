package cn.vko.ring.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SimpleViewPagerIndicator extends LinearLayout {

	private static final int COLOR_TEXT_NORMAL = 0xFF7B7B7B;
	private static final int COLOR_INDICATOR_COLOR = 0xFF378DCC;
	private static final int COLOR_LINE = 0xFFF3F3F3;
		
	private static final int COLOR_BACK_GROUND_BIUE = 0xFF0A9FEA;
	
	private static final int COLOR_TEXT_COURSE=0xFFC8DFEA;
	
	private static final int COLOR_BACK_COURSE=0xFF25EDFF;
	private String[] mTitles;
	private int mTabCount, mPosition;
	private int mIndicatorColor = COLOR_INDICATOR_COLOR;
	
	private int mIndicatorColortwo=COLOR_BACK_COURSE; //滑动线
	private float mTranslationX;
	private Paint mPaint = new Paint();
	private int mTabWidth;
	private ViewPager mViewPage;
	private Context ctx;

	public interface IScrollPageChangeListener {
		void toChangePage(int position);
	};

	public IScrollPageChangeListener iScrollpageChangeListener;

	public void setiScrollpageChangeListener(
			IScrollPageChangeListener iScrollpageChangeListener) {
		this.iScrollpageChangeListener = iScrollpageChangeListener;
	}

	public SimpleViewPagerIndicator(Context context) {
		this(context, null);
	}

	public SimpleViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.ctx = ctx;
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if(mTabCount != 0){
			mTabWidth = w / mTabCount;
		}
	}

	private Boolean flag = false;

	public void setBoolean(Boolean flag) {
		this.flag = flag;
	}

	public void setTitles(String[] titles, ViewPager mViewPage) {
		mTitles = titles;
		mTabCount = titles.length;
		this.mViewPage = mViewPage;
		if (mViewPage != null) {
			mViewPage.addOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					if (iScrollpageChangeListener != null) {
						iScrollpageChangeListener.toChangePage(position);
					}
				}

				@Override
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels) {
					scroll(position, positionOffset);
				}

				@Override
				public void onPageScrollStateChanged(int state) {

				}
			});

		}
		generateTitleView();

	}

	public void setIndicatorColor(int indicatorColor) {
		this.mIndicatorColor = indicatorColor;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
	
		if(!flag){
			mPaint.setColor(mIndicatorColor);			
		}else{
			mPaint.setColor(mIndicatorColortwo);
		}
		mPaint.setStrokeWidth(9.0F);
		canvas.save();
		canvas.translate(mTranslationX, getHeight() - 2);
		canvas.drawLine(0, 0, mTabWidth, 0, mPaint);
		canvas.restore();
	}

	private void scroll(int position, float offset) {
		/**
		 * <pre>
		 *  0-1:position=0 ;1-0:postion=0;
		 * </pre>
		 */

		if (!flag) {
			((TextView) getChildAt(mPosition == 0 ? mPosition : 2 * mPosition))
					.setTextColor(COLOR_TEXT_NORMAL);
			mTranslationX = getWidth() / mTabCount * (position + offset);
			mPosition = position;
			((TextView) getChildAt(mPosition == 0 ? mPosition : 2 * mPosition))
					.setTextColor(COLOR_INDICATOR_COLOR);
		} else {
			((TextView) getChildAt(mPosition == 0 ? mPosition : 2 * mPosition))
					.setTextColor(COLOR_TEXT_COURSE);
			mTranslationX = getWidth() / mTabCount * (position + offset);
			mPosition = position;
			((TextView) getChildAt(mPosition == 0 ? mPosition : 2 * mPosition))
					.setTextColor(COLOR_LINE);
		}

		invalidate();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	private void generateTitleView() {
		if (getChildCount() > 0)
			this.removeAllViews();
		int count = mTitles.length;

		setWeightSum(count);
		for (int i = 0; i < count; i++) {
			TextView tv = new TextView(getContext());
			LayoutParams lp = new LayoutParams(0,
					LayoutParams.MATCH_PARENT);
			lp.weight = 1;
			tv.setGravity(Gravity.CENTER);

			if (!flag) {
				if (i == mPosition) {
					tv.setTextColor(COLOR_INDICATOR_COLOR);
				} else {
					tv.setTextColor(COLOR_TEXT_NORMAL);
				}
			} else {
				if (i == mPosition) {
					tv.setTextColor(COLOR_LINE);
				} else {
					tv.setTextColor(COLOR_TEXT_COURSE);
				}
			}

			tv.setText(mTitles[i]);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			tv.setLayoutParams(lp);
			final int pos = i;
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mViewPage != null) {
						mViewPage.setCurrentItem(pos);
					}
				}
			});
			addView(tv);
			if (count != 1 && i != count - 1) {
				View v = new View(getContext());
				if(!flag){
					v.setBackgroundColor(COLOR_LINE);			
				}else{
					v.setBackgroundColor(COLOR_BACK_GROUND_BIUE);			
				}					
				LayoutParams params = new LayoutParams(1,
						LayoutParams.MATCH_PARENT);
				params.setMargins(0, 30, 0, 30);
				v.setLayoutParams(params);
				addView(v);
			}
		}
	}

}
