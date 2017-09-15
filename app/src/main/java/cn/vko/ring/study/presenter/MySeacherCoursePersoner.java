package cn.vko.ring.study.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.List;

import cn.shikh.utils.DateUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.common.widget.xlv.XListView.IXListViewListener;
import cn.vko.ring.home.activity.LoginActivity;
import cn.vko.ring.study.activity.CourseVideoViewActivity;
import cn.vko.ring.study.adapter.MyVideoCourseSeacherAdapter;
import cn.vko.ring.study.model.BaseCourseVideoSeacher;
import cn.vko.ring.study.model.VideoAttributes;
import cn.vko.ring.study.model.VideoList;

public class MySeacherCoursePersoner implements
		UIDataListener<BaseCourseVideoSeacher>, IXListViewListener,
		OnItemClickListener {
	public XListView xl_listview;
	public Context context;
	private String seacherString;
	private MyVideoCourseSeacherAdapter myVideoCourseAdapter;
	private int pageNo = 1;
	private VolleyUtils<BaseCourseVideoSeacher> volley;

	public MySeacherCoursePersoner(XListView xl_listview, Context context) {
		this.xl_listview = xl_listview;
		this.context = context;
		initAdapter();
	}

	private void initAdapter() {
		volley = new VolleyUtils<BaseCourseVideoSeacher>(context,BaseCourseVideoSeacher.class);
		myVideoCourseAdapter = new MyVideoCourseSeacherAdapter(context);
		xl_listview.setAdapter(myVideoCourseAdapter);
		xl_listview.setXListViewListener(this);
//		xl_listview.setIsAddFootView(false);
		xl_listview.setOnItemClickListener(this);
	}


	public void initData(String seacherString) {
		if (!TextUtils.isEmpty(seacherString)) {
			getDataTwo(seacherString, true,1);
		} else {
			Toast.makeText(context, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
		}
	}

	private void getDataTwo(String str,boolean loading,int page) {
		this.seacherString = str;
		String vuri = ConstantUrl.VKOIP.concat("/s/videos");
		Uri.Builder builder = volley.getBuilder(vuri);

		if (VkoContext.getInstance(context).getToken() != null) {
			builder.appendQueryParameter("token",
					VkoContext.getInstance(context).getToken() + "");
		}
		if (VkoContext.getInstance(context).getUser() != null) {
			if (VkoContext.getInstance(context).getUser().getLearn() != null) {
				builder.appendQueryParameter("learnId",
						VkoContext.getInstance(context).getUser().getLearn() + "");
			}
		}
		builder.appendQueryParameter("key", str + "");
		builder.appendQueryParameter("pageIndex", page + "");
		builder.appendQueryParameter("pageSize", 10 + "");
		Log.e("builder地址", builder.toString());
		volley.setUiDataListener(this);
		volley.sendGETRequest(loading,builder);
	}



	public void stopUp() {
		xl_listview.stopLoadMore();
		xl_listview.stopRefresh();
		xl_listview.setRefreshTime(DateUtils.getCurDateStr());
	}

	@Override
	public void onRefresh() {
		getDataTwo(seacherString, false,1);
	}

	@Override
	public void onLoadMore() {
		getDataTwo(seacherString, false,pageNo+1);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (VkoContext.getInstance(context).getUser() == null) {
			StartActivityUtil.startActivity(context, LoginActivity.class);
			return;
		}

		if (myVideoCourseAdapter.judgeHasEmpty()) {
			getDataTwo(seacherString, true,1);
		}

		VideoList videolist = myVideoCourseAdapter.getListItem(position - 1);
		if (videolist == null) {
			return;
		}
		VideoAttributes vab = new VideoAttributes();
		vab.setVideoId(videolist.getVideoId());
		Bundle bundle = new Bundle();
		bundle.putSerializable("video", vab);

		StartActivityUtil.startActivity(context, CourseVideoViewActivity.class, bundle,
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
	}

	@Override
	public void onDataChanged(BaseCourseVideoSeacher response) {
		if (response != null && response.getData() != null) {
			List<VideoList> videolist = response.getData().getVideos();
			if (videolist != null) {
				pageNo = response.getData().getPager().getPageNo();
				if (pageNo == 1) {
					myVideoCourseAdapter.clear();
				}
				myVideoCourseAdapter.add(videolist);
				int pageTotal = response.getData().getPager().getPageTotal();

				if (pageNo >= pageTotal) {
					xl_listview.setPullLoadEnable(false);
				} else {
					xl_listview.setPullLoadEnable(true);
				}
			}
		}
		myVideoCourseAdapter.judgeHasEmpty();
		myVideoCourseAdapter.notifyDataSetChanged();
		xl_listview.setBackgroundResource(R.color.bg_main_view);
		stopUp();
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		myVideoCourseAdapter.judgeHasEmpty();
		myVideoCourseAdapter.notifyDataSetChanged();
		stopUp();
	}


}
