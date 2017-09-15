package cn.vko.ring.study.fragment;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.listener.IOnClickItemListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.SubjectFilterLayout;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.home.model.SubjectInfo;
import cn.vko.ring.study.model.BaseSubjectInfoCourse;
import cn.vko.ring.study.presenter.MySeacherCoursePersoner;


public class BasekeFragment extends BaseFragment implements
        IOnClickItemListener<SubjectInfo> {
    @Bind(R.id.baseke_subject)
    public SubjectFilterLayout mSubjectLayout;
    @Bind(R.id.tv_baseke_select)
    public TextView tv_select;


    private VolleyUtils<BaseSubjectInfoCourse> volley;

    @Override
    public int setContentViewId() {
        return R.layout.fragment_baseke;
    }

    @Override
    public void initView(View root) {

        volley = new VolleyUtils<BaseSubjectInfoCourse>(atx,BaseSubjectInfoCourse.class);

        mSubjectLayout.setInitData(null, this);
        mSubjectLayout.setEvent(tv_select, VkoContext.getInstance(getContext()).getGradeId(), 1);
        mSubjectLayout.initData(VkoContext.getInstance(getContext()).getGradeId(), 0);
    }

    @Override
    public void onItemClick(int position, SubjectInfo subjectInfo, View v) {

    }

    public void getSubjectData() {
        Uri.Builder builder = volley.getBuilder(ConstantUrl.VK_STUDY_SUBJECT);
        if (VkoContext.getInstance(atx).isLogin()) {
            builder.appendQueryParameter("token", VkoContext.getInstance(atx)
                    .getUser().getToken());
        }
        volley.sendGETRequest(true,builder);
    }
}