package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.mine.model.RevisePersonMsgInfo;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

public class SelectSexActivity extends BaseActivity implements OnClickListener,
		UIDataListener<RevisePersonMsgInfo> {

	private ImageView ivLeft;
	private TextView tvTitle;
	private TextView tvBoy;
	private TextView tvGirl;
	private Intent intent;
	private Drawable defaultBg;
	private TextView tvTemp;

	@Override
	public int setContentViewResId() {
		return R.layout.activity_select_sex;
	}

	@Override
	public void initView() {
		ivLeft = (ImageView) this.findViewById(R.id.iv_back);
		tvTitle = (TextView) this.findViewById(R.id.tv_title);
		tvBoy = (TextView) this.findViewById(R.id.tv_boy);
		tvGirl = (TextView) this.findViewById(R.id.tv_girl);
		ivLeft.setOnClickListener(this);
		tvBoy.setOnClickListener(this);
		tvGirl.setOnClickListener(this);
		defaultBg = getResources().getDrawable(
				R.drawable.my_coursesetting_normel);
	}

	@Override
	public void initData() {
		tvTitle.setText("性别");
		int dender = getIntent().getExtras().getInt("GENDER");
		if (dender == 0) {
			tvTemp = tvBoy;
		} else {
			tvTemp = tvGirl;
		}
		switchDrawable(tvTemp);
	}

	@Override
	public void onClick(View v) {
		intent = new Intent();
		switch (v.getId()) {
		case R.id.tv_boy:
			intent.putExtra("sex", tvBoy.getText().toString().trim());
			intent.putExtra("sexId", "0");
			VkoContext.getInstance(this).getUser().setGender(0);
			getSexTask(intent);
			break;
		case R.id.tv_girl:
			intent.putExtra("sex", tvGirl.getText().toString().trim());
			intent.putExtra("sexId", "1");
			VkoContext.getInstance(this).getUser().setGender(1);
			getSexTask(intent);
			break;
		case R.id.iv_back:
			this.finish();
			break;
		}

	}

	// 给选中的性别做标记
	public void switchDrawable(TextView tv) {
		defaultBg.setBounds(0, 0, defaultBg.getMinimumWidth(),
				defaultBg.getMinimumHeight()); // 设置边界
		tv.setCompoundDrawables(null, null, defaultBg, null);// 画在右边
	}

	private void getSexTask(Intent i) {
		String url = ConstantUrl.VKOIP + "/user/mod";
		VolleyUtils<RevisePersonMsgInfo> volleyUtils = new VolleyUtils<RevisePersonMsgInfo>(this,RevisePersonMsgInfo.class);
		Map<String,String> params = new HashMap<>();
		Builder builder = volleyUtils.getBuilder(url);
		params.put("gender", i.getStringExtra("sexId"));
		params.put("udid", VkoContext.getInstance(this)
				.getUser().getUdid());
		params.put("token", VkoContext.getInstance(this).getToken());
		volleyUtils.setUiDataListener(this);
		volleyUtils.sendPostRequest(true,url,params);
//		volleyUtils.requestHttpPost(builder, this, RevisePersonMsgInfo.class);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// EventCountAction.onActivityResumCount(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// EventCountAction.onActivityPauseCount(this);
	}

	@Override
	public void onDataChanged(RevisePersonMsgInfo t) {
		if (t != null) {
			if ("0".equals(t.getCode())) {
				setResult(3, intent);
				finish();
			}
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
