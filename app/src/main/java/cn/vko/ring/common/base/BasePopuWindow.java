package cn.vko.ring.common.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public abstract class BasePopuWindow extends PopupWindow {
	public Context context;
	protected View contentView;
	protected ClickSureListener cliclSureListener;

	public void setCliclSureListener(ClickSureListener cliclSureListener) {
		this.cliclSureListener = cliclSureListener;
	}

	public interface ClickSureListener {
		public abstract void clickSureListner(String... param);
	}
	
	public BasePopuWindow(Context context) {
		super(context);
		this.context = context;
		contentView = View.inflate(context, getResView(), null);
		setContentView(contentView);
		if(getAnimationStyle() != 0){
			setAnimationStyle(getAnimationStyle());
		}
		setUpViews(contentView);
		setUpListener();
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new BitmapDrawable());
		if(!updateView(contentView)){
			update(0, 0, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			setFocusable(true);
			setOutsideTouchable(true);
		}
	}

	public abstract void setUpViews(View contentView);

	public abstract void setUpListener();
	public abstract int getAnimationStyle();
	public abstract int getResView();

	public abstract boolean updateView(View contentView);

	public void setAttribute(int width, int Height) {
		setWidth(width);
		setHeight(Height);
	}

}
