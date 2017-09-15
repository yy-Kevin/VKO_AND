/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ShowGradeActivity.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.activity 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-8-3 上午10:03:34 
 * @version: V1.0   
 */
package cn.vko.ring.study.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.widget.TimeLayout;
import cn.vko.ring.study.model.ParamTestResult;
import cn.vko.ring.study.presenter.ShowTestGradePresenter;


/**
 * 晒成绩&&查看解析
 * @ClassName: ShowGradeActivity
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-8-3 上午10:03:34
 */
public class ShowGradeActivity extends BaseActivity {
	@Bind(R.id.iv_back)
	ImageView ivBack;
	@Bind(R.id.tv_title)
	TextView tvTitle;
	@Bind(R.id.tv_submit_error)
	TextView tvErrorSub;
	@Bind(R.id.iv_user_header)
	ImageView headView;
	@Bind(R.id.tv_show_grade_title)
	TextView showTestTitle;
	@Bind(R.id.lay_time)
	TimeLayout timeLay;
	@Bind(R.id.lay_top_show_grade_title_num)
	LinearLayout mTopTitleIndexLay;
	@Bind(R.id.id_exam_test_show_grade_viewpager)
	ViewPager mVp;
	@Bind(R.id.view_show_grade_top)
	View showGradeTopView;
	private ShowTestGradePresenter mShowTestGradePresenter;

	private ParamTestResult mParamTestResult;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_show_grade;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public void initView() {
		getIntentData();
		if (mParamTestResult!=null) {
			if (mParamTestResult.isComeTest()) {
				tvTitle.setText("查看解析");
				showGradeTopView.setVisibility(View.GONE);
			}else{
				showGradeTopView.setVisibility(View.VISIBLE);
				tvTitle.setText("晒成绩");
			}
		}
		
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public void initData() {
		mShowTestGradePresenter = new ShowTestGradePresenter(this, headView, showTestTitle, timeLay, mTopTitleIndexLay,
				mVp,tvErrorSub);
		mShowTestGradePresenter.setAct(this);
		if (mParamTestResult!=null) {
			mShowTestGradePresenter.refreshData(mParamTestResult);
		}
	}
	public void getIntentData(){
		Intent i = getIntent();
		Bundle b = i.getExtras();
		mParamTestResult = (ParamTestResult) b.getSerializable(ParamTestResult.PARAM_TEST_RESULT);
		
	}
	@OnClick(R.id.iv_back)
	public void goBack() {
		finish();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		EventCountAction.onActivityPauseCount(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		EventCountAction.onActivityResumCount(this);
	}
}
