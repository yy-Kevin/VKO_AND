/*
 *SynchrCourse.java [V 1.0.0]
 *classes : cn.vko.ring.mine.views.SynchrCourse
 *宣义阳 Create at 2015-8-3 下午6:32:14
 */
package cn.vko.ring.mine.fragment;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.OnClick;
import android.content.Intent;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.AnimatedExpandableListView;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.adapter.CourseTreeAdapter;
import cn.vko.ring.mine.model.SubTitleInfo;
import cn.vko.ring.mine.model.SubTitleInfo.DataCourse;
import cn.vko.ring.mine.model.SubTitleInfo.DataCourse.Course;
import cn.vko.ring.mine.model.SubTitleInfo.DataCourse.Course.Section;
import cn.vko.ring.study.activity.SyncCourseVideoAndTestActivity;
import cn.vko.ring.study.model.KnowledgeSection;
import cn.vko.ring.study.model.SubjectInfoCourse;




/**	
 * 我的课程 同步课
 * cn.vko.ring.mine.views.SynchrCourse
 * @author 施坤海
 * 
 */
public class SynchrCourseFragment extends BaseFragment implements OnGroupClickListener,
OnChildClickListener, OnScrollListener{

	@Bind(R.id.lv_course)
	public AnimatedExpandableListView mAnimatedExpandableListView;
	@Bind(R.id.tv_line)
	View lineView;
	@Bind(R.id.layout_empty)
	View layoutEmpty;
	private String courseType = "41";
	private int pageIndex = 1,totalPage;
	private List<Course> courseList;
	private CourseTreeAdapter courseTreeAdapter;
	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_coursexlist;
	}

	@Override
	public void initView(View root) {
		// TODO Auto-generated method stub
		mAnimatedExpandableListView.setOnScrollListener(this);
		courseList = new ArrayList<Course>();
		courseTreeAdapter = new CourseTreeAdapter(courseList, getActivity());
		mAnimatedExpandableListView.setAdapter(courseTreeAdapter);
		mAnimatedExpandableListView.setGroupIndicator(null);// 去掉箭头
//		mAnimatedExpandableListView.setSelection(0);// 设置默认选中项
		mAnimatedExpandableListView.setOnGroupClickListener(this);
		mAnimatedExpandableListView.setOnChildClickListener(this);
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		getSynCourseTask(courseType, pageIndex);
	}

	/**
	 * 获取书标题
	 */
	public void getSynCourseTask(String courseType, int page) {
		// TODO Auto-generated method stub
		String url = ConstantUrl.VKOIP + "/user/myCourse";
		UserInfo user = VkoContext.getInstance(getActivity()).getUser();
		if (user == null) {
			return;
		}
		VolleyUtils<SubTitleInfo> volleyUtils = new VolleyUtils<SubTitleInfo>(atx,SubTitleInfo.class);
		Builder builder = volleyUtils.getBuilder(url);
		builder.appendQueryParameter("courseType", courseType);
		builder.appendQueryParameter("token", user.getToken());
		builder.appendQueryParameter("pageIndex", page + "");
		builder.appendQueryParameter("pageSize", "10");
		volleyUtils.sendGETRequest(true,builder);
		volleyUtils.setUiDataListener(new UIDataListener<SubTitleInfo>() {
			@Override
			public void onDataChanged(SubTitleInfo response) {
				if (response != null) {
					layoutEmpty.setVisibility(View.GONE);
					DataCourse data = response.getData();
					if (data != null) {
						if (data.getPager() != null) {
							pageIndex = data.getPager().getPageNo();
							totalPage = data.getPager().getPageTotal();
						}
						List<Course> course = data.getCourse();
						if (course != null && course.size() > 0) {
							courseList.addAll(course);
							lineView.setVisibility(View.VISIBLE);
							courseTreeAdapter.notifyDataSetChanged();
							return;
						}
					}
				}
					if(courseList == null || courseList.size()==0){
						layoutEmpty.setVisibility(View.VISIBLE);

				}
			}
			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {
					if(courseList == null || courseList.size()==0){
						layoutEmpty.setVisibility(View.VISIBLE);
					}
			}
		});
	}
	
	@OnClick(R.id.layout_empty)
	void onRefreshData(){
		getSynCourseTask(courseType, 1);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		switch (scrollState) {
        // 当不滚动时
        case OnScrollListener.SCROLL_STATE_IDLE:
            // 判断滚动到底部
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
            	if (pageIndex < totalPage) {
        			getSynCourseTask(courseType, pageIndex+1);
        		} else {
        			Toast.makeText(atx, "已加载完毕。。。", Toast.LENGTH_SHORT).show();
        		}
            }
          break;
		}
	}


	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		Course info = (Course) courseTreeAdapter.getGroup(groupPosition);
		Section section = info.getSection().get(childPosition);
		
		KnowledgeSection knowsection = new KnowledgeSection();
		knowsection.setBookId(info.getId());
		knowsection.setChapterId(section.getSectionId());
		knowsection.setGoodsId(info.getGoodsId());
		knowsection.setChapterName(section.getSectionName());
		SubjectInfoCourse subject = new SubjectInfoCourse();
		subject.setId(info.getSubjectId());
		subject.setCurrType("41");
		knowsection.setSubjectInfo(subject);
		
		Intent intent = new Intent(atx, SyncCourseVideoAndTestActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("TYPE", 1);
		bundle.putSerializable("KnowledgeSection", knowsection);
		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		return true;
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		// TODO Auto-generated method stub
		if (mAnimatedExpandableListView.isGroupExpanded(groupPosition)) {
			mAnimatedExpandableListView
					.collapseGroupWithAnimation(groupPosition);
		} else {
			mAnimatedExpandableListView.expandGroupWithAnimation(groupPosition);
		}
		return true;
	}


}
