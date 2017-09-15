/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ExamTestPresenter.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.presenter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-7-30 上午9:58:48 
 * @version: V1.0   
 */
package cn.vko.ring.study.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.textservice.TextInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.IActivityFinishListener;
import cn.vko.ring.common.listener.IResultCallBackListener;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog;
import cn.vko.ring.study.adapter.ExamPageAdaper;
import cn.vko.ring.study.model.ExamContentDetail;
import cn.vko.ring.study.model.ExamDatas;
import cn.vko.ring.study.model.ExamInfo;
import cn.vko.ring.study.model.ExamResultAndRecordModel;
import cn.vko.ring.study.model.ParamNewSyncAndComp;
import cn.vko.ring.study.model.STATE;
import cn.vko.ring.study.model.SelectButtonModel;
import cn.vko.ring.study.model.SuccessSubmitModel;
import cn.vko.ring.study.widget.TestTopViewRadioGroup;

/**
 * @ClassName: ExamTestPresenter
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-7-30 上午9:58:48
 */
public class ExamPaperPresenter implements TestTopViewRadioGroup.OnRbClickListener, OnPageChangeListener, OnClickListener,
		IResultCallBackListener<ExamResultAndRecordModel> {
	/**
	 * 主观题
	 * @fieldName: _0
	 * @fieldType: String
	 * @Description: TODO
	 */
	private static final String EXAM_TYPE_OF_SUBJECTIVE = "0";
	private Context mContext;
	private LinearLayout layExamTopView;
	private TestTopViewRadioGroup testTopView;
	private List<SelectButtonModel> mRadioBtns;
	private View abcdView;
	private ViewPager mExamVp;
	private ExamPageAdaper mAdaper;
	private List<WebView> examList;
	private Button rbA;
	private Button rbB;
	private Button rbC;
	private Button rbD;
	private List<Button> mRbList;
	/** 当前第几题 */
	private int currentIndex = 0;
	private int mAnswerIndex = 0;
	private int toPageIndex;
	private VolleyUtils<SuccessSubmitModel> mAnsVolleyUtlis;
	private ParamNewSyncAndComp paramNewSyncAndComp;
	private String startTime, endTime;
	private SweetAlertDialog mSweetAlertDialog;
	private Handler mHandler = new Handler();
	private IActivityFinishListener onActivityFinishListener;
	private Bundle b = null;
	private GetExamInfoPresenter mGetExamInfoPresenter;
	private List<ExamContentDetail> mExamDetails;
	private List<ImageView> mYNList;
	private View viewAnsYesNo;
	// 记录 5 道题的试题类型
	private List<String> examTypeList;
	private ExamInfo mExamInfo;
	private String mGid;
	// 多选题，是否是第一选择答案
	private boolean isFirstSee = true;
	/* 默认第一次交卷，处理快速交卷，取消，重复提交的问题 */
	private boolean isFirstSubmit = true;

	public void setOnActivityFinishListener(IActivityFinishListener onActivityFinishListener) {
		this.onActivityFinishListener = onActivityFinishListener;
	}

	public ExamPaperPresenter(Context mContext, LinearLayout layExamTopView, ViewPager mExamVp, List<Button> mRbList,
			View abcdView, List<ImageView> mYNList, View viewAnsYesNo) {
		super();
		this.mContext = mContext;
		this.layExamTopView = layExamTopView;
		this.mExamVp = mExamVp;
		this.mRbList = mRbList;
		this.abcdView = abcdView;
		this.mYNList = mYNList;
		this.viewAnsYesNo = viewAnsYesNo;
		initData();
	}
	/**
	 * @Title: initData
	 * @Description: TODO
	 * @return: void
	 */
	private void initData() {
		startTime = System.currentTimeMillis() + "";
		mAnsVolleyUtlis = new VolleyUtils<SuccessSubmitModel>(mContext,SuccessSubmitModel.class);
		examList = new ArrayList<WebView>();
		mRadioBtns = new ArrayList<SelectButtonModel>();
		mExamDetails = new ArrayList<ExamContentDetail>();
		examTypeList = new ArrayList<String>();
	}
	/**
	 * @Title: initEvent
	 * @Description: TODO
	 * @return: void
	 */
	public void refreshData(ParamNewSyncAndComp pp, ExamInfo mExamInfo) {
		this.paramNewSyncAndComp = pp;
		this.mExamInfo = mExamInfo;
		getExamData(pp, mExamInfo);
		setRbClick();
		mExamVp.setOnPageChangeListener(this);
	}
	/**
	 * 设置abcd
	 * @Title: setRbClick
	 * @Description: TODO
	 * @return: void
	 */
	private void setRbClick() {
		for (Button btn : mRbList) {
			btn.setOnClickListener(this);
		}
		for (ImageView btn : mYNList) {
			btn.setOnClickListener(this);
		}
	}
	/**
	 * 设置顶部 题号 和内容
	 * @Title: setTopIndexView
	 * @Description: TODO
	 * @param indexNum
	 * @return: void
	 */
	private void setTopAndContentView(int indexNum) {
		boolean isSingle = true;
		for (int i = 0; i < indexNum; i++) {
			SelectButtonModel sbtn = null;
			if (!TextUtils.isEmpty(mExamDetails.get(i).getTypeId())) {
				if (mExamDetails.get(i).getTypeId().equals("351")) {
					isSingle = true;
				} else if (mExamDetails.get(i).getTypeId().equals("352")) {
					isSingle = false;
				}
			} else {
				isSingle = true;
			}
			if (i == 0) {
				sbtn = new SelectButtonModel(i + 1, STATE.FOURSED, isSingle);
			} else {
				sbtn = new SelectButtonModel(i + 1, STATE.UNFOURSE, isSingle);
			}
			mRadioBtns.add(sbtn);
			WebView wv = new WebView(mContext);
			wv.setBackgroundColor(mContext.getResources().getColor(R.color.bg_exam_play));
			wv.getSettings().setDefaultTextEncodingName("UTF-8");
			wv.getSettings().setBlockNetworkImage(false);
			wv.getSettings().setJavaScriptEnabled(true);
//			wv.setScrollBarStyle(R.style.myWebView);
			wv.loadDataWithBaseURL(null, mExamDetails.get(i).getContext(), "text/html", "UTF-8", null);
			wv.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageFinished(WebView view, String url) {
				}
			});
			examList.add(wv);
		}
		testTopView = new TestTopViewRadioGroup(mContext, mRadioBtns);
		testTopView.setOnRbClickListener(this);
		layExamTopView.addView(testTopView);
		mAdaper = new ExamPageAdaper(examList, mContext);
		mExamVp.setAdapter(mAdaper);
	}
	private void getExamData(final ParamNewSyncAndComp p, ExamInfo info) {
		mExamDetails.clear();
		mGetExamInfoPresenter = new GetExamInfoPresenter(mContext, p);
		mGetExamInfoPresenter.setOnResultCallBackListener(this);
		if (info == null) {
			if (!p.isSubmitCome()) {
				// 再次测试
				mGetExamInfoPresenter
						.setOnResultCallBackListener(new IResultCallBackListener<ExamResultAndRecordModel>() {
							@Override
							public void onSuccess(ExamResultAndRecordModel t) {
								if (t.getData() != null) {
									getExamData(p, t.getData());
								}
							}
							@Override
							public void onFail() {
								// TODO Auto-generated method stub
							}
						});
				mGetExamInfoPresenter.getData();
				addData();
			} else {
				mGetExamInfoPresenter.getExamData();
				mGetExamInfoPresenter.setOnExamResultCallBackListener(new IResultCallBackListener<ExamDatas>() {
					@Override
					public void onSuccess(ExamDatas t) {
						if (t == null)
							return;
						mExamDetails = t.getExam();
						mGid = t.getGId();
						addData();
					}
					@Override
					public void onFail() {
						// TODO Auto-generated method stub
					}
				});
			}
		} else {
			mExamDetails = info.getExam();
			addData();
		}
	}
	private void addData() {
		if (mExamDetails != null && mExamDetails.size() > 0) {
			for (ExamContentDetail detail : mExamDetails) {
				examTypeList.add(detail.getObjective());
			}
			setTopAndContentView(mExamDetails.size());
			changeBottomView(0);
		} else {
			Toast.makeText(mContext, "暂无试题", Toast.LENGTH_LONG).show();
		}
	}
	/*
	 * 点击顶部题号
	 * @Description: TODO
	 * @param group
	 * @param checkedId
	 */
	@Override
	public void onCheckedRb(RadioGroup group, int checkedId) {
		if (currentIndex == checkedId - 1) {
			return;
		}
		if (checkedId - 1 != mRadioBtns.size()) {
			currentIndex = checkedId - 1;
			mRadioBtns.get(checkedId - 1).setState(STATE.FOURSED);
			resetTopAllBtn(checkedId - 1);
			mExamVp.setCurrentItem(checkedId - 1);
			// if (isFirstSee) {
			// resetAnswer();
			// }
		} else {
			((RadioButton) group.getChildAt(mRadioBtns.size())).setChecked(false);
			// 交卷
			submitExam();
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		isFirstSee = true;
		// resetAnswer();
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onPageSelected(int arg0) {
		currentIndex = mExamVp.getCurrentItem();
		changeTopIndexAndBottomButton();
		changeBottomView(arg0);
	}
	private void changeBottomView(int arg0) {
		if (examTypeList.get(arg0).equals("1")) {
			closeYesOrNotView();
			showAbcdView();
		} else {
			closeAbcdView();
			showYesOrNotView();
		}
	}
	/**
	 * 做完了吗
	 * @Title: isDidOver
	 * @Description: TODO
	 * @return: void
	 */
	private void isDidOver() {
		if (checkAnswerNum() == mRadioBtns.size()) {
			submitExam();
		}
	}
	/**
	 * 点击题号是改变状态
	 * @Title: resetTopAllBtn
	 * @Description: TODO
	 * @return: void
	 */
	private void resetTopAllBtn(int index) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mRadioBtns.size(); i++) {
			mRadioBtns.get(i).setState(STATE.UNFOURSE);
			if (i == index) {
				mRadioBtns.get(i).setState(STATE.FOURSED);
			}
			if (mRadioBtns.get(i).isSelected()) {
				mRadioBtns.get(i).setState(STATE.SELECTED);
			}
		}
		refreshTopAllBtn();
		if (mRadioBtns.get(index).isSelected()) {
			refreshAnswer(mRadioBtns.get(index).getSelectedAnswer());
		}
	}
	/**
	 * 重置所有选项
	 */
	private void resetAnswer() {
		for (int i = 0; i < mRbList.size(); i++) {
			mRbList.get(i).setBackgroundResource(R.drawable.shape_answer_unselect_bg);
			mRbList.get(i).setTextColor(mContext.getResources().getColor(R.color.common_dark_text_color));
			// mRbList.get(i).setBackgroundColor(mContext.getResources().getColor(R.color.answer_uncheck));
		}
		for (int i = 0; i < mYNList.size(); i++) {
			mYNList.get(0).setImageResource(R.drawable.test_default_exercise_iwill);
			mYNList.get(1).setImageResource(R.drawable.test_default_exercise_willnot);
		}
	}
	/**
	 * 更新选项答案
	 * @Title: refreshAnswer
	 * @Description: TODO
	 * @param answer
	 * @return: void
	 */
	private void refreshAnswer(String answer) {
		if (answer == null || answer.equals("")) {
			return;
		}
		int index = -1;
		if (answer.equals("A")) {
			index = 0;
		} else if (answer.equals("B")) {
			index = 1;
		} else if (answer.equals("C")) {
			index = 2;
		} else if (answer.equals("D")) {
			index = 3;
		} else if (answer.equals("1")) {
			mYNList.get(0).setImageResource(R.drawable.test_exercise_iwills);
		} else if (answer.equals("2")) {
			mYNList.get(1).setImageResource(R.drawable.test_exercise_willnot);
		}
		if (!answer.equals("1") && !answer.equals("2")) {
			if (index > -1 && index < mRbList.size()){
				mRbList.get(index).setBackgroundResource(R.drawable.shape_answer_select_bg);
			mRbList.get(index).setTextColor(mContext.getResources().getColor(R.color.white));}
		}
		// mRbList.get(index).setBackgroundColor(mContext.getResources().getColor(R.color.answer_checked));
	}
	/**
	 * 刷新状态
	 * @Title: refreshTopBtn
	 * @Description: TODO
	 * @return: void
	 */
	private void refreshTopAllBtn() {
		testTopView.refreshAllButton(mRadioBtns);
	}
	private void refreshTopBtn() {
		testTopView.refreshRadioButton(currentIndex);
	}
	/**
	 * ViewPage滑动时改变题号状态 及选项答案
	 * @Title: changeTopIndexButton
	 * @Description: TODO
	 * @return: void
	 */
	private void changeTopIndexAndBottomButton() {
		for (int i = 0; i < mRadioBtns.size(); i++) {
			mRadioBtns.get(i).setState(STATE.UNFOURSE);
			if (mRadioBtns.get(i).isSelected()) {
				mRadioBtns.get(i).setState(STATE.SELECTED);
			}
			if (i == currentIndex) {
				mRadioBtns.get(i).setState(STATE.FOURSED);
			}
		}
		setBottomButton();
		refreshTopAllBtn();
	}
	private void setBottomButton() {
		resetAnswer();
		if (mRadioBtns.get(currentIndex).isSingleSelect()) {
			if (mRadioBtns.get(currentIndex).isSelected()) {
				refreshAnswer(mRadioBtns.get(currentIndex).getSelectedAnswer());
			}
		} else {
			String selectAns = mRadioBtns.get(currentIndex).getSelectedAnswer();
			setAnsBg(selectAns);
		}
	}
	private void setAnsBg(String selectAns) {
		resetAnswer();
		if (selectAns.contains("A")) {
			refreshAnswer("A");
		}
		if (selectAns.contains("B")) {
			refreshAnswer("B");
		}
		if (selectAns.contains("C")) {
			refreshAnswer("C");
		}
		if (selectAns.contains("D")) {
			refreshAnswer("D");
		}
		if (selectAns.equals("1")) {
			mYNList.get(0).setImageResource(R.drawable.test_exercise_iwills);
		}
		if (selectAns.equals("2")) {
			mYNList.get(1).setImageResource(R.drawable.test_exercise_willnot);
		}
	}
	/**
	 * 去下一页
	 * @Title: goNext
	 * @Description: TODO
	 * @return: void
	 */
	public void goNext() {
		toPageIndex = mExamVp.getCurrentItem();
		if (toPageIndex == mRadioBtns.size() - 1) {
			return;
		}
		toPageIndex++;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mExamVp.setCurrentItem(toPageIndex);
			}
		}, 200);
	}
	private void answerSet(int index, String ans) {
		if (isFirstSee)
			resetAnswer();
		if (mRadioBtns.get(currentIndex).isSingleSelect()
				|| examTypeList.get(currentIndex).equals(EXAM_TYPE_OF_SUBJECTIVE)) {
			goNext();
		}
		if (mRadioBtns.get(currentIndex).isSingleSelect()) {
			mRadioBtns.get(currentIndex).setSelectedAnswer(ans);
			mRbList.get(index).setBackgroundResource(R.drawable.shape_answer_select_bg);
			mRbList.get(index).setTextColor(mContext.getResources().getColor(R.color.white));
			// refreshTopBtn();
			isDidOver();
		} else {
			mRadioBtns.get(currentIndex).setSelectedAnswer(ans);
			String currentAns = mRadioBtns.get(currentIndex).getSelectedAnswer();
			if (isFirstSee && TextUtils.isEmpty(currentAns)) {
				mRbList.get(index).setBackgroundResource(R.drawable.shape_answer_select_bg);
				mRbList.get(index).setTextColor(mContext.getResources().getColor(R.color.white));
			} else {
				setAnsBg(currentAns);
			}
			if (isFirstSee) {
				isFirstSee = false;
			}
		}
		refreshTopBtn();
	}
	/**
	 * 交卷
	 * @Title: submitExam
	 * @Description: TODO
	 * @return: void
	 */
	private void submitExam() {
		// TODO Auto-generated method stub
		if (checkAnswerNum() > 0) {
			if (isFirstSubmit) {
				showSubmitDialog();
				isFirstSubmit = false;
			}
		} else {
			showErrorDialog("亲，不能交白卷哦", "好，再来一次");
			isFirstSubmit = true;
		}
	}
	/**
	 * @Title: showErrorDialog
	 * @Description: TODO
	 * @return: void
	 */
	private void showErrorDialog(String content, String confirmText) {
		// TODO Auto-generated method stub
		if (mSweetAlertDialog!=null&&mSweetAlertDialog.isShowing()) {
			mSweetAlertDialog.dismiss();
		}
		SweetAlertDialog warningDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
		warningDialog.setTitleText(VkoContext.getInstance(mContext).getUser().getName());
		warningDialog.setContentText(content);
		warningDialog.setConfirmText(confirmText);
		if (warningDialog.isShowing()) {
			return;
		}
		warningDialog.show();
	}
	private void showSubmitDialog() {
		if (mSweetAlertDialog != null) {
			if (mSweetAlertDialog.isShowing()) {
				mSweetAlertDialog.dismiss();
			}
			mSweetAlertDialog = null;
		}
		mSweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
		mSweetAlertDialog.setTitleText("交卷");
		mSweetAlertDialog.setContentText(getInfo());
		mSweetAlertDialog.setCancelText("取消");
		mSweetAlertDialog.setConfirmText("确定");
		mSweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				sweetAlertDialog.dismiss();
				sweetAlertDialog = null;
				isFirstSubmit = true;
				/*解决重复选择同一个按钮无法选中的转态*/
				refreshTopAllBtn();
			}
		});
		mSweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				sweetAlertDialog.setTitleText("交卷").setContentText("正在交卷...").setConfirmClickListener(null);
				submitAnswers();
				sweetAlertDialog.showCancelButton(false);
				sweetAlertDialog.showConfirmButton(false);
			}
		});
		if (mSweetAlertDialog.isShowing()) {
			return;
		}
		mSweetAlertDialog.show();
	}
	private void submitAnswers() {
		Map<String,String> params = new HashMap<String,String>();
		endTime = System.currentTimeMillis() + "";
		Uri.Builder builder = mAnsVolleyUtlis.getBuilder(ConstantUrl.VK_TEST_NEWSUBMIT);
		params.put("token", VkoContext.getInstance(mContext).getUser().getToken());
		params.put("ht", paramNewSyncAndComp.getHt());
		params.put("subjectId", paramNewSyncAndComp.getSubjectId());
		params.put("t1", startTime);
		params.put("t2", endTime);
		params.put("learnId", VkoContext.getInstance(mContext).getUser().getGradeId());
		params.put("courseType", paramNewSyncAndComp.getCourseType());

		// 测试
		if (!paramNewSyncAndComp.isSync()) {
			params.put("k2", paramNewSyncAndComp.getKnowledgeId());
			params.put("k1", paramNewSyncAndComp.getK1());
		} else {
			params.put("courseId", paramNewSyncAndComp.getCourseId());
			params.put("sectionId", paramNewSyncAndComp.getSectionId());
			params.put("bookId", paramNewSyncAndComp.getBookId());
			params.put("courseName", paramNewSyncAndComp.getCourseName());
			// builder.appendQueryParameter("type", "1");
		}
		// }
		params.put("ans", getAnswers());
		if (mExamInfo != null) {
			params.put("gId", mExamInfo.getGId());
		} else if (!TextUtils.isEmpty(mGid)) {
			params.put("gId", mGid);
		}
		mAnsVolleyUtlis.setUiDataListener(new UIDataListener<SuccessSubmitModel>() {
			@Override
			public void onDataChanged(SuccessSubmitModel response) {
				if (response.getCode() == 0) {
					mGetExamInfoPresenter.getData();
					// closeDialog();
					EventBus.getDefault().post(Constants.EXAM_RESULT_FINISH);
					notifyStarRefresh();
//					refreshSyncListData();
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {
				showErrorDialog("交卷失败，请重新交卷", "好的");
			}
		});
		mAnsVolleyUtlis.sendPostRequest(true,ConstantUrl.VK_TEST_NEWSUBMIT,params);
	}
	/**
	 * 通知
	 * @Title: notifyStarRefresh
	 * @Description: TODO
	 * @return: void
	 */
	private void notifyStarRefresh() {
		EventBus.getDefault().post(Constants.STAR_REFRESH);
		EventBus.getDefault().post(Constants.REFRESH_COURSE_STAR_DATA);
	}
	
	/**
	 * 更新列表数据
	 * @Title: refreshSyncListData
	 * @Description: TODO
	 * @return: void
	 */
	private void refreshSyncListData() {
		if (paramNewSyncAndComp.isSync()) {
			EventBus.getDefault().post(Constants.REFRESH_SYNCTEST_DATA);
		}
	}
	private void closeDialog() {
		if (mSweetAlertDialog != null && mSweetAlertDialog.isShowing()) {
			mSweetAlertDialog.dismiss();
		}
	}
	/**
	 * 获取答案
	 * @Title: getAnswers
	 * @Description: TODO
	 * @return: void
	 */
	private String getAnswers() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mExamDetails.size(); i++) {
			sb.append(mExamDetails.get(i).getId() + "_");
			if (TextUtils.isEmpty(mRadioBtns.get(i).getSelectedAnswer())) {
				sb.append(",");
			} else {
				sb.append(mRadioBtns.get(i).getSelectedAnswer() + ",");
			}
		}
		return sb.toString().substring(0, sb.toString().lastIndexOf(","));
	}
	/**
	 * @Title: getInfo
	 * @Description: TODO
	 * @return
	 * @return: String
	 */
	private String getInfo() {
		// TODO Auto-generated method stub
		return "一共" + mRadioBtns.size() + "道，已答" + checkAnswerNum() + "道，确认交卷？";
	}
	public int checkAnswerNum() {
		int ansNum = 0;
		for (int i = 0; i < mRadioBtns.size(); i++) {
			if (mRadioBtns.get(i).isSelected()) {
				ansNum++;
			}
		}
		return ansNum;
	}

	Handler handler = new Handler();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.answer_a:
				answerSet(0, "A");
				break;
			case R.id.answer_b:
				answerSet(1, "B");
				break;
			case R.id.answer_c:
				answerSet(2, "C");
				break;
			case R.id.answer_d:
				answerSet(3, "D");
				break;
			case R.id.btn_yes:
				answerSet(0, "1");
				mYNList.get(0).setImageResource(R.drawable.test_exercise_iwills);
				break;
			case R.id.btn_no:
				answerSet(2, "2");
				mYNList.get(1).setImageResource(R.drawable.test_exercise_willnot);
				break;
			default:
				break;
		}
	}
	// @Override
	// public void onErrorResponse(VolleyError error) {
	// // TODO Auto-generated method stub
	// }
	// @Override
	// public void onResponse(ExamPaperModel response) {
	// if (response != null && response.getData() != null) {
	// mExamDetails = response.getData();
	// if (mExamDetails.size() > 0) {
	// setTopAndContentView(mExamDetails.size());
	// showAbcdView();
	// } else {
	// // Toast.makeText(mContext, "没有试题", 0).show();
	// }
	// } else {
	// // Toast.makeText(mContext, "没有试题", 0).show();
	// }
	// }
	/**
	 * @Title: showAbcdView
	 * @Description: TODO
	 * @return: void
	 */
	private void showAbcdView() {
		if (abcdView.getVisibility() == View.VISIBLE) {
			return;
		}
		abcdView.setVisibility(View.VISIBLE);
		startRoaAni(abcdView, true);
	}
	private void startRoaAni(View view, boolean show) {
		float startAngle = 0f;
		float endAngle = 0f;
		if (show) {
			startAngle = 180f;
		} else {
			startAngle = -180f;
		}
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator rotateAni = ObjectAnimator.ofFloat(view, "rotationX", startAngle, endAngle);
		ObjectAnimator alphaAni = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
		rotateAni.setDuration(300);
		alphaAni.setDuration(300);
		set.playTogether(rotateAni, alphaAni);
		set.start();
		// rotateAni.start();
	}
	private void closeAbcdView() {
		abcdView.setVisibility(View.GONE);
	}
	private void showYesOrNotView() {
		if (viewAnsYesNo.getVisibility() == View.VISIBLE) {
			return;
		}
		viewAnsYesNo.setVisibility(View.VISIBLE);
		startRoaAni(viewAnsYesNo, true);
	}
	private void closeYesOrNotView() {
		viewAnsYesNo.setVisibility(View.GONE);
	}
	@Override
	public void onSuccess(ExamResultAndRecordModel t) {
		if (onActivityFinishListener != null) {
			onActivityFinishListener.finishActivity();
			closeDialog();
		}
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public void onFail() {
		// TODO Auto-generated method stub
		closeDialog();
		isFirstSubmit = true;
	}
}
