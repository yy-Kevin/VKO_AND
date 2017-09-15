package cn.vko.ring.study.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import cn.shikh.utils.DateUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.ViewUtils;
import cn.vko.ring.ConstantUrl;

import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.study.activity.VideoTopicDetailActivity;
import cn.vko.ring.study.adapter.TopicListAdapter;
import cn.vko.ring.study.model.BaseTopic;
import cn.vko.ring.study.model.BaseTopicInfo;
import cn.vko.ring.study.model.TopicInfo;


import com.android.volley.VolleyError;


/**
 * @author shikh 专题列表
 */
public class TopicListPresenter implements XListView.IXListViewListener, OnItemClickListener, UIDataListener<BaseTopicInfo> {

	private Activity act;
	private XListView mListView;
	private TopicListAdapter mAdapter;
	private VolleyUtils<BaseTopicInfo> volley;
	private int page = 1;
	private int type;
	public TopicListPresenter(Activity act, XListView mListView,int type) {
		this.act = act;
		this.mListView = mListView;
		volley = new VolleyUtils<BaseTopicInfo>(act,BaseTopicInfo.class);
		volley.setCancelable(true);
		initListView();
		this.type = type;
		getDataTask(1, true,type);
	}

	public boolean isEmptly(){
		return mAdapter.getList() == null || mAdapter.getList().size() ==0;
	}
	/**
	 * 
	 */
	private void initListView() {
		// TODO Auto-generated method stub
		mAdapter = new TopicListAdapter(act);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
//		mAdapter.judgeHasEmpty();
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 
	 */
	private void getDataTask(int p, boolean isLoading,int type) {
		// TODO Auto-generated method stub
		String url;
		if(type == 1){
			url = ConstantUrl.VK_SPECIAL_LIST;
		}else{
			url = ConstantUrl.VK_MYCOURSEZT_LIST;
		}
		Uri.Builder builder = volley.getBuilder(url);
		builder.appendQueryParameter("pageIndex", p + "");
		builder.appendQueryParameter("pageSize", "10");
		builder.appendQueryParameter("token", VkoContext.getInstance(act).getToken());
		volley.setUiDataListener(this);
		volley.sendGETRequest(isLoading,builder);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (mAdapter.judgeHasEmpty()) {
			getDataTask(1, true,type);
		} else {
			TopicInfo info = (TopicInfo)mAdapter.getListItem(position-1);
			if (info != null) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("OBJECT", info);
				StartActivityUtil.startActivity(act, VideoTopicDetailActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
			}
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		getDataTask(1, false,type);

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		getDataTask(page + 1, false,type);

	}

	public void stopUp() {
		mListView.stopLoadMore();
		mListView.stopRefresh();
		mListView.setRefreshTime(DateUtils.getCurDateStr());
	}

	public void onPostExecute(BaseTopicInfo t) {
		// TODO Auto-generated method stub
		if (t != null) {
			if (t.getCode() == 0) {
				BaseTopic base = t.getData();
				if (base != null && base.getList() != null
						&& base.getList().size() > 0) {
					page = base.getPager().getPageNo();
					if (page == 1) {
						mAdapter.clear();
					}
					mAdapter.add(base.getList());
					int totalPage = base.getPager().getPageTotal();
					if (page >= totalPage) {
						mListView.setPullLoadEnable(false);
					} else {
						mListView.setPullLoadEnable(true);
					}
				}
			}
		}
		mAdapter.judgeHasEmpty();
		mAdapter.notifyDataSetChanged();
		/*if(mAdapter.getList()!= null && mListView.getChildCount() <=0){
			System.out.println("on-------");
			if(fargment != null){
			}
		}*/
		stopUp();
	}



	@Override
	public void onDataChanged(BaseTopicInfo data) {
		onPostExecute(data);
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		if(mAdapter.judgeHasEmpty()){
			mAdapter.notifyDataSetChanged();
		}
		stopUp();
	}
}
