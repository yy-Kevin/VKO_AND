package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.model.MsgInfo;

public class FeedBackActivity extends BaseActivity implements
		UIDataListener<MsgInfo> {
	private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0x10;
	private TextView tvLogin;
	private EditText edContent;
	private EditText edCtat;
	@Bind(R.id.tv_phone)
	public TextView tvPhone;

	@Override
	public int setContentViewResId() {
		return R.layout.activity_feedback;
	}

	@Override
	public void initView() {
		tvLogin = (TextView) findViewById(R.id.tv_title);
		edContent = (EditText) this.findViewById(R.id.ed_content);
		edCtat = (EditText) this.findViewById(R.id.ed_ctat);
	}

	@OnClick(R.id.but_freeback)
	public void freeBackClick() {
		if (TextUtils.isEmpty(edContent.getText().toString().trim())) {
			Toast.makeText(this, "输入内容", Toast.LENGTH_SHORT).show();
			return;
		}
		freeBackTask();
	}

	/**
	 * 意见反馈
	 */
	private void freeBackTask() {
		String url = ConstantUrl.VKOIP + "/app/sug";
		VolleyUtils<MsgInfo> volleyUtils = new VolleyUtils<MsgInfo>(this,MsgInfo.class);
		UserInfo user = VkoContext.getInstance(this).getUser();
		if (user == null) {
			return;
		}
		Map<String,String> params = new HashMap<String,String>();
		params.put("cnt", edContent.getText().toString().trim());
		params.put("ctat", edCtat.getText().toString().trim());
		params.put("token", user.getToken());
		volleyUtils.setUiDataListener(this);
		volleyUtils.sendPostRequest(true,url,params);

	}

	@Override
	public void initData() {
		tvLogin.setText("意见反馈");
	}

	@OnClick(R.id.iv_back)
	public void imgFinish() {
		this.finish();
	}

	// 联系客服
	@OnClick(R.id.tv_phone)
	public void tvPhoneClick() {
		// TODO Auto-generated method stub
		String number = tvPhone.getText().toString().trim();
		// 用intent启动拨打电话
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
		selfPermissionGranted(intent);
	}

	public void selfPermissionGranted(Intent intent) {
		// For Android < Android M, self permissions are always granted.
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			if (ContextCompat.checkSelfPermission(this,
//					Manifest.permission.CALL_PHONE)
//					!= PackageManager.PERMISSION_GRANTED) {
//
//				// Should we show an explanation?
//				if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,Manifest.permission.READ_CONTACTS)) {
//					// Show an expanation to the user *asynchronously* -- don't block
//					// this thread waiting for the user's response! After the user
//					// sees the explanation, try again to request the permission.
//					Toast.makeText(this,"开启通话权限才能使用",Toast.LENGTH_LONG).show();
//				} else {
//					// No explanation needed, we can request the permission.
//					ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
//					// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//					// app-defined int constant. The callback method gets the
//					// result of the request.
//				}
//			} else {
////				执行获取权限后的操作
//				startActivity(intent);
//			}
//		}else{
//			//执行获取权限后的操作
			startActivity(intent);
//		}
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
		hideSoftInput(this,edContent,true);
		// EventCountAction.onActivityPauseCount(this);
	}

	@Override
	public void onDataChanged(MsgInfo data) {
		if (data != null && data.getCode() == 0) {
			Toast.makeText(this, "反馈成功", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}

	/*@Override
	public void onRequestPermissionsResult(int permission, boolean granted) {
		super.onRequestPermissionsResult(permission, granted);
		Toast.makeText(this, "设置权限", Toast.LENGTH_SHORT).show();
	}*/
}
