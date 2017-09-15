/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ExamResultAndRecordPresenter.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.presenter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-17 上午11:24:23 
 * @version: V1.0   
 */
package cn.vko.ring.study.presenter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.common.listener.IActivityFinishListener;
import cn.vko.ring.common.listener.IResultCallBackListener;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.common.widget.xlv.XListView.IXListViewListener;
import cn.vko.ring.study.activity.ExamPaperActivity;
import cn.vko.ring.study.adapter.LvExamResultAndRecordAdapter;
import cn.vko.ring.study.model.EvalDetail;
import cn.vko.ring.study.model.ExamInfo;
import cn.vko.ring.study.model.ExamResultAndRecordModel;
import cn.vko.ring.study.model.ParamNewSyncAndComp;
import cn.vko.ring.study.model.SecInfo;


/**
 * @ClassName: ExamResultAndRecordPresenter
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-17 上午11:24:23
 */
public class ExamResultAndRecordPresenter implements IXListViewListener, IResultCallBackListener<ExamResultAndRecordModel> {
	private Context mContext;
	private TextView tvAllAndRight;
	private TextView tvRightRate;
	private TextView tvStarNum;
	private TextView tvStarInfo,tvStartInfoComp;
	private XListView lvRecord;
	private ParamNewSyncAndComp paramNewSyncAndComp;
	private ExamInfo mExamInfo;
	private LvExamResultAndRecordAdapter mLvAdapter;
	private List<EvalDetail> evalDatas;
	Button btnStartTest;
	private GetExamInfoPresenter mGetExamInfoPresenter;
private IActivityFinishListener onActivityFinishListener;

	public void setOnActivityFinishListener(IActivityFinishListener onActivityFinishListener) {
	this.onActivityFinishListener = onActivityFinishListener;
}
	public ExamResultAndRecordPresenter(Context mContext, TextView tvAllAndRight, TextView tvRightRate,
			TextView tvStartNum, TextView tvStartInfo, XListView lvRecord, ParamNewSyncAndComp paramNewSyncAndComp,
			ExamInfo mExamInfo, Button btnStartTest, TextView tvStarInfoComp) {
		super();
		this.mContext = mContext;
		this.tvAllAndRight = tvAllAndRight;
		this.tvRightRate = tvRightRate;
		this.tvStarNum = tvStartNum;
		this.tvStarInfo = tvStartInfo;
		this.lvRecord = lvRecord;
		this.paramNewSyncAndComp = paramNewSyncAndComp;
		this.mExamInfo = mExamInfo;
		this.btnStartTest = btnStartTest;
		this.tvStartInfoComp = tvStarInfoComp;
		init();
	}
	/**
	 * @Title: init
	 * @Description: TODO
	 * @return: void
	 */
	private void init() {
		mLvAdapter = new LvExamResultAndRecordAdapter(mContext);
		mLvAdapter.setSubjectId(paramNewSyncAndComp.getSubjectId());
		mGetExamInfoPresenter = new GetExamInfoPresenter(mContext, paramNewSyncAndComp);
		mGetExamInfoPresenter.setOnResultCallBackListener(this);
		lvRecord.setAdapter(mLvAdapter);
		lvRecord.setPullRefreshEnable(false);
		lvRecord.setPullLoadEnable(false);
		lvRecord.setXListViewListener(this);
		mLvAdapter.setOnFinishListener(new IActivityFinishListener() {
			@Override
			public void finishActivity() {
				if (onActivityFinishListener!=null) {
					onActivityFinishListener.finishActivity();
				}
			}
		});
		if (paramNewSyncAndComp.isSync()) {
			mLvAdapter.setShowTitle(paramNewSyncAndComp.getCourseName());
		}else{
			mLvAdapter.setShowTitle(paramNewSyncAndComp.getKnowledgeName());
			
		}
		btnStartTest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				paramNewSyncAndComp.setSubmitCome(true);
				Bundle b = new Bundle();
				b.putSerializable(ParamNewSyncAndComp.PARAMNEWSYNCCOMP, paramNewSyncAndComp);
				b.putSerializable(ExamInfo.EXAMINFO, null);
				StartActivityUtil.startActivity(mContext, ExamPaperActivity.class,b,0);
				if (onActivityFinishListener!=null) {
//					onActivityFinishListener.finishActivity();
				}
			}
		});
		showData();
	}
	/** 
	 * 数据展示
	 * @Title: showData 
	 * @Description: TODO
	 * @return: void
	 */
	private void showData() {
		showTopData();
		showRecodData();
	}
	private void showTopData() {
		SecInfo secInfo = mExamInfo.getSecInfo();
		if (secInfo != null) {
			setTopViewShow(secInfo);
		}
	}
	private void showRecodData() {
		evalDatas = mExamInfo.getEval();
		if (evalDatas != null && evalDatas.size() > 0) {
			setListRecordShow();
		}
	}
	private void setListRecordShow() {
		mLvAdapter.add(evalDatas);
		mLvAdapter.notifyDataSetChanged();
		if (evalDatas.size()==5) {
			
			lvRecord.setPullLoadEnable(true);
		}
		
	}
	/**
	 * @Title: setTopViewShow
	 * @Description: TODO
	 * @param secInfo
	 * @return: void
	 */
	private void setTopViewShow(SecInfo secInfo) {
		tvAllAndRight.setText("本节共测试" + secInfo.getDoneNum() + "题，答对" + secInfo.getRightNum() + "题");
		tvRightRate.setText(secInfo.getRate() + "%");
		tvStarNum.setText(secInfo.getStarNum());
//		if (!paramNewSyncAndComp.isSync()) {
//			tvStarNum.setVisibility(View.GONE);
//			tvStarInfo.setVisibility(View.GONE);
//			tvStartInfoComp.setVisibility(View.GONE);
//			mLvAdapter.setComp(true);
//		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		mGetExamInfoPresenter.loadMoreData();
	}
	/*  
	 * 
	 * @Description: TODO
	 *  
	 */
	@Override
	public void onSuccess(ExamResultAndRecordModel t) {
		mExamInfo = t.getData();
		if (mExamInfo.getEval().size()>0) {
			evalDatas.clear();
			showRecodData();
		}else{
			Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_LONG).show();
			lvRecord.setPullLoadEnable(false);
		
		}
		lvRecord.stopLoadMore();
		
	}
	/*  
	 * 
	 * @Description: TODO
	 *  
	 */
	@Override
	public void onFail() {
		// TODO Auto-generated method stub
		
	}
}
