/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: LvExamResultAndRecordAdapter.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.adapter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-17 上午11:29:40 
 * @version: V1.0   
 */
package cn.vko.ring.study.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.listener.IActivityFinishListener;
import cn.vko.ring.study.activity.CompCourseVideoAndTestActivity;
import cn.vko.ring.study.activity.ShowGradeActivity;
import cn.vko.ring.study.model.ComprehensiveCourses;
import cn.vko.ring.study.model.EvalDetail;
import cn.vko.ring.study.model.ParamTestResult;
import cn.vko.ring.study.model.SubjectInfoCourse;
import cn.vko.ring.study.widget.StarView;


/**
 * @ClassName: LvExamResultAndRecordAdapter
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-17 上午11:29:40
 */
public class LvExamResultAndRecordAdapter extends BaseListAdapter<EvalDetail> {
	Context context;
	private ParamTestResult mParamTestResult;
	private String showTitle;
	private boolean isComp;
	IActivityFinishListener  onFinishListener;
	private String subjectId;
	public LvExamResultAndRecordAdapter(Context ctx) {
		super(ctx);
		this.context = ctx;
	}
	@Override
	protected ViewHolder createViewHolder(View root) {
		MyViewHolder myHolder = new MyViewHolder(root);
		return myHolder;
	}
	@Override
	protected void fillView(View root, final EvalDetail item, ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		if (item == null) {
			return;
		}
		MyViewHolder vh = (MyViewHolder) holder;
		vh.tvStarSmallNum.setText(item.getStarNum());
		vh.tvTime.setText(item.getCrTime());
		if (TextUtils.isEmpty(item.getK1Name())) {
			vh.ViewRecommon.setVisibility(View.GONE);
		} else {
			vh.tvRecKnow.setText(item.getK1Name());
		}
		GvIndexItemAdapter mGvIndexItemAdapter = new GvIndexItemAdapter(context);
		mGvIndexItemAdapter.add(item.getAns());
		vh.gvIndex.setAdapter(mGvIndexItemAdapter);
		vh.gvIndex.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				mParamTestResult = new ParamTestResult();
				mParamTestResult.setComeTest(true);
				mParamTestResult.setTrackId(item.getTrackId());
				mParamTestResult.setIndex(position);
				mParamTestResult.setTitle(showTitle);
				Bundle b = new Bundle();
				b.putSerializable(ParamTestResult.PARAM_TEST_RESULT, mParamTestResult);
				StartActivityUtil.startActivity(ctx, ShowGradeActivity.class, b, 0);
			}
		});
		if (isComp) {
			vh.tvStarSmallNum.setVisibility(View.GONE);
		} else {
			vh.tvStarSmallNum.setVisibility(View.VISIBLE);
		}
		vh.ViewRecommon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				ComprehensiveCourses cc= new ComprehensiveCourses();
				cc.setId(item.getK1());
				cc.setName(item.getK1Name());
				SubjectInfoCourse sf = new SubjectInfoCourse();
				sf.setId(subjectId);
				cc.setSubjectInfo(sf);
				b.putSerializable("MyComPressAndTestActivity", cc);
				StartActivityUtil.startActivity(ctx, CompCourseVideoAndTestActivity.class, b, 0);
			}
		});
	}
	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_examresult_view;
	}

	class MyViewHolder extends ViewHolder {
		public MyViewHolder(View view) {
			super(view);
		}

		@Bind(R.id.tv_time_date)
		TextView tvTime;
		@Bind(R.id.tv_star_small_num)
		StarView tvStarSmallNum;
		@Bind(R.id.tv_recommond_know)
		TextView tvRecKnow;
		@Bind(R.id.gv_test_index)
		GridView gvIndex;
		@Bind(R.id.recommond_lay)
		LinearLayout ViewRecommon;
		@Bind(R.id.time_lay)
		LinearLayout ViewTimeView;
	}

	public void setShowTitle(String showTitle) {
		this.showTitle = showTitle;
	}
	public boolean isComp() {
		return isComp;
	}
	public void setComp(boolean isComp) {
		this.isComp = isComp;
	}
	public void setOnFinishListener(IActivityFinishListener onFinishListener) {
		this.onFinishListener = onFinishListener;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
}
