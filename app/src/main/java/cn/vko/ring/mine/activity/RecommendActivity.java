package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.IClickAlertListener;
import cn.vko.ring.common.umeng.BaseUMShare;
import cn.vko.ring.common.widget.dialog.InviteFriendDialog;
import cn.vko.ring.home.presenter.VbDealPrsenter;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class RecommendActivity extends BaseActivity implements IClickAlertListener, UMShareListener {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	public boolean iscomm;
	private InviteFriendDialog mDialog;
	private ShareAction shareAction;
	private BaseUMShare umengShare;

	@Override
	public int setContentViewResId() {
		return R.layout.activity_recommend;
	}
	@Override
	public void initView() {
	}
	@Override
	public void initData() {
		tvTitle.setText("邀请好友");
//		umengShare = UmengShare.getInstance();
//		umengShare.setShareListener(this);
		shareAction = new ShareAction(this).withTitle("微课圈").withMedia(new UMImage(this, R.drawable.login_logo))
				.withText("中学生贴身必备的学习助手，贴身的好老师，懂你的好朋友").withTargetUrl("http://m.vko.cn/reg.html");

	}
	// 设置要分享的内容
	private void setContent() {

	}
	// 通讯录邀请
	@OnClick(R.id.tv_communication)
	public void commnnicationClick() {
		iscomm = VkoConfigure.getConfigure(this).getBoolean("iscomm", false);
		if (iscomm) {
			Intent intent = new Intent(RecommendActivity.this, ContactsActivity.class);
			intent.putExtra("flag", 0);
			startActivity(intent);
		} else {
			Intent intent = new Intent(RecommendActivity.this, BindCommunicationActivity.class);
			startActivity(intent);
		}
	}
	// 微信邀请
	@OnClick(R.id.tv_weixin)
	public void weixinClick() {
		// shareWeixin();
		showAlertDialog(2);
	}
	// QQ邀请
	@OnClick(R.id.tv_qq)
	public void qqClick() {
		showAlertDialog(1);
	}
	// 弹出提示框
	private void showAlertDialog(int i) {
		// TODO Auto-generated method stub
		if (mDialog == null) {
			mDialog = new InviteFriendDialog(this);
			mDialog.setCanceledOnTouchOutside(true);
			mDialog.setOnClickAlertListener(this);
		}
		if (i == 1) {
			mDialog.initTextViewName(i, "QQ好友", "QQ空间");
		} else if (i == 2) {
			mDialog.initTextViewName(i, "微信好友", "微信朋友圈");
		}
		mDialog.show();
	}
	@OnClick(R.id.iv_back)
	public void getBack() {
		finish();
	}
	private void shareQzone() {
		// TODO Auto-generated method stub
		umengShare = new BaseUMShare(this,SHARE_MEDIA.QZONE);
		umengShare.setShareListener(this);
		umengShare.shareAction(shareAction);
	}
	private void shareQQ() {
		// TODO Auto-generated method stub
		umengShare = new BaseUMShare(this,SHARE_MEDIA.QQ);
		umengShare.setShareListener(this);
		umengShare.shareAction(shareAction);
	}
	private void shareFriend() {
		// TODO Auto-generated method stub
		umengShare = new BaseUMShare(this,SHARE_MEDIA.WEIXIN_CIRCLE);
		umengShare.setShareListener(this);
		umengShare.shareAction(shareAction);
	}
	private void shareWeixin() {
		// TODO Auto-generated method stub
		umengShare = new BaseUMShare(this,SHARE_MEDIA.WEIXIN);
		umengShare.setShareListener(this);
		umengShare.shareAction(shareAction);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// UMSsoHandler ssoHandler = UmengShape.getUMservice().getConfig()
		// .getSsoHandler(resultCode);
		// if (ssoHandler != null) {
		// ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		// }
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	// 点击屏幕下方提示框时回调
	@Override
	public void onClick(int type) {
		// TODO Auto-generated method stub
		if (mDialog.getType() == 1) {
			if (type == 1) {
				shareQQ();
			} else {
				shareQzone();
			}
		} else {
			if (type == 1) {
				shareWeixin();
			} else {
				shareFriend();
			}
		}
		mDialog.dismiss();
	}
	@Override
	public void onResult(SHARE_MEDIA share_media) {
		new VbDealPrsenter(this).doRequest(VbDealPrsenter.SHARE_FRIENDS);
	}

	@Override
	public void onError(SHARE_MEDIA share_media, Throwable throwable) {

	}

	@Override
	public void onCancel(SHARE_MEDIA share_media) {

	}
}
