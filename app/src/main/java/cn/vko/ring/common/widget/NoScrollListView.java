package cn.vko.ring.common.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author shikh
 * 
 */
public class NoScrollListView extends ListView {

	public NoScrollListView(Context context) {

		super(context);
	}

	public NoScrollListView(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

}
