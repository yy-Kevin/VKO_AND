/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: SubmitErrorPresenter.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.study.presenter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-21 下午5:43:54 
 * @version: V1.0   
 */
package cn.vko.ring.study.presenter;


import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.study.listener.ISubmitResultListener;
import cn.vko.ring.study.model.ErrorExamSubmit;
import cn.vko.ring.study.model.ErrorExamSubmitResult;
import android.content.Context;
import android.net.Uri;

/**
 * @ClassName: SubmitErrorPresenter
 * @Description: 试题纠错
 * @author: JiaRH
 * @date: 2015-12-21 下午5:43:54
 */
public class SubmitErrorPresenter implements UIDataListener<ErrorExamSubmitResult> {
	private Context context;
	private ErrorExamSubmit errorExamSubmit;
	private VolleyUtils<ErrorExamSubmitResult> mVolleyUtils;
	private ISubmitResultListener submitResultListener;

	public SubmitErrorPresenter(Context context, ErrorExamSubmit errorExamSubmit) {
		super();
		this.context = context;
		this.errorExamSubmit = errorExamSubmit;
		initData();
	}
	public void initData() {
		if (mVolleyUtils == null) {
			mVolleyUtils = new VolleyUtils<ErrorExamSubmitResult>(context,ErrorExamSubmitResult.class);
		}
		refreshData();
	}
	/**
	 * @Title: refreshData
	 * @Description: TODO
	 * @return: void
	 */
	private void refreshData() {
		// TODO Auto-generated method stub
		Uri.Builder b = mVolleyUtils.getBuilder(ConstantUrl.VK_ERROR_SUBMIT);
		b.appendQueryParameter("token", VkoContext.getInstance(context).getToken());
		b.appendQueryParameter("examId", errorExamSubmit.getId());
		b.appendQueryParameter("cnt", errorExamSubmit.getContent());
		mVolleyUtils.sendGETRequest(true,b);
		mVolleyUtils.setUiDataListener(this);
	}

	public void setSubmitResultListener(ISubmitResultListener submitResultListener) {
		this.submitResultListener = submitResultListener;
	}

	@Override
	public void onDataChanged(ErrorExamSubmitResult response) {
		if (submitResultListener != null) {
			submitResultListener.onSubmitResult(response);
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		if (submitResultListener != null) {
			submitResultListener.onFailSubmit();
		}
	}
}
