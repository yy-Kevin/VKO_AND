/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: TestTopView.java 
 * @Prject: AndroidTestDemo
 * @Package: com.jrh.views 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-7-27 下午7:20:07 
 * @version: V1.0   
 */
package cn.vko.ring.study.widget;

import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.vko.ring.R;
import cn.vko.ring.study.model.STATE;
import cn.vko.ring.study.model.SelectButtonModel;

/**
 * @ClassName: TestTopView
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-7-27 下午7:20:07
 */
public class TestTopViewRadioGroup extends RadioGroup implements OnCheckedChangeListener {
	/** 题目数量 默认5个 */
	private int mButtonNum = 5;
	private List<SelectButtonModel> mSelectBtnList;
	private Context mContext;
	private LinearLayout mRbLay;
	private OnRbClickListener onRbClickListener;
	private boolean isShowSubmitBtn = true;
	/* 默认0提交，纠错1 */
	private int type = 0;
	private String submitText;

	public void setOnRbClickListener(OnRbClickListener onRbClickListener) {
		this.onRbClickListener = onRbClickListener;
	}

	public interface OnRbClickListener {
		void onCheckedRb(RadioGroup group, int checkedId);
	}

	public TestTopViewRadioGroup(Context context, List<SelectButtonModel> mSelectBtnList) {
		super(context);
		this.mSelectBtnList = mSelectBtnList;
		initData(context);
	}
	public void initData(Context context) {
		mContext = context;
		setOrientation(HORIZONTAL);
		// 生成SelectButton
		createSelectBtn();
		setOnCheckedChangeListener(this);
	}
	private void createSelectBtn() {
		if (mSelectBtnList != null && mSelectBtnList.size() > 0) {
			int i = 0;
			for (SelectButtonModel sbm : mSelectBtnList) {
				i++;
				RadioButton sbtn = new RadioButton(mContext);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				lp.weight = 1;
				lp.setMargins(10, 10, 10, 10);
				sbtn.setLayoutParams(lp);
				sbtn.setId(i);
				sbtn.setText(i + "");
				ColorStateList csl = getResources().getColorStateList(R.color.color_title_test_selector);
				sbtn.setTextColor(csl);
				sbtn.setTextSize(18);
				sbtn.setPadding(20, 10, 20, 10);
				sbtn.setGravity(Gravity.CENTER);
				sbtn.setButtonDrawable(mContext.getResources().getDrawable(android.R.color.transparent));
				switch (sbm.getState()) {
					case UNFOURSE:
						if (!isShowSubmitBtn && !sbm.isRight()) {
							sbtn.setBackgroundResource(R.drawable.test_exercise_wrong);
							sbtn.setTextColor(getResources().getColor(R.color.red));
						} else {
							sbtn.setBackgroundResource(R.drawable.test_exercise_default);
						}
						break;
					case FOURSED:
						sbtn.setBackgroundResource(R.drawable.test_exercise_seletcedb);
						break;
					case SELECTED:
						sbtn.setBackgroundResource(R.drawable.test_exercise_ed);
						break;
				}
				addView(sbtn);
			}
			if (isShowSubmitBtn) {
				addSubmitBtn();
			}
		} else {
			throw new IllegalArgumentException("传递集合有误");
		}
	}
	/**
	 * @Title: addSubmitBtn
	 * @Description: TODO
	 * @return: void
	 */
	private void addSubmitBtn() {
		RadioButton sbtn = new RadioButton(mContext);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		lp.setMargins(10, 10, 10, 10);
		sbtn.setLayoutParams(lp);
		sbtn.setId(mSelectBtnList.size() + 1);
		sbtn.setText(getSubmitText());
		sbtn.setTextColor(mContext.getResources().getColor(R.color.white));
		sbtn.setTextSize(18);
		sbtn.setPadding(20, 10, 20, 10);
		sbtn.setGravity(Gravity.CENTER);
		sbtn.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_submit_exam));
		sbtn.setButtonDrawable(mContext.getResources().getDrawable(android.R.color.transparent));
		addView(sbtn);
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (onRbClickListener != null) {
			onRbClickListener.onCheckedRb(group, checkedId);
		} else {
			try {
				throw new Exception("请实现onRbClickListener接口");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void refreshRadioButton(int index) {
		STATE S = mSelectBtnList.get(index).getState();
		RadioButton rb = (RadioButton) getChildAt(index);
		switch (S) {
			case UNFOURSE:
				if (!isShowSubmitBtn && !mSelectBtnList.get(index).isRight()) {
					getChildAt(index).setBackgroundResource(R.drawable.test_exercise_wrong);
					rb.setTextColor(getResources().getColor(R.color.red));
				} else {
					getChildAt(index).setBackgroundResource(R.drawable.test_exercise_default);
					rb.setTextColor(getResources().getColor(R.color.tv_blue_corlor));
				}
				break;
			case FOURSED:
				rb.setTextColor(getResources().getColor(R.color.tv_blue_corlor));
				getChildAt(index).setBackgroundResource(R.drawable.test_exercise_seletcedb);
				break;
			case SELECTED:
				getChildAt(index).setBackgroundResource(R.drawable.test_exercise_ed);
				rb.setTextColor(getResources().getColor(R.color.white));
				break;
		}
		rb.setChecked(false);
	}
	public void refreshAllButton(List<SelectButtonModel> sblist) {
		for (int i = 0; i < sblist.size(); i++) {
			refreshRadioButton(i);
		}
	}
	public boolean isShowSubmitBtn() {
		return isShowSubmitBtn;
	}
	public void setShowSubmitBtn(boolean isShowSubmitBtn) {
		this.isShowSubmitBtn = isShowSubmitBtn;
		if (!isShowSubmitBtn) {
			removeViewAt(mSelectBtnList.size());
		}
	}
	public String getSubmitText() {
		if (getType() == 0) {
			submitText = "交卷";
		} else {
			submitText = "纠错";
		}
		return submitText;
	}
	public void setSubmitText(String submitText) {
		this.submitText = submitText;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
		if (isShowSubmitBtn) {
			RadioButton sbtn = (RadioButton) getChildAt(mSelectBtnList.size());
			sbtn.setText(getSubmitText());
		}
	}
}
