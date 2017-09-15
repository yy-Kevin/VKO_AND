package cn.vko.ring.circle.activity;

import com.android.volley.VolleyError;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.mine.model.BaseResultInfo;
import cn.vko.ring.utils.EditTextUtils;

public class GroupNameUpdateActivity extends BaseActivity {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_right)
	public TextView tvSave;
	@Bind(R.id.et_group_name)
	public EditText editText;
	
	private GroupInfo group;
	
	private VolleyUtils<BaseResultInfo> volley;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_group_name;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText("修改群名称");
		tvSave.setText("保存");
		tvSave.setVisibility(View.VISIBLE);
		new EditTextUtils(editText, 10, null);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		group = (GroupInfo) getIntent().getExtras().get("GROUP");
		if (group != null) {
			editText.setText(group.getName());
		}

	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		finish();
	}

	@OnClick(R.id.tv_right)
	void saySave() {
		String name = editText.getText().toString();
		if(TextUtils.isEmpty(name)){
			Toast.makeText(this, "群名称不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		updateGroupInfo(name);
	}
	
	private void updateGroupInfo(final String name){
		if(volley == null){
			volley = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
		}
		Map<String,String> params = new HashMap<String,String>();
		Uri.Builder builder = volley.getBuilder(ConstantUrl.VK_CIRCLE_UPDATE_GROUP);
		params.put("groupId", group.getId());
		params.put("groupName", name);
		params.put("token", VkoContext.getInstance(this).getUserId());
		volley.sendPostRequest(true,ConstantUrl.VK_CIRCLE_UPDATE_GROUP,params);
		volley.setUiDataListener(new UIDataListener<BaseResultInfo>() {
			@Override
			public void onDataChanged(BaseResultInfo response) {
				if(response != null && response.getCode() == 0){
					if(response.isData()){
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putString("NAME", name);
						intent.putExtras(bundle);
						setResult(RESULT_OK, intent);
						finish();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		hideSoftInput(this, editText, true);
		super.onPause();
	}

}
