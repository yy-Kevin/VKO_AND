package cn.vko.ring.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import cn.shikh.utils.ViewUtils;

/**
 * @author shikh
 * 
 */
public class GridLineView extends View {

	Rect mDrawRect;
	int width, height;

	/**
	 * @param context
	 * @param attrs
	 */
	public GridLineView(Context context, AttributeSet attrs) {

		super(context, attrs);

		// TODO Auto-generated constructor stub
		width = ViewUtils.getScreenWidth(context);
		height = ViewUtils.getScreenHeight(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// TODO Auto-generated method stub
		super.onDraw(canvas);
		mDrawRect = new Rect(0, 0, width, height); // in screen space
		if (canvas != null) {
			Paint mGuidelinePaint = new Paint();
			String SEMI_TRANSPARENT = "#AAFFFFFF";
			mGuidelinePaint.setColor(Color.parseColor(SEMI_TRANSPARENT));
			mGuidelinePaint.setStrokeWidth(2.0f);
			drawRuleOfThirdsGuidelines(canvas, mGuidelinePaint);

		}
	}

	private void drawRuleOfThirdsGuidelines(Canvas canvas, Paint mGuidelinePaint) {
		int left = mDrawRect.left;
		int right = mDrawRect.right;
		int top = mDrawRect.top;
		int bottom = mDrawRect.bottom;
		float width = mDrawRect.width();
		float height = mDrawRect.height();
		// Draw vertical guidelines.
		final float oneThirdCropWidth = width / 3;
		final float x1 = left + oneThirdCropWidth;
		canvas.drawLine(x1, top, x1, bottom, mGuidelinePaint);
		final float x2 = right - oneThirdCropWidth;
		canvas.drawLine(x2, top, x2, bottom, mGuidelinePaint);

		// Draw horizontal guidelines.
		final float oneThirdCropHeight = height / 3;

		final float y1 = top + oneThirdCropHeight;
		canvas.drawLine(left, y1, right, y1, mGuidelinePaint);
		final float y2 = bottom - oneThirdCropHeight;
		canvas.drawLine(left, y2, right, y2, mGuidelinePaint);
	}

}
