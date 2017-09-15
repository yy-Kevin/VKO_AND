package cn.vko.ring.study.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.XGridView;
import cn.vko.ring.home.model.BaseSubjectInfo;
import cn.vko.ring.study.activity.VideoTopicListActivity;
import cn.vko.ring.study.adapter.SubjectListAdapter;
import cn.vko.ring.study.model.BookInfo;
import cn.vko.ring.study.model.SubjectInfoCourse;

public class MySubjectPersoner {
	public LinearLayout layout_subject;
	private List<BookInfo> models;
	public Context context;
	private List<SubjectInfoCourse> listSubject;
	
//	public VolleyUtils<BaseSubjectInfo> volley;
//	private int activitvImgRes[] = { R.drawable.subject_china, R.drawable.subject_matheatics,
//			R.drawable.subject_endshish, R.drawable.subject_physical, R.drawable.subject_chemistry,
//			R.drawable.subject_biological, R.drawable.subject_hostory, R.drawable.subject_geograhy,
//			R.drawable.subject_political };
//	private String subjectNames[] = { "语文", "数学", "英语", "物理", "化学", "生物", "历史", "地理", "政治" };
	private SubjectInfoCourse special;
	public  Map<String,Integer> subjectMap = new HashMap<>();
	public MySubjectPersoner(LinearLayout layout_subject, Context context) {
		this.layout_subject = layout_subject;
		this.context = context;
//		volley = new VolleyUtils<BaseSubjectInfo>(context,BaseSubjectInfo.class);
		initSpecial();
		initMapView();
//		initData(listSubject);
		initView();
	}

	private void initView() {
		layout_subject.setOrientation(LinearLayout.VERTICAL);
		layout_subject.setGravity(Gravity.CENTER);
		layout_subject.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		fl = new LinearLayout(context);
		fl2 = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
//		lp.setMargins(0,10,0,10);
		// lp.gravity = Gravity.CENTER_HORIZONTAL;
		lp.gravity = Gravity.CENTER;
		fl.setOrientation(LinearLayout.HORIZONTAL);
		fl2.setOrientation(LinearLayout.HORIZONTAL);
		fl.setPadding(0,15,0,15);
		fl2.setPadding(0,0,0,30);
		fl.setLayoutParams(lp);
		fl2.setLayoutParams(lp);

	}

	private void initSpecial() {
		special = new SubjectInfoCourse();
		special.setName("专题");
		special.setVname("免费");
		special.setId("-1");
	}

	private void initMapView() {
		subjectMap.put("语文",R.drawable.subject_china);
		subjectMap.put("数学",R.drawable.subject_matheatics);
		subjectMap.put("英语",R.drawable.subject_endshish);
		subjectMap.put("物理",R.drawable.subject_physical);
		subjectMap.put("化学",R.drawable.subject_chemistry);
		subjectMap.put("生物",R.drawable.subject_biological);
		subjectMap.put("历史",R.drawable.subject_hostory);
		subjectMap.put("地理",R.drawable.subject_geograhy);
		subjectMap.put("政治",R.drawable.subject_political);
		subjectMap.put("专题",R.drawable.subject_special);
	}

	public void initData(List<SubjectInfoCourse> listSubject) {
		this.listSubject = listSubject;
		if (listSubject != null && listSubject.size() > 0) {
			listSubject.add(special);
			initmodle(listSubject);
		}
	}
	SubjectListAdapter mAdapter;
	public void initGridView(List<SubjectInfoCourse> listSubject, XGridView mGridView) {
		if (listSubject != null && listSubject.size() > 0) {
			listSubject.add(special);
			if(listSubject.size() > 6){
				mGridView.setNumColumns(5);
			}else {
				mGridView.setNumColumns(3);
			}
		}
		if(mAdapter == null){
			mAdapter = new SubjectListAdapter(context);
			mGridView.setAdapter(mAdapter);
			mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					SubjectInfoCourse info = (SubjectInfoCourse) mAdapter.getItem(i);
					if(info.getId().equals("-1")){
						StartActivityUtil.startActivity(context, VideoTopicListActivity.class);
					}else{
						if (iselectSubject != null) {
							iselectSubject.selectSubject(info);
						}
					}
				}
			});
		}
		mAdapter.clear();
		mAdapter.add(listSubject);
		mAdapter.notifyDataSetChanged();
	}
	LinearLayout fl,fl2;
	private void initmodle(final List<SubjectInfoCourse> subjectList) {
		if (layout_subject == null) {
			return;
		}
		layout_subject.removeAllViews();
		fl.removeAllViews();
		fl2.removeAllViews();
		for (int j = 0; j < subjectList.size(); j++) {
			final SubjectInfoCourse info = subjectList.get(j);
			View view =View.inflate(context,R.layout.item_subject_select, null);
			ImageView ivSubject = (ImageView) view.findViewById(R.id.iv_subject_image);
			TextView tv_subject_name = (TextView) view.findViewById(R.id.tv_subject_name);
			TextView tvVersionName = (TextView) view.findViewById(R.id.tv_verision_name);

			// 科目名字
			if (!TextUtils.isEmpty(info.getName())) {
				tv_subject_name.setText(info.getName());
				ivSubject.setImageResource(subjectMap.get(info.getName()));
				tv_subject_name.setTextColor(context.getResources().getColor(R.color.text_show_color));
			}
			// 版本名字
			if (!TextUtils.isEmpty(info.getVname())) {
				tvVersionName.setVisibility(View.VISIBLE);
				tvVersionName.setText(info.getVname());
			} else {
				tvVersionName.setVisibility(View.INVISIBLE);
			}
			LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			lpp.gravity = Gravity.CENTER;
//			lpp.weight=1;
			view.setLayoutParams(lpp);
			if (j < 5) {
				fl.addView(view);
			} else {
				fl2.addView(view);
			}
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(info.getId().equals("-1")){
						StartActivityUtil.startActivity(context, VideoTopicListActivity.class);
					}else{
						if (iselectSubject != null) {
							iselectSubject.selectSubject(info);
						}
					}

				}
			});

		}
		layout_subject.addView(fl, 0);
		layout_subject.addView(fl2, 1);
//		layout_subject.setBackgroundResource(R.color.study_bg);
	}

	public interface ISelectSubjectlistener {
		void selectSubject(SubjectInfoCourse info);
	}

	public ISelectSubjectlistener iselectSubject;

	public void setIselectSubject(ISelectSubjectlistener iselectSubject) {
		this.iselectSubject = iselectSubject;
	}
//	public void setErrorRefreshTv(TextView errorRefreshTv) {
//		this.errorRefreshTv = errorRefreshTv;
//	}
}
