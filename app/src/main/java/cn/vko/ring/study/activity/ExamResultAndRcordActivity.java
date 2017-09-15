/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ExamResultAndRcordActivity.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.activity 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-17 上午11:11:44 
 * @version: V1.0   
 */
package cn.vko.ring.study.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.IActivityFinishListener;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.study.model.ExamInfo;
import cn.vko.ring.study.model.ParamNewSyncAndComp;
import cn.vko.ring.study.presenter.ExamResultAndRecordPresenter;

/**
 * 新 做题结果 和 记录显示页
 * @ClassName: ExamResultAndRcordActivity
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-17 上午11:11:44
 */
public class ExamResultAndRcordActivity extends BaseActivity implements IActivityFinishListener {
	@Bind(R.id.iv_back)
	ImageView ivBack;
	@Bind(R.id.tv_title)
	TextView tvTitle;
	@Bind(R.id.tv_all_and_right)
	TextView tvAllAndRight;
	@Bind(R.id.tv_right_rate)
	TextView tvRightRate;
	@Bind(R.id.tv_star_num)
	TextView tvStarNum;
	@Bind(R.id.tv_star_info)
	TextView tvStarInfo;
	@Bind(R.id.tv_start_info_comp)
	TextView tvStarInfoComp;
	@Bind(R.id.lv_record)
	XListView lvRecord;
	@Bind(R.id.btn_start_test)
	Button btnStartTest;
	private ParamNewSyncAndComp paramNewSyncAndComp;
	private ExamInfo mExamInfo;
	private ExamResultAndRecordPresenter mExamResultAndRecordPresenter;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_exam_result_view;
	}
	@Override
	public void initView() {
		EventBus.getDefault().register(this);
	}
	@Override
	public void initData() {
		getIntentData();
		mExamResultAndRecordPresenter = new ExamResultAndRecordPresenter(this, tvAllAndRight, tvRightRate, tvStarNum,
				tvStarInfo, lvRecord, paramNewSyncAndComp, mExamInfo, btnStartTest, tvStarInfoComp);
		mExamResultAndRecordPresenter.setOnActivityFinishListener(this);
		
	}
	
	/**
	 * @Title: getIntentData
	 * @Description: TODO
	 * @return: void
	 */
	private void getIntentData() {
		Intent i = getIntent();
		Bundle b = i.getExtras();
		paramNewSyncAndComp = (ParamNewSyncAndComp) b.getSerializable(ParamNewSyncAndComp.PARAMNEWSYNCCOMP);
		mExamInfo = (ExamInfo) b.getSerializable(ExamInfo.EXAMINFO);
		if (paramNewSyncAndComp != null) {
			if (paramNewSyncAndComp.isSync()) {
				if (!TextUtils.isEmpty(paramNewSyncAndComp.getCourseName())) {
					tvTitle.setText(paramNewSyncAndComp.getCourseName());
				}
			} else {
				if (!TextUtils.isEmpty(paramNewSyncAndComp.getKnowledgeName())) {
					tvTitle.setText(paramNewSyncAndComp.getKnowledgeName());
				}
			}
		}
	}
	
	@OnClick(R.id.iv_back)
	public void goBack() {
		finish();
	}
	@Subscribe
	public void onEventMainThread(String event) {
		if (event.equals(Constants.EXAM_RESULT_FINISH)) {
			finishActivity();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public void finishActivity() {
		if (!this.isFinishing()) {
			finish();
		}
	}
}
