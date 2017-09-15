package cn.vko.ring.common.widget;
import cn.vko.ring.R;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NumViewPagerIndicator extends LinearLayout
{

	private static final int COLOR_TEXT_NORMAL = 0xFFD0D0D0;
	private static final int COLOR_TEXT_SELECTED = 0xFFFFFFFF;
	private int mTabCount,mPosition;
	private ViewPager mViewPage;
	public NumViewPagerIndicator(Context context)
	{
		this(context, null);
	}

	public NumViewPagerIndicator(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
	}

	public void setEntry(int count,ViewPager mViewPage)
	{
		this.mViewPage = mViewPage;
		mTabCount = count;
		if(mViewPage != null){
			mViewPage.setOnPageChangeListener(new OnPageChangeListener()
			{
				@Override
				public void onPageSelected(int position)
				{
					change(position);
				}

				@Override
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels)
				{
					
				}

				@Override
				public void onPageScrollStateChanged(int state)
				{

				}
			});

			
		}
		if(mTabCount >0){
			generateTitleView();
		}
	}

	private void change(int position)
	{
		TextView tvOld = (TextView)getChildAt(mPosition);
		tvOld.setTextColor(COLOR_TEXT_NORMAL);
		tvOld.setBackgroundResource(R.drawable.shape_num_normbl);
		mPosition = position; 
		TextView tvNew =(TextView)getChildAt(mPosition);
		tvNew.setTextColor(COLOR_TEXT_SELECTED);
		tvNew.setBackgroundResource(R.drawable.shape_num_selected);
		invalidate();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		return super.dispatchTouchEvent(ev);
	}

	private void generateTitleView()
	{
		if (getChildCount() > 0)
			this.removeAllViews();

//		setWeightSum(mTabCount);
		for (int i = 0; i < mTabCount; i++)
		{
			TextView tv = new TextView(getContext());
//			tv.setPadding(10, 10, 10, 10);
			int size = (int) getResources().getDimension(R.dimen.dimen30);
			int margin = (int) getResources().getDimension(R.dimen.dimen10);
			LayoutParams lp = new LayoutParams(size,size);
			lp.rightMargin = margin;
			lp.leftMargin = margin;
			tv.setGravity(Gravity.CENTER);
			
			if(i == mPosition){
				tv.setTextColor(COLOR_TEXT_SELECTED);
				tv.setBackgroundResource(R.drawable.shape_num_selected);
			}else{
				tv.setTextColor(COLOR_TEXT_NORMAL);
				tv.setBackgroundResource(R.drawable.shape_num_normbl);
			}
			tv.setText((i+1)+"");
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			tv.setLayoutParams(lp);
			final int pos = i;
			tv.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(mViewPage != null){
						mViewPage.setCurrentItem(pos);
					}
				}
			});
			addView(tv);
		}
	}

}
