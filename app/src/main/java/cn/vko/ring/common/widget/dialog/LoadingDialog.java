package cn.vko.ring.common.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseDialog;

public class LoadingDialog extends BaseDialog {

//	private TextView textView;
	private LoadingDialog dialog;
	
	public LoadingDialog(Context context) {
		super(context, R.style.Dialog);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	public LoadingDialog createDialog(Context ctx,int stringid) {
		if (dialog == null) {
			dialog = new LoadingDialog(ctx);
			dialog.setPromptTxt(stringid);
		}
		return dialog;
	}
	@Override
	public void setUpViews(View root) {
		// TODO Auto-generated method stub
//		textView = (TextView) root.findViewById(R.id.tv_loading);
	}

	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getCancelableOnclick() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getGravity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRootId() {
		// TODO Auto-generated method stub
		return R.layout.dialog_loading;
	}

	@Override
	public int getAnimatStly() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LayoutParams getILayoutParams() {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		return lp;
	}

	/**
	 * 传0显示默认字体
	 * 
	 * @param stringId
	 */
	public void setPromptTxt(int stringId) {
//		textView.setVisibility(View.VISIBLE);
//		if (stringId == 0) {
//			return;
//		}
//		textView.setText(context.getResources().getString(stringId));
	}

}
