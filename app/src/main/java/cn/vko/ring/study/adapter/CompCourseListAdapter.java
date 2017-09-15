package cn.vko.ring.study.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.study.model.ComprehensiveCourses;
import cn.vko.ring.study.widget.ProgressBarView;


/**
 * 综合
 * @author Administrator
 *
 */
public class CompCourseListAdapter extends BaseListAdapter<ComprehensiveCourses> {
	public CompCourseListAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	protected ViewHolder createViewHolder(
			View root) {
		MyViewHolder h = new MyViewHolder(root);
		h.tv_section_name = (TextView) root
				.findViewById(R.id.tv_section_name);		
		h.rect_progress = (ProgressBarView) root
				.findViewById(R.id.rect_progress);
		h.tv_study_progress = (TextView) root
				.findViewById(R.id.tv_study_progress);
		h.tv_xunzhang_count=(TextView) root.findViewById(R.id.tv_xunzhang_count);
		h.layout_unlock = (LinearLayout) root
				.findViewById(R.id.layout_unlock);
		h.tv_chapter_count = (TextView) root.findViewById(R.id.tv_chapter_cnt);
		return h;
	}

	@Override
	protected void fillView(View root, final ComprehensiveCourses item,
			ViewHolder holder,
			int position) {
		if (item == null) {
			return;
		}
		MyViewHolder h = (MyViewHolder) holder;
		Log.e("sectionname", item.getName()+"");
		h.tv_section_name.setText(item.getName()+"");
		h.tv_study_progress.setText("学习进度："+item.getTrackRate()+ "%");
		h.tv_xunzhang_count.setText(item.getStar()+"");			
		h.tv_chapter_count.setText(item.getVideoCnt()+"节");
		if(item.getTrackRate()>100){
			h.rect_progress.setCurrentPosition(100);	
		}else{
			h.rect_progress.setCurrentPosition(item.getTrackRate());
		}
		
//		if(item.getLockState() == 1){
			h.layout_unlock.setVisibility(View.GONE);
//		}else{
//			h.layout_unlock.setVisibility(View.VISIBLE);
//		}
		
	}	
	
	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_course_list;
	}

	class MyViewHolder extends ViewHolder {
		TextView tv_section_name, tv_study_progress,tv_xunzhang_count;
		ProgressBarView rect_progress;
		LinearLayout layout_unlock;
		TextView tv_chapter_count;
		public MyViewHolder(View view) {
			super(view);

		}

	}

}
