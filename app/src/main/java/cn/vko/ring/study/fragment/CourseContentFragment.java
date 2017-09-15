package cn.vko.ring.study.fragment;

import android.text.TextUtils;
import android.view.View;

import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.study.presenter.MySeacherCoursePersoner;

/**
 *
 * @author shikh
 * @time 2016/5/10 10:50
 */


public class CourseContentFragment extends BaseFragment {
	
	@Bind(R.id.course_xlistview)
	public XListView xl_listview;
	
	private String seacherString;
	private MySeacherCoursePersoner msfp;

	@Override
	public int setContentViewId() {
		return R.layout.fragment_course_seacher_result;
	}

	@Override
	public void initView(View root) {
	}

	public void setlistVideo(String string) {
		if (TextUtils.isEmpty(string)) {
			return;
		}
		if (string.equals(seacherString)) {
			return;
		}
		this.seacherString = string;
		if(msfp == null){
			msfp = new MySeacherCoursePersoner(xl_listview,atx);
		}
		msfp.initData(seacherString);
	}

}
