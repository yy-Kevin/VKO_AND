/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ExamPagerActivity.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.activity 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-7-29 下午1:52:12 
 * @version: V1.0   
 */
package cn.vko.ring.study.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.IActivityFinishListener;
import cn.vko.ring.study.model.ExamInfo;
import cn.vko.ring.study.model.ParamNewSyncAndComp;
import cn.vko.ring.study.presenter.ExamPaperPresenter;

/**
 * 做题界面
 * @ClassName: ExamPagerActivity
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-7-29 下午1:52:12
 */
public class ExamPaperActivity extends BaseActivity implements IActivityFinishListener {
	@Bind(R.id.iv_back)
	ImageView ivBack;
	@Bind(R.id.tv_title)
	TextView tvTitle;
	@Bind(R.id.lay_exam_top)
	LinearLayout layExamTopView;
	@Bind(R.id.id_exam_test_viewpager)
	ViewPager mExamVp;
	@Bind(R.id.answer_a)
	Button rbA;
	@Bind(R.id.answer_b)
	Button rbB;
	@Bind(R.id.answer_c)
	Button rbC;
	@Bind(R.id.answer_d)
	Button rbD;

	@Bind(R.id.abcd_view)
	View viewAns;
	@Bind(R.id.yes_or_not_view)
	View viewAnsYesNo;
	@Bind(R.id.btn_yes)
	ImageView btnYes;
	@Bind(R.id.btn_no)
	ImageView btnNo;
	private ExamPaperPresenter mExamPaperPresenter;
	private ParamNewSyncAndComp paramNewSyncAndComp;
	private ExamInfo mExamInfo;
	List<Button> mRbList;
	List<ImageView> mYNList;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_exam_page_view;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public void initView() {
		mRbList = new ArrayList<Button>();
		mRbList.add(rbA);
		mRbList.add(rbB);
		mRbList.add(rbC);
		mRbList.add(rbD);
		mYNList = new ArrayList<ImageView>();
		mYNList.add(btnYes);
		mYNList.add(btnNo);
	}
	@Override
	public void initData() {
		getIntentData();
		if (paramNewSyncAndComp != null) {
			if (paramNewSyncAndComp.isSync()) {
				tvTitle.setText(paramNewSyncAndComp.getCourseName());
			} else {
				tvTitle.setText(paramNewSyncAndComp.getKnowledgeName());
			}
			mExamPaperPresenter = new ExamPaperPresenter(this, layExamTopView, mExamVp, mRbList, viewAns,mYNList,viewAnsYesNo);
			mExamPaperPresenter.setOnActivityFinishListener(this);
			mExamPaperPresenter.refreshData(paramNewSyncAndComp,mExamInfo);
		}
	}
	/**
	 * @Title: getIntentData
	 * @Description: TODO
	 * @return: void
	 */
	private void getIntentData() {
		// TODO Auto-generated method stub
		Intent i = getIntent();
		Bundle b = i.getExtras();
		paramNewSyncAndComp = (ParamNewSyncAndComp) b.get(ParamNewSyncAndComp.PARAMNEWSYNCCOMP);
		mExamInfo = (ExamInfo) b.getSerializable(ExamInfo.EXAMINFO);
	}
	@OnClick(R.id.iv_back)
	public void goBack() {
		finish();
	}
	@Override
	public void finishActivity() {
		finish();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// EventCountAction.onActivityPauseCount(this);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// EventCountAction.onActivityResumCount(this);
	}
}
