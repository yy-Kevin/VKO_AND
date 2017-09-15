package cn.vko.ring.study.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.Date;
import java.util.List;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.LinearLayoutForListView;
import cn.vko.ring.common.widget.ListScrollView;
import cn.vko.ring.common.widget.NoScrollListView;
import cn.vko.ring.common.widget.PullToRefreshView;
import cn.vko.ring.study.adapter.CourseCommentListAdapter;
import cn.vko.ring.study.adapter.VideoCommentListAdapter;
import cn.vko.ring.study.model.PeopleComments;
import cn.vko.ring.study.model.VideoCommentInfo;

public class VideoCommonListPresenter implements PullToRefreshView.OnFooterRefreshListener {
	private Context context;
	private LinearLayoutForListView mListView;
	private  PullToRefreshView mPullToRefreshView;
	private String vid;
	private int pageNo = 1;
	private int pageTotal = 0;
	private ScrollView svLoad;
	public List<VideoCommentInfo> commentlist;
	private CourseCommentListAdapter myCommentsAdapter;

	public VideoCommonListPresenter(final Context context, String vid, LinearLayoutForListView listView, PullToRefreshView mPullToRefreshView,ScrollView svLoad) {
		this.context = context;
		this.svLoad = svLoad;
		this.vid = vid;
		this.mListView = listView;
		this.mPullToRefreshView = mPullToRefreshView;
		myCommentsAdapter = new CourseCommentListAdapter(context);
		mListView.setAdapter(myCommentsAdapter);
		initView();
//		initData();
	}

	public void updateData(String vid) {
		this.vid = vid;
		myCommentsAdapter.clear();
		pageNo = 1;
		pageTotal = 0;
		initData();
	}

	private void initView() {
//		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mPullToRefreshView.setLastUpdated(new Date().toLocaleString());
		mListView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(myCommentsAdapter.isEmpty()) {
						getCommentslist(true, 1);
					}
				}
			});


//		new Handler().post(new Runnable() {
//			@Override
//			public void run() {
//				svLoad.fullScroll(ScrollView.FOCUS_DOWN);
//			}
//		});
	}

	private void initData() {
		getCommentslist(false,1);
	}


	/**
	 * 获取评论列表
	 */

	private void getCommentslist(boolean isLoading,int p) {
		if(vid == null) return;
		String commenturl = ConstantUrl.VKOIP.concat("/course/videoComList/");
		// http://192.168.1.77:9999/course/videoComList?videoId=18500193685&pageIndex=1&pageSize=5
		VolleyUtils<PeopleComments> volley = new VolleyUtils<PeopleComments>(context,PeopleComments.class);
		Uri.Builder builder = volley.getBuilder(commenturl);
		builder.appendQueryParameter("videoId", vid);
		builder.appendQueryParameter("pageIndex", p + "");
		builder.appendQueryParameter("pageSize", 5 + "");
		volley.setUiDataListener(new UIDataListener<PeopleComments>() {
			@Override
			public void onDataChanged(PeopleComments comments) {
				if (comments != null && comments.getData() != null) {
					commentlist = comments.getData().getCommentList();
					myCommentsAdapter.add(commentlist);
					mListView.bindLinearLayout();
					pageNo = comments.getData().getPager().getPageNo();
					pageTotal = comments.getData().getPager().getPageTotal();
					mPullToRefreshView.onFooterRefreshComplete();
				}
				myCommentsAdapter.judgeHasEmpty();
				myCommentsAdapter.notifyDataSetChanged();
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {
				myCommentsAdapter.judgeHasEmpty();
				myCommentsAdapter.notifyDataSetChanged();
				mPullToRefreshView.onFooterRefreshComplete();
			}
		});
		volley.sendGETRequest(isLoading,builder);
	}


	public void addComment(VideoCommentInfo comment) {
		myCommentsAdapter.add(comment, 0);
		mListView.bindLinearLayout();
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		if (pageNo < pageTotal) {
			getCommentslist(false,pageNo + 1);
		}else{
			mPullToRefreshView.onFooterRefreshComplete();
		}
	}
}
