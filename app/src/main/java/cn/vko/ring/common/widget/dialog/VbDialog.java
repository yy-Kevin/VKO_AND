/**   
 * Copyright © 2016 cn.vko.com. All rights reserved.
 * 
 * @Title: VbDialog.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.common.widget.dialog 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2016-1-7 下午5:45:16 
 * @version: V1.0   
 */
package cn.vko.ring.common.widget.dialog;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseDialog;
import cn.vko.ring.common.listener.IActivityFinishListener;

/**
 * @ClassName: VbDialog
 * @Description: TODO
 * @author: JiaRH
 * @date: 2016-1-7 下午5:45:16
 */
public class VbDialog extends BaseDialog {
	private VbDialog instance;
	@Bind(R.id.vb_info)
	TextView tvVbInfo;
	@Bind(R.id.tv_vb_num)
	TextView tvVbNum;
	@Bind(R.id.iv_vb_image)
	ImageView ivImage;
	@Bind(R.id.vb_bg)
	View vbBg;

	IActivityFinishListener ifinishListener;
	
	public void setIfinishListener(IActivityFinishListener ifinishListener) {
		this.ifinishListener = ifinishListener;
	}
	public VbDialog(Context context) {
		super(context, R.style.MaskDialog);
		instance = this;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public void setUpViews(View root) {
		
		scaleAnimRun(ivImage);
		scaleAnimRun(tvVbInfo);
		scaleAnimRun(tvVbNum);
	}
	public void scaleAnimRun(final View view) {
		ObjectAnimator anim = ObjectAnimator//
				.ofFloat(view, "vko", 0.0F, 1.5F, 1.0F)//
				.setDuration(500);//
		anim.start();
		anim.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float cVal = (Float) animation.getAnimatedValue();
				view.setScaleX(cVal);
				view.setScaleY(cVal);
				if (view instanceof TextView) {
					view.setAlpha(cVal);
				}
			}
		});
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub
	}
	public void showDialog(int vbNum) {
		if(context != null && !((Activity)context).isFinishing()){
			tvVbNum.setText(vbNum + "V币");
			show();
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					closeDialog();
				}
			}, 3000);
		}
	}
	private void closeDialog() {
		if (instance != null && instance.isShowing()) {
			instance.dismiss();
//			if (ifinishListener!=null) {
//				ifinishListener.finishActivity();
//			}
		}
	}
	@OnClick({R.id.tv_vb_num,R.id.iv_vb_image,R.id.vb_bg,R.id.vb_info})
	public void onClickecd(){
		closeDialog();
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public boolean getCancelableOnclick() {
		// TODO Auto-generated method stub
		return true;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public int getGravity() {
		// TODO Auto-generated method stub
		return Gravity.CENTER;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public int getRootId() {
		// TODO Auto-generated method stub
		return R.layout.dialog_vb_layout;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public int getAnimatStly() {
		// TODO Auto-generated method stub
		return 0;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public LayoutParams getILayoutParams() {
		LayoutParams lp = new LayoutParams(ViewUtils.getScreenWidth(context), ViewUtils.getScreenHeight(context));
		return lp;
	}
}
