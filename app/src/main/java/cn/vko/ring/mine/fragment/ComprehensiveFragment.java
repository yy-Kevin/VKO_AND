package cn.vko.ring.mine.fragment;

import java.util.List;

import cn.shikh.utils.DateUtils;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.study.activity.CompCourseVideoAndTestActivity;
import u.aly.co;

import com.android.volley.VolleyError;

import butterknife.Bind;
import android.content.Intent;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.common.widget.xlv.XListView.IXListViewListener;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.adapter.AllClassAdapter;
import cn.vko.ring.mine.model.AllClassInfo;
import cn.vko.ring.mine.model.AllClassInfo.Data.Course;
import cn.vko.ring.study.model.ComprehensiveCourses;
import cn.vko.ring.study.model.SubjectInfoCourse;


/**
 * 我的课程  综合课程
 * @author Administrator
 *
 */
public class ComprehensiveFragment extends BaseFragment implements OnItemClickListener, IXListViewListener,
		UIDataListener<AllClassInfo> {

	@Bind(R.id.xlistview)
	public XListView mListView;
	@Bind(R.id.tv_line)
	View lineView;
	
	private AllClassAdapter mAdapter;
	private int pageIndex = 1;
	private int pageSize = 10;
	private String courseType = "43";
	private VolleyUtils<AllClassInfo> volleyUtils;
	private List<Course> course;
	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_comrehensive_course;
	}

	@Override
	public void initView(View root) {
		// TODO Auto-generated method stub
		mAdapter = new AllClassAdapter(getActivity());
		volleyUtils = new VolleyUtils<AllClassInfo>(getActivity(),AllClassInfo.class);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
		mAdapter = new AllClassAdapter(getActivity());
		mListView.setAdapter(mAdapter);
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		getDataTask(pageIndex, true, courseType);
	}
	/**
	 * 
	 */
	private void getDataTask(int p, boolean isLoading, String courseType) {
		// TODO Auto-generated method stub
		String url = ConstantUrl.VKOIP + "/user/myCourse";
		Builder builder = volleyUtils.getBuilder(url);
		UserInfo user = VkoContext.getInstance(getActivity()).getUser();
		builder.appendQueryParameter("courseType", courseType);
		builder.appendQueryParameter("token", user.getToken());
		builder.appendQueryParameter("pageIndex", p + "");
		builder.appendQueryParameter("pageSize", pageSize + "");
		volleyUtils.sendGETRequest(isLoading,builder);
		volleyUtils.setUiDataListener(this);

	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		if(mAdapter.judgeHasEmpty()){
			getDataTask(1, false, courseType);
		}else{
			Course course = mAdapter.getListItem(position-1);
			ComprehensiveCourses compreCourse = new ComprehensiveCourses();
			compreCourse.setId(course.getId());
			compreCourse.setName(course.getName());
			compreCourse.setTypeFrom(1);
			SubjectInfoCourse subject = new SubjectInfoCourse();
			subject.setId(course.getSubjectId());
			subject.setCurrType("43");
			compreCourse.setSubjectInfo(subject);
			Intent intent = new Intent(atx, CompCourseVideoAndTestActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("TYPE", 1);
			bundle.putSerializable("MyComPressAndTestActivity", compreCourse);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}


	public void stopUp() {
		mListView.stopLoadMore();
		mListView.stopRefresh();
		mListView.setRefreshTime(DateUtils.getCurDateStr());
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		getDataTask(1, false, courseType);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		getDataTask(pageIndex + 1, false, courseType);
	}

	@Override
	public void onDataChanged(AllClassInfo response) {
		if (response != null) {
			AllClassInfo.Data data = response.getData();
			if (data!=null) {
				course =data.getCourse();
			}

			if (data != null && data.getPager() != null) {
				pageIndex = data.getPager().getPageNo();
				if (pageIndex == 1) {
					mAdapter.clear();
				}
				if (course.size() > 0) {
					lineView.setVisibility(View.VISIBLE);
					mAdapter.add(course);
				}
				int totalPage = data.getPager().getPageTotal();
				if (pageIndex >= totalPage) {
					mListView.setPullLoadEnable(false);
				} else {
					mListView.setPullLoadEnable(true);
				}
			}
		}
		mAdapter.judgeHasEmpty();
		mAdapter.notifyDataSetChanged();
		stopUp();
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		mAdapter.judgeHasEmpty();
		mAdapter.notifyDataSetChanged();
		stopUp();
	}
}
