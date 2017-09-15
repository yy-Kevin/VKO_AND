package cn.vko.ring.circle.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.adapter.SearchGroupListAdapter;
import cn.vko.ring.circle.event.Event;
import cn.vko.ring.circle.event.EventManager;
import cn.vko.ring.circle.event.IEventListener;
import cn.vko.ring.circle.model.BaseGroupListInfo;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.common.widget.xlv.XListView.IXListViewListener;
import cn.vko.ring.home.model.Pager;
import com.android.volley.VolleyError;

public class SearchGroupActivity extends BaseActivity implements IXListViewListener,OnItemClickListener,IEventListener<GroupInfo> {

	@Bind(R.id.iv_back)
	public ImageView ivBack;
	@Bind(R.id.et_seacher)
	public EditText etSearch;
	@Bind(R.id.lv_group)
	public XListView mListView;

	private SearchGroupListAdapter mAdapter;
	
	private VolleyUtils<BaseGroupListInfo> volley;
	private int page = 1;
	private String search;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_search_group;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		EventManager.register(this);
		etSearch.setHint("输入群名或群号");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
		
		mAdapter = new SearchGroupListAdapter(this);
		mListView.setAdapter(mAdapter);
	}
	Builder builder;
	private void getGroupListTask(String search,int p) {
		// TODO Auto-generated method stub
		if(volley == null){
			volley = new VolleyUtils<BaseGroupListInfo>(this,BaseGroupListInfo.class);
		}
		builder = volley.getBuilder(ConstantUrl.VK_CIRCLE_SEARCH_GROUP);
		builder.appendQueryParameter("pageSize", "10");
		builder.appendQueryParameter("groupName", search);
		builder.appendQueryParameter("pageIndex", p+"");
		builder.appendQueryParameter("token", VkoContext.getInstance(this).getToken());
		volley.sendGETRequest(true,builder);
		volley.setUiDataListener(new UIDataListener<BaseGroupListInfo>() {
			@Override
			public void onDataChanged(BaseGroupListInfo response) {
				if(response != null && response.getCode() == 0 && mListView != null){
					if(response.getData() != null){
						mAdapter.add(response.getData().getData());
						mAdapter.judgeHasEmpty();
						mAdapter.notifyDataSetChanged();
						Pager p = response.getData().getPager();
						page = p.getPageNo();
						if(p.getPageTotal() > page){
							mListView.setPullLoadEnable(true);
						}else{
							mListView.setPullLoadEnable(false);
						}
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
	
	@OnClick(R.id.tv_search)
	void saySearch(){
		search = etSearch.getText().toString();
		if(TextUtils.isEmpty(search)){
			Toast.makeText(this, "请输入你要搜的内容",Toast.LENGTH_LONG).show();
			return;
		}
		mAdapter.clear();
		getGroupListTask(search,1);
	}
	
	@OnClick(R.id.iv_back)
	void sayBack(){
		finish();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		getGroupListTask(search, page +1);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		// TODO Auto-generated method stub
		if(mAdapter.getList() == null || mAdapter.getList().size() == 0){
			saySearch();
		}else{
			GroupInfo group = mAdapter.getListItem(position-1);
			Bundle bundle = new Bundle();
			bundle.putSerializable("GROUPID", group.getId());
			bundle.putString("URL", "http://m.vko.cn/group/crowdHome.html?groupId="+group.getId());
			StartActivityUtil.startActivity(this, GroupHomeActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
		}
	}

	@Override
	public void event(Event<GroupInfo> event) {
		// TODO Auto-generated method stub
		if(event != null && event.getEvent() != null){
			GroupInfo info = event.getEvent();
			if(mAdapter.getList().contains(info)){
				if(event.getEventType() == Event.DETEIL_EVENT || event.getEventType() == Event.QUIT_EVENT){
					mAdapter.remove(info);
				}else{
					mAdapter.replace(info);
				}
				mAdapter.judgeHasEmpty();
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public int getFilter() {
		// TODO Auto-generated method stub
		return Event.JOIN_EVENT |Event.QUIT_EVENT|Event.DETEIL_EVENT;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventManager.unRegister(this);
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		hideSoftInput(this, etSearch, true);
	}

}
