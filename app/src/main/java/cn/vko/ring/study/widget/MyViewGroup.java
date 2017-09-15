package cn.vko.ring.study.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MyViewGroup extends ViewGroup {
	private final static String TAG = "MyViewGroup";

	private final static int VIEW_MARGIN = 2;

	public MyViewGroup(Context context) {
		super(context);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		Log.d(TAG, "widthMeasureSpec = " + widthMeasureSpec
				+ " heightMeasureSpec" + heightMeasureSpec);
		
		for (int index = 0; index < getChildCount(); index++) {

			final View child = getChildAt(index);
	
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {

		Log.e(TAG, "changed = " + arg0 + " left = " + arg1 + " top = " + arg2
				+ " right = " + arg3 + " botom = " + arg4);

		final int count = getChildCount();

		int row = 0;// which row lay you view relative to parent

		int lengthX = arg1; // right position of child relative to parent

		int lengthY = arg2; // bottom position of child relative to parent
		int distanceX=arg3;
		
		for (int i = 0; i < count; i++) {
			
			final View child = this.getChildAt(i);
			child.setPadding(0, 0, 20, 0);
			int width = child.getMeasuredWidth();

			int height = child.getMeasuredHeight();

			lengthX += width + VIEW_MARGIN;

			lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height
					+ arg2;
	
			if (lengthX > distanceX) {
				
				lengthX = width + VIEW_MARGIN + arg1;
				Log.e("view的长度", "lengthX="+lengthX+"arg3="+arg3+"distanceX="+distanceX);
				row++;			
				lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height
						+ arg2;
				
			}
			
			child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
			
		}
		
	}		
	
}
