package cn.vko.ring.circle.activity;

import com.android.volley.VolleyError;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.ViewUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.adapter.GroupClassListAdapter;
import cn.vko.ring.circle.event.Event;
import cn.vko.ring.circle.event.EventManager;

import cn.vko.ring.circle.model.BaseCreateGroup;
import cn.vko.ring.circle.model.BaseGroupClass;
import cn.vko.ring.circle.model.GroupClassInfo;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.NoTouchCheckBox;
import cn.vko.ring.mine.model.BaseResultInfo;
import cn.vko.ring.utils.ACache;

public class CreateGroupTwoActivity extends BaseActivity implements OnItemClickListener{

	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_right)
	public TextView tvFinish;
	@Bind(R.id.lv_class)
	public ListView mListView;
	
	private GroupClassListAdapter mAdapter;
	
	private String name,intro;
	
	private VolleyUtils<BaseGroupClass> volley;
	private VolleyUtils<BaseCreateGroup> http;
	private VolleyUtils<BaseResultInfo> util;
	private GroupInfo group;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_create_group_two;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText(R.string.circle_group_class_text);
		tvFinish.setText(R.string.circle_finish_text);
		tvFinish.setVisibility(View.VISIBLE);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		name = getIntent().getExtras().getString("NAME");
		intro = getIntent().getExtras().getString("INTRO");
		
		group = (GroupInfo) getIntent().getExtras().get("GROUP");
		
		mAdapter = new GroupClassListAdapter(this);
		if(group != null){
			mAdapter.setInfo(group.getGroupClassInfo());
			tvFinish.setVisibility(View.GONE);
		}
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		
		BaseGroupClass.ClassListInfo classInfo = (BaseGroupClass.ClassListInfo) ACache.get(CreateGroupTwoActivity.this).getAsObject("CIRCLE_CLASS");
		if(classInfo != null){
			mAdapter.add(classInfo.getData());
			mAdapter.postNotifyDataSetChanged();
		}
		getGroupClassListTask();
	}

	private void getGroupClassListTask() {
		// TODO Auto-generated method stub
		if(volley == null){
			volley = new VolleyUtils<BaseGroupClass>(this,BaseGroupClass.class);
		}
		Uri.Builder builder = volley.getBuilder(ConstantUrl.VK_CIRCLE_CLASS);
		builder.appendQueryParameter("pageIndex", "1");
		builder.appendQueryParameter("pageSize", "100");
		volley.sendGETRequest(true,builder);
		volley.setUiDataListener(new UIDataListener<BaseGroupClass>() {
			@Override
			public void onDataChanged(BaseGroupClass response) {
				if(response != null && response.getCode() == 0){
					if(response.getData() != null ){
						ACache.get(CreateGroupTwoActivity.this).put("CIRCLE_CLASS", response.getData());
						mAdapter.add(response.getData().getData());
						mAdapter.judgeHasEmpty();
						mAdapter.notifyDataSetChanged();
						return;
					}
				}
				mAdapter.judgeHasEmpty();
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {
				mAdapter.judgeHasEmpty();
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		// TODO Auto-generated method stub
		if(mAdapter.getList() == null || mAdapter.getList().size() == 0){
			getGroupClassListTask();
			return;
		}
		NoTouchCheckBox cb = (NoTouchCheckBox) view.findViewById(R.id.cb);
		if(!cb.isChecked()){
			mAdapter.setInfo(mAdapter.getListItem(position));
			mAdapter.notifyDataSetChanged();
		}
		if(group != null){
			updateGroupInfo(mAdapter.getListItem(position));
		}
	}
	@OnClick(R.id.iv_back)
	void sayGoBack(){
		finish();
	}
	
	@OnClick(R.id.tv_right)
	void saySubmit(){// 创建群组
		GroupClassInfo info = mAdapter.getInfo();
		if(info == null){
			Toast.makeText(this,"选择一个群分类",Toast.LENGTH_LONG).show();
			return;
		}
		
		//摸似 请求接口创建成功   关闭第一个界面和本界面 跳转到第三界面
		if(http == null){
			http = new VolleyUtils<BaseCreateGroup>(this,BaseCreateGroup.class);
		}
		Map<String,String> params = new HashMap<String,String>();
		params.put("userName", VkoContext.getInstance(this).getUser().getName());
		params.put("groupName", name);
		params.put("groupInfo", intro);
		params.put("gTypeId", info.getId());
		params.put("token", VkoContext.getInstance(this).getToken());
		http.sendPostRequest(true,ConstantUrl.VK_CIRCLE_CREATE_GROUP,params);
		http.setUiDataListener(new UIDataListener<BaseCreateGroup>() {
			@Override
			public void onDataChanged(BaseCreateGroup response) {
				if(response != null && response.getCode() == 0){
					if(response.getData() != null){
						sendBroadcast(new Intent("FINISH"));
						Bundle bundle = new Bundle();
						bundle.putSerializable("GROUP", response.getData());
						StartActivityUtil.startActivity(CreateGroupTwoActivity.this, CreateGroupFinishActivity.class, bundle,
								Intent.FLAG_ACTIVITY_SINGLE_TOP);
						EventManager.fire(new Event<GroupInfo>(response.getData(), Event.CREATE_EVENT));
						finish();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}
	
	private void updateGroupInfo(final GroupClassInfo info){
		if(util == null){
			util = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
		}
		Map<String,String> params = new HashMap<String,String>();
		params.put("groupId", group.getId());
		params.put("gTypeId", info.getId());
		params.put("token", VkoContext.getInstance(this).getUserId());
		util.sendPostRequest(true,ConstantUrl.VK_CIRCLE_UPDATE_GROUP,params);
		util.setUiDataListener(new UIDataListener<BaseResultInfo>() {
			@Override
			public void onDataChanged(BaseResultInfo response) {
				if(response != null && response.getCode() == 0){
					if(response.isData()){
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putSerializable("CLASSINFO", info);
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

}
