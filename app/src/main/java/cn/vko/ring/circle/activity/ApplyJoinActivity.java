package cn.vko.ring.circle.activity;

import com.android.volley.VolleyError;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.event.Event;
import cn.vko.ring.circle.event.EventManager;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.mine.model.BaseResultInfo;
import cn.vko.ring.utils.EditTextUtils;


/**
 * 申请加群
 * @author Administrator
 *
 */
public class ApplyJoinActivity extends BaseActivity {

	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_right)
	public TextView tvSubmit;
	@Bind(R.id.iv_back)
	public ImageView ivBack;
	@Bind(R.id.et_apply_reason)
	public EditText etReason;
	@Bind(R.id.layout_finish)
	public RelativeLayout layoutFinish;
	@Bind(R.id.layout_apply)
	public RelativeLayout layoutApply;
	@Bind(R.id.tv_num_filter)
	public TextView mTextView;
	private VolleyUtils<BaseResultInfo> http;
	private GroupInfo group;
	private boolean isFinish;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_apply_join;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText("申请加群");
		tvSubmit.setText("发送");
		tvSubmit.setVisibility(View.VISIBLE);
		new EditTextUtils(etReason, 50, mTextView);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		group = (GroupInfo) getIntent().getExtras().get("GROUP");
		
	}
	
	@OnClick(R.id.tv_right)
	void sayJoniGroup(){
		if(!isFinish){
			String reason = etReason.getText().toString();
			joinGroup(reason);
		}else{
			setResult(RESULT_OK);
			finish();
		}
	}
	
	private void joinGroup(String reason) {
		// TODO Auto-generated method stub
		if(group == null)return;
		if (http == null) {
			http = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
		}
		Uri.Builder builder = http.getBuilder(ConstantUrl.VK_CIRCLE_JOIN_GROUP);
		builder.appendQueryParameter("groupIds", group.getId());
		builder.appendQueryParameter("userName", VkoContext.getInstance(this).getUser().getName());
		builder.appendQueryParameter("pId", group.getParentType() + "");
		builder.appendQueryParameter("reason", reason);
		builder.appendQueryParameter("token", VkoContext.getInstance(this)
				.getToken());
		http.sendGETRequest(true,builder);
		http.setUiDataListener(new UIDataListener<BaseResultInfo>() {
			@Override
			public void onDataChanged(BaseResultInfo response) {
				if (response != null && response.getCode() == 0) {
					if (response.isData()) {
						if(group.getParentType().equals("4")){
							group.setFlag(1);
						}else{
							group.setFlag(2);
						}
						EventManager.fire(new Event<GroupInfo>(group,Event.JOIN_EVENT));
						switchView();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}

			});
	}

	protected void switchView() {
		// TODO Auto-generated method stub
		isFinish = true;
		tvSubmit.setText("关闭");
		ivBack.setVisibility(View.GONE);
		layoutFinish.setVisibility(View.VISIBLE);
		layoutApply.setVisibility(View.GONE);
	}
	
	@OnClick(R.id.iv_back)
	void sayBack() {
		finish();
	}


}
