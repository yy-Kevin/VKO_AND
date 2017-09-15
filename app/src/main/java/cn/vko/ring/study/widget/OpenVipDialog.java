package cn.vko.ring.study.widget;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.common.listener.IOnClickItemListener;
import cn.vko.ring.common.widget.dialog.UserVbDialog;
import cn.vko.ring.home.model.BtnInfo;
import cn.vko.ring.mine.activity.MembersCenterActivity;

public class OpenVipDialog {
//	private AlertDialog dialog;
	public Activity act;
	private UserVbDialog dialog;
	public OpenVipDialog(Activity act) {
		this.act = act;
		showOpenVipDialog();
	}	
	private void showOpenVipDialog(){
		if (dialog == null) {
			dialog = new UserVbDialog(act);
			dialog.setContentText("开通会员后才可观看视频");
			dialog.setHeadImage(R.drawable.icon_openvip_head);
			dialog.setBtnInfo(new BtnInfo("取消", R.color.white, R.drawable.shape_gray_btn),new BtnInfo("开通会员", R.color.white,
					R.drawable.selector_light_blue_button));
			dialog.setOnClickItemListener(new IOnClickItemListener<String>() {

				@Override
				public void onItemClick(int position, String t, View v) {
					// TODO Auto-generated method stub
					if(position == 1){
						StartActivityUtil.startActivity(act, MembersCenterActivity.class, Intent.FLAG_ACTIVITY_SINGLE_TOP);
					}
					dialog.dismiss();
				}
			});
		}
		if(!dialog.isShowing()){
			dialog.show();
		}
	}
}
