package cn.vko.ring.study.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {
	private static final String TAG = "FlowLayout";
	private int mShowLineNums = 2;
	private int LastIndex;
	public static final String TAG_TV = "tv";
	public static final String TAG_REFRESH = "fr";
	private int mWidth, mPadingLeft, mPadingRight;

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p) {
		return new MarginLayoutParams(p);
	}
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}
	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	/**
	 * 负责设置子控件的测量模式和大小 根据所有子控件设置自己的宽和高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 获得它的父容器为它设置的测量模式和大小
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
		mPadingLeft = getPaddingLeft();
		mPadingRight = getPaddingRight();
		// 如果是warp_content情况下，记录宽和高
		int width = 0;
		int height = 0;
		/**
		 * 记录每一行的宽度，width不断取最大宽度
		 */
		int lineWidth = 0;
		/**
		 * 每一行的高度，累加至height
		 */
		int lineHeight = 0;
		int cCount = getChildCount();
		// 遍历每个子元素
		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			// 测量每一个child的宽和高
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			// 得到child的lp
			MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
			// 当前子空间实际占据的宽度
			int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
			// 当前子空间实际占据的高度
			int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
			/**
			 * 如果加入当前child，则超出最大宽度，则的到目前最大宽度给width，类加height 然后开启新行
			 */
			if (lineWidth + childWidth > sizeWidth) {
				width = Math.max(lineWidth, childWidth);// 取最大的
				lineWidth = childWidth; // 重新开启新行，开始记录
				// 叠加当前高度，
				height += lineHeight;
				// 开启记录下一行的高度
				lineHeight = childHeight;
			} else
			// 否则累加值lineWidth,lineHeight取最大高度
			{
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight, childHeight);
			}
			// 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
			if (i == cCount - 1) {
				width = Math.max(width, lineWidth);
				height += lineHeight;
			}
		}
		mWidth = sizeWidth;
		setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width,
				(modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
	}

	/**
	 * 存储所有的View，按行记录
	 */
	private List<List<View>> mAllViews = new ArrayList<List<View>>();
	/**
	 * 记录每一行的最大高度
	 */
	private List<Integer> mLineHeight = new ArrayList<Integer>();

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mAllViews.clear();
		mLineHeight.clear();
		int width = getWidth();
		int lineWidth = 0;
		int lineHeight = 0;
		// 存储每一行所有的childView
		List<View> lineViews = new ArrayList<View>();
		int cCount = getChildCount();
		// 遍历所有的孩子
		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();
			// 如果已经需要换行
			if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width) {
				// 记录这一行所有的View以及最大高度
				mLineHeight.add(lineHeight);
				// 将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
				mAllViews.add(lineViews);
				lineWidth = 0;// 重置行宽
				lineViews = new ArrayList<View>();
			}
			/**
			 * 如果不需要换行，则累加
			 */
			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
			lineViews.add(child);
		}
		// 记录最后一行
		mLineHeight.add(lineHeight);
		mAllViews.add(lineViews);
		// 得到总行数
		int lineNums = mAllViews.size();
		if (lineNums > 1) {
			setLastViewPostion(lineNums);
		}
		/** 待优化*/
		if (!setLayoutPosition(t, width, lineNums, false)) {
			removeAview();
			setLayoutPosition(t, width, lineNums, true);
		}else{
			setLayoutPosition(t, width, lineNums, true);
		}
	}
	private boolean setLayoutPosition(int t, int width, int lineNums, boolean isLay) {
		int lineHeight;
		List<View> lineViews;
		int left = 0;
		int top = 0;
		for (int i = 0; i < Math.min(mShowLineNums, lineNums); i++) {
			// 每一行的所有的views
			lineViews = mAllViews.get(i);
			// 当前行的最大高度
			lineHeight = mLineHeight.get(i);
			// 遍历当前行所有的View
			for (int j = 0; j < lineViews.size(); j++) {
				View child = lineViews.get(j);
				if (child.getVisibility() == View.GONE) {
					continue;
				}
				MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
				// 计算childView的left,top,right,bottom
				int lc = left + lp.leftMargin;
				int tc = top + lp.topMargin;
				int rc = lc + child.getMeasuredWidth();
				int bc = tc + child.getMeasuredHeight();
				if (rc > width) {
					return false;
				}
				if (isLay) {
					child.layout(lc, tc, rc, bc);
				}
				left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
			}
			left = 0;
			top += lineHeight;
		}
		return true;
	}
	private void setLastViewPostion(int lineNums) {
		if (mAllViews.size() < mShowLineNums - 1) {
			// 不足mShowLineNums行
			return;
		}
		if (mAllViews.get(0).size() < 1) {
			// 没有数据
			return;
		}
		/* 获取刷新按钮 */
		View lastView = getLastView(lineNums);
		if (lastView == null) {
			return;
		}
		if (getLastView(mShowLineNums) instanceof RefreshView) {
			// 第mShowLineNums行的最后一个元素是刷新按钮
			if (mAllViews.get(mShowLineNums - 1).size() == 1) {
				// 就一个刷新按钮
				return;
			} else {
				if (isShowComplete(mAllViews.get(mShowLineNums - 1))) {
					// 刚好完全显示
					return;
				} else {
					// 如果没完全显示移除第mShowLineNums行倒数第二个元素
					removeAview();
					setLastViewPostion(lineNums);
					return;
				}
			}
		} else {
			// 将刷新按钮放在第mShowLineNums行最后
			mAllViews.get(mShowLineNums - 1).add(mAllViews.get(mShowLineNums - 1).size(), lastView);
			setLastViewPostion(lineNums);
		}
	}
	private void removeAview() {
		if (mAllViews.get(mShowLineNums - 1).size() > 1) {
			mAllViews.get(mShowLineNums - 1).remove(mAllViews.get(mShowLineNums - 1).size() - 2);
		}
	}
	/**
	 * @Title: calculateWith
	 * @Description: 计算宽度,看是否完全显示
	 * @param list
	 * @return: void
	 */
	private boolean isShowComplete(List<View> list) {
		int tempLineWith = 0;
		int left = 0;
		// 遍历所有的孩子
		for (int i = 0; i < list.size(); i++) {
			View child = getChildAt(i);
			// MarginLayoutParams lp = (MarginLayoutParams) child
			// .getLayoutParams();
			// int childWidth = child.getMeasuredWidth();
			// int childHeight = child.getMeasuredHeight();
			//
			// // 如果已经需要换行
			// if (childWidth + lp.leftMargin + lp.rightMargin + tempLineWith > mWidth) {
			// return false;
			// }
			// /**
			// * 如果不需要换行，则累加
			// */
			// tempLineWith += childWidth + lp.leftMargin + lp.rightMargin;
			//
			MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
			// 计算childView的left,top,right,bottom
			int lc = left + lp.leftMargin;
			int rc = lc + child.getMeasuredWidth();
			left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
			if (rc > mWidth) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 获取某一行的最后一个view
	 * @Title: getLastView
	 * @Description: TODO
	 * @param lineNums
	 * @return
	 * @return: View
	 */
	private View getLastView(int lineNums) {
		if (lineNums > 0 && lineNums - 1 < mAllViews.size()) {
			List<View> list = mAllViews.get(lineNums - 1);
			if (list.size() > 0) {
				return list.get(list.size() - 1);
			}
		}
		return null;
	}
	public void setmShowLineNums(int mShowLineNums) {
		this.mShowLineNums = mShowLineNums;
	}
}
