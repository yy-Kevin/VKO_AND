package cn.vko.ring.mine.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.DateUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.common.widget.xlv.XListView.IXListViewListener;
import cn.vko.ring.mine.adapter.VideoAdapter;
import cn.vko.ring.mine.model.BaseFavoriteVideoInfo;
import cn.vko.ring.mine.model.CancleCollectEvent;
import cn.vko.ring.mine.model.FavoriteVideoInfo;
import cn.vko.ring.study.activity.CourseVideoViewActivity;
import cn.vko.ring.study.model.VideoAttributes;

import com.android.volley.VolleyError;

public class FavoriteVideoFragment extends BaseFragment implements IXListViewListener, OnItemClickListener,
		UIDataListener<BaseFavoriteVideoInfo> {
	@Bind(R.id.lv_video)
	public XListView videoListView;
	@Bind(R.id.empty)
	View emptyView;
	private boolean flag;// 是否为编辑状态
	public List<FavoriteVideoInfo> videoList;
	public VideoAdapter videoAdapter;
	VolleyUtils<BaseFavoriteVideoInfo> volley;
	int index=1;
	int pageSize=10;
	boolean isLoadMore=false;
	boolean currentState;
	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_favorite_video;
	}
	@Override
	public void initView(View root) {
		// TODO Auto-generated method stub
		videoList = new ArrayList<FavoriteVideoInfo>();
		videoListView.setPullLoadEnable(false);
		videoListView.setPullRefreshEnable(true);
		videoListView.setXListViewListener(this);
		videoListView.setOnItemClickListener(this);
		videoAdapter = new VideoAdapter(atx, videoList, flag);
		videoListView.setAdapter(videoAdapter);
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
			volley = new VolleyUtils<BaseFavoriteVideoInfo>(atx,BaseFavoriteVideoInfo.class);
		}
		String commenturl = ConstantUrl.VKOIP.concat("/store/getMyFavoriteVideo");
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
		Log.i("position", position + "");
		VideoAttributes vab = new VideoAttributes();
		vab.setVideoId(videoList.get(position - 1).getVideoId());
		Bundle bundle = new Bundle();
		bundle.putSerializable("video", vab);
		StartActivityUtil.startActivity(atx, CourseVideoViewActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
	/**
	 * 取消收藏时刷新界面
	 * @Title: refreshView 
	 * @Description: TODO
	 * @param event
	 * @return: void
	 */
	public void refreshView(CancleCollectEvent event){
		if (event!=null&&!TextUtils.isEmpty(event.getId())) {
			if (videoList!=null&&videoList.size()>0) {
				for(FavoriteVideoInfo videoInfo :videoList){
					if (videoInfo.getVideoId().equals(event.getId())) {
						int inex = videoList.indexOf(videoInfo);
						if (inex<0) {
							return;
						}
						videoList.remove(inex);
						break;
					}
				}
				videoAdapter.notifyDataSetChanged();
				if (videoList.size()==0) {
					showErrorView();
				}
			}
		}
		
	}

	public void stopUp() {
		videoListView.stopLoadMore();
		videoListView.stopRefresh();
		videoListView.setRefreshTime(DateUtils.getCurDateStr());
	}

	private void checkRefresh(BaseFavoriteVideoInfo response) {
		if (response.getData().getPager().getPageNo()!=response.getData().getPager().getPageTotal()) {
			videoListView.setPullLoadEnable(true);
			currentState = true;
		}else{
			videoListView.setPullLoadEnable(false);
			currentState = false;
		}
	}
	
	public void stopLoad(){
		videoListView.setPullLoadEnable(false);
		videoListView.setPullRefreshEnable(false);
	}
	public void resetLoad(){
			videoListView.setPullLoadEnable(currentState);
			videoListView.setPullRefreshEnable(true);
	}
	/**
	 * @Title: showlistView
	 * @Description: TODO
	 * @return: void
	 */
	private void showlistView() {
		emptyView.setVisibility(View.GONE);
		videoListView.setVisibility(View.VISIBLE);
	}
	public void showErrorView() {
		emptyView.setVisibility(View.VISIBLE);
		videoListView.setVisibility(View.GONE);
	}

	@Override
	public void onDataChanged(BaseFavoriteVideoInfo response) {
		if (response != null && response.getCode() == 0 && response.getData() != null) {
			if (response.getData().getVideos().size()>0) {
				if (!isLoadMore) {
					videoList.clear();
				}
			}
			showlistView() ;

			videoList.addAll(response.getData().getVideos());
			videoAdapter.setData(videoList);
			videoAdapter.notifyDataSetChanged();
			checkRefresh(response);
			if (!(videoList.size() > 0)) {
				showErrorView();
			}
		} else {
			showErrorView();
		}
		stopUp();
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		if (!(videoList.size() > 0)) {
			showErrorView();
		}
		stopUp();
	}
}
