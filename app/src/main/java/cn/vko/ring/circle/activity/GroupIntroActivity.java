package cn.vko.ring.circle.activity;

import com.android.volley.VolleyError;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class GroupIntroActivity extends BaseActivity {
	
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_right)
	public TextView tvSave;
	@Bind(R.id.et_group_intro)
	public EditText editText;
	@Bind(R.id.tv_num_filter)
	public TextView mTextView;
	private GroupInfo group;
	private int type;// 1 修改 0 展示
	private VolleyUtils<BaseResultInfo> volley;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_group_intro;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		new EditTextUtils(editText, 50, mTextView);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		group = (GroupInfo) getIntent().getExtras().get("GROUP");
		if(group == null) return;
		type = getIntent().getExtras().getInt("TYPE");
		if(type == 1){
			tvTitle.setText("修改群简介");
			tvSave.setText("保存");
			tvSave.setVisibility(View.VISIBLE);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}else{
			editText.setFocusable(false);
			editText.setEnabled(false);
			tvTitle.setText("群简介");
		}	
		editText.setText(group.getIntroduction());
	}
	
	@OnClick(R.id.iv_back)
	void sayBack(){
		finish();
	}
	
	@OnClick(R.id.tv_right)
	void saySave(){
		String intro = editText.getText().toString();
		updateGroupInfo(intro);
	}
	
	private void updateGroupInfo(final String intro){
		if(volley == null){
			volley = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
		}
		Map<String,String> params = new HashMap<String,String>();
		Uri.Builder builder = volley.getBuilder(ConstantUrl.VK_CIRCLE_UPDATE_GROUP);
		params.put("groupId", group.getId());
		params.put("groupInfo", intro);
		params.put("token", VkoContext.getInstance(this).getUserId());
		volley.sendPostRequest(true,ConstantUrl.VK_CIRCLE_UPDATE_GROUP,params);
		volley.setUiDataListener(new UIDataListener<BaseResultInfo>() {
			@Override
			public void onDataChanged(BaseResultInfo response) {
				if(response != null && response.getCode() == 0){
					if(response.isData()){
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putString("INTRO", intro);
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
