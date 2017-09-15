package cn.vko.ring.mine.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.DateUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.activity.TopicDetailH5Activity;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.common.widget.xlv.XListView.IXListViewListener;
import cn.vko.ring.mine.adapter.TopicAdapter;
import cn.vko.ring.mine.model.CancleCollectEvent;
import cn.vko.ring.mine.model.FavoriteTopicInfo;
import cn.vko.ring.mine.model.BaseFavoriteTopicInfo;

public class FavoriteTopicFragment extends BaseFragment implements IXListViewListener, OnItemClickListener,
		UIDataListener<BaseFavoriteTopicInfo> {
	@Bind(R.id.lv_topic)
	public XListView topicListView;
	@Bind(R.id.empty)
	View emptyView;
	@Bind(R.id.tv_error)
	TextView tvError;
	private boolean flag;// 是否为编辑状态
	public List<FavoriteTopicInfo> topicList;
	public TopicAdapter topicAdapter;
	VolleyUtils<BaseFavoriteTopicInfo> volley;
	int index=1;
	int pageSize=10;
	boolean isLoadMore=false;
	boolean currentState;

	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_favorite_topic;
	}
	@Override
	public void initView(View root) {
		// TODO Auto-generated method stub
		topicList = new ArrayList<FavoriteTopicInfo>();
		topicListView.setPullLoadEnable(false);
		topicListView.setPullRefreshEnable(true);
		topicListView.setXListViewListener(this);
		topicListView.setOnItemClickListener(this);
		topicAdapter = new TopicAdapter(atx, topicList, flag);
		topicListView.setAdapter(topicAdapter);
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		refreshData();
	}
	@OnClick(R.id.empty)
	public void onEmptyClicked() {
		refreshData();
	}
	public void refreshData() {
		if (volley == null) {
			volley = new VolleyUtils<BaseFavoriteTopicInfo>(atx,BaseFavoriteTopicInfo.class);
		}
		String commenturl = ConstantUrl.VKOIP.concat("/store/getMyFavoriteArticle");
		Uri.Builder builder = volley.getBuilder(commenturl);
		builder.appendQueryParameter("pageIndex", index + "");
		builder.appendQueryParameter("pageSize", pageSize + "");
		builder.appendQueryParameter("token", VkoContext.getInstance(atx).getToken());
		volley.sendGETRequest(true,builder);
		volley.setUiDataListener(this);

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putString("URL", "http://m.vko.cn/group/scTopicDetail.html?flag=1&articleId="
				+ topicList.get(position - 1).getArticleId());
		StartActivityUtil.startActivity(atx, TopicDetailH5Activity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}
	@Override
	public void onRefresh() {
		isLoadMore=false;
		index=1;
		refreshData();
	}
	@Override
	public void onLoadMore() {
		isLoadMore=true;
		index++;
		refreshData();
	}
	public void stopLoad(){
		topicListView.setPullLoadEnable(false);
		topicListView.setPullRefreshEnable(false);
	}
	public void resetLoad(){
		topicListView.setPullLoadEnable(currentState);
		topicListView.setPullRefreshEnable(true);
	}

	private void checkRefresh(BaseFavoriteTopicInfo response) {
		if (response.getData().getPager().getPageNo()!=response.getData().getPager().getPageTotal()) {
			topicListView.setPullLoadEnable(true);
			currentState=true;
		}else{
			topicListView.setPullLoadEnable(false);
			currentState=false;
		}
	}
	/**
	 * 取消收藏时刷新界面
	 * @Title: refreshView 
	 * @Description: TODO
	 * @param event
	 * @return: void
	 */
	public void refreshView(CancleCollectEvent event){
		if (event!=null&&!TextUtils.isEmpty(event.getId())) {
			if (topicList!=null&&topicList.size()>0) {
				for(FavoriteTopicInfo topic :topicList){
					if (topic.getArticleId().equals(event.getId())) {
						int inex = topicList.indexOf(topic);
						if (inex<0) {
							return;
						}
						topicList.remove(inex);
						break;
					}
				}
				if (topicAdapter!=null) {
					topicAdapter.notifyDataSetChanged();
					if (topicList.size()==0) {
						showErrorView();
					}
				}
			}
		}
		
	}
	public void stopUp() {
		topicListView.stopLoadMore();
		topicListView.stopRefresh();
		topicListView.setRefreshTime(DateUtils.getCurDateStr());
	}
	/**
	 * @Title: showlistView
	 * @Description: TODO
	 * @return: void
	 */
	private void showlistView() {
		emptyView.setVisibility(View.GONE);
		topicListView.setVisibility(View.VISIBLE);
	}
	public void showErrorView() {
		emptyView.setVisibility(View.VISIBLE);
		topicListView.setVisibility(View.GONE);
	}

	@Override
	public void onDataChanged(BaseFavoriteTopicInfo response) {
		if (response != null && response.getCode() == 0) {
			if (response.getData() != null) {
				showlistView();
				if (response.getData().getArticle().size()>0) {

					if (!isLoadMore) {
						topicList.clear();
					}
				}
				topicList .addAll(response.getData().getArticle()) ;
				topicAdapter.setData(topicList);
				topicAdapter.notifyDataSetChanged();
				checkRefresh(response);
			}
			if (!(topicList.size() > 0)) {
				showErrorView();
			}
		} else {
			if (!(topicList.size() > 0)) {
				showErrorView();
			}
		}
		stopUp();
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		if (!(topicList.size() > 0)) {
			showErrorView();
		}
		stopUp();
	}
}
