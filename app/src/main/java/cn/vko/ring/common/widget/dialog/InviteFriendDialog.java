/*******************************************************************************
 * Copyright (c) 2015 by Luxury Corporation all right reserved.
 * 2015-8-12 
 * 
 *******************************************************************************/
package cn.vko.ring.common.widget.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseDialog;
import cn.vko.ring.common.listener.IClickAlertListener;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 
 * 编写日期:	2015-8-12
 * 作者:	宋宁宁
 * 
 * 历史记录
 *    修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class InviteFriendDialog extends BaseDialog implements
		View.OnClickListener {

	private Button btnFirend, btnQzone, btnCancel;
	private int type;
	private IClickAlertListener listener;

	public void setOnClickAlertListener(IClickAlertListener listener) {
		this.listener = listener;
	}

	/**
	 * @param context
	 *
	 */
	public InviteFriendDialog(Context context) {
		super(context, R.style.Dialog);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return ssssssss
	 */
	public int getType() {
		return type;
	}

	@Override
	public void setUpViews(View root) {
		// TODO Auto-generated method stub
		btnFirend = (Button) root.findViewById(R.id.invite_good_frind);
		btnQzone = (Button) root.findViewById(R.id.invite_good_frind_circle);
		btnCancel = (Button) root.findViewById(R.id.invite_cancel);
	}

	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub
		btnCancel.setOnClickListener(this);
		btnFirend.setOnClickListener(this);
		btnQzone.setOnClickListener(this);
	}

	@Override
	public boolean getCancelableOnclick() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getGravity() {
		// TODO Auto-generated method stub
		return Gravity.BOTTOM;
	}

	@Override
	public int getRootId() {
		// TODO Auto-generated method stub
		return R.layout.dialog_invite_qq;
	}

	@Override
	public int getAnimatStly() {
		// TODO Auto-generated method stub
		return R.style.dialog_anim;
	}

	@Override
	public LayoutParams getILayoutParams() {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		return lp;
	}

	public void initTextViewName(int type, String friend, String qzone) {
		btnFirend.setText(friend);
		btnQzone.setText(qzone);
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnCancel) {
			dismiss();
		} else if (listener != null) {
			if (v == btnFirend) {
				listener.onClick(1);
			} else {
				listener.onClick(2);
			}
		}
	}

}
