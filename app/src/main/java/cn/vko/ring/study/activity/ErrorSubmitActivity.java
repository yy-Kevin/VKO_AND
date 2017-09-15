/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ErrorSubmitActivity.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.study.activity 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-22 下午6:02:48 
 * @version: V1.0   
 */
package cn.vko.ring.study.activity;

import butterknife.Bind;
import butterknife.OnClick;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.study.listener.ISubmitResultListener;
import cn.vko.ring.study.model.ErrorExamSubmit;
import cn.vko.ring.study.model.ErrorExamSubmitResult;
import cn.vko.ring.study.presenter.SubmitErrorPresenter;

/**
 * @ClassName: ErrorSubmitActivity
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-22 下午6:02:48
 */
public class ErrorSubmitActivity extends BaseActivity {
	@Bind(R.id.tv_cancel)
	TextView tvCancle;
	@Bind(R.id.tv_submit)
	TextView tvSubmit;
	@Bind(R.id.et_content)
	EditText subContent;
	ErrorExamSubmit errorExamSubmit;
	private SubmitErrorPresenter submitErrorPresenter;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_submit_error;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public void initView() {
		// TODO Auto-generated method stub
	}
	@OnClick(R.id.tv_cancel)
	public void onCancleClick(){
		exit();
	}
	private void exit() {
		
		finish();
		overridePendingTransition(0, R.anim.bottom_out);
	}
	@OnClick(R.id.tv_submit)
	public void onSubmitClick(){
		String content =subContent.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(this, "纠错内容不能为空",Toast.LENGTH_LONG).show();
			return;
		}
		errorExamSubmit.setContent(content);
		submitError(errorExamSubmit);
	}
	@Override
	public void initData() {
	getIntentData();
	}
	/** 
	 * @Title: getIntentData 
	 * @Description: TODO
	 * @return: void
	 */
	private void getIntentData() {
		// TODO Auto-generated method stub
		errorExamSubmit = (ErrorExamSubmit) getIntent().getSerializableExtra(ErrorExamSubmit.ERROR_SUBMIT);
	}
	/**
	 * @Title: submitError
	 * @Description: 纠错
	 * @return: void
	 */
	private void submitError(ErrorExamSubmit errorExamSubmit) {
		submitErrorPresenter = new SubmitErrorPresenter(this, errorExamSubmit);
		submitErrorPresenter.setSubmitResultListener(new ISubmitResultListener() {
			@Override
			public void onSubmitResult(ErrorExamSubmitResult errorRes) {
				if (errorRes != null && errorRes.getData() != null) {
					Toast.makeText(ErrorSubmitActivity.this,errorRes.getData().getContent(),Toast.LENGTH_LONG).show();
					if (!TextUtils.isEmpty(errorRes.getData().getStatus())&&errorRes.getData().getStatus().equals("success")) {
						exit();
					}
				}
			}
			@Override
			public void onFailSubmit() {
				Toast.makeText(ErrorSubmitActivity.this,"提交失败",Toast.LENGTH_LONG).show();
			}
		});
	}
	
}
