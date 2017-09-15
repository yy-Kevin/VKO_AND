package cn.vko.ring.study.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.listener.IOnClickItemListener;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.widget.SubjectFilterLayout;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.home.model.SubjectInfo;
import cn.vko.ring.study.model.BaseSubjectInfoCourse;
import cn.vko.ring.study.presenter.TopicListPresenter;

/*
* 特色课程 2016年11月18日10:27:45  Agree
* */

public class SpecialFragment extends BaseFragment {
    @Bind(R.id.xlistview)
    public XListView mListView;
    private TopicListPresenter presenter;




    @Override
    public int setContentViewId() {
        return R.layout.fragment_special;
    }

    @Override
    public void initView(View root) {

        presenter =	new TopicListPresenter(getActivity(), mListView,1);

    }

}