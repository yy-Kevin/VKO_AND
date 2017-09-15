/*******************************************************************************
 * Copyright (c) 2015 by Luxury Corporation all right reserved.
 * 2015-8-12 
 * 
 *******************************************************************************/
package cn.vko.ring.common.widget.dialog;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
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
public class ChosePicDialog extends BaseDialog implements
		View.OnClickListener {

	private Button btnCancel;
	private TextView tvCamera,tvPhoto,tvOther;
	private View line;
	private IClickAlertListener listener;
	private boolean isClickFir = true,isClickSec = true,isClickThr = true;
	public void setOnClickAlertListener(IClickAlertListener listener) {
		this.listener = listener;
	}

	/**
	 * @param context
	 *
	 */
	public ChosePicDialog(Context context) {
		super(context, R.style.MaskDialog);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setUpViews(View root) {
		// TODO Auto-generated method stub
		tvCamera = (TextView) root.findViewById(R.id.invite_camera);
		tvPhoto = (TextView) root.findViewById(R.id.invite_photo);
		btnCancel = (Button) root.findViewById(R.id.invite_cancel);
		tvOther = (TextView) root.findViewById(R.id.invite_other);
		line = root.findViewById(R.id.line);
	}

	public void setTextViewWord(String first,boolean isClickFir,String second,boolean isClickSec){
		tvCamera.setText(first);
		tvPhoto.setText(second);
		tvOther.setVisibility(View.GONE);
		line.setVisibility(View.GONE);
		if(!isClickFir){
			tvCamera.setTextColor(context.getResources().getColor(R.color.text_gray_color));
		}else{
			tvCamera.setTextColor(context.getResources().getColor(R.color.invite_alertdialog_btn_bg));
		}
		if(!isClickSec){
			tvPhoto.setTextColor(context.getResources().getColor(R.color.text_gray_color));
		}else{
			tvPhoto.setTextColor(context.getResources().getColor(R.color.invite_alertdialog_btn_bg));
		}
		this.isClickFir = isClickFir;
		this.isClickSec = isClickSec;
	}
	
	public void setTextViewWord(String first,boolean isClickFir,String second,boolean isClickSec,String thred,boolean isClickThr){
		tvCamera.setText(first);
		tvPhoto.setText(second);
		tvOther.setText(thred);
		tvOther.setVisibility(View.VISIBLE);
		line.setVisibility(View.VISIBLE);
		if(!isClickFir){
			tvCamera.setTextColor(context.getResources().getColor(R.color.text_gray_color));
		}else{
			tvCamera.setTextColor(context.getResources().getColor(R.color.invite_alertdialog_btn_bg));
		}
		if(!isClickSec){
			tvPhoto.setTextColor(context.getResources().getColor(R.color.text_gray_color));
		}else{
			tvPhoto.setTextColor(context.getResources().getColor(R.color.invite_alertdialog_btn_bg));
		}
		if(!isClickThr){
			tvOther.setTextColor(context.getResources().getColor(R.color.text_gray_color));
		}else{
			tvOther.setTextColor(context.getResources().getColor(R.color.invite_alertdialog_btn_bg));
		}
		this.isClickThr = isClickThr;
		this.isClickFir = isClickFir;
		this.isClickSec = isClickSec;
	}
	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub
		btnCancel.setOnClickListener(this);
		tvCamera.setOnClickListener(this);
		tvOther.setOnClickListener(this);
		tvPhoto.setOnClickListener(this);
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
		return R.layout.dialog_chose_pic;
	}

	@Override
	public int getAnimatStly() {
		// TODO Auto-generated method stub
		return R.style.dialog_anim;
	}

	@Override
	public LayoutParams getILayoutParams() {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		return lp;
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
			if (v == tvCamera && isClickFir) {
				listener.onClick(1);
			} else if(v == tvPhoto && isClickSec) {
				listener.onClick(2);
			}else if(v == tvOther && isClickThr){
				listener.onClick(3);
			}
			dismiss();
		}
	}

}
