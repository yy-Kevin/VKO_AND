package cn.vko.ring.study.presenter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import cn.shikh.utils.DateUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.ViewUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.NoScrollListView;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.study.activity.CourseVideoViewActivity;
import cn.vko.ring.study.adapter.TopicCourseListAdapter;
import cn.vko.ring.study.model.BaseTopicDetailInfo;
import cn.vko.ring.study.model.BaseTopicDetailInfo.TopicDetailInfo;

import cn.vko.ring.study.model.CourceInfo;
import cn.vko.ring.study.model.TopicInfo;

import com.android.volley.VolleyError;

/**
 * @author shikh 专题详情 课程列表
 */
public class TopicCourseListPresenter implements UIDataListener<BaseTopicDetailInfo>, OnItemClickListener {
	private TopicInfo info1;
	private VolleyUtils<BaseTopicDetailInfo> volley;
	private Activity act;
	private NoScrollListView mListView;
	private TopicCourseListAdapter mAdapter;
	private TextView topicIntro;
	private String goodsId;

	public TopicCourseListPresenter(Activity act, NoScrollListView mListView, TopicInfo info1, TextView tvIntro) {
		this.info1 = info1;
		this.act = act;
		this.mListView = mListView;
		this.topicIntro = tvIntro;
		initListView();
		volley = new VolleyUtils<BaseTopicDetailInfo>(act,BaseTopicDetailInfo.class);
		getCourseListTask();
	}
	/**
	 * 
	 */
	private void initListView() {
		// TODO Auto-generated method stub
		// mListView.setPullLoadEnable(false);
		// mListView.setPullRefreshEnable(false);
		mListView.setOnItemClickListener(this);
		mAdapter = new TopicCourseListAdapter(act);
		// mAdapter.judgeHasEmpty();
		mListView.setAdapter(mAdapter);
	}

	private void getCourseListTask() {
		// TODO Auto-generated method stub
		Uri.Builder builder = volley.getBuilder(ConstantUrl.VK_SPECIAL_LIST.concat("/").concat(info1.getId()));
		Log.e("======>info.getId",info1.getId());
		goodsId=info1.getId();
		volley.setUiDataListener(this);
		volley.sendGETRequest(false,builder);
	}

	public void stopUp(XListView xlistview) {
		xlistview.stopLoadMore();
		xlistview.stopRefresh();
		xlistview.setRefreshTime(DateUtils.getCurDateStr());
	}
	public TopicInfo getInfo() {
		return info1;
	}
//	List<IntegraSectionCourse> intelist;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (mAdapter.getList() == null || mAdapter.getList().size() == 0) {
			getCourseListTask();
		} else {			
			CourceInfo info = mAdapter.getListItem(position);

			if(info != null){
				Bundle bundle = new Bundle();
				bundle.putSerializable("video", info.getVideoAttributes());
				bundle.putSerializable("goodsId",goodsId);
				bundle.putInt("TYPE", 2);
				StartActivityUtil.startActivity(act, CourseVideoViewActivity.class,bundle,Intent.FLAG_ACTIVITY_SINGLE_TOP);
			}
		}
	}

	@Override
	public void onDataChanged(BaseTopicDetailInfo t) {
		if (t != null) {
			if (t.getCode() == 0) {
				TopicDetailInfo base = t.getData();
				if (base != null && base.getObj() != null) {
					topicIntro.setText(base.getObj().getIntro());
					info1.setSubjectId(base.getObj().getSubjectId());
					mAdapter.setInfo(info1);
				}
				if (base != null && base.getVideos() != null && base.getVideos().size() > 0) {
					mAdapter.add(base.getVideos());
				}
			}
		}
		mAdapter.judgeHasEmpty();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		if (mAdapter.judgeHasEmpty()) {
			mAdapter.notifyDataSetChanged();
		}
	}
}
