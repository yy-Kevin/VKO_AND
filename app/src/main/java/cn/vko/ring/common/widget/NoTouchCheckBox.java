package cn.vko.ring.common.widget;

import cn.vko.ring.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class NoTouchCheckBox extends ImageView implements Checkable {
	int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
	private boolean isChecked;
	public NoTouchCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
//		imageId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
	}
	public static Bitmap getDrawable(Resources res, int id){  
	    return BitmapFactory.decodeStream(res.openRawResource(id));  
	}
	@Override
	public void setChecked(boolean isChecked) {
		if (this.isChecked != isChecked) {
			this.isChecked = isChecked;
			refreshDrawableState();
		}
		if (listener != null) {
			listener.onCheckedChange(isChecked);
		}
	}
	@Override
	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void toggle() {
		setChecked(!isChecked);
	}
	@Override
	public int[] onCreateDrawableState(int extraSpace) {
//        return super.onCreateDrawableState(extraSpace);
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked()) {
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		}
		return drawableState;
	}
	public void setOnCheckedChangeListener(ICheckedChangeListener listener) {
		this.listener = listener;
	}

	private ICheckedChangeListener listener;

	public interface ICheckedChangeListener {
		void onCheckedChange(boolean isChecked);
	}

}
