/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: GetExamInfoPresenter.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.presenter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-18 下午4:55:23 
 * @version: V1.0   
 */
package cn.vko.ring.study.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.IResultCallBackListener;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.study.activity.ExamPaperActivity;
import cn.vko.ring.study.activity.ExamResultAndRcordActivity;
import cn.vko.ring.study.model.ExamDatas;
import cn.vko.ring.study.model.ExamDatasModel;
import cn.vko.ring.study.model.ExamInfo;
import cn.vko.ring.study.model.ExamResultAndRecordModel;
import cn.vko.ring.study.model.ParamNewSyncAndComp;

/**
 * @ClassName: GetExamInfoPresenter
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-18 下午4:55:23
 */
public class GetExamInfoPresenter implements UIDataListener<ExamResultAndRecordModel> {
	Context mContext;
	VolleyUtils<ExamResultAndRecordModel> mVolleyUtils;
	VolleyUtils<ExamDatasModel> mExamVolleyUtils;
	private ParamNewSyncAndComp paramNewSyncAndComp;
	private IResultCallBackListener<ExamResultAndRecordModel> onResultCallBackListener;
	private IResultCallBackListener<ExamDatas> onExamResultCallBackListener;
	private boolean isLoadMore;
	private int pageIndex=1;
	private int pageSize=5;
	public void setOnExamResultCallBackListener(IResultCallBackListener<ExamDatas> onExamResultCallBackListener) {
		this.onExamResultCallBackListener = onExamResultCallBackListener;
	}
	public void setOnResultCallBackListener(IResultCallBackListener<ExamResultAndRecordModel> onResultCallBackListener) {
		this.onResultCallBackListener = onResultCallBackListener;
	}
	public GetExamInfoPresenter(Context mContext, ParamNewSyncAndComp paramNewSyncAndComp) {
		super();
		this.mContext = mContext;
		this.paramNewSyncAndComp = paramNewSyncAndComp;
		init();
	}
	public void init() {
		mVolleyUtils = new VolleyUtils<ExamResultAndRecordModel>(mContext,ExamResultAndRecordModel.class);
		mExamVolleyUtils = new VolleyUtils<ExamDatasModel>(mContext,ExamDatasModel.class);
	}
	/** 获取 试题 或 答题记录
	 * @Title: getData 
	 * @Description: TODO
	 * @return: void
	 */
	public void getData() {
		if (VkoContext.getInstance(mContext).getUser()==null) {
			return;
		}
		// courseId=3031&bookId=110&subjectId=26&token=7d7dfd780d2c4b7797feb5bdf874b9b5&courseType=41&learnId=3
		Uri.Builder b = mVolleyUtils.getBuilder(ConstantUrl.VK_TEST_TKLISTV2);
		if (paramNewSyncAndComp.isSync()) {
			b.appendQueryParameter("courseId", paramNewSyncAndComp.getCourseId());
			b.appendQueryParameter("bookId", paramNewSyncAndComp.getBookId());
		} else {
			b.appendQueryParameter("knowledgeId", paramNewSyncAndComp.getKnowledgeId());
//			b.appendQueryParameter("knowledgeId", paramNewSyncAndComp.getCourseId());
		}
		b.appendQueryParameter("courseType", paramNewSyncAndComp.getCourseType());
		b.appendQueryParameter("learnId", VkoContext.getInstance(mContext).getUser().getGradeId());
		b.appendQueryParameter("subjectId", paramNewSyncAndComp.getSubjectId());
		b.appendQueryParameter("token", VkoContext.getInstance(mContext).getUser().getToken());
		b.appendQueryParameter("pageIndex", pageIndex+"");
		b.appendQueryParameter("pageSize", pageSize+"");
		mVolleyUtils.sendGETRequest(true,b);
		mVolleyUtils.setUiDataListener(this);
	}
	
	/** 获取试题 详情
	 * @Title: getExamData 
	 * @Description: TODO
	 * @return: void
	 */
	public void getExamData(){
		Uri.Builder b = mExamVolleyUtils.getBuilder(ConstantUrl.VK_TEST_RANTKLIST);
		if (paramNewSyncAndComp.isSync()) {
			b.appendQueryParameter("courseId", paramNewSyncAndComp.getCourseId());
			b.appendQueryParameter("bookId", paramNewSyncAndComp.getBookId());
		} else {
			b.appendQueryParameter("knowledgeId", paramNewSyncAndComp.getKnowledgeId());
			b.appendQueryParameter("courseType", paramNewSyncAndComp.getCourseType());
			b.appendQueryParameter("learnId", VkoContext.getInstance(mContext).getUser().getGradeId());
		}
		b.appendQueryParameter("subjectId", paramNewSyncAndComp.getSubjectId());
		b.appendQueryParameter("token", VkoContext.getInstance(mContext).getUser().getToken());
		mExamVolleyUtils.sendGETRequest(true,b);
		mExamVolleyUtils.setUiDataListener(new UIDataListener<ExamDatasModel>() {
			@Override
			public void onDataChanged(ExamDatasModel response) {
				if (response!=null&&onExamResultCallBackListener!=null) {
					onExamResultCallBackListener.onSuccess(response.getData());
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	
	public void loadMoreData(){
		pageIndex++;
		isLoadMore = true;
		getData();
	}
	/** 
	 * @Title: goToExamPager 
	 * @Description: TODO
	 * @param info
	 * @return: voidH
	 */
	private void goToExamPager(ExamInfo info) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putSerializable(ParamNewSyncAndComp.PARAMNEWSYNCCOMP, paramNewSyncAndComp);
		bundle.putSerializable(info.EXAMINFO, info);
//		Log.e("===测试pager","getCourseType"+paramNewSyncAndComp.getCourseType());
//		Log.e("===测试pager","getSubjectId"+paramNewSyncAndComp.getSubjectId());
//		Log.e("===测试pager","getBookId"+paramNewSyncAndComp.getBookId());
//		Log.e("===测试pager","getCourseId"+paramNewSyncAndComp.getCourseId());
//		Log.e("===测试pager","getCourseName"+paramNewSyncAndComp.getCourseName());
//		Log.e("===测试pager","getHt"+paramNewSyncAndComp.getHt());
//		Log.e("===测试pager","getK1"+paramNewSyncAndComp.getK1());
//		Log.e("===测试pager","getKnowledgeId"+paramNewSyncAndComp.getKnowledgeId());
//		Log.e("===测试pager","getKnowledgeName"+paramNewSyncAndComp.getKnowledgeName());
//		Log.e("===测试pager","getSectionId"+paramNewSyncAndComp.getSectionId());
//		Log.e("===测试pager","=====================================>");
//		Log.e("===测试pager","getGId"+info.getGId());
//		Log.e("===测试pager","getEval"+info.getEval());
//		Log.e("===测试pager","getExam"+info.getExam());
//		Log.e("===测试pager","getSecInfo"+info.getSecInfo());
		StartActivityUtil.startActivity(mContext, ExamPaperActivity.class,bundle,Intent.FLAG_ACTIVITY_NEW_TASK);
	}
	/** 
	 * @Title: goToExamResult 
	 * @Description: TODO
	 * @param info
	 * @return: void
	 */
	private void goToExamResult(ExamInfo info) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(ParamNewSyncAndComp.PARAMNEWSYNCCOMP, paramNewSyncAndComp);
		bundle.putSerializable(info.EXAMINFO, info);
//		Log.e("===测试Result","getCourseType"+paramNewSyncAndComp.getCourseType());
//		Log.e("===测试Result","getSubjectId"+paramNewSyncAndComp.getSubjectId());
//		Log.e("===测试Result","getBookId"+paramNewSyncAndComp.getBookId());
//		Log.e("===测试Result","getCourseId"+paramNewSyncAndComp.getCourseId());
//		Log.e("===测试Result","getCourseName"+paramNewSyncAndComp.getCourseName());
//		Log.e("===测试Result","getHt"+paramNewSyncAndComp.getHt());
//		Log.e("===测试Result","getK1"+paramNewSyncAndComp.getK1());
//		Log.e("===测试Result","getKnowledgeId"+paramNewSyncAndComp.getKnowledgeId());
//		Log.e("===测试Result","getKnowledgeName"+paramNewSyncAndComp.getKnowledgeName());
//		Log.e("===测试Result","getSectionId"+paramNewSyncAndComp.getSectionId());
//		Log.e("===测试Result","=====================================>");
//		Log.e("===测试Result","getGId"+info.getGId());
//		Log.e("===测试Result","getEval"+info.getEval());
//		Log.e("===测试Result","getExam"+info.getExam());
//		Log.e("===测试Result","getSecInfo"+info.getSecInfo());
		StartActivityUtil.startActivity(mContext, ExamResultAndRcordActivity.class,bundle,0);
	}

	@Override
	public void onDataChanged(ExamResultAndRecordModel response) {
		if (response==null) {
			return;
		}
		ExamInfo info = response.getData();
		if (info!=null) {
			if (!isLoadMore) {
				if (info.isIsdone()) {
					goToExamResult(info);
				}else{
					goToExamPager(info);
				}
			}
			if (response!=null&&onResultCallBackListener!=null) {
				onResultCallBackListener.onSuccess(response);
			}
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		if (onResultCallBackListener!=null) {
			onExamResultCallBackListener.onFail();
		}
	}
}
