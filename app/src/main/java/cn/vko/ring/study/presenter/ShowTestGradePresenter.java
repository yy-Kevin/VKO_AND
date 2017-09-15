/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ShowTestGradePresenter.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.presenter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-8-3 上午10:22:32 
 * @version: V1.0   
 */
package cn.vko.ring.study.presenter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.TimeLayout;
import cn.vko.ring.study.activity.ErrorSubmitActivity;
import cn.vko.ring.study.adapter.ExamPageAdaper;

import cn.vko.ring.study.model.ErrorExamSubmit;
import cn.vko.ring.study.model.ExamPaperModel;
import cn.vko.ring.study.model.ParamTestResult;
import cn.vko.ring.study.model.SelectButtonModel;
import cn.vko.ring.study.widget.TestTopViewRadioGroup;


import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;

/**
 * 晒成绩&&试题解析
 * @ClassName: ShowTestGradePresenter
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-8-3 上午10:22:32
 */
public class ShowTestGradePresenter implements TestTopViewRadioGroup.OnRbClickListener, OnPageChangeListener,
		UIDataListener<ExamPaperModel> {
	private ViewPager mVp;
	private LinearLayout topLay;
	private Context mContext;
	private ImageView headView;
	private TextView title;
	private TimeLayout timeLay;
	private TestTopViewRadioGroup ttv;
	private List<SelectButtonModel> mSelectList;
	private List<WebView> examList;
	private ExamPageAdaper mAdaper;
	ParamTestResult mParamTestResult;
	private VolleyUtils<ExamPaperModel> mVolleyUtils;
	private List<ExamPaperModel.ExamDetail> mExamDetails;

	private int currrentIndex;// 当前题
	private TextView tvErrorSubmit;
	private Activity act;

	public ShowTestGradePresenter(Context mContext, ImageView headView, TextView title, TimeLayout timeLay,
			LinearLayout topLay, ViewPager mVp, TextView tvErrorSubmit) {
		super();
		this.mVp = mVp;
		this.topLay = topLay;
		this.mContext = mContext;
		this.headView = headView;
		this.title = title;
		this.timeLay = timeLay;
		this.tvErrorSubmit = tvErrorSubmit;
		initData();
	}
	/**
	 * @Title: initData
	 * @Description: TODO
	 * @return: void
	 */
	private void initData() {
		mVolleyUtils = new VolleyUtils<ExamPaperModel>(mContext,ExamPaperModel.class);
		mExamDetails = new ArrayList<ExamPaperModel.ExamDetail>();
		mSelectList = new ArrayList<SelectButtonModel>();
	}
	public void refreshData(ParamTestResult mParamTestResult) {
		this.mParamTestResult = mParamTestResult;
		topUserInfoData();
		bottomExamData();
		tvErrorSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				upLoadError();
			}
		});
	}
	/**
	 * @Title: bottomExamData
	 * @Description: TODO
	 * @return: void
	 */
	private void bottomExamData() {
		mVp.setOnPageChangeListener(this);
		examList = new ArrayList<WebView>();
		getExamData();
	}
	private void getExamData() {
		mAdaper = new ExamPageAdaper(examList, mContext);
		mVp.setAdapter(mAdaper);
		Uri.Builder b = mVolleyUtils.getBuilder(ConstantUrl.VK_TEST_QUERYRESULT);
		b.appendQueryParameter("trackId", mParamTestResult.getTrackId());
		mVolleyUtils.setUiDataListener(this);
		mVolleyUtils.sendGETRequest(true,b);
	}
	/**
	 * @Title: topUserInfoData
	 * @Description: TODO
	 * @return: void
	 */
	private void topUserInfoData() {
		if (!mParamTestResult.isComeTest()) {
			// timeLay.setVisibility(View.GONE);
			ImageListener imgListener = ImageLoader.getImageListener(headView, R.drawable.icon_default_head,
					R.drawable.icon_default_head);
			mVolleyUtils.getImageLoader().get(mParamTestResult.getHeadUrl(), imgListener);
		}
		title.setText(mParamTestResult.getTitle());
	}
	@Override
	public void onCheckedRb(RadioGroup group, int checkedId) {
		// if (checkedId == group.getChildCount()) {
		//
		// } else {
		currrentIndex = checkedId - 1;
		ttv.refreshAllButton(mSelectList);
		((RadioButton) group.getChildAt(checkedId - 1)).setBackgroundResource(R.drawable.test_exercise_seletcedb);
		mVp.setCurrentItem(checkedId - 1);
		// }
		// refreshState();
	}
	private void upLoadError() {
		Intent i = new Intent(mContext, ErrorSubmitActivity.class);
		ErrorExamSubmit error = new ErrorExamSubmit();
		if (currrentIndex < mExamDetails.size()) {
			error.setId(mExamDetails.get(currrentIndex).getId());
		}
		i.putExtra(ErrorExamSubmit.ERROR_SUBMIT, error);
		mContext.startActivity(i);
		act.overridePendingTransition(R.anim.bottom_in, 0);
		
	}
	/**
	 * @Title: refreshState
	 * @Description: 刷新按钮的点击状态
	 * @return: void
	 */
	private void refreshState() {
		ttv.refreshAllButton(mSelectList);
		ttv.getChildAt(currrentIndex).setBackgroundResource(R.drawable.test_exercise_seletcedb);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onPageSelected(int arg0) {
		currrentIndex = arg0;
		ttv.refreshAllButton(mSelectList);
		ttv.getChildAt(arg0).setBackgroundResource(R.drawable.test_exercise_seletcedb);
	}
	/**
	 * 设置顶部 题号 和内容
	 * @Title: setTopIndexView
	 * @Description: TODO
	 * @param indexNum
	 * @return: void
	 */
	private void setTopAndContentView(int indexNum) {
		for (int i = 0; i < indexNum; i++) {
			SelectButtonModel sbtn = null;
			sbtn = new SelectButtonModel(true);
			mSelectList.add(sbtn);
			WebView wv = new WebView(mContext);
			wv.getSettings().setDefaultTextEncodingName("UTF-8");
			wv.getSettings().setBlockNetworkImage(false);
			wv.loadDataWithBaseURL(null, mExamDetails.get(i).getContext(), "text/html", "UTF-8", null);
//			wv.setScrollBarStyle(R.style.common_bar_style);
			wv.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageFinished(WebView view, String url) {
				}
			});
			examList.add(wv);
		}
		ttv = new TestTopViewRadioGroup(mContext, mSelectList);
		ttv.setOnRbClickListener(this);
		ttv.setType(0);
		ttv.setShowSubmitBtn(false);
		ttv.refreshAllButton(mSelectList);
		topLay.addView(ttv);
		ttv.getChildAt(mParamTestResult.getIndex()).setBackgroundResource(R.drawable.test_exercise_seletcedb);
		mAdaper = new ExamPageAdaper(examList, mContext);
		mVp.setAdapter(mAdaper);
		mVp.setCurrentItem(mParamTestResult.getIndex());
		currrentIndex = mParamTestResult.getIndex();
	}
	public void setAct(Activity act) {
		this.act = act;
	}


	@Override
	public void onDataChanged(ExamPaperModel response) {
		if (response != null && response.getData() != null) {
			mExamDetails = response.getData();
			if (mExamDetails.size() > 0) {
				setTopAndContentView(mExamDetails.size());
				timeLay.setDuration(Long.parseLong(mExamDetails.get(0).getElapse()) * 1000);
			} else {
				Toast.makeText(mContext, "获取数据异常", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(mContext, "没有试题", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
