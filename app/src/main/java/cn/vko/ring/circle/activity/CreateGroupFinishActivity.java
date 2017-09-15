package cn.vko.ring.circle.activity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.umeng.BaseUMShare;
import cn.vko.ring.utils.Cord2VUtil;

public class CreateGroupFinishActivity extends BaseActivity {

	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.iv_back)
	public ImageView ivBack;
	@Bind(R.id.iv_code)
	public ImageView ivCode;
	@Bind(R.id.tv_school)
	public TextView tvSchool;
	@Bind(R.id.group_name)
	public TextView tvGruopName;
	@Bind(R.id.tv_group_no)
	public TextView tvGruopNo;

	private GroupInfo group;
	private Bitmap bm;
	private String url;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_create_group_finish;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText("创建完成");
		ivBack.setVisibility(View.GONE);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		group = (GroupInfo) getIntent().getExtras().get("GROUP");
		if (group == null)
			return;
		if(TextUtils.isEmpty(group.getSchoolName()) && TextUtils.isEmpty(group.getGradeName())){
			tvSchool.setVisibility(View.GONE);
		}else{
			tvSchool.setText(group.getSchoolName() + " " + group.getGradeName());
		}
		tvGruopName.setText(group.getName());
		tvGruopNo.setText(group.getId());
		try {
			// Bitmap bm = Cord2VUtil.Create2DCode(this, "群帐号", 500);
			Bitmap logo = BitmapFactory.decodeResource(super.getResources(),
					R.drawable.ic_logo);
			url = "http://m.vko.cn/group/crowdHome.html?groupId="+group.getId();
			bm = Cord2VUtil.createCode(url, logo,BarcodeFormat.QR_CODE, 500);
			ivCode.setImageBitmap(bm);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	@OnClick(R.id.but_see)
	void saySeeGroup() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("GROUPID", group.getId());
		bundle.putString("URL", "http://m.vko.cn/group/crowdHome.html?groupId="
				+ group.getId());
		StartActivityUtil.startActivity(this, GroupHomeActivity.class, bundle,
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
		finish();
	}

	@OnClick(R.id.tv_share_qq)
	void sayShapeQQ() {
		if(group == null) return;

		new BaseUMShare(this, SHARE_MEDIA.QQ).shareByBitmap(group.getName(),group.getIntroduction(),bm,url);
	}

	@OnClick(R.id.tv_share_weixue)
	void sayShapeWeiXue() {
		if(group == null) return;
		 new BaseUMShare(CreateGroupFinishActivity.this, SHARE_MEDIA.WEIXIN).shareByBitmap(group.getName(),group.getIntroduction(),bm,url);
	}

}
