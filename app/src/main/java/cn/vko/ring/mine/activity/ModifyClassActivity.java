package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.net.Uri.Builder;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.model.RevisePersonMsgInfo;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

public class ModifyClassActivity extends BaseActivity implements
		UIDataListener<RevisePersonMsgInfo> {

	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_right)
	public TextView tvRight;
	@Bind(R.id.ed_name)
	public EditText edName;
	private String realClass;
	private String newClass;
	private Intent intent;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_modify_class;
	}

	@Override
	public void initView() {
		if (getIntent() != null) {
			realClass = getIntent().getStringExtra("class");
		}
		edName.setText(realClass);
		tvTitle.setText("班级");
		tvRight.setVisibility(View.VISIBLE);
		tvRight.setText("保存");
	}

	@Override
	public void initData() {

	}

	@OnClick(R.id.iv_back)
	public void imgFinishClick() {
		this.finish();
	}

	/** 保存信息 */
	@OnClick(R.id.tv_right)
	public void saveClick() {
		newClass = edName.getText().toString().trim();

		if (newClass != null && !newClass.isEmpty()) {
			if (newClass.length() < 4) {
				if (Integer.parseInt(newClass) > 0
						&& Integer.parseInt(newClass) <= 1000
						&& Integer.parseInt(newClass) < 2147483647) {
					intent = new Intent();
					intent.putExtra("newClass", newClass);
					getSexTask(newClass);
				} else {
					Toast.makeText(this, "请输入0-1000之间的数字", Toast.LENGTH_SHORT)
							.show();
				}

			} else {
				Toast.makeText(this, "请输入0-1000之间的数字", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(this, "请输入您的班级", Toast.LENGTH_SHORT).show();
		}

	}

	private void getSexTask(String newClass) {
		UserInfo userInfo = VkoContext.getInstance(this).getUser();
		String url = ConstantUrl.VKOIP + "/user/mod";
		VolleyUtils<RevisePersonMsgInfo> volleyUtils = new VolleyUtils<RevisePersonMsgInfo>(this,RevisePersonMsgInfo.class);
		Map<String,String> params = new HashMap<String,String>();
//		Builder builder = volleyUtils.getBuilder(url);
		params.put("clasz", newClass);
		params.put("udid", userInfo.getUdid());
		params.put("token", userInfo.getToken());
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
		hideSoftInput(this,edName , true);
		// EventCountAction.onActivityPauseCount(this);
	}

	@Override
	public void onDataChanged(RevisePersonMsgInfo t) {
		if (t != null) {
			if (t.isData() && "0".equals(t.getCode())) {
				Intent intent = new Intent();
				intent.putExtra("newClass", newClass);
				setResult(8, intent);
				finish();
			}
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
