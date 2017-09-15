package cn.vko.ring.study.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.study.adapter.CompCourseListAdapter;
import cn.vko.ring.study.listener.IinsertCourseListener;
import cn.vko.ring.study.model.ComprehensiveCourses;
import cn.vko.ring.study.model.IntegratedCourse;
import cn.vko.ring.study.model.SubjectInfoCourse;

/**
 * 综合课程列表
 * Created by shikh on 2016/5/10.
 */
public class CompCourseListActivity extends BaseCourseListActivity {
    @Bind(R.id.tv_select_book)
    public TextView tv_select_book;
    @Bind(R.id.tv_tocomplex)
    public ImageView tv_tocomplex;
    @Bind(R.id.lv_courseVideo)
    public XListView lvCourseVideo;
    private CompCourseListAdapter mAdapter;
    private  VolleyUtils<IntegratedCourse> volley;
    private ComprehensiveCourses compreCourse;
    @Override
    public void getCourseListTask() {
        if(volley == null){
            volley = new VolleyUtils<IntegratedCourse>(this,IntegratedCourse.class);
        }
        getComprehensiveCoursest(subjectInfo.getId(),VkoContext.getInstance(this).getUser().getGradeId());
    }

    @Override
    public void initView() {
        mAdapter = new CompCourseListAdapter(this);
        super.initView();
        tv_tocomplex.setVisibility(View.GONE);
        lvCourseVideo.setAdapter(mAdapter);
        tv_select_book.setText("综合提升");
        subjectInfo = (SubjectInfoCourse) getIntent().getExtras().getSerializable("MyComprehensiveupgradeActivity");
    }

    @Override
    public XListView setXListView() {
        return lvCourseVideo;
    }

    @Override
    public int setContentViewResId() {
        return R.layout.activity_compre_upgrade;
    }

    private void getComprehensiveCoursest(final String subjectId, final String learnId) {

        String url = ConstantUrl.VKOIP.concat("/zonghe/k1");

        Uri.Builder builder = volley.getBuilder(url);
        builder.appendQueryParameter("token", VkoContext.getInstance(this).getToken() + "");
        builder.appendQueryParameter("learnId", learnId + "");
        builder.appendQueryParameter("subjectId", subjectId + "");
        volley.sendGETRequest(true,builder);
        volley.setUiDataListener(new UIDataListener<IntegratedCourse>() {
            @Override
            public void onDataChanged(IntegratedCourse course) {
                if (course != null) {
                    if (course.getData() != null) {
                        mAdapter.clear();
                        mAdapter.add(course.getData());
                    }
                }
                mAdapter.postNotifyDataSetChanged();
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {
                mAdapter.postNotifyDataSetChanged();
            }
        });
    }

   /* @Override
    public void insertCourseData(SubjectInfoCourse detail) {
        Intent intent = new Intent(this, CompCourseVideoAndTestActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyComPressAndTestActivity", detail);
        intent.putExtras(bundle);
        startActivity(intent);
    }*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (mAdapter.judgeHasEmpty()) {
           getCourseListTask();
            return;
        }
        compreCourse = mAdapter.getListItem(position - 1);
        compreCourse.setSubjectInfo(subjectInfo);
        VkoConfigure.getConfigure(this).put("k1", compreCourse.getId());
        Intent intent = new Intent(this, CompCourseVideoAndTestActivity.class);
        Bundle bundle = new Bundle();
        Log.e("=====lockState",compreCourse.getLockState()+"");
        if (compreCourse.getLockState() != 1) {
            bundle.putInt("TYPE", 3);
        } else {
            bundle.putInt("TYPE", 1);
        }
        bundle.putSerializable("MyComPressAndTestActivity", compreCourse);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
